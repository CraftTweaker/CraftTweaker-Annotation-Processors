package com.blamejared.crafttweaker.annotation.processor.util;

import io.toolisticon.aptk.tools.MessagerUtils;

import javax.lang.model.element.Element;
import java.util.Set;
import java.util.stream.Collectors;

public class ZenCodeKeywordUtil {
    
    private static final Set<String> KNOWN_KEYWORDS = Set.of(
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
    
    public static String convertToZenCode(String name) {
        
        if(name.equals("boolean")) {
            return "bool";
        }
        if(name.equals("String") || name.equals("java.lang.String")) {
            return "string";
        }
        return name;
    }
    
    public static boolean isKeyword(String name) {
        
        return KNOWN_KEYWORDS.contains(name);
    }
    
    public static void checkName(Element element) {
        
        final String name = element.getSimpleName().toString();
        checkName(name, element);
    }
    
    public static void checkName(String name, Element element) {
        
        if(isKeyword(name)) {
            final String message = String.format("Name '%s' is a ZenCode keyword!", name);
            MessagerUtils.error(element, message);
        }
    }
    
    public static void checkName(Set<String> names, Element element) {
        
        Set<String> invalidNames = names.stream().filter(ZenCodeKeywordUtil::isKeyword).collect(Collectors.toSet());
        
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
            MessagerUtils.error(element, builder.toString());
        }
    }
    
}
