package com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.comment.linktag;

import com.blamejared.crafttweaker.annotation.processor.document.page.type.TypeParameterTypeInfo;

import javax.lang.model.element.Element;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public interface LinkConversionRule {
    
    boolean canConvert(String link);
    
    Optional<String> tryConvertToClickableMarkdown(String link, Element element, Context context);
    
    
    public class Context {
        
        private List<TypeParameterTypeInfo> overridenTypeParams = new ArrayList<>();
        
        private Context() {}
        
        public static Context empty() {
            
            return new Context();
        }
        
        public Context overrideTypeParams(List<TypeParameterTypeInfo> params) {
            
            this.overridenTypeParams = params;
            return this;
        }
    
        public List<TypeParameterTypeInfo> getOverridenTypeParams() {
        
            return Collections.unmodifiableList(overridenTypeParams);
        }
    
    }
    
}
