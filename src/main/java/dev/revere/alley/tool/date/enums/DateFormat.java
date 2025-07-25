package dev.revere.alley.tool.date.enums;

import lombok.Getter;

/**
 * @author Emmy
 * @project Alley
 * @since 05/04/2025
 */
@Getter
public enum DateFormat {
    TIME_PLUS_DATE("HH:mm:ss dd/MM/yyyy"),
    DATE_PLUS_TIME("dd/MM/yyyy HH:mm:ss"),
    TIME("HH:mm:ss"),
    DATE("dd/MM/yyyy"),

    ;

    private final String format;

    /**
     * Constructor for the EnumDateFormat enum.
     *
     * @param format The date format string.
     */
    DateFormat(String format) {
        this.format = format;
    }
}