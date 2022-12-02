package com.appsdeveloperblog.photo.ApiUser.ui.service;


import com.appsdeveloperblog.photo.ApiUser.ui.model.UserDto;
import com.appsdeveloperblog.photo.ApiUser.ui.model.UserEntity;
import com.appsdeveloperblog.photo.ApiUser.ui.repo.UsersRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class UsersServiceImpl implements UsersService {

    private Logger log = LoggerFactory.getLogger(UsersServiceImpl.class);

    private UsersRepository usersRepository;
    private BCryptPasswordEncoder bCrypt;

    // 89.In order for bCrypt to be injected in to the constructor.  bCrypt must exist in the application context. Which is done in the main() method
    @Autowired
    public UsersServiceImpl(UsersRepository usersRepository, BCryptPasswordEncoder bCrypt) {
        this.usersRepository = usersRepository;
        this.bCrypt = bCrypt;
    }

    @Override
    public UserDto createUser(UserDto userDetail) {

        // 80.
        String userUUID = UUID.randomUUID().toString();
        userDetail.setUserId(userUUID);
        userDetail.setEncryptedPassword(bCrypt.encode(userDetail.getPassword()));

        // 84. Take information from DTO to Entity where we can persis data to DB
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        // Mapper: DTO -> Entity Class
        UserEntity userEntity = modelMapper.map(userDetail, UserEntity.class);

        // Repo persist Entity Class
        usersRepository.save(userEntity); // Automatically update userEntity with Table id

        // Convert Entity back to DTO
        return modelMapper.map(userEntity, UserDto.class);
    }

    /**
     * 96. This is essentially the same as .findByEmail(email) that you see in loadUserByUsername().
     * But since this method is being used by Spring Security we created another method that returns
     * UserDto for AuthenticationFilter > successfulAuthentication()
     * @param email - String
     * @return UserDto - object that contains user ID
     */
    @Override
    public UserDto getUserDetailsByEmail(String email) {

        UserEntity userEntity = usersRepository.findByEmail(email);
        if(userEntity == null) throw new UsernameNotFoundException(email);
        return new ModelMapper().map(userEntity, UserDto.class);
    }

    /**
     * 95.
     * Spring Security Object [ UserDetailsService ]
     * When spring trying to authenticate a user it will call on this method.
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = usersRepository.findByEmail(email);

        if(userEntity == null) throw new UsernameNotFoundException(email);

        // 7:04 org.springframework.security.core.userdetails.User;
        return new User(userEntity.getEmail(),
                userEntity.getEncryptedPassword(),
                true,
                true,
                true,
                true,
                new ArrayList<>());
    }
}
