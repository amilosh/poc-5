package pl.amilosh.managementservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.amilosh.managementservice.dto.EmployeeDto;
import pl.amilosh.managementservice.exception.ResourceNotFoundException;
import pl.amilosh.managementservice.mapper.EmployeeMapper;
import pl.amilosh.managementservice.model.Employee;
import pl.amilosh.managementservice.repository.EmployeeRepository;
import pl.amilosh.managementservice.service.EmployeeService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeMapper employeeMapper;
    private final EmployeeRepository employeeRepository;

    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        var employee = employeeMapper.toEntity(employeeDto);
        persist(employee);
        return toDto(employee);
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
        var employees = employeeRepository.findAll();
        return employeeMapper.toDtos(employees);
    }

    @Override
    public EmployeeDto getEmployeeById(Integer id) {
        var employee = getById(id);
        return toDto(employee);
    }

    @Override
    public EmployeeDto updateEmployee(EmployeeDto employeeDto) {
        var employee = getById(employeeDto.getId());
        employeeMapper.updateEntity(employeeDto, employee);
        persist(employee);
        return toDto(employee);
    }

    @Override
    public void persist(Employee employee) {
        employeeRepository.save(employee);
    }

    @Override
    public Employee getById(Integer id) {
        return employeeRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Employee %s not found", id));
    }

    @Override
    public EmployeeDto toDto(Employee employee) {
        return employeeMapper.toDto(employee);
    }
}
