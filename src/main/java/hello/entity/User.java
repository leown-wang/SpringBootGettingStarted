package hello.entity;

import java.time.Instant;

public class User {
    Integer id;
    String username;
    String avatar;
    Instant createAt;
    Instant updateAt;

    public User(Integer id, String username) {
        this.id = id;
        this.username = username;
        this.avatar = "";
        this.createAt = Instant.now();
        this.updateAt = Instant.now();
    }

    public Integer getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getAvatar() {
        return avatar;
    }

    public Instant getCreateAt() {
        return createAt;
    }

    public Instant getUpdateAt() {
        return updateAt;
    }
}
