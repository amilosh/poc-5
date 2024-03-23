package pl.amilosh.managementservice.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Repository;
import pl.amilosh.managementservice.dto.request.EmployeeSearchRequest;
import pl.amilosh.managementservice.model.Employee;
import pl.amilosh.managementservice.model.Employee_;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class EmployeeRepositoryImpl implements IEmployeeRepository {

    private final EntityManager entityManager;

    public Page<Employee> getAllEmployeesPagesCriteriaBuilder(EmployeeSearchRequest employeeSearchRequest) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Employee> query = builder.createQuery(Employee.class);
        Root<Employee> root = query.from(Employee.class);

        Predicate finalAndPredicate = getFinalAndPredicate(builder, root, employeeSearchRequest);
        List<Order> orders = employeeSearchRequest.defineOrders(builder, root);

        query.select(root)
            .where(finalAndPredicate)
            .orderBy(orders);

        var result = entityManager.createQuery(query)
            .setFirstResult(employeeSearchRequest.getOffset())
            .setMaxResults(employeeSearchRequest.getSize())
            .getResultList();

        Long count = calculateCount(builder, employeeSearchRequest);

        return new PageImpl<>(result, employeeSearchRequest.getPageRequest(), count);
    }

    private Predicate getFinalAndPredicate(CriteriaBuilder builder, Root<Employee> root, EmployeeSearchRequest employeeSearchRequest) {
        var email = employeeSearchRequest.getEmail();
        var firstName = employeeSearchRequest.getFirstName();
        var lastName = employeeSearchRequest.getLastName();

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

        return builder.and(predicates);
    }

    private Long calculateCount(CriteriaBuilder builder, EmployeeSearchRequest employeeSearchRequest) {
        CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
        Root<Employee> countRoot = countQuery.from(Employee.class);
        Predicate countFinalAndPredicate = getFinalAndPredicate(builder, countRoot, employeeSearchRequest);
        countQuery.select(builder.count(countRoot))
            .where(countFinalAndPredicate);

        return entityManager.createQuery(countQuery).getSingleResult();
    }
}
