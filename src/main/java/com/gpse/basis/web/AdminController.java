package com.gpse.basis.web;

import com.gpse.basis.domain.UserModel;
import com.gpse.basis.services.UserServices;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final UserServices userService;

    public AdminController(UserServices userService) {
        this.userService = userService;
    }
    @Operation(summary = "Lädt Nutzerdaten", description = "Funktion "
       + "um alle Nutzerdaten beim mounten der Seite in die Tabelle zu laden")
    @GetMapping("/getUserData")
    public ArrayList<UserModel> getAllUserData() {
        return userService.getAllUsers();
    }

    @GetMapping("/getUserById")
    public UserModel getUserById(final WebRequest request) {
        //UserModel user = userService.loadUserByUsername(request.getParameter("username"));
        //return user;
        return null;
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<Boolean> deleteUser(final WebRequest request) {
        String username = request.getParameter("username");
        return ResponseEntity.ok(userService.deleteUser(username));
    }
}
