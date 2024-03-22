package pl.amilosh.managementservice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pl.amilosh.managementservice.enumeration.EmployeeStatus;

@Entity
@Table(name = "employee")
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class Employee extends AuditableEntity<Integer> {

    @Column(name = "email", unique = true)
    @EqualsAndHashCode.Include
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Convert(converter = EmployeeStatus.Converter.class)
    @Column(name = "status")
    private EmployeeStatus status;
}
