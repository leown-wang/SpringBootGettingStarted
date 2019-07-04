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
//    private Map<String,User> users = new ConcurrentHashMap<>();
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private UserMapper userMapper;
    @Inject
    public UserService(BCryptPasswordEncoder bCryptPasswordEncoder,UserMapper userMapper) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userMapper = userMapper;
//        save("user","123");
                
//        save("user","123");
    }

    public void save(String username,String password){
        userMapper.save(username,bCryptPasswordEncoder.encode(password));
//        users.put(username,new User(1,username,bCryptPasswordEncoder.encode(password)));
    }


    public User getUserByUsername(String username){
        return userMapper.findUserByname(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = getUserByUsername(username);
        if (user == null){
            throw  new UsernameNotFoundException(username+"not exist!");
        }
        return  new org.springframework.security.core.userdetails.User(username,user.getPassword(), Collections.emptyList());
    }
}
