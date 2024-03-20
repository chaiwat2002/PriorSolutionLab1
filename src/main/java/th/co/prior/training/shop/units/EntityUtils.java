package th.co.prior.training.shop.units;

import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.stream.Stream;

@Component
public class EntityUtils {

    public boolean hasEntity(Object... entities) {
        return Stream.of(entities).allMatch(Objects::nonNull);
    }

    public boolean equals(Object a, Object b) {
        return Objects.equals(a, b);
    }
}
