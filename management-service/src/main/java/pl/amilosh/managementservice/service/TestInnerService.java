package pl.amilosh.managementservice.service;

import pl.amilosh.managementservice.model.Employee;

public interface TestInnerService {

    void invokeInner();

    void invokeInner(Integer id, int age);

    Employee setId(Integer id);
}
