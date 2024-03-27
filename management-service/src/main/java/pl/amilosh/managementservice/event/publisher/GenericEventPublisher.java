package pl.amilosh.managementservice.event.publisher;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import pl.amilosh.managementservice.event.GenericEvent;

@Component
@RequiredArgsConstructor
public class GenericEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public void publishGenericEvent(String email, boolean applicable) {
        GenericEvent<String> genericEvent = new GenericEvent<String>();
        genericEvent.setSource("" + email + " " + applicable);
        genericEvent.setApplicable(applicable);
        applicationEventPublisher.publishEvent(genericEvent);
    }
}
