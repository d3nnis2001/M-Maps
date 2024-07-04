package com.gpse.basis.web;

import com.gpse.basis.domain.UserModel;
import com.gpse.basis.services.UserServices;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Admin Controller for all REST API's in the UserConfig.
 */
@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final UserServices userService;
    private final String userName = "userName";

    public AdminController(UserServices userService) {
        this.userService = userService;
    }
    @Operation(summary = "Lädt Nutzerdaten",
        description = "Funktion um alle Nutzerdaten beim mounten der Seite in die Tabelle zu laden")
    @GetMapping("/getUserData")
    public ArrayList<UserModel> getAllUserData() {
        return userService.getAllUsers();
    }

    @Operation(summary = "Lädt einen einzelnen Nutzer",
        description = "Funktion um den ausgewählten Nutzer beim mounten der Seite zu laden")
    @GetMapping("/getUserByUsername")
    public UserModel getUserByUsername(final WebRequest request) {
        return userService.getUserByUsername(request.getParameter(userName));
    }

    @Operation(summary = "Löscht Nutzer",
        description = "Funktion um den ausgewählten Nutzer aus der Datenbank zu entfernen")
    @DeleteMapping("/deleteUser")
    public ResponseEntity<Boolean> deleteUser(final WebRequest request) {
        String username = request.getParameter(userName);
        return ResponseEntity.ok(userService.deleteUser(username));
    }

    /**
     * Updates all changes to the user roles.
     * @param request
     * @return boolean
     */
    @Operation(summary = "Aktualisiert die Rollen",
        description = "Funktion um alle Rollenänderung des ausgewählten Nutzer zu aktualisieren")
    @PostMapping("/updateRoles")
    public ResponseEntity<Boolean> updateRoles(final WebRequest request) {
        String username = request.getParameter(userName);
        if (username == null) {
            return ResponseEntity.badRequest().body(false);
        }
        String rolesString = request.getParameter("roles");
        assert rolesString != null;
        String[] rolesList = rolesString.split(",");
        ArrayList<String> roles = new ArrayList<>();
        Collections.addAll(roles, rolesList);
        boolean unlockSuccessful = userService.updateRoles(username, roles);

        return ResponseEntity.ok(unlockSuccessful);
    }

    /**
     * Unlocks the user.
     * @param request
     * @return boolean
     */
    @Operation(summary = "Schaltet einen neuen Nutzer frei",
        description = "Funktion um den ausgewählten Nutzer freizuschalten, damit er die Funktionen der App nutzen kann")
    @PostMapping("/unlockUser")
    public ResponseEntity<Boolean> unlockUser(final WebRequest request) {
        String username = request.getParameter(userName);
        System.out.println(username);
        if (username == null) {
            System.out.println("Bad Request");
            return ResponseEntity.badRequest().body(false);
        }
        boolean unlockSuccessful = userService.unlockUser(username);

        return ResponseEntity.ok(unlockSuccessful);
    }
}
