package com.blamejared.crafttweaker.annotation.processor.util;

import javax.tools.SimpleJavaFileObject;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class StringSourceFile extends SimpleJavaFileObject {
    
    private final String name;
    private final String content;
    
    public StringSourceFile(String name, String content) {
        
        super(URI.create(name.replace('.', '/') + Kind.SOURCE.extension), Kind.SOURCE);
        this.name = name;
        this.content = content;
    }
    
    @Override
    public String getName() {
        
        return name;
    }
    
    @Override
    public InputStream openInputStream() {
        
        return new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
    }
    
    @Override
    public Reader openReader(boolean ignoreEncodingErrors) {
        
        return new StringReader(content);
    }
    
    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
        
        return content;
    }
    
}
