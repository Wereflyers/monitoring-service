package ru.ylab.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * The list of Indication types.
 */
public class IndicationType {
    /**
     * List of types
     */
    public static final List<String> types = new ArrayList<>();

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
