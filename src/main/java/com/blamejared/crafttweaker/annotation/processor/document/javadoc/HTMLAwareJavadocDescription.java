package com.blamejared.crafttweaker.annotation.processor.document.javadoc;

import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.JavaDocContainerElement;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.JavadocBlockTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.JavadocInlineTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.JavadocNewLine;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.JavadocRootElement;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.JavadocSnippet;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.html.JavadocBrTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.html.JavadocCodeTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.html.JavadocEmTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.html.JavadocLiTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.html.JavadocPTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.html.JavadocStrongTag;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.html.JavadocUlTag;
import com.blamejared.crafttweaker.annotation.processor.util.Tools;
import com.sun.source.util.TreePath;
import com.sun.source.util.Trees;
import io.toolisticon.aptk.common.ToolingProvider;

import javax.lang.model.element.Element;
import java.util.Optional;

public class HTMLAwareJavadocDescription {
    
    public static Optional<JavadocRootElement> parse(Element element) {
        
        Trees trees = Tools.TREES.apply(ToolingProvider.getTooling().getProcessingEnvironment());
        TreePath path = trees.getPath(element);
        if(path != null) {
            String docComment = trees.getDocComment(path);
            if(docComment != null) {
                return Optional.of(parse(docComment));
            }
        }
        return Optional.empty();
    }
    
