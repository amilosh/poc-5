package pl.amilosh.managementservice.controller.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import pl.amilosh.managementservice.dto.EmployeeDto;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import pl.amilosh.managementservice.dto.request.PageableRequest;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(
    name = "Employee controller",
    description = "Manages operations under Employee entity"
)
public interface EmployeeControllerApi {

    @Operation(
        summary = "Create new Employee.",
        description = "Create new Employee with unique email.",
        requestBody = @RequestBody(
            description = "Employee",
            required = true,
            content = {
                @Content(
                    mediaType = APPLICATION_JSON_VALUE,
                    schema = @Schema(
                        implementation = EmployeeDto.class
                    )
                )
            }
        )
    )
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "201",
                description = "Employee is created",
                content = {
                    @Content(
                        mediaType = APPLICATION_JSON_VALUE,
                        schema = @Schema(
                            implementation = EmployeeDto.class
                        )
                    )
                }
            ),
            @ApiResponse(
                responseCode = "400",
                description = "Bad request"
            ),
            @ApiResponse(
                responseCode = "500",
                description = "Internal server error"
            )
        }
    )
    EmployeeDto create(EmployeeDto employeeDto);

    @Operation(
        summary = "Get all Employees.",
        description = "Get all Employees.",
        tags = {"Get operations"})
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "All Employees are retrieved"
            )
        }
    )
    List<EmployeeDto> getAll();

    Page<EmployeeDto> getAllPages(PageableRequest pageableRequest);

    @Operation(
        summary = "Get Employee by id.",
        description = "Get Employee by id. Throw exception if not found.",
        tags = {"Get operations"})
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Employee is retrieved by id"
            )
        }
    )
    EmployeeDto getById(
        @Parameter(
            in = ParameterIn.PATH,
            name = "id",
            description = "Employee id",
            required = true,
            schema = @Schema(
                type = "Integer"
            )
        )
    Integer id);

    @Operation(
        summary = "Update Employee.",
        description = "Update Employee. Employee must exist.")
    @ApiResponses(
        value = {
            @ApiResponse(
                responseCode = "200",
                description = "Employee is updated"
            )
        }
    )
    EmployeeDto update(EmployeeDto employeeDto);
}
