package pl.amilosh.managementservice.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import pl.amilosh.managementservice.model.Employee;

import java.util.Collection;

public final class SpecificationUtils {

    private SpecificationUtils() {
    }

    public static <T> Specification<T> and() {
        return (root, query, builder) -> builder.conjunction();
    }

    public static <T> Specification<T> or() {
        return (root, query, builder) -> builder.disjunction();
    }

    public static <T> Specification<T> andSpecifications(Collection<Specification<T>> specs) {
        return specs.stream().reduce(and(), Specification::and);
    }

    public static <T> Specification<T> orSpecifications(Collection<Specification<T>> specs) {
        return specs.stream().reduce(or(), Specification::or);
    }

    public static <T> Specification<T> getLikePredicate(String field, String value) {
        return ((root, query, builder) -> builder.like(root.get(field), "%" + value + "%"));
    }

    public static Specification<Employee> searchByFieldAnd(String field, String value) {
        if (value == null || value.trim().isEmpty()) {
            return and();
        }

        return getLikePredicate(field, value);
    }

    public static Specification<Employee> searchByFieldOr(String field, String value) {
        if (value == null || value.trim().isEmpty()) {
            return or();
        }

        return getLikePredicate(field, value);
    }
}
