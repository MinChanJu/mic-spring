package com.example.mic_spring.service;

import org.springframework.stereotype.Service;

import com.example.mic_spring.domain.dto.*;
import com.example.mic_spring.domain.entity.*;
import com.example.mic_spring.security.*;

import java.io.*;
import java.util.*;
import java.net.URI;
import java.util.concurrent.*;
import java.time.ZonedDateTime;
import java.lang.reflect.Method;
import javax.tools.*;

@Service
public class CodeService {

  private ProblemService problemService;
  private ExampleService exampleService;
  private SolveService solveService;

  public CodeService(ProblemService problemService, ExampleService exampleService, SolveService solveService) {
    this.problemService = problemService;
    this.exampleService = exampleService;
    this.solveService = solveService;
  }

  public CodeResultDTO runCode(CodeDTO codeDTO, Token token) {
    String userId = codeDTO.getUserId();
    String code = codeDTO.getCode();
    String lang = codeDTO.getLang();
    Long problemId = codeDTO.getProblemId();

    Problem problem = problemService.getProblemById(problemId, token);
    List<Example> examples = exampleService.getAllExamplesByProblemId(problemId);

    String result = runCode(code, lang, problem, examples);

    Solve solve = new Solve(null, userId, problemId, (short) 0, lang, code, ZonedDateTime.now());
    if (result.matches("[+-]?\\d*(\\.\\d+)?"))
      solve.setScore((short) (Double.parseDouble(result) * 10));

    solveService.solveProblem(solve, token);

    List<Solve> solves = solveService.getAllSolvesByUserId(userId);

    return new CodeResultDTO(result, solves);
  }

  public String runCode(String code, String lang, Problem problem, List<Example> examples) {
    switch (lang) {
      case "Python":
        return processCode((co, in, out) -> PythonCompile(co, in, out), code, problem, examples);
      case "C":
        return processCode((co, in, out) -> ClanguageCompile(co, in, out), code, problem, examples);
      case "JAVA":
        return processCode((co, in, out) -> JavaCompile(co, in, out), code, problem, examples);
      default:
        return "제출 오류";
    }
  }

  @FunctionalInterface
  public interface TriFunction<T, U, V, R> {
    R apply(T t, U u, V v);
  }

  public String processCode(TriFunction<String, String, String, String> compileFunction, String code, Problem problem,
      List<Example> examples) {
    int total = examples.size() + 1;
    int count = 0;
    String result;

    try {
      result = compileFunction.apply(code, problem.getProblemExampleInput(), problem.getProblemExampleOutput());
      if (result.equals("success")) {
        count++;
      } else if (result.equals("process run fail: Timeout")) {
        return "시간초과";
      } else if (!result.equals("fail")) {
        return result;
      }

      for (Example example : examples) {
        result = compileFunction.apply(code, example.getExampleInput(), example.getExampleOutput());
        if (result.equals("success")) {
          count++;
        } else if (result.equals("process run fail: Timeout")) {
          return "시간초과";
        } else if (!result.equals("fail")) {
          return result;
        }
      }

      return String.format("%.1f", ((double) count / (double) total) * 100);
    } catch (Exception e) {
      e.printStackTrace();
      return "서버 에러: " + e.getMessage();
    }
  }

