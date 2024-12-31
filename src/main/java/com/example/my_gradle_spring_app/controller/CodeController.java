package com.example.my_gradle_spring_app.controller;

import com.example.my_gradle_spring_app.model.CodeDTO;
import com.example.my_gradle_spring_app.model.Example;
import com.example.my_gradle_spring_app.model.Problem;
import com.example.my_gradle_spring_app.service.ExampleService;
import com.example.my_gradle_spring_app.service.ProblemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.tools.*;
import java.io.*;
import java.net.URI;
import java.util.*;
import java.util.concurrent.*;
import java.lang.reflect.Method;

@RestController
@RequestMapping("/api/code")
public class CodeController {

    @Autowired
    private ExampleService exampleService;
    @Autowired
    private ProblemService problemService;

    @PostMapping
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String submitCode(@RequestBody CodeDTO codeDTO) {
        String code = codeDTO.getCode();
        String lang = codeDTO.getLang();
        Integer problemId = codeDTO.getProblemId();
        List<Example> examples = exampleService.getExamplesByProblemId(problemId);
        Optional<Problem> problem = problemService.getProblemById(problemId.longValue());
        if (problem.isPresent()) {
            Problem actualProblem = problem.get();
            return runCode(code, lang, actualProblem, examples);
        } else {
            return "서버 에러";
        }
    }

    public String runCode(String code, String lang, Problem problem, List<Example> examples) {
        int total = examples.size() + 1;
        int count = 0;

        if (lang.equals("Python")) {
            String result;
            try {
                result = pythonCompile(code, problem.getProblemExampleInput(), problem.getProblemExampleOutput());
                if (result.equals("success")) {
                    count++;
                } else if (result.equals("process run fail: Timeout")) {
                    return "시간초과";
                } else if (!result.equals("fail")) {
                    return result;
                }
            } catch (Exception e) {
                e.printStackTrace(); // 예외 발생 시 스택 트레이스를 출력하여 문제를 진단
                return "서버 에러: " + e.getMessage();
            }
            for (Example example : examples) {
                try {
                    result = pythonCompile(code, example.getExampleInput(), example.getExampleOutput());
                    if (result.equals("success")) {
                        count++;
                    } else if (result.equals("process run fail: Timeout")) {
                        return "시간초과";
                    } else if (!result.equals("fail")) {
                        return result;
                    }
                } catch (Exception e) {
                    e.printStackTrace(); // 예외 발생 시 스택 트레이스를 출력하여 문제를 진단
                    return "서버 에러: " + e.getMessage();
                }
            }
            return String.format("%.1f", ((double) count / (double) total) * 100);
        } else if (lang.equals("C")) {
            String result;
            try {
                result = ClanguageCompile(code, problem.getProblemExampleInput(), problem.getProblemExampleOutput());
                if (result.equals("success")) {
                    count++;
                } else if (result.equals("process run fail: Timeout")) {
                    return "시간초과";
                } else if (!result.equals("fail")) {
                    return result;
                }
            } catch (Exception e) {
                e.printStackTrace(); // 예외 발생 시 스택 트레이스를 출력하여 문제를 진단
                return "서버 에러: " + e.getMessage();
            }
            for (Example example : examples) {
                try {
                    result = ClanguageCompile(code, example.getExampleInput(), example.getExampleOutput());
                    if (result.equals("success")) {
                        count++;
                    } else if (result.equals("process run fail: Timeout")) {
                        return "시간초과";
                    } else if (!result.equals("fail")) {
                        return result;
                    }
                } catch (Exception e) {
                    e.printStackTrace(); // 예외 발생 시 스택 트레이스를 출력하여 문제를 진단
                    return "서버 에러: " + e.getMessage();
                }
            }
            return String.format("%.1f", ((double) count / (double) total) * 100);
        } else if (lang.equals("JAVA")) {
            String result;
            try {
                result = javaCompile(code, problem.getProblemExampleInput(), problem.getProblemExampleOutput());
                if (result.equals("success")) {
                    count++;
                } else if (result.equals("process run fail: Timeout")) {
                    return "시간초과";
                } else if (!result.equals("fail")) {
                    return result;
                }
            } catch (Exception e) {
                e.printStackTrace(); // 예외 발생 시 스택 트레이스를 출력하여 문제를 진단
                return "서버 에러: " + e.getMessage();
            }
            for (Example example : examples) {
                try {
                    result = javaCompile(code, example.getExampleInput(), example.getExampleOutput());
                    if (result.equals("success")) {
                        count++;
                    } else if (result.equals("process run fail: Timeout")) {
                        return "시간초과";
                    } else if (!result.equals("fail")) {
                        return result;
                    }
                } catch (Exception e) {
                    e.printStackTrace(); // 예외 발생 시 스택 트레이스를 출력하여 문제를 진단
                    return "서버 에러: " + e.getMessage();
                }
            }
            return String.format("%.1f", ((double) count / (double) total) * 100);
        } else {
            return "제출 오류";
        }
    }

