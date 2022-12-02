package com.appsdeveloperblog.photo.ApiUser.ui.repo;

import com.appsdeveloperblog.photo.ApiUser.ui.model.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UsersRepository extends CrudRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);
}
