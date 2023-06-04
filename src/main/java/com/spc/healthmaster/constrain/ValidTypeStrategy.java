package com.spc.healthmaster.constrain;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TypeStrategyValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidTypeStrategy {
    String message() default "Invalid typeStrategy value, Must be one of [TOMCAT_SERVER, GLASSFISH_SERVER, GLASSFISH_APP, TOMCAT_APP]";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
