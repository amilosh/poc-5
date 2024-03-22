package pl.amilosh.managementservice.enumeration;

public interface BaseEnum<E extends BaseEnum<E>> {

    int getCode();

    default boolean any(E... values) {
        for (E val : values) {
            if (this == val) {
                return true;
            }
        }
        return false;
    }

    default boolean none(E... values) {
        for (E val : values) {
            if (this == val) {
                return false;
            }
        }
        return true;
    }
}
