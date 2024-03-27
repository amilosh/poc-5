package pl.amilosh.managementservice.event.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import pl.amilosh.managementservice.event.EmployeeEvent;

@Slf4j
@Component
public class EmployeeEventListener {

    @Async
    @EventListener
    public void handleAddEmployeeEvent(EmployeeEvent employeeEvent) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            log.error(ex.getMessage());
        }

        log.info("EmployeeEventListener thread: {}" + Thread.currentThread().getName());
        log.info("EmployeeEvent email: {}", employeeEvent);
    }
}
