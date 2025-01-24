package com.upload.upload.services;

import com.upload.upload.Repository.UserRepository;
import com.upload.upload.entities.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@Service
@CrossOrigin
public class UserService {
  @Autowired
  private UserRepository repository;

  public void save(UserEntity userEntity){
    repository.save(userEntity);
  }

  public List<UserEntity> list() {
    return repository.findAll(Sort.by(Sort.Direction.ASC, "name")); // Replace 'fieldName' with the property to sort by
  }

  public UserEntity findByUsername(String name){
    return repository.findByUsername(name);
  }

  public void deleteById(int id){
    repository.deleteById(id);
  }

  public UserEntity findById(int id){
    return repository.findById(id).orElse(null);
  }
}
