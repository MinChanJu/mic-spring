package com.example.my_gradle_spring_app.service;

import java.time.ZonedDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.my_gradle_spring_app.model.User;

@SpringBootTest
@Transactional
public class UserServiceTest {

    @Autowired private UserService userService;

    @Test
    void 회원가입() {
        User user = new User();
        user.setId(-1L);
        user.setName("주민찬");
        user.setUserId("chany0207");
        user.setUserPw("chany020207");
        user.setPhone("01085927570");
        user.setEmail("mcj00220077@gmail.com");
        user.setAuthority(5L);
        user.setContestId(-1L);
        user.setCreatedAt(ZonedDateTime.now());

        User find = userService.createUser(user);

        System.out.println(find == null);
        System.out.println("----------------------");
    }
}
