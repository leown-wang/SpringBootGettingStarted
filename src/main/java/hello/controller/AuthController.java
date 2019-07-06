package hello.controller;

import hello.entity.Result;
import hello.entity.User;
import hello.service.UserService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
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
       // String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loggedInUser = userService.getUserByUsername(authentication == null?null:authentication.getName());
        if (loggedInUser == null){
            return Result.failure("unlogin！");
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
            return   Result.failure("unlogin!");
        }else {
            SecurityContextHolder.clearContext();
            return Result.success("logout success!");
        }
    }

    @PostMapping("/auth/register")
    @ResponseBody
    public  Result register(@RequestBody Map<String,Object> usernameAndPasswordJson){
        String username = (String) usernameAndPasswordJson.get("username");
        String password = (String) usernameAndPasswordJson.get("password");
        if (username == null || password == null){
            return   Result.failure("null illegal!");
        }
        if (username.length()<1||username.length()>15){
            return  Result.failure("invalid username!");
        }
        if (password.length()<1||password.length()>15){
            return   Result.failure("invalid password!");
        }

        User user  = userService.getUserByUsername(username);

       try {
            userService.save(username,password);
       }catch (DuplicateKeyException e){
           return   Result.failure("user exist");
        }
        return Result.success("login success");
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
                return   Result.failure("user not exist");
            }
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails,password,userDetails.getAuthorities());
            try{
                authenticationManager.authenticate(usernamePasswordAuthenticationToken);
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                return new Result("ok","login success",true,userService.getUserByUsername(username));
            }catch (BadCredentialsException e){
                return  Result.failure("password wrong！");
            }
    }

}
