package org.sopt.global.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.sopt.domain.member.constant.Gender;
import org.sopt.global.annotation.ValidGender;

import java.util.Arrays;

public class GenderValidator implements ConstraintValidator<ValidGender, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(s == null || s.trim().isEmpty()) {return false;}
        return Arrays.stream(Gender.values()).anyMatch(g -> g.name().equalsIgnoreCase(s.trim()));
    }
}
