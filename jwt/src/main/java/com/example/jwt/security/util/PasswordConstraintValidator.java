package com.example.jwt.security.util;

import java.util.Arrays;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.example.jwt.security.annotation.Password;

import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.EnglishSequenceData;
import org.passay.IllegalSequenceRule;
import org.passay.LengthRule;
import org.passay.PasswordData;
import org.passay.PasswordValidator;
import org.passay.RuleResult;
import org.passay.WhitespaceRule;

public class PasswordConstraintValidator implements ConstraintValidator<Password, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        PasswordValidator validator = new PasswordValidator(
            Arrays.asList(
                // 길이범위
                new LengthRule(8, 16),
                // 최소입력유형
                new CharacterRule(EnglishCharacterData.UpperCase, 1),
                new CharacterRule(EnglishCharacterData.LowerCase, 1),
                new CharacterRule(EnglishCharacterData.Digit, 1),
                new CharacterRule(EnglishCharacterData.LowerCase, 1),
                new CharacterRule(EnglishCharacterData.Special, 1),
                // 빈공간 제한
                new WhitespaceRule(),
                // 순차입력 제한
                new IllegalSequenceRule(EnglishSequenceData.Alphabetical, 3, false),
                new IllegalSequenceRule(EnglishSequenceData.Numerical, 3, false)
            )
        );

        RuleResult ruleResult = validator.validate(new PasswordData(value));
        if (ruleResult.isValid()) {
            return true;
        }

        List<String> errors = validator.getMessages(ruleResult);
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(errors.toString()).addConstraintViolation();

        return false;
    }
    
}
