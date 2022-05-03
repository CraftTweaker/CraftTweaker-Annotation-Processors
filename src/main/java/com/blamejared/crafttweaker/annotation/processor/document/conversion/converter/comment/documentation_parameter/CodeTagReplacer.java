package com.blamejared.crafttweaker.annotation.processor.document.conversion.converter.comment.documentation_parameter;

import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeTagReplacer {
    
    private static final Pattern CODE_TAG_PATTERN = Pattern.compile("\\{@code[ \t\n\r]+([^}]+)}");
    
    public String replaceCodeTags(final String docComment) {
        
        final Matcher matcher = CODE_TAG_PATTERN.matcher(docComment);
        return replaceCodeTags(matcher, docComment);
    }
    
    private String replaceCodeTags(final Matcher matcher, final String docComment) {
        
        final StringBuffer buffer = new StringBuffer();
        
        while(matcher.find()) {
            final MatchResult matchResult = matcher.toMatchResult();
            final String replacement = String.format("`%s`", matchResult.group(1));
            matcher.appendReplacement(buffer, replacement);
        }
        
        matcher.appendTail(buffer);
        return buffer.toString();
    }
    
}
