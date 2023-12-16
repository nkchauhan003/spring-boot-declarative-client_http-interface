package com.cb.controller;

import com.cb.client.UserClient;
import com.cb.client.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("user-client")
public class UserController {

    @Autowired
    private UserClient userClient;

    @PostMapping("/")
    public User saveUser(@RequestBody User user) {
        return userClient.saveUser(user);
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable Integer userId) {
        return userClient.getUserById(userId);
    }

    @GetMapping("/")
    public List<User> getuserList() {
        return userClient.getuserList();
    }

    @PutMapping("/")
    public User updateUser(@RequestBody User user) {
        return userClient.updateUser(user);
    }

    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable Integer userId) {
        return userClient.deleteUser(userId);
    }

    @GetMapping("/name/{userId}")
    public String getName(@PathVariable Integer userId) {
        return userClient.getName(userId, Map.of("language", "EN-us"));
    }
}
