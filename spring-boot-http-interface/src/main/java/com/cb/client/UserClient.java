package com.cb.client;

import com.cb.client.model.User;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.*;

import java.util.List;

@HttpExchange(
        url = "/user",
        accept = MediaType.APPLICATION_JSON_VALUE)
public interface UserClient {

    @PostExchange("/")
    public User saveUser(@RequestBody User user);

    @GetExchange("/{userId}")
    public User getUserById(@PathVariable Integer userId);

    @GetExchange("/")
    public List<User> getuserList();

    @PutExchange("/")
    public User updateUser(@RequestBody User user);

    @DeleteExchange("/{userId}")
    public String deleteUser(@PathVariable Integer userId);
}
