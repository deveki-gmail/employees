package practice.microservice.deveki.employees.controller;

import org.springframework.web.bind.annotation.*;

import practice.microservice.deveki.employees.model.Employee;
import practice.microservice.deveki.employees.repository.EmployeeRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

	private static final Logger log = LoggerFactory.getLogger(EmployeeController.class);

	private final EmployeeRepository repository;

	public EmployeeController(EmployeeRepository repository) {
		this.repository = repository;
	}

	@PostMapping
	public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
		log.info("Received request to create employee with id: {}", employee.getEmpId());

		Employee saved = repository.save(employee);

		log.info("Employee created successfully with id: {} name: {}", saved.getEmpId(), saved.getName());
		return ResponseEntity.ok(saved);
	}

	@GetMapping
	public ResponseEntity<List<Employee>> getEmployees(@RequestParam(required = false) String city,
			@RequestParam(required = false) String company) {

		log.info("Fetching employees with filters - city: {}, company: {}", city, company);

		List<Employee> employees = repository.findAll();

		if (city != null) {
			employees = employees.stream().filter(e -> e.getCity().equalsIgnoreCase(city)).collect(Collectors.toList());
		}

		if (company != null) {
			employees = employees.stream().filter(e -> e.getCompany().equalsIgnoreCase(company)).collect(Collectors.toList());
		}

		log.info("Returning {} employees", employees.size());
		return ResponseEntity.ok(employees);
	}

	@GetMapping("/{empId}")
	public ResponseEntity<Employee> getEmployee(@PathVariable String empId) {
		log.info("Fetching employee with id: {}", empId);

		return repository.findById(empId).map(emp -> {
			log.info("Employee found: {}", empId);
			return ResponseEntity.ok(emp);
		}).orElseGet(() -> {
			log.warn("Employee not found: {}", empId);
			return ResponseEntity.notFound().build();
		});
	}

	@PutMapping("/{empId}")
	public ResponseEntity<Employee> updateEmployee(@PathVariable String empId, @RequestBody Employee updated) {

		log.info("Updating employee with id: {}", empId);

		if (!repository.existsById(empId)) {
			log.warn("Update failed. Employee not found: {}", empId);
			return ResponseEntity.notFound().build();
		}

		Employee employee = new Employee(updated.getName(), updated.getCity(), updated.getSalary(), updated.getCompany(), empId);

		Employee saved = repository.save(employee);

		log.info("Employee updated successfully: {}", empId);
		return ResponseEntity.ok(saved);
	}

	@DeleteMapping("/{empId}")
	public ResponseEntity<Void> deleteEmployee(@PathVariable String empId) {

		log.info("Deleting employee with id: {}", empId);

		if (!repository.existsById(empId)) {
			log.warn("Delete failed. Employee not found: {}", empId);
			return ResponseEntity.notFound().build();
		}

		repository.deleteById(empId);

		log.info("Employee deleted successfully: {}", empId);
		return ResponseEntity.noContent().build();
	}
}