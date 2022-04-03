package com.millenium.milleniumserver.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Type utilities class
 */
public final class TypeUtils {

    private TypeUtils() {
        throw new IllegalStateException(
                "Forbidden to create an instance of ru.ds.rsb.escm.content.dao.utils.TypeUtils class"
        );
    }

    /**
     * Parses from input string value
     * and returns value appropriate for input clazz parameters
     * or returns value if value instance of input clazz parameters
     * <p>
     * Supports class types for valueOf:
     * - {@link LocalDateTime}
     * - {@link LocalDate}
     * - {@link Integer}
     * - {@link Long}
     * - {@link BigDecimal}
     *
     * @param value - parsable value
     * @param clazz - appropriate class
     * @param <T>
     * @return
     */
    public static <T> T valueOf(Object value, Class<T> clazz) {

        if (value == null) {
            return null;
        } else if (value.getClass() == clazz) {
            return (T) value;
        } else if (!(value instanceof String)) {
            throw new IllegalArgumentException(
                    "Input parameter 'value' must have type of 'clazz' or String"
            );
        } else if (clazz == LocalDateTime.class) {
            return (T) LocalDateTime.parse(value.toString(), DateTimeFormatter.ISO_DATE_TIME);
        } else if (clazz == LocalDate.class) {
            return (T) LocalDate.parse(value.toString(), DateTimeFormatter.ISO_DATE);
        } else if (clazz == Long.class) {
            return (T) Long.valueOf(value.toString());
        } else if (clazz == Integer.class) {
            return (T) Integer.valueOf(value.toString());
        } else if (clazz == BigDecimal.class) {
            return (T) new BigDecimal(value.toString());
        } else if (clazz == Double.class) {
            return (T) Double.valueOf(value.toString());
        }

        throw new IllegalArgumentException(
                "Couldn't determinate type for valueOf execution"
        );
    }
}
