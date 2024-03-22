package pl.amilosh.managementservice.service;

import org.springframework.stereotype.Component;
import pl.amilosh.managementservice.enumeration.EmployeeStatus;
import pl.amilosh.managementservice.model.Employee;

@Component
public class EmployeeStatusProvider {

    public static Employee returnDraftEmployee(Employee employee) {
        return employee;
    }
}
