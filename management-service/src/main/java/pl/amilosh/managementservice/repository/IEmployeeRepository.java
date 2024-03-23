package pl.amilosh.managementservice.repository;

import org.springframework.data.domain.Page;
import pl.amilosh.managementservice.dto.request.EmployeeSearchRequest;
import pl.amilosh.managementservice.model.Employee;

public interface IEmployeeRepository {

    Page<Employee> getAllEmployeesPagesCriteriaBuilder(EmployeeSearchRequest employeeSearchRequest);
}
