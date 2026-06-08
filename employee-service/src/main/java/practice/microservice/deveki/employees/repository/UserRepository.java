package practice.microservice.deveki.employees.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import practice.microservice.deveki.employees.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    // Custom methods
    List<User> findByName(String name);

    User findByEmail(String email);
}