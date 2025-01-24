package com.upload.upload.Repository;

import com.upload.upload.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<UserEntity, Integer> {
  public UserEntity findByUsername(String username);
}
