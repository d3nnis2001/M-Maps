package com.gpse.basis.web;

import com.gpse.basis.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {
    private UserServices userService;
    @Autowired
    public UserController(UserServices userService) {
        this.userService = userService;
    }
    @GetMapping ("/authenticate")
    public ResponseEntity<Boolean> authenticate(@RequestParam String email) {
        boolean exists = userService.checkExistanceEmail(email.trim());
        return ResponseEntity.ok(exists);
    }
}
