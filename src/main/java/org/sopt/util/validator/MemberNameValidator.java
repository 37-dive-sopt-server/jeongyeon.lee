package org.sopt.util.validator;

import static org.sopt.constant.ErrorMessage.NAME_BLANK;

public class MemberNameValidator {
    public static void validateName(String name) {
        if(name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(NAME_BLANK.getMessage());
        }
    }
}
