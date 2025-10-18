package org.sopt.util.validator;

import java.util.regex.Pattern;

import static org.sopt.exception.constant.ErrorMessage.*;

public class EmailValidator {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );

    public static void validateEmail(String email) {
        if(email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException(EMAIL_BLANK.getMessage());
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException(INVALID_EMAIL.getMessage());
        }

    }
}
