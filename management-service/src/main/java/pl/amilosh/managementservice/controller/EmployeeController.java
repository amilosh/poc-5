package pl.amilosh.managementservice.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.amilosh.managementservice.annotations.Generated;
import pl.amilosh.managementservice.controller.api.EmployeeControllerApi;
import pl.amilosh.managementservice.dto.EmployeeDto;
import pl.amilosh.managementservice.dto.request.EmployeeSearchRequest;
import pl.amilosh.managementservice.dto.request.PageableRequest;
import pl.amilosh.managementservice.dto.validation.group.CreateGroup;
import pl.amilosh.managementservice.dto.validation.group.UpdateGroup;
import pl.amilosh.managementservice.service.EmployeeService;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Generated
@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController implements EmployeeControllerApi {

    private final EmployeeService employeeService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(CREATED)
    public EmployeeDto create(@RequestBody @Validated(CreateGroup.class) EmployeeDto employeeDto) {
        return employeeService.createEmployee(employeeDto);
    }

    @GetMapping(value = "/all", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public List<EmployeeDto> getAll() {
        return employeeService.getAllEmployees();
    }

    @GetMapping(value = "/all-pages", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public Page<EmployeeDto> getAllPages(@Valid PageableRequest pageableRequest) {
        return employeeService.getAllEmployeesPages(pageableRequest);
    }

    @GetMapping(value = "/all-pages-criteria-builder", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public Page<EmployeeDto> getAllPagesCriteriaBuilder(@Valid EmployeeSearchRequest employeeSearchRequest) {
        return employeeService.getAllEmployeesPagesCriteriaBuilder(employeeSearchRequest);
    }

    @GetMapping(value = "/all-pages-specification-and", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public Page<EmployeeDto> getAllPagesSpecificationAnd(@Valid EmployeeSearchRequest employeeSearchRequest) {
        return employeeService.getAllEmployeesPagesSpecificationAnd(employeeSearchRequest);
    }

    @GetMapping(value = "/all-pages-specification-or", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public Page<EmployeeDto> getAllPagesSpecificationOr(@Valid EmployeeSearchRequest employeeSearchRequest) {
        return employeeService.getAllEmployeesPagesSpecificationOr(employeeSearchRequest);
    }

    @GetMapping(value = "/{id}", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public EmployeeDto getById(@PathVariable(value = "id") Integer id) {
        return employeeService.getEmployeeById(id);
    }

    @PutMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(OK)
    public EmployeeDto update(@RequestBody @Validated(UpdateGroup.class) EmployeeDto employeeDto) {
        return employeeService.updateEmployee(employeeDto);
    }

    @PostMapping(value = "/publish-event")
    @ResponseStatus(OK)
    public String publishEmployeeEvent(@RequestBody EmployeeDto employeeDto) {
        return employeeService.publishEmployeeEvent(employeeDto.getEmail());
    }
}
