package pl.amilosh.managementservice.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestServiceTest {

    @InjectMocks
    private TestServiceImpl testService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testHello() {
        var result = testService.hello();
        assertEquals("hello", result);
    }
}
