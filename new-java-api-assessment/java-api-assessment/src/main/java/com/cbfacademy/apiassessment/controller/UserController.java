package com.cbfacademy.apiassessment.controller;


import com.cbfacademy.apiassessment.dto.Response;
import com.cbfacademy.apiassessment.dto.UserRequest;
import com.cbfacademy.apiassessment.dto.UserRequestDto;
import com.cbfacademy.apiassessment.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;
    public UserController (UserService userService){
        this.userService = userService;
    }

    @PostMapping
    public Response registerUser(@RequestBody UserRequest userRequest){
        return  userService.registerUser(userRequest);
    }

    @GetMapping
    public List<Response> allUsers(){
        return userService.allUsers();
    }

    @GetMapping("/{userId}")
    public Response fetchUser(@PathVariable("userId") Long userId){
        return userService.fetchUser(userId);
    }

    @GetMapping("/accountNumber")
    public Response balanceEnquiry(@RequestParam(name = "accountNumber") String accountNumber){
        return userService.balanceEnquiry(accountNumber);
    }

    @GetMapping("/nameEnquiry")
    public Response nameEnquiry(@RequestParam(name = "accountNumber")String accountNumber){
        return  userService.nameEnquiry(accountNumber);
    }

    @DeleteMapping("/deleted")
    public String deleteUser(@RequestParam(name = "id") Long id){
        return userService.deleteUser(id);
    }

    @PutMapping("/{id}")
    public Response updateUser(@PathVariable long id,
                                                    @RequestBody UserRequestDto userRequestDto) {

        return userService.updateUser(id,userRequestDto);
    }

    }

