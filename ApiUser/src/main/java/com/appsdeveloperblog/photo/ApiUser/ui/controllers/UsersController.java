package com.appsdeveloperblog.photo.ApiUser.ui.controllers;

import com.appsdeveloperblog.photo.ApiUser.ui.model.CreateUserModel;
import com.appsdeveloperblog.photo.ApiUser.ui.model.UserDto;
import com.appsdeveloperblog.photo.ApiUser.ui.model.UserResponseModel;
import com.appsdeveloperblog.photo.ApiUser.ui.service.UsersService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UsersController {

    @Autowired
    private Environment evn;

    @Value("${token.secret}")
    private String tokenSecret;

    private UsersService usersService;

    @Autowired
    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/status/ok")
    public String status(){
        String envPropertyPort = evn.getProperty("local.server.port");
        return "Start up...working on port " + envPropertyPort + ", Token Secrete: " + tokenSecret;
    }

    @PostMapping
    public ResponseEntity<UserResponseModel> createUser(@Valid @RequestBody CreateUserModel createUser){

        // 1. new Mapper; 84. ModelMapper explained
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        // 2. CreateUserModel which contain validation annotation -> convert to DTO
        UserDto userDto = modelMapper.map(createUser, UserDto.class);

        //3. service accept DTO object & returns updated DTO object
        UserDto returnData = usersService.createUser(userDto);

        //4. Convert to UserResponseModel
        UserResponseModel userResponse = modelMapper.map(returnData, UserResponseModel.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }


}
