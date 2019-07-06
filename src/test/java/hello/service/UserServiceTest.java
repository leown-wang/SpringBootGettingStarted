package hello.service;

import hello.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

        @Mock
        BCryptPasswordEncoder mockEncoder;
        @Mock
        UserMapper mockMapper;
        @InjectMocks
        UserService userService;

        @Test
        public void  testSave(){

                Mockito.when(mockEncoder.encode("myPassword")).thenReturn("myEncodedPassword");

                userService.save("myUser","myPassword");

                Mockito.verify(mockMapper).save("myUser","myEncodedPassword");
        }
}