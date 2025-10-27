package org.sopt.global.validator;

import org.sopt.global.exception.customexception.BadRequestException;

import static org.sopt.global.exception.constant.MemberErrorCode.NAME_BLANK;

public class MemberNameValidator {
    public static void validateName(String name) {
        if(name == null || name.trim().isEmpty()) {
            throw new BadRequestException(NAME_BLANK);
        }
    }
}
