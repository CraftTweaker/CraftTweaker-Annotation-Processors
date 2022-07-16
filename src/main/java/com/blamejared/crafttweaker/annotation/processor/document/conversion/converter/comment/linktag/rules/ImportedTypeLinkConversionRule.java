package com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.comment.linktag.rules;

import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.comment.linktag.LinkConversionRule;
import com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.comment.linktag.LinkConverter;
import com.blamejared.crafttweaker.annotation.processor.document.page.type.TypeParameterTypeInfo;
import com.blamejared.crafttweaker.annotation.processor.util.Pair;

import javax.lang.model.element.Element;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

public class ImportedTypeLinkConversionRule implements LinkConversionRule {
    
    private final LinkConverter linkConverter;
    private final AccessibleTypesFinder accessibleTypesFinder;
    
    public ImportedTypeLinkConversionRule(LinkConverter linkConverter, AccessibleTypesFinder accessibleTypesFinder) {
        
        this.linkConverter = linkConverter;
        this.accessibleTypesFinder = accessibleTypesFinder;
    }
    
    
    @Override
    public boolean canConvert(String link) {
        
        return true;
    }
    
    @Override
    public Optional<String> tryConvertToClickableMarkdown(String link, Element element, LinkConversionRule.Context context) {
        
        try {
            Pair<Type, Integer> construct = extract(link, element, false);
            
            return Optional.of(construct.first().compile(linkConverter, element));
        } catch(Exception ignored) {
            return Optional.empty();
        }
    }
    
    private Pair<Type, Integer> extract(String link, Element element, boolean subType) {
        
        Type type = new Type();
        StringBuilder name = new StringBuilder();
        int i = 0;
        outer:
        while(i < link.length()) {
            char c = link.charAt(i++);
            if(Character.isWhitespace(c)) {
                continue;
            }
            switch(c) {
                case '<', ',' -> {
                    if(',' == c && subType) {
                        i--;
                        break outer;
                    }
                    Pair<Type, Integer> construct = extract(link.substring(i), element, true);
                    Type param = construct.first();
                    type.addParam(param);
                    i += construct.second();
                }
                case '>' -> {
                    break outer;
                }
                default -> name.append(c);
            }
        }
        type.name(getQualifiedNameFor(name.toString(), element));
        return new Pair<>(type, i);
    }
    
    private String getQualifiedNameFor(String link, Element element) {
        
        final List<String> importedTypes = getImportedTypesAt(element);
        return qualifyName(link, importedTypes);
    }
    
    private List<String> getImportedTypesAt(Element element) {
        
        return accessibleTypesFinder.getAllAccessibleTypesAt(element);
    }
    
    private String qualifyName(String link, List<String> importedTypes) {
        
        final String[] split = link.split("\\.", 2);
        
        for(String importedType : importedTypes) {
            if(doesImportMatchLink(importedType, split[0])) {
                return split.length == 1 ? importedType : (String.format("%s.%s", importedType, split[1]));
            }
        }
        throw new IllegalArgumentException("Could not qualify " + link);
    }
    
    private boolean doesImportMatchLink(String importedType, String link) {
        
        return importedType.endsWith("." + link);
    }
    
    private static class Type {
        
        private String name;
        private final List<Type> typeParams = new ArrayList<>();
        
        public void addParam(Type type) {
            
            this.typeParams.add(type);
        }
        
        public void name(String name) {
            
            this.name = name;
        }
        
        public String getName() {
            
            return name;
        }
        
        public List<Type> getTypeParams() {
            
            return typeParams;
        }
        
        public String compile(LinkConverter linkConverter, Element element) {
            
            return linkConverter.convertLinkToClickableMarkdown(name, element, Context.empty()
                    .overrideTypeParams(getTypeParams().stream()
                            .map(type -> new TypeParameterTypeInfo(type.compile(linkConverter, element)))
                            .toList()));
        }
        
        @Override
        public String toString() {
            
            return new StringJoiner(", ", Type.class.getSimpleName() + "[", "]")
                    .add("name='" + name + "'")
                    .add("typeParams=" + typeParams)
                    .toString();
        }
        
    }
    
}
