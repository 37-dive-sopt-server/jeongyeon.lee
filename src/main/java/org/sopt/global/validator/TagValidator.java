package org.sopt.global.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.sopt.domain.article.constant.ArticleTag;
import org.sopt.global.annotation.ValidTag;

import java.util.Arrays;

public class TagValidator implements ConstraintValidator<ValidTag, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return Arrays.stream(ArticleTag.values())
                .anyMatch(articleTag -> articleTag.name().equals(s));
    }
}
