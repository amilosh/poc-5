package pl.amilosh.managementservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.amilosh.managementservice.dto.validation.group.CreateGroup;
import pl.amilosh.managementservice.dto.validation.group.UpdateGroup;

import static pl.amilosh.managementservice.utils.Constants.EMAIL_REGEXP;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDto {

    @NotNull(groups = UpdateGroup.class)
    private Integer id;

    @NotNull(groups = {CreateGroup.class})
    @Size(min = 5, max = 128, groups = {CreateGroup.class, UpdateGroup.class})
    @Email(regexp = EMAIL_REGEXP, groups = {CreateGroup.class, UpdateGroup.class})
    private String email;

    @Size(max = 64, groups = {CreateGroup.class, UpdateGroup.class})
    private String firstName;

    @Size(max = 64, groups = {CreateGroup.class, UpdateGroup.class})
    private String lastName;
}
