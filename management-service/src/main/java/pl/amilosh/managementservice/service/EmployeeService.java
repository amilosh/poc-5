package pl.amilosh.managementservice.service;

import org.springframework.data.domain.Page;
import pl.amilosh.managementservice.dto.EmployeeDto;
import pl.amilosh.managementservice.dto.request.EmployeeSearchRequest;
import pl.amilosh.managementservice.dto.request.PageableRequest;
import pl.amilosh.managementservice.model.Employee;

import java.util.List;

public interface EmployeeService {

    EmployeeDto createEmployee(EmployeeDto employeeDto);

    List<EmployeeDto> getAllEmployees();

    Page<EmployeeDto> getAllEmployeesPages(PageableRequest pageableRequest);

    Page<EmployeeDto> getAllEmployeesPagesCriteriaBuilder(EmployeeSearchRequest employeeSearchRequest);

    Page<EmployeeDto> getAllEmployeesPagesSpecification(EmployeeSearchRequest employeeSearchRequest);

    EmployeeDto getEmployeeById(Integer id);

    EmployeeDto updateEmployee(EmployeeDto employeeDto);

    void persist(Employee employee);

    Employee getById(Integer id);

    EmployeeDto toDto(Employee employee);
}
