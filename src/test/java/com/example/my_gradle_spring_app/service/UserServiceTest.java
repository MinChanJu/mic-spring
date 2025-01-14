package com.example.my_gradle_spring_app.service;

import com.example.my_gradle_spring_app.exception.CustomException;
import com.example.my_gradle_spring_app.model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired private UserService userService;
    @Autowired private DataSource dataSource;

    @BeforeEach
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
        User user1 = new User(-1L, "user1", "test1", "test1111", "010-1111-1111", "abc111@gmail.com", 1L, -1L, ZonedDateTime.now());
        User user2 = new User(-1L, "user2", "test1", "test2222", "010-2222-2222", "abc222@gmail.com", 2L, 2L, ZonedDateTime.now());     // 아이디 중복
        User user3 = new User(-1L, "user3", "test3", "test333", "010-3333-3333", "abc333@gmail.com", 3L, 3L, ZonedDateTime.now());      // 안되는 비밀번호

        User find1 = userService.createUser(user1);
        
        assertThat(find1.getName()).isEqualTo(user1.getName());
        assertThatThrownBy(() -> userService.createUser(user2))
            .isInstanceOf(CustomException.class)
            .hasMessage("이미 존재하는 사용자 ID입니다.");
        assertThatThrownBy(() -> userService.createUser(user3))
            .isInstanceOf(CustomException.class)
            .hasMessage("비밀번호의 길이가 8자 미만입니다.");

    }

    @Test
    void read() {
        int size1 = userService.getAllUsers().size();
        int size2 = userService.getAllUsersByContestId(3L).size();
        User user1 = new User(-1L, "user1", "test1", "test1111", "010-1111-1111", "abc111@gmail.com", 1L, -1L, ZonedDateTime.now());
        User user2 = new User(-1L, "user2", "test2", "test2222", "010-2222-2222", "abc222@gmail.com", 2L, 2L, ZonedDateTime.now());
        User user3 = new User(-1L, "user3", "test3", "test3333", "010-3333-3333", "abc333@gmail.com", 3L, 3L, ZonedDateTime.now());
        User user4 = new User(-1L, "user4", "test4", "test4444", "010-4444-4444", "abc444@gmail.com", 3L, 3L, ZonedDateTime.now()); 
        User user5 = new User(-1L, "user5", "test5", "test5555", "010-5555-5555", "abc555@gmail.com", 3L, 3L, ZonedDateTime.now()); 

        User sub = userService.createUser(user1);
        userService.createUser(user2);
        userService.createUser(user3);
        userService.createUser(user4);
        userService.createUser(user5);

        User find1 = userService.getUserById(sub.getId());
        User find2 = userService.getUserByUserId("test2");
        List<User> find3 = userService.getAllUsers();
        List<User> find4 = userService.getAllUsersByContestId(3L);
        
        assertThat(find1.getName()).isEqualTo(user1.getName());
        assertThat(find2.getUserPw()).isEqualTo(user2.getUserPw());
        assertThat(find3.size()-size1).isEqualTo(5);
        assertThat(find4.size()-size2).isEqualTo(3);
        for (User user : find4) assertThat(user.getContestId()).isEqualTo(3);

    }

    @Test
    void update() {
        User user1 = new User(-1L, "user1", "test1", "test1111", "010-1111-1111", "abc111@gmail.com", 1L, -1L, ZonedDateTime.now());
        User user2 = new User(-1L, "user2", "test2", "test2222", "010-2222-2222", "abc222@gmail.com", 2L, 2L, ZonedDateTime.now());

        User find1 = userService.createUser(user1);
        User find2 = userService.createUser(user2);

        assertThat(find1.getName()).isEqualTo(user1.getName());
        assertThat(find2.getUserPw()).isEqualTo(user2.getUserPw());

        user1.setId(find1.getId());
        user1.setName("updqte1");
        user1.setEmail("updqte111@gmail.com");
        user1.setAuthority(8L);

        user2.setId(15L);
        user2.setName("updqte2");
        user2.setEmail("updqte222@gmail.com");
        user2.setAuthority(14L);

        User update1 = userService.updateUser(user1);

        assertThat(update1.getName()).isEqualTo(user1.getName());
        assertThatThrownBy(() -> userService.updateUser(user2))
            .isInstanceOf(CustomException.class)
            .hasMessage("사용자를 찾을 수 없습니다.");
    }

    @Test
    void delete() {
        User user1 = new User(-1L, "user1", "test1", "test1111", "010-1111-1111", "abc111@gmail.com", 1L, -1L, ZonedDateTime.now());
        User user2 = new User(-1L, "user2", "test2", "test2222", "010-2222-2222", "abc222@gmail.com", 2L, 2L, ZonedDateTime.now());

        User find1 = userService.createUser(user1);
        User find2 = userService.createUser(user2);

        assertThat(find1.getName()).isEqualTo(user1.getName());
        assertThat(find2.getUserPw()).isEqualTo(user2.getUserPw());

        userService.deleteUser(find1.getId());
        assertThatThrownBy(() -> userService.deleteUser(15L))
            .isInstanceOf(CustomException.class)
            .hasMessage("사용자를 찾을 수 없습니다.");
    }
}