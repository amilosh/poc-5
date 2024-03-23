package pl.amilosh.managementservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeSearchRequest extends PageableRequest {

    private String email;
    private String firstName;
    private String lastName;
}