  // pythonCompile
  public String PythonCompile(String code, String exampleInput, String exampleOutput) {
    // Python 실행 명령어와 파라미터 준비
    ProcessBuilder processBuilder = new ProcessBuilder("python3", "-c", code);
    processBuilder.redirectErrorStream(true); // 표준 에러를 표준 출력으로 병합

    // 프로세스를 실행할 ExecutorService와 시간 제한 설정 (예: 5초)
    ExecutorService executor = Executors.newSingleThreadExecutor();
    Future<String> future = executor.submit(() -> {
      try {
        Process process = processBuilder.start();

        // Python 코드에 입력 값을 전달
        try (OutputStream stdin = process.getOutputStream()) {
          stdin.write(exampleInput.getBytes());
          stdin.flush();
        }

        // Python 코드 실행 결과 읽기
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
          String line;
          while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
          }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
          throw new Exception("Python execution failed with exit code: " + exitCode);
        }

        return output.toString().trim();
      } catch (IOException | InterruptedException e) {
        throw new Exception("Error Python execution: " + e.getMessage());
      }
    });

    String output;
    try {
      // 지정된 시간(5초) 내에 작업이 완료되지 않으면 TimeoutException 발생
      output = future.get(5, TimeUnit.SECONDS);
    } catch (TimeoutException e) {
      future.cancel(true); // 시간 초과 시 프로세스 중지
      return "process run fail: Timeout";
    } catch (ExecutionException e) {
      return "process run fail: " + e.getCause().getMessage();
    } catch (InterruptedException e) {
      return "process interrupted: " + e.getMessage();
    } finally {
      executor.shutdown();
    }

    // 출력 결과를 비교
    String[] expectedOutput = exampleOutput.split("\n");
    String[] actualOutput = output.split("\n");

    boolean matches = true;
    for (int i = 0; i < actualOutput.length; i++) {
      if (i >= expectedOutput.length)
        matches = false;
      if (!actualOutput[i].trim().equals(expectedOutput[i].trim()))
        matches = false;
      if (!matches)
        break;
    }

    if (matches && actualOutput.length == expectedOutput.length) {
      return "success";
    } else {
      return "fail";
    }
  }

  // ClanguageCompile
  public String ClanguageCompile(String code, String exampleInput, String exampleOutput) {
    String result;
    String[] expectedOutput = exampleOutput.split("\n");

    // 쓰기 권한이 있는 디렉토리 (예: /tmp) 설정
    String tempDir = "/tmp";
    String sourceFilePath = tempDir + "/temp_program.c";
    String executableFilePath = tempDir + "/temp_program";

    // ExecutorService를 사용하여 별도 스레드에서 컴파일 및 실행
    ExecutorService executor = Executors.newSingleThreadExecutor();
    Future<String> future = executor.submit(() -> {
      try {
        // 임시 C 소스 파일 생성
        try (FileWriter writer = new FileWriter(sourceFilePath)) {
          writer.write(code);
        }

        // `gcc`를 사용하여 C 코드를 컴파일
        ProcessBuilder compileProcessBuilder = new ProcessBuilder("gcc", sourceFilePath, "-o",
            executableFilePath);
        compileProcessBuilder.redirectErrorStream(true);
        Process compileProcess = compileProcessBuilder.start();

        // 컴파일 결과 읽기
        StringBuilder compileOutput = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
            new InputStreamReader(compileProcess.getInputStream()))) {
          String line;
          while ((line = reader.readLine()) != null) {
            compileOutput.append(line).append("\n");
          }
        }

        int compileExitCode = compileProcess.waitFor();
        if (compileExitCode != 0) {
          throw new Exception("Compilation failed: " + compileOutput.toString());
        }

        // 컴파일된 바이너리를 실행하기 위해 프로세스 생성
        ProcessBuilder runProcessBuilder = new ProcessBuilder(executableFilePath);
        runProcessBuilder.redirectErrorStream(true);
        Process runProcess = runProcessBuilder.start();

        // 실행 중 입력값 전달
        try (OutputStream stdin = runProcess.getOutputStream()) {
          stdin.write(exampleInput.getBytes());
          stdin.flush();
        }

        // 실행 결과 읽기
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()))) {
          String line;
          while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
          }
        }

        int runExitCode = runProcess.waitFor();
        if (runExitCode != 0) {
          throw new Exception("Execution failed with exit code: " + runExitCode);
        }

        return output.toString().trim();

      } catch (Exception e) {
        return "process run fail: " + e.getMessage();
      } finally {
        // 임시 파일 삭제
        new File(sourceFilePath).delete();
        new File(executableFilePath).delete();
      }
    });

    try {
      // 5초 시간 제한 설정
      String output = future.get(5, TimeUnit.SECONDS);
      String[] actualOutput = output.split("\n");

      // 출력 결과를 비교
      boolean matches = true;
      int i = 0;
      while (i < actualOutput.length && i < expectedOutput.length) {
        if (!actualOutput[i].trim().equals(expectedOutput[i].trim())) {
          matches = false;
          break;
        }
        i++;
      }

      if (matches && i == expectedOutput.length) {
        result = "success";
      } else {
        result = "fail";
      }

    } catch (TimeoutException e) {
      future.cancel(true); // 시간 초과 시 실행 중인 프로세스를 중지
      result = "process run fail: Timeout";
    } catch (ExecutionException e) {
      result = "process run fail: " + e.getCause().getMessage();
    } catch (InterruptedException e) {
      result = "process interrupted: " + e.getMessage();
    } finally {
      executor.shutdown(); // ExecutorService 종료
    }

    return result;
  }

  // javaCompile
  public String JavaCompile(String code, String exampleInput, String exampleOutput) {
    String result;
    String[] expectedOutput = exampleOutput.split("\n");

    // 메모리 내에서 Java 소스 파일을 작성합니다.
    JavaFileObject javaFile = new InMemoryJavaFileObject("Main", code);

    // Java 컴파일러 API를 사용하여 컴파일
    JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
    DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
    StandardJavaFileManager stdFileManager = compiler.getStandardFileManager(diagnostics, null, null);

    // 메모리에서 컴파일된 클래스 파일을 저장할 Map
    Map<String, ByteArrayOutputStream> classFiles = new HashMap<>();

    // In-memory file manager를 사용하여 컴파일된 결과를 메모리에 저장
    JavaFileManager fileManager = new ForwardingJavaFileManager<>(stdFileManager) {
      @Override
      public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind,
          FileObject sibling) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        classFiles.put(className, baos);
        return new SimpleJavaFileObject(URI.create("mem:///" + className.replace('.', '/') + kind.extension),
            kind) {
          @Override
          public OutputStream openOutputStream() {
            return baos;
          }
        };
      }
    };

    // 컴파일 작업을 설정하고 실행
    JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, null, null,
        Arrays.asList(javaFile));
    boolean success = task.call();

    if (success) {
      ExecutorService executor = Executors.newSingleThreadExecutor(); // 스레드 풀 생성
      Future<String> future = executor.submit(() -> {
        try {
          // 컴파일된 클래스를 로드하고 실행
          InMemoryClassLoader classLoader = new InMemoryClassLoader(classFiles);
          Class<?> cls = classLoader.loadClass("Main");
          Method mainMethod = cls.getDeclaredMethod("main", String[].class);

          // 실행 결과를 캡처하기 위해 PrintStream을 사용
          ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
          PrintStream ps = new PrintStream(outputStream);
          PrintStream oldOut = System.out;
          System.setOut(ps);

          // System.in을 가짜 InputStream으로 설정하여 Scanner 입력 시뮬레이션
          InputStream oldIn = System.in;
          InputStream inputStream = new ByteArrayInputStream(exampleInput.getBytes());
          System.setIn(inputStream);

          // 메인 메서드 실행
          mainMethod.invoke(null, (Object) new String[] {});

          // 실행 후 원래 System.in 및 System.out 복구
          System.setOut(oldOut);
          System.setIn(oldIn);

          // 출력 결과를 반환
          return outputStream.toString().trim();

        } catch (Exception e) {
          return "process run fail: " + e.getMessage();
        }
      });

      try {
        // 5초 시간 제한 설정
        String output = future.get(5, TimeUnit.SECONDS);
        String[] actualOutput = output.split("\n");

        // 출력 결과를 비교
        boolean matches = true;
        int i = 0;
        while (i < actualOutput.length && i < expectedOutput.length) {
          if (!actualOutput[i].trim().equals(expectedOutput[i].trim())) {
            matches = false;
            break;
          }
          i++;
        }

        if (matches && i == expectedOutput.length) {
          result = "success";
        } else {
          result = "fail";
        }

      } catch (TimeoutException e) {
        future.cancel(true); // 시간 초과 시 실행 중인 프로세스를 중지
        result = "process run fail: Timeout";
      } catch (ExecutionException e) {
        result = "process run fail: " + e.getCause().getMessage();
      } catch (InterruptedException e) {
        result = "process interrupted: " + e.getMessage();
      } finally {
        executor.shutdown(); // ExecutorService 종료
      }
    } else {
      // 컴파일 오류 수집
      StringBuilder errorMsg = new StringBuilder();
      for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
        errorMsg.append(diagnostic.getMessage(null)).append("\n");
      }
      result = "compile fail: " + errorMsg.toString();
    }

    return result;
  }

  // 메모리 내에서 Java 소스를 나타내는 클래스
  static class InMemoryJavaFileObject extends SimpleJavaFileObject {
    private final String code;

    public InMemoryJavaFileObject(String name, String code) {
      super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
      this.code = code;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
      return code;
    }
  }

  // 메모리 내에서 컴파일된 클래스를 로드하는 클래스 로더
  static class InMemoryClassLoader extends ClassLoader {
    private final Map<String, ByteArrayOutputStream> classFiles;

    public InMemoryClassLoader(Map<String, ByteArrayOutputStream> classFiles) {
      this.classFiles = classFiles;
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
      ByteArrayOutputStream baos = classFiles.get(name);
      if (baos == null) {
        return super.findClass(name);
      }
      byte[] bytes = baos.toByteArray();
      return defineClass(name, bytes, 0, bytes.length);
    }
  }
}