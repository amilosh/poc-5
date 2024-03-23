package pl.amilosh.managementservice.dto.request;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageableRequest {

    @PositiveOrZero
    @Builder.Default
    private int page = 0;

    @Positive
    @Builder.Default
    private int size = 10;

    private String[] sort;

    public Pageable getPageRequest() {
        if (sort == null || sort.length == 0) {
            return PageRequest.of(page, size);
        }

        return PageRequest.of(page, size, defineSort());
    }

    private Sort defineSort() {
        List<Sort.Order> orders = new ArrayList<>();
        for (String s : sort) {
            var parts = s.split(":");
            var property = parts[0];
            var direction = Sort.Direction.ASC.name();
            if (parts.length > 1) {
                direction = parts[1];
            }
            orders.add(new Sort.Order(Sort.Direction.fromString(direction), property));
        }
        return Sort.by(orders);
    }
}
