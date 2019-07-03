package hello.controller;

import hello.entity.User;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.Map;

@Controller
public class AuthController {
    private UserDetailsService userDetailsService;
    private AuthenticationManager authenticationManager;

    @Inject
    public AuthController(UserDetailsService userDetailsService, AuthenticationManager authenticationManager) {
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;

    }

    @GetMapping("/auth")
    @ResponseBody
    public  Object auth(){
        return new Result("ok","unlogin！",false);
    }
    @PostMapping("/auth/login")
    @ResponseBody
    public Result login(@RequestBody Map<String,Object> usernameAndPasswordJson){
            String  username = (String) usernameAndPasswordJson.get("username");
            String password = (String) usernameAndPasswordJson.get("password");
            UserDetails userDetails = null;
            try{
               userDetails = userDetailsService.loadUserByUsername(username);
            }catch (UsernameNotFoundException e){
                return  new Result("fail","user not exist",false);
            }
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails,password,userDetails.getAuthorities());
            try{
                authenticationManager.authenticate(usernamePasswordAuthenticationToken);
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                User loggedInUser = new User(1,"leown");
                return new Result("OK","login success",true,loggedInUser);
            }catch (BadCredentialsException e){
                return new Result("ok","password wroing！",false);
            }
    }
    private static  class Result{
        String status;
        String msg;
        boolean isLogin;
        Object data;
        public String getStatus() {
            return status;
        }

        public String getMsg() {
            return msg;
        }

        public boolean isLogin() {
            return isLogin;
        }

        public Result(String status, String msg, boolean isLogin) {
            this(status,msg,isLogin,null);
        }

        public Result(String status, String msg, boolean isLogin,Object data) {
            this.status = status;
            this.msg = msg;
            this.isLogin = isLogin;
            this.data = data;
        }
    }



}
