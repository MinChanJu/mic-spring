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
        user.setName("민찬");
        user.setUserId("chany");
        user.setUserPw("chany");
        user.setPhone("010");
        user.setEmail("naver");
        user.setAuthority(1L);
        user.setContestId(-1L);
        user.setCreatedAt(ZonedDateTime.now());

        userService.createUser(user);

        System.out.println(user.getId());
        System.out.println("----------------------");
    }
}