    // javaCompile
    public String javaCompile(String code, String exampleInput, String exampleOutput) throws Exception {
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
            public JavaFileObject getJavaFileForOutput(Location location, String className, JavaFileObject.Kind kind, FileObject sibling) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                classFiles.put(className, baos);
                return new SimpleJavaFileObject(URI.create("mem:///" + className.replace('.', '/') + kind.extension), kind) {
                    @Override
                    public OutputStream openOutputStream() {
                        return baos;
                    }
                };
            }
        };

        // 컴파일 작업을 설정하고 실행
        JavaCompiler.CompilationTask task = compiler.getTask(null, fileManager, diagnostics, null, null, Arrays.asList(javaFile));
        boolean success = task.call();

        if (success) {
            ExecutorService executor = Executors.newSingleThreadExecutor();  // 스레드 풀 생성
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
                    mainMethod.invoke(null, (Object) new String[]{});

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
                future.cancel(true);  // 시간 초과 시 실행 중인 프로세스를 중지
                result = "process run fail: Timeout";
            } catch (ExecutionException e) {
                result = "process run fail: " + e.getCause().getMessage();
            } catch (InterruptedException e) {
                result = "process interrupted: " + e.getMessage();
            } finally {
                executor.shutdown();  // ExecutorService 종료
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

    // pythonCompile
    public String pythonCompile(String code, String exampleInput, String exampleOutput) throws Exception {
        StringBuilder result = new StringBuilder();
        String[] expectedOutput = exampleOutput.split("\n");

        // Python 실행 명령어와 파라미터 준비
        ProcessBuilder processBuilder = new ProcessBuilder("python3", "-c", code);
        processBuilder.redirectErrorStream(true);  // 표준 에러를 표준 출력으로 병합

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
                    throw new Exception("Python script execution failed with exit code: " + exitCode);
                }

                return output.toString().trim();
            } catch (IOException | InterruptedException e) {
                throw new Exception("Error occurred during Python script execution: " + e.getMessage());
            }
        });

        String output;
        try {
            // 지정된 시간(5초) 내에 작업이 완료되지 않으면 TimeoutException 발생
            output = future.get(5, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);  // 시간 초과 시 프로세스 중지
            result.append("process run fail: Timeout");
            return result.toString();
        } catch (ExecutionException e) {
            result.append("process run fail: ").append(e.getCause().getMessage());
            return result.toString();
        } catch (InterruptedException e) {
            result.append("process interrupted: ").append(e.getMessage());
            return result.toString();
        } finally {
            executor.shutdown();
        }

        // 출력 결과를 비교
        String[] actualOutput = output.split("\n");

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
            result.append("success");
        } else {
            result.append("fail");
        }

        return result.toString();
    }

    // ClanguageCompile
    public String ClanguageCompile(String code, String exampleInput, String exampleOutput) throws Exception {
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
                ProcessBuilder compileProcessBuilder = new ProcessBuilder("gcc", sourceFilePath, "-o", executableFilePath);
                compileProcessBuilder.redirectErrorStream(true);
                Process compileProcess = compileProcessBuilder.start();

                // 컴파일 결과 읽기
                StringBuilder compileOutput = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(compileProcess.getInputStream()))) {
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
            future.cancel(true);  // 시간 초과 시 실행 중인 프로세스를 중지
            result = "process run fail: Timeout";
        } catch (ExecutionException e) {
            result = "process run fail: " + e.getCause().getMessage();
        } catch (InterruptedException e) {
            result = "process interrupted: " + e.getMessage();
        } finally {
            executor.shutdown();  // ExecutorService 종료
        }

        return result;
    }
}
