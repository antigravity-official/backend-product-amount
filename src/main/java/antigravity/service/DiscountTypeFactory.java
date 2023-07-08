package antigravity.service;

import antigravity.domain.entity.DiscountType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class DiscountTypeFactory {

    private final List<DiscountTypeService> services;

    public DiscountTypeService get(DiscountType type) {
        return services.stream().filter(service -> service.findType(type))
                .findFirst().orElseThrow(NoSuchElementException::new);
    }
}
