package com.blamejared.crafttweaker.annotation.processor.validation.util;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic;
import java.util.Set;
import java.util.stream.Collectors;

public class ZenCodeKeywordUtil {
    
    private final Set<String> knownKeywords = Set.of(
            "import",
            "alias",
            "class",
            "function",
            "interface",
            "enum",
            "struct",
            "expand",
            "variant",
            "abstract",
            "final",
            "override",
            "const",
            "private",
            "public",
            "export",
            "internal",
            "static",
            "protected",
            "implicit",
            "virtual",
            "extern",
            "immutable",
            "val",
            "var",
            "get",
            "implements",
            "set",
            "void",
            "bool",
            "byte",
            "sbyte",
            "short",
            "ushort",
            "int",
            "uint",
            "long",
            "ulong",
            "usize",
            "float",
            "double",
            "char",
            "string",
            "if",
            "else",
            "do",
            "while",
            "for",
            "throw",
            "panic",
            "lock",
            "try",
            "catch",
            "finally",
            "return",
            "break",
            "continue",
            "switch",
            "case",
            "default",
            "in",
            "is",
            "as",
            "match",
            "throws",
            "super",
            "this",
            "null",
            "true",
            "false",
            "new"
    );
    
    public boolean isKeyword(String name) {
        
        return knownKeywords.contains(name);
    }
    
    public void checkName(Element element, Messager messager) {
        
        final String name = element.getSimpleName().toString();
        checkName(name, element, messager);
    }
    
    public void checkName(String name, Element element, Messager messager) {
        
        if(isKeyword(name)) {
            final String message = String.format("Name '%s' is a ZenCode keyword!", name);
            messager.printMessage(Diagnostic.Kind.ERROR, message, element);
        }
    }
    
    public void checkName(Set<String> names, Element element, Messager messager) {
        
        Set<String> invalidNames = names.stream().filter(this::isKeyword).collect(Collectors.toSet());
        
        if(!invalidNames.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            if(names.size() == 1) {
                builder.append("Name '").append(invalidNames.iterator().next()).append("' is a ZenCode keyword!");
            } else {
                builder.append("Invalid name(s) found in full name: '")
                        .append(String.join(".", names))
                        .append("': '")
                        .append(String.join(", ", invalidNames))
                        .append("'");
            }
            messager.printMessage(Diagnostic.Kind.ERROR, builder, element);
        }
    }
    
}
