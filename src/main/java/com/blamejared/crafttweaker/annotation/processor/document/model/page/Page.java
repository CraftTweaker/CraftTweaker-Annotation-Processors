package com.blamejared.crafttweaker.annotation.processor.document.model.page;

import com.blamejared.crafttweaker.annotation.processor.document.model.comment.Comment;
import com.blamejared.crafttweaker.annotation.processor.document.model.extra.Extra;
import com.blamejared.crafttweaker.annotation.processor.util.Tools;
import com.blamejared.crafttweaker.annotation.processor.util.Util;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;

@SuppressWarnings("ALL")
public class Page implements Comparable<Page> {
    
    public static final Codec<Page> CODEC = PageKind.CODEC.dispatch("kind", Page::kind, PageKind::codec);
    private final PageVersion version;
    private final String key; // path
    private final String displayName; // Friendly simple display name
    private final PageKind kind;
    private final Optional<Comment> comment;
    private final Extra extra;
    
    public Page(PageVersion version, String key, String displayName, PageKind kind, Optional<Comment> comment, Extra extra) {
        
        this.version = version;
        this.key = key;
        this.displayName = displayName;
        this.kind = kind;
        this.comment = comment;
        this.extra = extra;
    }
    
    public void write(Path basePath) {
        
        try {
            Path filePath = basePath.resolve(this.key + ".json");
            Files.createDirectories(filePath.getParent());
            
            try(BufferedWriter writer = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING)) {
                JsonElement result = Page.CODEC.encodeStart(JsonOps.INSTANCE, this)
                        .getOrThrow(false, s -> System.out.println(s));
                Tools.GSON.toJson(Util.sort(result), writer);
            } catch(IOException e) {
                throw new RuntimeException("Error writing page to json!", e);
            }
            
        } catch(IOException e) {
            throw new RuntimeException("Error writing page to json!", e);
        }
        
    }
    
    public String key() {
        
        return key;
    }
    
    public String displayName() {
        
        return displayName;
    }
    
    public PageKind kind() {
        
        return kind;
    }
    
    public Optional<Comment> comment() {
        
        return comment;
    }
    
    public Extra extra() {
        
        return extra;
    }
    
    public PageVersion version() {
        
        return version;
    }
    
    
    @Override
    public boolean equals(Object o) {
        
        if(o == null || getClass() != o.getClass()) {
            return false;
        }
        
        Page page = (Page) o;
        return version == page.version && key.equals(page.key) && displayName.equals(page.displayName) && kind == page.kind && comment.equals(page.comment) && extra.equals(page.extra);
    }
    
    @Override
    public int hashCode() {
        
        int result = version.hashCode();
        result = 31 * result + key.hashCode();
        result = 31 * result + displayName.hashCode();
        result = 31 * result + kind.hashCode();
        result = 31 * result + comment.hashCode();
        result = 31 * result + extra.hashCode();
        return result;
    }
    
    @Override
    public int compareTo(@NotNull Page o) {
        
        return this.key.compareTo(o.key);
    }
    
}
