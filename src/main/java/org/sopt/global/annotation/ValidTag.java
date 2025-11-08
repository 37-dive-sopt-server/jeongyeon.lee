package org.sopt.global.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.sopt.global.validator.TagValidator;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TagValidator.class)
@Documented
public @interface ValidTag {
    String message() default "태그는 CS, DB, SPRING, ETC 중에서만 선택해주세요.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
