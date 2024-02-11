package com.cbfacademy.apiassessment.service.serviceImpl;


import com.cbfacademy.apiassessment.dto.Data;
import com.cbfacademy.apiassessment.dto.Response;
import com.cbfacademy.apiassessment.dto.UserRequest;
import com.cbfacademy.apiassessment.dto.UserRequestDto;
import com.cbfacademy.apiassessment.entity.User;
import com.cbfacademy.apiassessment.exceptions.customException.UserAlreadyExists;
import com.cbfacademy.apiassessment.exceptions.customException.UserNotFoundException;
import com.cbfacademy.apiassessment.repository.UserRepository;
import com.cbfacademy.apiassessment.service.UserService;
import com.cbfacademy.apiassessment.utils.ResponseUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private  UserRepository userRepository;
    @Autowired
    private ResponseUtils responseUtils;

//    public UserServiceImpl(UserRepository userRepository,ResponseUtils responseUtils) {
//        this.userRepository = userRepository;
//        this.responseUtils = responseUtils;
//    }

    @Override
    public Response registerUser(UserRequest userRequest) {
        boolean isEmailExist = userRepository.existsByEmail(userRequest.getEmail());
        if (isEmailExist) {
            throw new UserAlreadyExists("User already exists");
        }

        User user = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .otherName(userRequest.getOtherName())
                .gender(userRequest.getGender())
                .address(userRequest.getAddress())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .accountNumber(ResponseUtils.generateAccountNumber(ResponseUtils.lengthOfAccountNumber))
                .accountBalance(userRequest.getAccountBalance())
                .email(userRequest.getEmail())
                .phoneNumber(userRequest.getPhoneNumber())
                .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                .status("ACTIVE")
                .dateOfBirth(userRequest.getDateOfBirth())
                .build();

        userRepository.save(user);
        log.info("saved user {}",user);

        return Response.builder()
                .responseCode(ResponseUtils.SUCCESS)
                .responseMessage(ResponseUtils.USER_REGISTERED_SUCCESS)
                .data(Data.builder()
                        .accountBalance(user.getAccountBalance())
                        .accountNumber(user.getAccountNumber())
                        .accountName(user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName())
                        .build()
                )
                .build();
    }


    @Override
    public List<Response> allUsers() {
        List<User> usersList = userRepository.findAll();
        List<Response> response = new ArrayList<>();
        for (User user : usersList) {
            response.add(Response.builder()
                    .responseCode(ResponseUtils.USER_REGISTERED_SUCCESS)
                    .responseMessage(ResponseUtils.SUCCESS_MESSAGE)
                    .data(Data.builder()
                            .accountName(user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName())
                            .accountNumber(user.getAccountNumber())
                            .accountBalance(user.getAccountBalance())
                            .build())
                    .build());
        }
 return response;
    }

    @Override
    public Response fetchUser(Long id) {
        if (!userRepository.existsById(id)) {
            return Response.builder()
                    .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                    .responseMessage(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .data(null)
                    .build();
        }
        User user = userRepository.findById(id).get();
        return Response.builder()
                .responseMessage(ResponseUtils.SUCCESS_MESSAGE)
                .responseCode(ResponseUtils.SUCCESS)
                .data(Data.builder()
                        .accountNumber(user.getAccountNumber())
                        .accountName(user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName())
                        .accountBalance(user.getAccountBalance())
                        .build())
                .build();
    }

    @Override
    public Response balanceEnquiry(String accountNumber) {
        boolean isAccountNumberExist = userRepository.existsByAccountNumber(accountNumber);
        if (!isAccountNumberExist) {
            return Response.builder()
                    .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                    .responseMessage(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .data(null)
                    .build();
        }
        User user = userRepository.findByAccountNumber(accountNumber);
        return Response.builder()
                .responseMessage(ResponseUtils.SUCCESS_MESSAGE)
                .responseCode(ResponseUtils.SUCCESS)
                .data(Data.builder()
                        .accountBalance(user.getAccountBalance())
                        .accountNumber(user.getAccountNumber())
                        .accountName(user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName())
                        .build())
                .build();
    }

    @Override
    public Response nameEnquiry(String accountNumber) {
        boolean isAccountNumberExist = userRepository.existsByAccountNumber(accountNumber);
        if (!isAccountNumberExist) {
            return Response.builder()
                    .responseCode(ResponseUtils.USER_NOT_FOUND_CODE)
                    .responseMessage(ResponseUtils.USER_NOT_FOUND_MESSAGE)
                    .data(null)
                    .build();
        }
        User user = userRepository.findByAccountNumber(accountNumber);
        return Response.builder()
                .responseMessage(ResponseUtils.SUCCESS_MESSAGE)
                .responseCode(ResponseUtils.SUCCESS)
                .data(Data.builder()
                        .accountBalance(null)
                        .accountNumber(user.getAccountNumber())
                        .accountName(user.getFirstName() + " " + user.getLastName() + " " + user.getOtherName())
                        .build())
                .build();
    }

    @Override
    public String deleteUser(Long id) {
        User user  = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        userRepository.deleteById(id);
        return "User has been deleted";

    }

    @Override
    public Response updateUser(long id, UserRequestDto userRequestDTO) {
        User userEntity = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        mapToUserEntity(userRequestDTO, userEntity);
        userRepository.save(userEntity);

        return Response.builder()
                .responseMessage(ResponseUtils.SUCCESS_MESSAGE)
                .responseCode(ResponseUtils.SUCCESS)
                .data(Data.builder()
                        .accountNumber(userEntity.getAccountNumber())
                        .accountName(userEntity.getFirstName() + " " + userEntity.getLastName() + " " + userEntity.getOtherName())
                        .accountBalance(userEntity.getAccountBalance())
                        .build())
                .build();
    }

    private User mapToUserEntity(UserRequestDto userRequestDTO, User userEntity) {

        userEntity.setFirstName(userRequestDTO.getFirstName());
        userEntity.setOtherName(userRequestDTO.getOtherName());
        userEntity.setLastName(userRequestDTO.getLastName());
        userEntity.setPhoneNumber(userRequestDTO.getPhoneNumber());
        return userEntity;
    }
}
