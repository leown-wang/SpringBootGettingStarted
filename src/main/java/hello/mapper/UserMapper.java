package hello.mapper;

import hello.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
        import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {


    @Select("select * from user where username = #{username}")
    User findUserByname(@Param("username") String username);

    @Select("insert into user(username,password,created_at,updated_at)"+
            "values(#{username},#{password},now(),now())")
    void save(@Param("username") String username, @Param("password")String encode);
}