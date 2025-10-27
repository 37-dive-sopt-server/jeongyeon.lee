package org.sopt.global.validator;

import org.sopt.global.exception.customexception.BadRequestException;

import java.util.regex.Pattern;

import static org.sopt.global.exception.constant.MemberErrorCode.EMAIL_BLANK;
import static org.sopt.global.exception.constant.MemberErrorCode.INVALID_EMAIL;

public class EmailValidator {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    public static void validateEmail(String email) {
        if(email == null || email.trim().isEmpty()) {
            throw new BadRequestException(EMAIL_BLANK);
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new BadRequestException(INVALID_EMAIL);
        }

    }
}
