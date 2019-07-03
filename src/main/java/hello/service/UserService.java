package hello.service;

import hello.entity.User;
import hello.mapper.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Service
public class UserService implements UserDetailsService {
    private Map<String,String> userPasswords = new ConcurrentHashMap<>();
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Inject
    public UserService(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        save("gebilaowang","gebilaowang");
    }

//    public UserService(){
//        userPasswords.put("gebilaowang","gebilaowang");
//    }

    public void save(String username,String password){
        userPasswords.put(username,bCryptPasswordEncoder.encode(password));
    }

    public String getPassword(String username){
        return userPasswords.get(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!userPasswords.containsKey(username)){
            throw  new UsernameNotFoundException(username+"not exist!");
        }
       String password = userPasswords.get(username);
        return  new org.springframework.security.core.userdetails.User(username,password, Collections.emptyList());
    }
}
