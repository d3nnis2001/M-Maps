package com.gpse.basis.web;

import com.gpse.basis.domain.InspectionOrder;
import com.gpse.basis.domain.UserModel;
import com.gpse.basis.services.UserServices;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final UserServices userService;
    private static final Logger log = LoggerFactory.getLogger(AdminController.class);

    public AdminController(UserServices userService) {
        this.userService = userService;
    }
    @Operation(summary = "Lädt Nutzerdaten", description = "Funktion um alle Nutzerdaten beim mounten der Seite in die Tabelle zu laden")
    @GetMapping("/getUserData")
    public ArrayList<UserModel> getAllUserData() {
        return userService.getAllUsers();
    }

    /*
    @GetMapping("/getUserByUsername")
    @ResponseBody
    public UserModel getUserByUsername(final WebRequest request) {
        log.debug("Request to show user with email: {}", request.getParameter("userName"));
        return userService.loadUserByUsername(request.getParameter("userName"));
    }

     */
    @GetMapping("/getUserByUsername")
    public UserModel getUserByUsername(final String username) {
        return userService.getUserByUsername(username);
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<Boolean> deleteUser(final WebRequest request) {
        String username = request.getParameter("username");
        return ResponseEntity.ok(userService.deleteUser(username));
    }

    @PostMapping("/updateRoles")
    public ResponseEntity<Boolean> updateRoles(final WebRequest request) {
        String username = request.getParameter("userName");
        if (username == null) {
            return ResponseEntity.badRequest().body(false);
        }
        String rolesString = request.getParameter("roles");
        assert rolesString != null;
        String[] rolesList = rolesString.split(",");
        ArrayList<String> roles = new ArrayList<>();
        Collections.addAll(roles, rolesList);
        userService.updateRoles(username, roles);
        return ResponseEntity.ok(true);
    }
}
