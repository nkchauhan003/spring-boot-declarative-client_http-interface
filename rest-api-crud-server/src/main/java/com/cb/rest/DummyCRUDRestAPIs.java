package com.cb.rest;

import com.cb.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user")
@Slf4j
public class DummyCRUDRestAPIs {

    @PostMapping("/")
    public User saveUser(@RequestBody User user) {
        // code to save data in DB
        return user;
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable Integer userId) {
        // code to get user by id from the DB
        return new User(userId, "Tech", "tb@burps.com");
    }

    @GetMapping("/")
    public List<User> getuserList() {
        // code to get user list from the DB
        return List.of(
                new User(1, "Tech-1", "tb-1@burps.com"),
                new User(2, "Tech-2", "tb-2@burps.com"),
                new User(3, "Tech-3", "tb-3@burps.com")
        );
    }

    @PutMapping("/")
    public User updateUser(@RequestBody User user) {
        // code to get and update user in the DB
        return user;
    }

    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable Integer userId) {
        // code to delete user in the DB
        return "OK!";
    }

    // Accepting a header in Spring Boot REST API
    @GetMapping("/name/{userId}")
    public String getName(
            @PathVariable Integer userId,
            @RequestHeader String language) {
        log.info("Header, language: " + language);
        // code to get user by id from the DB
        // logic to convert name as per "language" header
        return "Tech";
    }

}
