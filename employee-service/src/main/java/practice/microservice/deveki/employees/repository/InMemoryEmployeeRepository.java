package practice.microservice.deveki.employees.repository;


import org.springframework.stereotype.Repository;

import practice.microservice.deveki.employees.model.Employee;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryEmployeeRepository implements EmployeeRepository {

    private final Map<String, Employee> store = new ConcurrentHashMap<>();

    @Override
    public Employee save(Employee employee) {
        store.put(employee.getEmpId(), employee);
        return employee;
    }

    @Override
    public Optional<Employee> findById(String empId) {
        return Optional.ofNullable(store.get(empId));
    }

    @Override
    public List<Employee> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void deleteById(String empId) {
        store.remove(empId);
    }

    @Override
    public boolean existsById(String empId) {
        return store.containsKey(empId);
    }
}