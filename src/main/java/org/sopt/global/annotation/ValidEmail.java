package org.sopt.global.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.sopt.global.validator.EmailValidator;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailValidator.class)
@Documented
public @interface ValidEmail {
    String message() default "이메일 형식에 맞게 입력해주세요.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
