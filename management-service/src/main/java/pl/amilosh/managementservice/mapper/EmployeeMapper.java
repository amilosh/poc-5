package pl.amilosh.managementservice.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;
import pl.amilosh.managementservice.dto.EmployeeDto;
import pl.amilosh.managementservice.model.Employee;

import java.util.List;

@Component
@Mapper(
    componentModel = "spring",
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface EmployeeMapper {

    @Mapping(target = "id", ignore = true)
    Employee toEntity(EmployeeDto employeeDto);

    List<Employee> toEntities(List<EmployeeDto> employeeDto);

    EmployeeDto toDto(Employee employee);

    List<EmployeeDto> toDtos(List<Employee> employee);

    @Mapping(target = "id", ignore = true)
    void updateEntity(EmployeeDto EmployeeDto, @MappingTarget Employee employee);
}
