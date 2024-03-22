package pl.amilosh.managementservice.service;

import pl.amilosh.managementservice.enumeration.EmployeeStatus;
import pl.amilosh.managementservice.model.Employee;

public interface TestService {

    String hello();

    Employee setEmployeeStatus(EmployeeStatus employeeStatus);

    Employee findEmployeeById(Integer id);

    Employee setId(Integer id);

    String getTestValue();

    Employee getStatusFromStatic(Employee employee);

    Employee buildEmployee(Integer id);

    Employee buildEmployee(Integer id, EmployeeStatus employeeStatus);
}
