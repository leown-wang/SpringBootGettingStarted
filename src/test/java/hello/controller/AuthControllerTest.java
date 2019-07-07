package hello.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.servlet.http.HttpSession;
import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
class AuthControllerTest {
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;


    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
    @BeforeEach
    void SetUp(){
        mockMvc = MockMvcBuilders.standaloneSetup(new AuthController(userService,authenticationManager)).build();
    }

    @Test
    void testLogin() throws Exception {
        mockMvc.perform(get("/auth")) .andExpect(status().isOk()).andExpect(mvcResult ->
                Assertions.assertTrue(mvcResult.getResponse().getContentAsString().contains("unlogin")));

        Map<String,String> usernamePassword = new HashMap<>();
        usernamePassword.put("username","MyUser");
        usernamePassword.put("password","MyPassword");

        Mockito.when(userService.loadUserByUsername("MyUser")).
                thenReturn(new User("MyUser", bCryptPasswordEncoder.encode("MyPassword"),Collections.emptyList()));
        Mockito.when(userService.getUserByUsername("MyUser")).
                thenReturn(new hello.entity.User(123, "MyUser","MyPassword"));

        System.out.println(new ObjectMapper().writeValueAsString(usernamePassword));
        MvcResult response = mockMvc.perform(post("/auth/login").contentType
                (MediaType.APPLICATION_JSON_UTF8).content(new ObjectMapper().writeValueAsString(usernamePassword) ))
                .andExpect(status().isOk()).andExpect(
                        new ResultMatcher() {
                            @Override
                            public void match(MvcResult mvcResult) throws Exception {
                                Assertions.assertTrue(mvcResult.getResponse().getContentAsString().contains("login success"));
                            }
                        }
                ).andReturn();

        HttpSession session = response.getRequest().getSession();

        mockMvc.perform(get("/auth").session((MockHttpSession) session)) .andExpect(status().isOk()).andExpect(mvcResult ->
                Assertions.assertTrue(mvcResult.getResponse().getContentAsString().contains("MyUser")));
    }
}