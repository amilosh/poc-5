package pl.amilosh.managementservice.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.core.ResolvableType;
import org.springframework.core.ResolvableTypeProvider;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GenericEvent<T> implements ResolvableTypeProvider {

    private T source;
    protected boolean applicable;

    @Override
    public ResolvableType getResolvableType() {
        return ResolvableType.forClassWithGenerics(
            getClass(),
            ResolvableType.forInstance(this.source)
        );
    }
}
