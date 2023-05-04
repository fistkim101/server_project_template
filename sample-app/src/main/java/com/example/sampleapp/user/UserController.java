package com.example.sampleapp.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @GetMapping("/user")
    public String index() {
        return "hello";
    }

    @PostMapping("/user")
    public User create(@RequestBody User user) {
        System.out.println("UserController.create >>> " + user.toString());
        return new User("hihi", 50);
    }

    @Getter
    @Setter
    static class User {
        String name;
        int age;

        public User() {
        }

        public User(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public String toString() {
            return "User{" +
                    "name='" + name + '\'' +
                    ", age=" + age +
                    '}';
        }
    }

}
