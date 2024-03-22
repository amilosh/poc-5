package pl.amilosh.managementservice.enumeration;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import pl.amilosh.managementservice.exception.ConversionException;

import java.util.Map;
import java.util.Optional;

import static java.util.Arrays.stream;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

@Converter
public class BaseEnumConverter<E extends BaseEnum<E>> implements AttributeConverter<E, Integer> {

    private final Map<Integer, E> enumByCodeMap;
    private final String enumName;

    protected BaseEnumConverter(Class<E> clazz) {
        this.enumByCodeMap = stream(clazz.getEnumConstants()).collect(toMap(BaseEnum::getCode, identity()));
        this.enumName = clazz.getSimpleName();
    }

    @Override
    public Integer convertToDatabaseColumn(E attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getCode();
    }

    @Override
    public E convertToEntityAttribute(Integer code) {
        if (code == null) {
            return null;
        }

        return Optional.ofNullable(enumByCodeMap.get(code))
            .orElseThrow(() -> new ConversionException("%s not found by code %s", enumName, code));
    }
}
