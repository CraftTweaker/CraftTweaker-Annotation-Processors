package com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.comment.example;


import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.comment.SimpleCommentConverter;
import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.comment.documentation_parameter.ParameterInfo;
import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.comment.documentation_parameter.ParameterInformationList;
import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.comment.documentation_parameter.ParameterReader;
import com.blamejared.crafttweaker.annotation.processor.document.page.member.header.examples.Example;
import com.blamejared.crafttweaker.annotation.processor.document.page.member.header.examples.ExampleData;

import javax.lang.model.element.Element;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class ExampleDataConverter implements SimpleCommentConverter<ExampleData> {

    private final ParameterReader reader;
    
    public ExampleDataConverter(ParameterReader reader) {
        
        this.reader = reader;
    }
    
    @Override
    public ExampleData fromParameterInfo(ParameterInfo info) {
        
        final Map<String, Example> examples = getExamplesFrom(info);
        return new ExampleData(examples);
    }
    
    @Override
    public String getMarker() {
        
        return "docParam";
    }
    
    @Override
    public BiFunction<String, Element, ParameterInformationList> getParameterInformationList() {
        
        return reader::readParametersFrom;
    }
    
    private Map<String, Example> getExamplesFrom(ParameterInfo docParamInfo) {
        
        return docParamInfo.getOccurrences()
                .stream()
                .map(this::getExampleFromOccurrence)
                .collect(asMapWithMergedExamples());
    }
    
    private Collector<Example, ?, Map<String, Example>> asMapWithMergedExamples() {
        
        final Function<Example, String> keyMapper = Example::getName;
        final Function<Example, Example> valueMapper = Function.identity();
        final BinaryOperator<Example> mergeFunction = Example::merge;
        
        return Collectors.toMap(keyMapper, valueMapper, mergeFunction);
    }
    
    private Example getExampleFromOccurrence(String occurrence) {
        
        verifyThatOccurenceCanBeSplit(occurrence);
        
        final String exampleName = getExampleNameFrom(occurrence);
        final String textValue = getExampleTextValueFrom(occurrence);
        
        return new Example(exampleName, textValue);
    }
    
    private void verifyThatOccurenceCanBeSplit(String occurrence) {
        
        if(splitOccurence(occurrence).length != 2) {
            throw new IllegalArgumentException("Cannot split exampleData " + occurrence);
        }
    }
    
    private String[] splitOccurence(String occurrence) {
        
        return occurrence.split(" ", 2);
    }
    
    private String getExampleNameFrom(String occurrence) {
        
        return splitOccurence(occurrence)[0];
    }
    
    private String getExampleTextValueFrom(String occurrence) {
        
        return splitOccurence(occurrence)[1];
    }
    
}
