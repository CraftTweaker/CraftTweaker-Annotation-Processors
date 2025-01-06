package com.blamejared.crafttweaker.annotation.processor.util.annotations;

import javax.annotation.Nonnull;
import javax.annotation.meta.TypeQualifierDefault;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Nonnull
@TypeQualifierDefault({ElementType.METHOD})
@Retention(RetentionPolicy.SOURCE)
public @interface MethodsReturnNonnullByDefault {
}
