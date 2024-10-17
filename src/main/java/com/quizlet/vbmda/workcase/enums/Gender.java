/*
 * Copyright 2019 (C) VinBrain
 */

package net.vinbrain.vbmda.workcase.enums;

import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @author Nguyen Minh Man
 */
public enum Gender {
    MALE,
    FEMALE,
    UNKNOWN;

    public String getFirstLetter() {
        switch (this) {
            case FEMALE: return "F";
            case MALE: return "M";
            default: return "O";
        }
    }

    public static Gender toGender(final String value) {
        if (StringUtils.isBlank(value)) {
            return null;
        }

        switch (value) {
            case "F": return FEMALE;
            case "M": return MALE;
            default: return UNKNOWN;
        }
    }

    public static String convertToGenderByInteger(final Integer gender) {
        if (Objects.isNull(gender)) {
            return null;
        }

        switch (gender) {
            case 1: return "M";
            case 2: return "F";
            default: return "O";
        }
    }

}
