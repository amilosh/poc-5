package pl.amilosh.managementservice.service;

import pl.amilosh.managementservice.dto.EmployeeDto;
import pl.amilosh.managementservice.model.Employee;

import java.util.List;

public interface EmployeeService {

    EmployeeDto createEmployee(EmployeeDto employeeDto);

    List<EmployeeDto> getAllEmployees();

    EmployeeDto getEmployeeById(Integer id);

    EmployeeDto updateEmployee(EmployeeDto employeeDto);

    void persist(Employee employee);

    Employee getById(Integer id);

    EmployeeDto toDto(Employee employee);
}
