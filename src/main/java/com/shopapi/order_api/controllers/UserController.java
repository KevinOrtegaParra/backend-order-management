package com.shopapi.order_api.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopapi.order_api.dtos.users.UserRequestDTO;
import com.shopapi.order_api.dtos.users.UserRequestUpdateDTO;
import com.shopapi.order_api.dtos.users.UserResponseDTO;
import com.shopapi.order_api.exceptions.RestException;
import com.shopapi.order_api.services.ifaces.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Tag(name = "Users Controller", description = "Operations related to user management")
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    IUserService userService;

    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "Authorization")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Internal Error Server")
    })
    @Operation(summary = "Show all Users", description = "Endpoint to Show all users")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getUsers() throws RestException {
        return ResponseEntity.ok(userService.getUsers());
    }

    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "Authorization")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Error Server")
    })
    @Operation(summary = "Show a user by their id", description = "Endpoint to Show a user by their id")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUser(@PathVariable Long id) throws RestException {
        return ResponseEntity.ok(userService.findById(id));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "500", description = "Internal Error Server")
    })
    @Operation(summary = "Save a user", description = "Endpoint to save a user")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> postUser(@Valid @RequestBody UserRequestDTO userRequestDTO)
            throws RestException {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(userRequestDTO));
    }

    @PreAuthorize("hasRole('USER')")
    @SecurityRequirement(name = "Authorization")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Error Server")
    })
    @Operation(summary = "Update user", description = "End point to Update your user.")
    @ResponseStatus(HttpStatus.CREATED)
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> putUser(@Valid @RequestBody UserRequestUpdateDTO requestUpdateDTO,
    Authentication authentication) throws RestException {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.update(requestUpdateDTO, authentication ));

    }

    @PreAuthorize("hasRole('ADMIN')")
    @SecurityRequirement(name = "Authorization")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Error Server")
    })
    @Operation(summary = "Delete a user", description = "Endpoint to delete a user")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) throws RestException {
        userService.deletById(id);
        return ResponseEntity.noContent().build();
    }
}
