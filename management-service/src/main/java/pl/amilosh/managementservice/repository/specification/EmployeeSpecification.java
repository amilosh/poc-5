package pl.amilosh.managementservice.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import pl.amilosh.managementservice.dto.request.EmployeeSearchRequest;
import pl.amilosh.managementservice.model.Employee;
import pl.amilosh.managementservice.model.Employee_;

import java.util.List;

import static pl.amilosh.managementservice.repository.specification.SpecificationUtils.andSpecifications;
import static pl.amilosh.managementservice.repository.specification.SpecificationUtils.orSpecifications;
import static pl.amilosh.managementservice.repository.specification.SpecificationUtils.searchByFieldAnd;
import static pl.amilosh.managementservice.repository.specification.SpecificationUtils.searchByFieldOr;

public final class EmployeeSpecification {

    private EmployeeSpecification() {
    }

    public static Specification<Employee> searchByFieldsAnd(EmployeeSearchRequest employeeSearchRequest) {
        var email = employeeSearchRequest.getEmail();
        var firstName = employeeSearchRequest.getFirstName();
        var lastName = employeeSearchRequest.getLastName();

        var searchByEmail = searchByEmailAnd(email);
        var searchByFirstName = searchByFirstNameAnd(firstName);
        var searchByLastName = searchByLastNameAnd(lastName);
        var searchByFields = List.of(searchByEmail, searchByFirstName, searchByLastName);
        return andSpecifications(searchByFields);
    }

    public static Specification<Employee> searchByFieldsOr(EmployeeSearchRequest employeeSearchRequest) {
        var email = employeeSearchRequest.getEmail();
        var firstName = employeeSearchRequest.getFirstName();
        var lastName = employeeSearchRequest.getLastName();

        var searchByEmail = searchByEmailOr(email);
        var searchByFirstName = searchByFirstNameOr(firstName);
        var searchByLastName = searchByLastNameOr(lastName);
        var searchByFields = List.of(searchByEmail, searchByFirstName, searchByLastName);
        return orSpecifications(searchByFields);
    }

    private static Specification<Employee> searchByEmailAnd(String email) {
        return searchByFieldAnd(Employee_.EMAIL, email);
    }

    private static Specification<Employee> searchByFirstNameAnd(String firstName) {
        return searchByFieldAnd(Employee_.FIRST_NAME, firstName);
    }

    private static Specification<Employee> searchByLastNameAnd(String lastName) {
        return searchByFieldAnd(Employee_.LAST_NAME, lastName);
    }

    private static Specification<Employee> searchByEmailOr(String email) {
        return searchByFieldOr(Employee_.EMAIL, email);
    }

    private static Specification<Employee> searchByFirstNameOr(String firstName) {
        return searchByFieldOr(Employee_.FIRST_NAME, firstName);
    }

    private static Specification<Employee> searchByLastNameOr(String lastName) {
        return searchByFieldOr(Employee_.LAST_NAME, lastName);
    }
}
