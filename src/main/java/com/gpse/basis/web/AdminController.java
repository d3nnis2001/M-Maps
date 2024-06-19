package com.gpse.basis.web;

import com.gpse.basis.domain.InspectionOrder;
import com.gpse.basis.domain.UserModel;
import com.gpse.basis.services.UserServices;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import java.lang.reflect.Array;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final UserServices userService;

    public AdminController(UserServices userService) {
        this.userService = userService;
    }
    @Operation(summary = "Lädt Nutzerdaten", description = "Funktion um alle Nutzerdaten beim mounten der Seite in die Tabelle zu laden")
    @GetMapping("/getUserData")
    public ArrayList<UserModel> getAllUserData() {
        return userService.getAllUsers();
    }

    @GetMapping("/getUserByUsername")
    public UserModel getUserByUsername(final WebRequest request) {
        return userService.loadUserByUsername(request.getParameter("userName"));
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<Boolean> deleteUser(final WebRequest request) {
        String username = request.getParameter("username");
        return ResponseEntity.ok(userService.deleteUser(username));
    }

    @PostMapping("/updateRoles")
    public ResponseEntity<Boolean> updateRoles(final WebRequest request) {
        String rolesString = request.getParameter("roles");
        assert rolesString != null;
        String[] rolesList = rolesString.split(",");
        ArrayList<String> roles = new ArrayList<>();
        for (String role : rolesList) {
            roles.add(role.trim());
        }
        String username = request.getParameter("username");
        userService.updateRoles(username, roles);
        return ResponseEntity.ok(true);
    }
}
