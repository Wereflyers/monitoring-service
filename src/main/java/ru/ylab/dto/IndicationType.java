package ru.ylab.dto;

import java.util.HashSet;
import java.util.Set;

/**
 * The list of Indication types.
 */
public class IndicationType {
    /**
     * List of types
     */
    public static final Set<String> types = new HashSet<>();

    static {
        types.add("ГВ");
        types.add("ХВ");
        types.add("Отопление");
    }

    /**
     * Add type.
     *
     * @param type the type
     */
    public static void addType(String type) {
        types.add(type);
    }
}
