package pl.amilosh.managementservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.amilosh.managementservice.enumeration.EmployeeStatus;
import pl.amilosh.managementservice.exception.ResourceNotFoundException;
import pl.amilosh.managementservice.model.Employee;
import pl.amilosh.managementservice.service.EmployeeStatusProvider;
import pl.amilosh.managementservice.service.TestInnerService;
import pl.amilosh.managementservice.service.TestService;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    @Value("test.value")
    private String testValue;

    private final TestInnerService testInnerService;

    @Override
    public String hello() {
        return "hello";
    }

    @Override
    public Employee setEmployeeStatus(EmployeeStatus employeeStatus) {
        return Employee.builder()
            .status(employeeStatus)
            .build();
    }

    @Override
    public Employee findEmployeeById(Integer id) {
        if (id == null) {
            throw new ResourceNotFoundException("id == null");
        }
        return null;
    }

    private Employee findEmployeeById2(Integer id) {
        if (id == null) {
            throw new ResourceNotFoundException("id == null");
        }
        return null;
    }

    @Override
    public Employee setId(Integer id) {
        testInnerService.invokeInner();
        testInnerService.invokeInner();
        testInnerService.invokeInner(id, 10);
        var employee = testInnerService.setId(id);
        return employee;
    }

    @Override
    public String getTestValue() {
        return testValue;
    }

    private Employee getPrivateEmployee(Integer id, EmployeeStatus employeeStatus) {
        return Employee.builder()
            .id(id)
            .status(employeeStatus)
            .build();
    }

    @Override
    public Employee getStatusFromStatic(Employee employee) {
        var result = EmployeeStatusProvider.returnDraftEmployee(employee);
        return result;
    }

    @Override
    public Employee buildEmployee(Integer id) {
        return Employee.builder()
            .id(id)
            .build();
    }

    @Override
    public Employee buildEmployee(Integer id, EmployeeStatus employeeStatus) {
        return Employee.builder()
            .id(id)
            .status(employeeStatus)
            .build();
    }
}
