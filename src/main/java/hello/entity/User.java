package hello.entity;

import java.time.Instant;

public class User {
    Integer id;
    String username;
    String password;
    String avatar;
    Instant createAt;
    Instant updateAt;

    public User(Integer id, String username,String password) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.avatar = "";
        this.createAt = Instant.now();
        this.updateAt = Instant.now();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Instant getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Instant createAt) {
        this.createAt = createAt;
    }

    public Instant getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Instant updateAt) {
        this.updateAt = updateAt;
    }
}
