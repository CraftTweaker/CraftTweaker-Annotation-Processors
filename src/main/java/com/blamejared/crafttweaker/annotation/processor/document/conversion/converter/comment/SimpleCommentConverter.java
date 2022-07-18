package com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.comment;

import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.comment.documentation_parameter.ParameterInfo;
import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.comment.documentation_parameter.ParameterInformationList;

import javax.annotation.Nullable;
import javax.lang.model.element.Element;
import java.util.Optional;
import java.util.function.BiFunction;

public interface SimpleCommentConverter<T> {
    
    default Optional<T> fromComment(@Nullable final String docComment, final Element element) {
        
        if(docComment == null) {
            return Optional.empty();
        }
        
        final ParameterInformationList list = getParameterInformationList().apply(docComment, element);
        
        if(list.hasParameterInfoWithName(getMarker())) {
            return Optional.of(this.fromParameterInfo(list.getParameterInfoWithName(getMarker())));
        }
        
        return Optional.empty();
    }
    
    T fromParameterInfo(ParameterInfo info);
    
    String getMarker();
    
    BiFunction<String, Element, ParameterInformationList> getParameterInformationList();
    
    
    public interface StringConverter extends SimpleCommentConverter<String> {
        
        @Override
        default String fromParameterInfo(ParameterInfo info) {
            
            return java.lang.String.join("", info.getOccurrences());
        }
        
    }
    
}
