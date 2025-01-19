package com.example.mic_spring.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.mic_spring.domain.dto.UserDTO;
import com.example.mic_spring.domain.dto.UserLoginDTO;
import com.example.mic_spring.domain.entity.User;
import com.example.mic_spring.exception.CustomException;
import com.example.mic_spring.exception.ErrorCode;

import java.sql.Connection;
import java.sql.Statement;
import java.time.ZonedDateTime;
import java.util.List;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired
    private UserService userService;
    @Autowired
    private DataSource dataSource;

    @BeforeEach
    @AfterEach
    void resetAutoIncrement() {
        try (Connection conn = dataSource.getConnection();
                Statement stmt = conn.createStatement()) {
            stmt.execute("SELECT setval('users_id_seq', (SELECT COALESCE(MAX(id), 0) + 1 FROM users), false)");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void create() {
        User user1 = new User(null, "user1", "test11", "test1111", "010-1111-1111", "abc111@gmail.com", 1L, -1L,
                ZonedDateTime.now());
        User user2 = new User(null, "user2", "test11", "test2222", "010-2222-2222", "abc222@gmail.com", 2L, 2L,
                ZonedDateTime.now()); // 아이디 중복
        User user3 = new User(null, "user3", "test33", "12345678", "010-3333-3333", "abc333@gmail.com", 2L, 2L,
                ZonedDateTime.now()); // 영문자 미포함
        User user4 = new User(null, "user4", "test44", "abcdefgh", "010-4444-4444", "abc444@gmail.com", 2L, 2L,
                ZonedDateTime.now()); // 숫자 미포함
        User user5 = new User(null, "user5", "test55", "abc123!@", "010-5555-5555", "abc555@gmail.com", 2L, 2L,
                ZonedDateTime.now()); // 영문자, 숫자 제외한 문자 포함
        User user6 = new User(null, "user6", "test66", "abc123", "010-6666-6666", "abc666@gmail.com", 3L, 3L,
                ZonedDateTime.now()); // 8자 미만

        User find1 = userService.createUser(user1);

        assertThat(find1.getName()).isEqualTo(user1.getName());
        assertThatThrownBy(() -> userService.createUser(user2))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.DUPLICATE_USER_ID.getMessage());
        assertThatThrownBy(() -> userService.createUser(user3))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_PASSWORD_ALP.getMessage());
        assertThatThrownBy(() -> userService.createUser(user4))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_PASSWORD_NUM.getMessage());
        assertThatThrownBy(() -> userService.createUser(user5))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_PASSWORD_SPE.getMessage());
        assertThatThrownBy(() -> userService.createUser(user6))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.INVALID_PASSWORD_LEN.getMessage());

    }

    @Test
    void read() {
        int size1 = userService.getAllUsers().size();
        int size2 = userService.getAllUsersByContestId(3L).size();

        User user1 = new User(null, "user1", "test11", "test1111", "010-1111-1111", "abc111@gmail.com", 1L, -1L,
                ZonedDateTime.now());
        User user2 = new User(null, "user2", "test22", "test2222", "010-2222-2222", "abc222@gmail.com", 2L, 2L,
                ZonedDateTime.now());
        User user3 = new User(null, "user3", "test33", "test3333", "010-3333-3333", "abc333@gmail.com", 3L, 3L,
                ZonedDateTime.now());
        User user4 = new User(null, "user4", "test44", "test4444", "010-4444-4444", "abc444@gmail.com", 3L, 3L,
                ZonedDateTime.now());
        User user5 = new User(null, "user5", "test55", "test5555", "010-5555-5555", "abc555@gmail.com", 3L, 3L,
                ZonedDateTime.now());

        userService.createUser(user1);
        userService.createUser(user2);
        userService.createUser(user3);
        userService.createUser(user4);
        userService.createUser(user5);

        User find1 = userService.getUserById(user1.getId());
        UserDTO find2 = userService.getUserByUserId("test22");
        User find3 = userService.getUserByUserIdAndUserPw(new UserLoginDTO("test33", "test3333"));
        List<User> find4 = userService.getAllUsers();
        List<User> find5 = userService.getAllUsersByContestId(3L);

        assertThat(find1.getName()).isEqualTo(user1.getName());
        assertThat(find2.getEmail()).isEqualTo(user2.getEmail());
        assertThat(find3.getPhone()).isEqualTo(user3.getPhone());
        assertThat(find4.size() - size1).isEqualTo(5);
        assertThat(find5.size() - size2).isEqualTo(3);
        for (User user : find5) {
            assertThat(user.getContestId()).isEqualTo(3);
        }

        assertThatThrownBy(() -> userService.getUserById(null))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.USER_NOT_FOUND.getMessage());
        assertThatThrownBy(() -> userService.getUserByUserId("null"))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.USER_NOT_FOUND.getMessage());
        assertThatThrownBy(() -> userService.getUserByUserIdAndUserPw(new UserLoginDTO("null", "null")))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.USER_NOT_FOUND.getMessage());
        assertThatThrownBy(() -> userService.getAllUsersByContestId(null))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.USER_NOT_FOUND.getMessage());
    }

    @Test
    void update() {
        User user1 = new User(null, "user1", "test11", "test1111", "010-1111-1111", "abc111@gmail.com", 1L, -1L,
                ZonedDateTime.now());
        User user2 = new User(null, "user2", "test22", "test2222", "010-2222-2222", "abc222@gmail.com", 2L, 2L,
                ZonedDateTime.now());

        userService.createUser(user1);
        userService.createUser(user2);

        user1.setName("updqte1");
        user1.setEmail("updqte111@gmail.com");
        user1.setAuthority(8L);

        User noExistUser2 = new User(null, "user2", "test22", "test2222", "010-0000-0000", "test@gmail.com", 0L, 0L,
                ZonedDateTime.now());

        User update1 = userService.updateUser(user1);

        assertThat(update1.getName()).isEqualTo(user1.getName());
        assertThatThrownBy(() -> userService.updateUser(noExistUser2))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.USER_NOT_FOUND.getMessage());
    }

    @Test
    void delete() {
        User user1 = new User(null, "user1", "test11", "test1111", "010-1111-1111", "abc111@gmail.com", 1L, -1L,
                ZonedDateTime.now());
        User user2 = new User(null, "user2", "test22", "test2222", "010-2222-2222", "abc222@gmail.com", 2L, 2L,
                ZonedDateTime.now());

        userService.createUser(user1);
        userService.createUser(user2);

        userService.deleteUser(user1.getId());
        assertThatThrownBy(() -> userService.deleteUser(-1L))
                .isInstanceOf(CustomException.class)
                .hasMessage(ErrorCode.USER_NOT_FOUND.getMessage());
    }
}