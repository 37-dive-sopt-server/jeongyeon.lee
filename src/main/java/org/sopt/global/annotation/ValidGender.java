package org.sopt.global.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.sopt.global.validator.GenderValidator;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = GenderValidator.class)
@Documented
public @interface ValidGender {
    String message() default "성별은 MALE, FEMALE 중에서만 선택해주세요.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}