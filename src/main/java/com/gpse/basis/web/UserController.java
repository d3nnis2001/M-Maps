package com.gpse.basis.web;

import com.gpse.basis.domain.UserModel;
import com.gpse.basis.services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

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
    @PostMapping("/register")
    public ResponseEntity<Boolean> register(final WebRequest request) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String roles = request.getParameter("roles");
        String service = request.getParameter("service");
        String region = request.getParameter("region");

        if (email == null || email.trim().isEmpty() ||
            password == null ||
            firstName == null ||
            lastName == null ||
            roles == null ||
            service == null ||
            region == null) {
            return ResponseEntity.badRequest().body(false);
        }

        UserModel us = new UserModel(email, password, firstName, lastName);
        String[] arrRoles = roles.split(",");
        for (int i = 0;i<arrRoles.length;i++) {
            us.addRole(arrRoles[i]);
        }
        us.setService(service);
        us.addRegion(region);
        if (!userService.checkExistanceEmail(request.getParameter("email").trim())) {
            boolean success = userService.addUser(us);
            return ResponseEntity.ok(success);
        }
        return ResponseEntity.ok(false);
    }
}
