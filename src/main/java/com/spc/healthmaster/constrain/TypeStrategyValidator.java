package com.spc.healthmaster.constrain;

import com.google.common.collect.ImmutableList;
import com.spc.healthmaster.enums.TypeStrategy;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class TypeStrategyValidator implements ConstraintValidator<ValidTypeStrategy, TypeStrategy> {

    private final List<TypeStrategy> acceptedValues = ImmutableList.of(
            TypeStrategy.TOMCAT_SERVER,
            TypeStrategy.GLASSFISH_SERVER,
            TypeStrategy.GLASSFISH_APP,
            TypeStrategy.TOMCAT_APP
    );

    @Override
    public boolean isValid(TypeStrategy value, ConstraintValidatorContext context) {
        return value != null && acceptedValues.contains(value);
    }
}
