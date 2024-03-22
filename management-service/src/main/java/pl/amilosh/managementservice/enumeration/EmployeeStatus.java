package pl.amilosh.managementservice.enumeration;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

public enum EmployeeStatus implements BaseEnum<EmployeeStatus> {

    @EmployeeStatusConfig(availableEmployeeAccesses = EmployeeAccess.NONE)
    DRAFT(1, "Draft") {
        @Override
        public boolean isActive() {
            return true;
        }
    },
    @EmployeeStatusConfig(availableEmployeeAccesses = EmployeeAccess.LIMITED)
    ACTIVE(2, "Active"),
    @EmployeeStatusConfig(availableEmployeeAccesses = EmployeeAccess.RESTRICTED)
    INACTIVE(3, "Inactive"),
    @EmployeeStatusConfig(availableEmployeeAccesses = EmployeeAccess.LIMITED)
    DISMISSING(4, "Dismissing"),
    DISMISSED(5, "Dismissed");

    private static final Map<String, EmployeeStatus> ENUM_BY_DESCRIPTION_1;
    private static final Map<String, EmployeeStatus> ENUM_BY_DESCRIPTION_2 = new HashMap<>();

    private final int code;
    private final String description;

    static {
        ENUM_BY_DESCRIPTION_1 = Arrays.stream(EmployeeStatus.values()).collect(toMap(e -> e.description, identity()));
        for (EmployeeStatus o : values()) {
            ENUM_BY_DESCRIPTION_2.put(o.description, o);
        }
    }

    EmployeeStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    @Override
    public int getCode() {
        return code;
    }

    public static EmployeeStatus getEnumByDescription1(String description) {
        return ENUM_BY_DESCRIPTION_1.get(description);
    }

    public static EmployeeStatus getEnumByDescription2(String description) {
        return ENUM_BY_DESCRIPTION_2.get(description);
    }

    public boolean isActive() {
        return false;
    }

    public String format(String token) {
        return switch (this) {
            case DRAFT -> token + " draft";
            case ACTIVE -> token + " active";
            case INACTIVE -> token + " inactive";
            case DISMISSING -> token + " dismissing";
            case DISMISSED -> token + " dismissed";
        };
    }

    public EnumSet<EmployeeStatus> getNextAvailableStatuses() {
        return switch (this) {
            case DRAFT -> EnumSet.of(ACTIVE);
            case ACTIVE -> EnumSet.of(INACTIVE, DISMISSING);
            case INACTIVE -> EnumSet.of(ACTIVE, DISMISSED);
            case DISMISSING -> EnumSet.of(ACTIVE, INACTIVE, DISMISSED);
            case DISMISSED -> EnumSet.noneOf(EmployeeStatus.class);
        };
    }

    public static class Converter extends BaseEnumConverter<EmployeeStatus> {

        public Converter() {
            super(EmployeeStatus.class);
        }
    }

    public Set<EmployeeAccess> getAvailableEmployeeAccesses() {
        return Optional.ofNullable(getField().getAnnotation(EmployeeStatusConfig.class))
            .map(EmployeeStatusConfig::availableEmployeeAccesses)
            .map(Set::of)
            .orElse(Set.of());
    }

    private Field getField() {
        Field field;
        try {
            field = EmployeeStatus.class.getField(this.name());
        } catch (NoSuchFieldException ex) {
            throw new RuntimeException();
        }
        return field;
    }
}
