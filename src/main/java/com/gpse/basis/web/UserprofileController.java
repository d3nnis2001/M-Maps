package com.gpse.basis.web;

import com.gpse.basis.domain.UserModel;
import com.gpse.basis.services.UserServices;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

/**
 * Admin Controller for all REST API's in the UserConfig.
 */
@RestController
@RequestMapping("/api/userprofile")
public class UserprofileController {


    private final UserServices userService;

    public UserprofileController(UserServices userService) {
        this.userService = userService;
    }

    @Operation(summary = "Lädt einen einzelnen Nutzer",
        description = "Funktion um den ausgewählten Nutzer beim mounten der Seite zu laden")
    @GetMapping("/getUserByUsername")
    public UserModel getUserByUsername(final WebRequest request) {
        String userName = "userName";
        return userService.getUserByUsername(request.getParameter(userName));
    }
}
