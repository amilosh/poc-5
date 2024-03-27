package pl.amilosh.managementservice.event.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.amilosh.managementservice.event.EmployeeEvent;

@Slf4j
@Component
public class EmployeeEventListener {

    @EventListener
    public void handleAddEmployeeEvent(EmployeeEvent employeeEvent) {
        log.info("EmployeeEvent email: {}", employeeEvent);
    }
}
