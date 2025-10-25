package org.sopt.global.validator;

import org.sopt.domain.member.constant.Gender;

import java.util.Arrays;

import static org.sopt.global.exception.constant.ErrorMessage.GENDER_BLANK;
import static org.sopt.global.exception.constant.ErrorMessage.INVALID_GENDER;

public class GenderValidator {
    public static void validateGender(String gender) {
        if (gender == null || gender.trim().isEmpty()) {
            throw new IllegalArgumentException(GENDER_BLANK.getMessage());
        }

        boolean isValid = Arrays.stream(Gender.values())
                .anyMatch(g -> g.name().equalsIgnoreCase(gender.trim()));

        if (!isValid) {
            throw new IllegalArgumentException(INVALID_GENDER.getMessage());
        }
    }
}
