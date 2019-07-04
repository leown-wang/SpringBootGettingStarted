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
    private Map<String,User> users = new ConcurrentHashMap<>();
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
        users.put(username,new User(1,username,bCryptPasswordEncoder.encode(password)));
    }


    public User getUserByUsername(String username){
        return users.get(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!users.containsKey(username)){
            throw  new UsernameNotFoundException(username+"not exist!");
        }
       User user = users.get(username);
        return  new org.springframework.security.core.userdetails.User(username,user.getPassword(), Collections.emptyList());
    }
}
