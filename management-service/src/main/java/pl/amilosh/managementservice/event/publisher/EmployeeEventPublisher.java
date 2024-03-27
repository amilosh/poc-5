package pl.amilosh.managementservice.event.publisher;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import pl.amilosh.managementservice.event.EmployeeEvent;

@Component
@RequiredArgsConstructor
public class EmployeeEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishEmployeeEvent(String email) {
        var employeeEvent = EmployeeEvent.builder()
            .email(email)
            .build();
        applicationEventPublisher.publishEvent(employeeEvent);
    }
}
