package practice.microservice.deveki.employees.repository;



import java.util.List;
import java.util.Optional;

import practice.microservice.deveki.employees.model.Employee;

public interface EmployeeRepository {

    Employee save(Employee employee);

    Optional<Employee> findById(String empId);

    List<Employee> findAll();

    void deleteById(String empId);

    boolean existsById(String empId);
}
