package pl.amilosh.managementservice.repository.specification;

import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import pl.amilosh.managementservice.dto.request.EmployeeSearchRequest;
import pl.amilosh.managementservice.model.Employee;
import pl.amilosh.managementservice.model.Employee_;

import java.util.ArrayList;
import java.util.List;

public final class EmployeeSpecification {

    private EmployeeSpecification() {
    }

    public static Specification<Employee> search(EmployeeSearchRequest employeeSearchRequest) {
        var email = employeeSearchRequest.getEmail();
        var firstName = employeeSearchRequest.getFirstName();
        var lastName = employeeSearchRequest.getLastName();

        return ((root, query, builder) -> {
            List<Predicate> predicatesList = new ArrayList<>();
            if (email != null && !email.trim().isEmpty()) {
                Predicate emailPredicate = builder.like(root.get(Employee_.EMAIL), "%" + email + "%");
                predicatesList.add(emailPredicate);
            }

            if (firstName != null && !firstName.trim().isEmpty()) {
                Predicate firstNamePredicate = builder.like(root.get(Employee_.FIRST_NAME), "%" + firstName + "%");
                predicatesList.add(firstNamePredicate);
            }

            if (lastName != null && !lastName.trim().isEmpty()) {
                Predicate lastNamePredicate = builder.like(root.get(Employee_.LAST_NAME), "%" + lastName + "%");
                predicatesList.add(lastNamePredicate);
            }

            Predicate[] predicates = predicatesList.toArray(Predicate[]::new);

            final List<Order> orderList = employeeSearchRequest.defineOrders(builder, root);
            query.orderBy(orderList);

            return builder.and(predicates);
        });
    }
}
