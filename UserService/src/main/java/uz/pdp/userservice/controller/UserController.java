package uz.pdp.userservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.pdp.userservice.entity.User;
import uz.pdp.userservice.payload.UserDto;
import uz.pdp.userservice.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("/{userId}")
    public UserDto getUser(@PathVariable Integer userId){
        return userService.getUser(userId);
    }






}
