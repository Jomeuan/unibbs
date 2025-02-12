package com.jomeuan.unibbs.profile;

import java.util.Random;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.jomeuan.unibbs.entity.ProfilePo;
import com.jomeuan.unibbs.profile.controller.ProfileController;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
public class UnibbsProfileApplicationTest {

    @Autowired ProfileController profileController;
    @Test
    public void setProfileTest() throws Exception {
        ProfilePo profilePo = new ProfilePo();
        profilePo.setId(1L);
        profilePo.setEmail("example@example");

        profileController.setProfile(profilePo);
    }

    @Test 
    public void newProfileTest() {
        Random random = new Random();
        ProfilePo profilePo = new ProfilePo();
        profilePo.setId(random.nextLong(0,Long.MAX_VALUE));
        profilePo.setEmail("example1@example");
        // profileController.newProfile(profilePo,null);
    }

}