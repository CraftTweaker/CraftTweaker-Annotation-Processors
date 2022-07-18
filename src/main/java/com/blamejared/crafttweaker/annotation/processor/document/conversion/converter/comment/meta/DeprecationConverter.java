package com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.comment.meta;

import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.comment.SimpleCommentConverter;
import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.comment.documentation_parameter.ParameterInformationList;
import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.comment.documentation_parameter.ParameterReader;

import javax.lang.model.element.Element;
import java.util.function.BiFunction;

public class DeprecationConverter implements SimpleCommentConverter.StringConverter {
    
    private final ParameterReader reader;
    
    public DeprecationConverter(final ParameterReader reader) {
        
        this.reader = reader;
    }
    
    @Override
    public String getMarker() {
        
        return "deprecated";
    }
    
    @Override
    public BiFunction<String, Element, ParameterInformationList> getParameterInformationList() {
        
        return reader::readParametersFrom;
    }
    
}
