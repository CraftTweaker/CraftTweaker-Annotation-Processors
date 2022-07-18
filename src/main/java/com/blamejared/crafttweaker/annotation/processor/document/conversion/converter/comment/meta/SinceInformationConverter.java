package com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.comment.meta;

import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.comment.SimpleCommentConverter;
import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.comment.documentation_parameter.ParameterInformationList;
import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.comment.documentation_parameter.ParameterReader;

import javax.lang.model.element.Element;
import java.util.function.BiFunction;

public class SinceInformationConverter implements SimpleCommentConverter.StringConverter {
    
    private final ParameterReader reader;
    
    public SinceInformationConverter(final ParameterReader reader) {
        
        this.reader = reader;
    }
    
    @Override
    public String getMarker() {
        
        return "since";
    }
    
    @Override
    public BiFunction<String, Element, ParameterInformationList> getParameterInformationList() {
        
        return reader::readParametersFrom;
    }
    
}