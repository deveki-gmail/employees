package practice.microservice.deveki.employees.model;

import java.util.Objects;

public class Employee {

    private String name;
    private String city;
    private int salary;
    private String company;
    private String empId;

    // Constructor
    public Employee(String name, String city, int salary, String company, String empId) {
        this.name = name;
        this.city = city;
        this.salary = salary;
        this.company = company;
        this.empId = empId;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public int getSalary() {
        return salary;
    }

    public String getCompany() {
        return company;
    }

    public String getEmpId() {
        return empId;
    }

    // Optional: toString
    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", salary=" + salary +
                ", company='" + company + '\'' +
                ", empId='" + empId + '\'' +
                '}';
    }

    // Optional: equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return salary == employee.salary &&
                Objects.equals(name, employee.name) &&
                Objects.equals(city, employee.city) &&
                Objects.equals(company, employee.company) &&
                Objects.equals(empId, employee.empId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, city, salary, company, empId);
    }
}