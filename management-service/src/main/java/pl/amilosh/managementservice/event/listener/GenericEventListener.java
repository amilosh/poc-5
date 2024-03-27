package pl.amilosh.managementservice.event.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import pl.amilosh.managementservice.event.GenericEvent;

@Slf4j
@Component
public class GenericEventListener {

    @EventListener(condition = "#event.applicable")
    public void handle(GenericEvent<String> event) {
        log.info("GenericEvent: {}", event);
    }
}
