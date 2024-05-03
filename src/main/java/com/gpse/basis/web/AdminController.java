package com.gpse.basis.web;

import com.gpse.basis.domain.UserModel;
import com.gpse.basis.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.reflect.Array;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final UserServices userService;

    public AdminController(UserServices userService) {
        this.userService = userService;
    }
    @GetMapping("/getuserdata")
    public ArrayList<UserModel> getAllUserData() {
        return userService.getAllUsers();
    }


}
