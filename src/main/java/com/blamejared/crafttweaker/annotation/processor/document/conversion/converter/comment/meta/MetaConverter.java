package com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.comment.meta;


import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.comment.SimpleCommentConverter;
import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.comment.documentation_parameter.ParameterInfo;
import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.comment.documentation_parameter.ParameterInformationList;
import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.comment.documentation_parameter.ParameterReader;
import com.blamejared.crafttweaker.annotation.processor.document.meta.MetaData;
import com.blamejared.crafttweaker.annotation.processor.document.page.member.header.examples.Example;

import javax.lang.model.element.Element;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class MetaConverter implements SimpleCommentConverter<MetaData> {
    
    private static final String DOC_SHORT_DESC = "docShortDescription";
    
    private final ParameterReader reader;
    
    public MetaConverter(ParameterReader reader) {
        
        this.reader = reader;
    }
    
    @Override
    public MetaData fromParameterInfo(ParameterInfo info) {
    
        return new MetaData(info.getAnyOccurrence());
    }
    
    @Override
    public String getMarker() {
        
        return "docShortDescription";
    }
    
    @Override
    public BiFunction<String, Element, ParameterInformationList> getParameterInformationList() {
        
        return reader::readParametersFrom;
    }
    
}
