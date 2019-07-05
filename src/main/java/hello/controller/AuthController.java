package hello.controller;

import hello.entity.User;
import hello.service.UserService;
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
import javax.naming.spi.ResolveResult;
import java.util.Map;

@Controller
public class AuthController {
    private UserService userService;
    private AuthenticationManager authenticationManager;

    @Inject
    public AuthController(UserService userService, AuthenticationManager authenticationManager) {
        this.userService = userService;

        this.authenticationManager = authenticationManager;

    }

    @GetMapping("/auth")
    @ResponseBody
    public  Object auth(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User loggedInUser = userService.getUserByUsername(username);
        if (loggedInUser == null){
            return new Result("ok","unlogin！",false);
        }else {
            return  new Result("ok",null,true,loggedInUser);
        }

    }

    @GetMapping("/auth/logout")
    @ResponseBody
    public Result logout(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User loggedInUser = userService.getUserByUsername(username);

        if(loggedInUser == null){
            return  new Result("fail","unlogin!",false);
        }else {
            SecurityContextHolder.clearContext();
            return new Result("ok","success",false);
        }
    }

    @PostMapping("/auth/register")
    @ResponseBody
    public  Result register(@RequestBody Map<String,Object> usernameAndPasswordJson){
        String username = (String) usernameAndPasswordJson.get("username");
        String password = (String) usernameAndPasswordJson.get("password");
        if (username == null || password == null){
            return  new Result("fail","null illegal!",false);
        }
        if (username.length()<1||username.length()>15){
            return  new Result("fail","invalid username!",false);
        }
        if (password.length()<1||password.length()>15){
            return  new Result("fail","invalid password!",false);
        }

        User user  = userService.getUserByUsername(username);

        if (user == null){
            userService.save(username,password);
            return new Result("ok","login success",false);
        }else {
            return  new Result("fail","user exist",false);
        }
}

    @PostMapping("/auth/login")
    @ResponseBody
    public Result login(@RequestBody Map<String,Object> usernameAndPasswordJson){
            String  username = (String) usernameAndPasswordJson.get("username");
            String password = (String) usernameAndPasswordJson.get("password");
            UserDetails userDetails = null;
            try{
               userDetails = userService.loadUserByUsername(username);
            }catch (UsernameNotFoundException e){
                return  new Result("fail","user not exist",false);
            }
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails,password,userDetails.getAuthorities());
            try{
                authenticationManager.authenticate(usernamePasswordAuthenticationToken);
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                return new Result("ok","login success",true,userService.getUserByUsername(username));
            }catch (BadCredentialsException e){
                return new Result("ok","password wrong！",false);
            }
    }
    private static  class Result{
        String status;
        String msg;
        boolean isLogin;
        Object data;

        public Object getData() {
            return data;
        }

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
