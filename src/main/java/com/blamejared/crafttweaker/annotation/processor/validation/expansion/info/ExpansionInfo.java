package com.blamejared.crafttweaker.annotation.processor.validation.expansion.info;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

public record ExpansionInfo(TypeMirror expandedType, TypeElement expansionType) {}
