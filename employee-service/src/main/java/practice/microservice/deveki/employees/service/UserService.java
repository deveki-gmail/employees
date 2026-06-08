package practice.microservice.deveki.employees.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import practice.microservice.deveki.employees.model.User;
import practice.microservice.deveki.employees.repository.UserRepository;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public User saveUser(User user) {
        return repository.save(user);
    }

    public List<User> getAllUsers() {
        return repository.findAll();
    }

    public User getUserById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public List<User> getUserByName(String name) {
        return repository.findByName(name);
    }

    public void deleteUser(Long id) {
        repository.deleteById(id);
    }
}