package com.gpse.basis.web;

import com.gpse.basis.domain.UserModel;
import com.gpse.basis.services.EmailServices;
import com.gpse.basis.services.UserServices;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;

@RestController
@RequestMapping("/api")
public class UserController {
    private String email_string = "email";
    private String passwort_string = "password";
    private final UserServices userService;
    private final EmailServices emailService;
    @Autowired
    public UserController(final UserServices userService, final EmailServices emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }
    @Operation(summary = "Authentifizierung", description = "Überprüft die Existenz einer E-Mail-Adresse.")
    @GetMapping ("/authenticate")
    public ResponseEntity<Boolean> authenticate(@RequestParam String email) {
        boolean exists = userService.checkExistanceEmail(email.trim());
        return ResponseEntity.ok(exists);
    }
    @PostMapping("/register")
    public ResponseEntity<Boolean> register(final WebRequest request) {
        String email = request.getParameter(email_string);
        String password = request.getParameter(passwort_string);
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String roles = request.getParameter("roles");
        String service = request.getParameter("service");
        String region = request.getParameter("region");

        if (email == null || email.trim().isEmpty()
            || password == null
            || firstName == null
            || lastName == null
            || roles == null
            || service == null
            || region == null) {
            return ResponseEntity.badRequest().body(false);
        }

        UserModel us = new UserModel(email, password, firstName, lastName);
        String[] arrRoles = roles.split(",");
        for (int i = 0; i < arrRoles.length; i++) {
            us.addRole(arrRoles[i]);
        }
        us.setService(service);
        us.addRegion(region);
        if (!userService.checkExistanceEmail(request.getParameter(email_string).trim())) {
            boolean success = userService.addUser(us);
            return ResponseEntity.ok(success);
        }
        return ResponseEntity.ok(false);
    }

    @PostMapping("/login")
    public ResponseEntity<Boolean> login(final WebRequest request) {
        String email = request.getParameter(email_string);
        String password = request.getParameter(passwort_string);
        if (userService.checkCredentials(email, password)) {
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }
    @PostMapping("/user/resetPassword")
    public ResponseEntity<Boolean> changePassword(final WebRequest request) {
        String email = request.getParameter(email_string);
        String token = userService.getToken(email);
        if (userService.checkExistanceEmail(email)) {
            emailService.sendEmailwithToken(email, token);
            return ResponseEntity.ok(true);
        }
        return ResponseEntity.ok(false);
    }
    @PostMapping("/user/setPassword")
    public ResponseEntity<Boolean> resetPassword(final WebRequest request) {
        String password = request.getParameter(passwort_string);
        String token = request.getParameter("token");
        String email = request.getParameter(email_string);
        boolean response = userService.setPasswordNew(email, password, token);
        if (response) {
            return ResponseEntity.ok(true);
        } else {
            return ResponseEntity.ok(false);
        }
    }

    @PostMapping("/user/getToken")
    public String getToken (final WebRequest request) {
        String password = request.getParameter("password");
        String email = request.getParameter("email");

        String token = userService.getToken(email, password);
        System.out.println("TEST-controller: " +token);
        return token;
    }

    @PostMapping("/user/getRolesByToken")
    public ArrayList<String> getRolesByToken (final WebRequest request) {
        String email = request.getParameter("email");
        String token = request.getParameter("token");

        ArrayList<String> roles = userService.getRoles(email, token);
        return roles;
    }
    @PostMapping("/user/getUserByToken")
    public String getUserByToken(final WebRequest request) {
        String token = request.getParameter("token");
        String username = userService.getUserByToken(token);
        return username;
    }
}
