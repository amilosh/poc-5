package pl.amilosh.managementservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import pl.amilosh.managementservice.dto.EmployeeDto;
import pl.amilosh.managementservice.dto.request.EmployeeSearchRequest;
import pl.amilosh.managementservice.dto.request.PageableRequest;
import pl.amilosh.managementservice.exception.ResourceNotFoundException;
import pl.amilosh.managementservice.mapper.EmployeeMapper;
import pl.amilosh.managementservice.model.Employee;
import pl.amilosh.managementservice.repository.EmployeeRepository;
import pl.amilosh.managementservice.service.EmployeeService;

import java.util.List;

import static pl.amilosh.managementservice.repository.specification.EmployeeSpecification.search;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeMapper employeeMapper;
    private final EmployeeRepository employeeRepository;

    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        log.info("Create Employee {}", employeeDto.getEmail());
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
    public Page<EmployeeDto> getAllEmployeesPages(PageableRequest pageableRequest) {
        var pageable = pageableRequest.getPageRequest();
        var employees = employeeRepository.findAll(pageable);
        return employees.map(employeeMapper::toDto);
    }

    @Override
    public Page<EmployeeDto> getAllEmployeesPagesCriteriaBuilder(EmployeeSearchRequest employeeSearchRequest) {
        var employees = employeeRepository.getAllEmployeesPagesCriteriaBuilder(employeeSearchRequest);
        return employees.map(employeeMapper::toDto);
    }

    @Override
    public Page<EmployeeDto> getAllEmployeesPagesSpecification(EmployeeSearchRequest employeeSearchRequest) {
        var employeeSpecification = search(employeeSearchRequest);
        var pageable = employeeSearchRequest.getPageRequest();
        var employees = employeeRepository.findAll(employeeSpecification, pageable);
        return employees.map(employeeMapper::toDto);
    }

    @Override
    public EmployeeDto getEmployeeById(Integer id) {
        log.info("Get Employee {}", id);
        var employee = getById(id);
        return toDto(employee);
    }

    @Override
    public EmployeeDto updateEmployee(EmployeeDto employeeDto) {
        log.info("Update Employee {} {}", employeeDto.getId(), employeeDto.getEmail());
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
