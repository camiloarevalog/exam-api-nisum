package com.exam.nisum.user.api.controller;

import com.exam.nisum.user.api.dto.request.UserRequestDTO;
import com.exam.nisum.user.api.dto.response.UserResponseDTO;
import com.exam.nisum.user.api.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/nisum/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private UserService userService;


    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getUsers() {
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> saveUser(@RequestBody UserRequestDTO userRequestDTO) {
        return new ResponseEntity<>(userService.saveUser(userRequestDTO.toModel()), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<UserResponseDTO> updateUser(@RequestBody UserRequestDTO userRequestDTO) {
        return new ResponseEntity<>(userService.updateUser(userRequestDTO.toModel()), HttpStatus.OK);
    }
}
