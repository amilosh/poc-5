package pl.amilosh.managementservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import pl.amilosh.managementservice.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer>, IEmployeeRepository, JpaSpecificationExecutor<Employee> {
}
