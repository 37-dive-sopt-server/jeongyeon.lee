package org.sopt.util.validator;

import org.sopt.exception.customexception.BadRequestException;

import static org.sopt.exception.constant.ErrorMessage.NAME_BLANK;

public class MemberNameValidator {
    public static void validateName(String name) {
        if(name == null || name.trim().isEmpty()) {
            throw new BadRequestException(NAME_BLANK.getMessage());
        }
    }
}
