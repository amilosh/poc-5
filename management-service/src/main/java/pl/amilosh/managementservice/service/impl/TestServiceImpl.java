package pl.amilosh.managementservice.service.impl;

import org.springframework.stereotype.Service;
import pl.amilosh.managementservice.service.TestService;

@Service
public class TestServiceImpl implements TestService {

    @Override
    public String hello() {
        return "hello";
    }
}
