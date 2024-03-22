package pl.amilosh.managementservice.service.impl;

import org.springframework.stereotype.Service;
import pl.amilosh.managementservice.model.Employee;
import pl.amilosh.managementservice.service.TestInnerService;

@Service
public class TestInnerServiceImpl implements TestInnerService {

    @Override
    public void invokeInner() {
        System.out.println("invokeInner()");
    }

    @Override
    public void invokeInner(Integer id, int age) {
        System.out.println("invokeInner(Integer id)");
    }

    @Override
    public Employee setId(Integer id) {
        return Employee.builder()
            .id(id)
            .build();
    }
}
