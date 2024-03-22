package pl.amilosh.managementservice.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import pl.amilosh.managementservice.enumeration.EmployeeStatus;
import pl.amilosh.managementservice.exception.ResourceNotFoundException;
import pl.amilosh.managementservice.model.Employee;
import pl.amilosh.managementservice.service.EmployeeStatusProvider;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.junit.jupiter.params.provider.EnumSource.Mode.EXCLUDE;
import static org.junit.jupiter.params.provider.EnumSource.Mode.INCLUDE;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TestServiceTest {

    @InjectMocks
    private TestServiceImpl testService;

    @Mock
    private TestInnerServiceImpl testInnerService;

    MockedStatic<EmployeeStatusProvider> staticMock;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        staticMock = Mockito.mockStatic(EmployeeStatusProvider.class);
        staticMock.when(() -> EmployeeStatusProvider.returnDraftEmployee(any(Employee.class)))
            .thenReturn(Employee.builder().status(EmployeeStatus.DRAFT).build());

        ReflectionTestUtils.setField(testService, "testValue", "testValue");
    }

    @AfterEach
    void clear() {
        staticMock.close();
    }

    @Test
    public void testHello() {
        var result = testService.hello();
        assertEquals("hello", result);
    }

    @ParameterizedTest
    @EnumSource(value = EmployeeStatus.class, mode = INCLUDE, names = {"ACTIVE"})
    public void testSetEmployeeStatusInclude(EmployeeStatus employeeStatus) {
        var employee = testService.setEmployeeStatus(employeeStatus);
        assertTrue(employee.getStatus().isActive());
    }

    @ParameterizedTest
    @EnumSource(value = EmployeeStatus.class, mode = EXCLUDE, names = {"ACTIVE"})
    public void testSetEmployeeStatusExclude(EmployeeStatus employeeStatus) {
        var employee = testService.setEmployeeStatus(employeeStatus);
        assertFalse(employee.getStatus().isActive());
    }

    @Test
    void testFindEmployeeById() {
        var exception = assertThrows(ResourceNotFoundException.class, () -> testService.findEmployeeById(null));
        assertEquals("id == null", exception.getMessage());

        assertNull(testService.findEmployeeById(1));
    }

    @Test
    void testFindEmployeeById2() {
        Integer id = null;
        var exception = assertThrows(ResourceNotFoundException.class,
            () -> ReflectionTestUtils.invokeMethod(testService, "findEmployeeById", id));
        assertEquals("id == null", exception.getMessage());

        assertNull(ReflectionTestUtils.invokeMethod(testService, "findEmployeeById", 1));
    }

    @Test
    void testSetId() {
        Integer id1 = 1;
        Integer id2 = 2;
        int age = 10;
        var employee = Employee.builder()
            .id(id2)
            .build();
        doNothing().when(testInnerService).invokeInner();
        doNothing().when(testInnerService).invokeInner(eq(id1), eq(age));
        when(testInnerService.setId(eq(id1))).thenReturn(employee);
        var result = testService.setId(id1);
        assertEquals(id2, result.getId());
        verify(testInnerService, times(2)).invokeInner();
        verify(testInnerService).invokeInner(any(Integer.class), anyInt());
        verify(testInnerService).setId(any(Integer.class));
    }

    @Test
    public void testGetTestValue() {
        var result = testService.getTestValue();
        assertEquals("testValue", result);
    }

    @Test
    public void testGetPrivateEmployee() {
        Integer id = 1;
        var result = (Employee) ReflectionTestUtils.invokeMethod(testService, "getPrivateEmployee", id, EmployeeStatus.DRAFT);
        assertEquals(1, result.getId());
        assertEquals(EmployeeStatus.DRAFT, result.getStatus());
    }

    @Test
    public void testGetStatusFromStatic() {
        var employee = Employee.builder()
            .status(EmployeeStatus.ACTIVE)
            .build();
        var result = testService.getStatusFromStatic(employee);
        assertEquals(EmployeeStatus.DRAFT, result.getStatus());
    }

    static Stream<Arguments> testBuildEmployeeArgs() {
        return Stream.of(
            arguments(1, EmployeeStatus.DRAFT, 1, EmployeeStatus.DRAFT),
            arguments(2, EmployeeStatus.ACTIVE, 2, EmployeeStatus.ACTIVE),
            arguments(3, EmployeeStatus.DISMISSED, 3, EmployeeStatus.DISMISSED)
        );
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void testBuildEmployee(int id) {
        var result = testService.buildEmployee(id);
        assertNotNull(result);
        assertNotNull(result.getId());
    }

    @ParameterizedTest
    @MethodSource("testBuildEmployeeArgs")
    void testBuildEmployee(Integer id, EmployeeStatus employeeStatus, Integer expectedId, EmployeeStatus expectedEmployeeStatus) {
        var result = testService.buildEmployee(id, employeeStatus);
        assertEquals(expectedId, result.getId());
        assertEquals(expectedEmployeeStatus, result.getStatus());
    }

    @ParameterizedTest
    @CsvSource({"1,DRAFT", "2,ACTIVE", "3,DISMISSED"})
    void testBuildEmployee(Integer id, EmployeeStatus employeeStatus) {
        var result = testService.buildEmployee(id, employeeStatus);
        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getStatus());
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/csv/buildEmployeeData.csv", numLinesToSkip = 1)
    void testBuildEmployeeFormCCsv(Integer id, EmployeeStatus employeeStatus) {
        var result = testService.buildEmployee(id, employeeStatus);
        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getStatus());
    }
}