    public static JavadocRootElement parse(String text) {
        return parse(text, true);
    }
    public static JavadocRootElement parse(String text, boolean eatNonExplicitNewLines) {
        
        // We don't want any trailing whitespace
        text = text.trim();
        JavadocRootElement root = new JavadocRootElement();
        JavaDocContainerElement currentContainer = root;
        for(int i = 0; i < text.length(); i++) {
            int originalIndex = i;
            char c = getChar(text, i);
            //TODO this needs to deal with multiline @things, investigate currentContainer and why it ends up messing up, stop eating the line, let it add it all as contents
            //TODO blocktags can only be at the start of the line, in theory the character before them is always \n, so maybe check for that?
            if(c == '@') {
                // eat the @
                i++;
                int endIndex = getNextIndex(text.substring(i), '\n');
                if(endIndex == -1) {
                    endIndex = text.length() - i;
                }
                String line = text.substring(i, i + endIndex);
                int typeEnd = getNextIndex(text.substring(i), ' ');
                String type = line.substring(0, typeEnd);
                
                // capture the type and the space after
                int contentStart = i + typeEnd + 1;
                // This should be the start of the next block,
                int endOfContent = text.substring(contentStart).indexOf("\n @");
                
                if(endOfContent == -1) {
                    endOfContent = text.length() - contentStart;
                } else {
                    if(getChar(text, contentStart + endOfContent - 1) == '\n') {
                        // -1 to not eat the \n
                        endOfContent -= 1;
                    }
                }
                String content = text.substring(contentStart, contentStart + endOfContent);
                currentContainer.addElement(JavadocBlockTag.from(currentContainer, type, Optional.of(content)));
                
                i = contentStart + endOfContent - 1;
            } else if(c == '{' && getChar(text, i + 1) == '@') {
                // advance to eat the {@
                i += 2;
                int bound = text.substring(i).indexOf("}");
                if(bound == -1) {
                    i = originalIndex;
                    continue;
                }
                String match = text.substring(i, i + bound);
                i += bound;
                int endIndex = match.indexOf(" ");
                if(endIndex == -1) {
                    currentContainer.addElement(JavadocInlineTag.from(currentContainer, match, Optional.empty()));
                    continue;
                    
                }
                
                String name = match.substring(0, endIndex);
                String content = match.substring(endIndex + 1);
                currentContainer.addElement(JavadocInlineTag.from(currentContainer, name, Optional.of(content)));
            } else if(c == '<' && text.substring(i).contains(">")) {
                // We *probably* contain a tag, lets figure out which one it is
                
                int endIndex = getNextIndex(text.substring(i + 1), '>') + 1;
                String foundTag = text.substring(i + 1, i + endIndex);
                boolean closed = foundTag.startsWith("/");
                if(foundTag.endsWith("/")) {
                    foundTag = foundTag.substring(0, foundTag.length() - 1);
                    closed = true;
                }
                switch(foundTag) {
                    case "em":
                        currentContainer = currentContainer.addElement(new JavadocEmTag(currentContainer));
                        break;
                    case "strong":
                        currentContainer = currentContainer.addElement(new JavadocStrongTag(currentContainer));
                        break;
                    case "p":
                        currentContainer = currentContainer.addElement(new JavadocPTag(currentContainer));
                        break;
                    case "li":
                        currentContainer = currentContainer.addElement(new JavadocLiTag(currentContainer));
                        break;
                    case "ul":
                        currentContainer = currentContainer.addElement(new JavadocUlTag(currentContainer));
                        break;
                    case "br":
                        // This also matches a closed br tag, <br/>, but we will end up pop that a few lines down
                        // So we need to add it as a container
                        JavadocBrTag brContainer = currentContainer.addElement(new JavadocBrTag(currentContainer));
                        if(closed) {
                            currentContainer = brContainer;
                        }
                        if(getChar(text, i + endIndex + 1) == '\n') {
                            // eat non-explicit newlines
                            i++;
                        }
                        break;
                    case "/em":
                    case "/strong":
                    case "/p":
                    case "/li":
                    case "/ul":
                    case "/br":
                        break;
                    default:
                        // We didn't match any tag we know about, lets just insert it incase it is a bep
                        currentContainer.addElement(new JavadocSnippet(currentContainer, "<" + foundTag + ">"));
                        closed = false;
                        break;
                }
                if(closed) {
                    currentContainer = currentContainer.container();
                    // If currentContainer is null, then something is really wrong, lets just fallback and play it safe
                    if(currentContainer == null) {
                        JavadocRootElement fallback = new JavadocRootElement();
                        fallback.addElement(new JavadocSnippet(fallback, text));
                        return fallback;
                    }
                }
                i += endIndex;
            } else if(c == '`' && text.substring(i).contains("`")) {
                //TODO check if the char is a `, then try and eat 2 more, if we can, then it is multiline
                i++;
                //TODO this should deal with ```code```
                int endIndex = getNextIndex(text.substring(i + 1), '`') + 1;
                
                JavadocCodeTag element = new JavadocCodeTag(currentContainer);
                element.addElement(new JavadocSnippet(element, text.substring(i, i + endIndex)));
                currentContainer.addElement(element);
                i += endIndex;
            } else if(c == '\n') {
                currentContainer.addElement(new JavadocNewLine(currentContainer));
                if(getChar(text, i + 1) == ' ') {
                    // Eat spaces after newlines
                    i++;
                }
            } else {
                currentContainer.addElement(new JavadocSnippet(currentContainer, String.valueOf(c)));
                if(eatNonExplicitNewLines && getChar(text, i + 1) == '\n') {
                    // eat non-explicit newlines
                    i++;
                    //TODO do I want to eat spaces after non explicit newlines? it leads to issues in addGlobalAttributeModifiers
                    //                    if(getChar(text, i + 1) == ' ') {
                    //                        // Eat spaces after newlines
                    //                        i++;
                    //                    }
                }
            }
        }
        
        return root;
    }
    
    private static char getChar(String source, int index) {
        
        if(index >= source.length()) {
            return '';
        }
        return source.charAt(index);
    }
    
    private static boolean matches(String source, int index, String text) {
        
        if(index + text.length() >= source.length()) {
            return false;
        }
        for(int i = 0; i < text.length(); i++) {
            if(getChar(source, index + i) != text.charAt(i)) {
                return false;
            }
        }
        return true;
    }
    
    
    private static int getNextIndex(String source, char c) {
        
        return source.indexOf(c);
    }
    
}
