package org.sopt.global.validator;

import org.sopt.domain.member.constant.Gender;
import org.sopt.global.exception.customexception.BadRequestException;

import java.util.Arrays;

import static org.sopt.global.exception.constant.MemberErrorCode.GENDER_BLANK;
import static org.sopt.global.exception.constant.MemberErrorCode.INVALID_GENDER;

public class GenderValidator {
    public static void validateGender(String gender) {
        if (gender == null || gender.trim().isEmpty()) {
            throw new BadRequestException(GENDER_BLANK);
        }

        boolean isValid = Arrays.stream(Gender.values())
                .anyMatch(g -> g.name().equalsIgnoreCase(gender.trim()));

        if (!isValid) {
            throw new BadRequestException(INVALID_GENDER);
        }
    }
}
