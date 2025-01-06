package com.blamejared.crafttweaker.annotation.processor.document.model;

import com.blamejared.crafttweaker.annotation.processor.document.model.page.Page;
import com.blamejared.crafttweaker.annotation.processor.util.codec.RecursiveCodec;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class DocFolder implements Comparable<DocFolder> {
    
    public static final Codec<DocFolder> NAV_CODEC = new RecursiveCodec<>(decoder -> Codec.unboundedMap(Codec.STRING, Codec.either(decoder, Codec.STRING))
            .comapFlatMap(stringEitherMap -> DataResult.error("Cannot Deserialize a doc folder!"), DocFolder::nav));
    
    private Map<String, Either<DocFolder, String>> nav() {
        
        Map<String, Either<DocFolder, String>> result = new HashMap<>();
        this.children().forEach((s, page) -> {
            result.put(StringUtils.capitalize(page.displayName()), Either.right(page.key() + ".json"));
        });
        this.subFolders().forEach((s, folder) -> {
            result.put(StringUtils.capitalize(folder.displayName()), Either.left(folder));
        });
        return result;
    }
    
    public static final DocFolder ROOT = new DocFolder("docs", "nav", new TreeMap<>(), new TreeMap<>());
    private final String key; // path
    private final String displayName; // friendly display name
    private final Map<String, DocFolder> subFolders;
    private final Map<String, Page> children;
    
    public DocFolder(String key, String displayName, Map<String, DocFolder> subFolders, Map<String, Page> children) {
        
        this.key = key;
        this.displayName = displayName;
        this.subFolders = subFolders;
        this.children = children;
    }
    
    
    public void write(Path outputDirectory) {
        
        write(outputDirectory, outputDirectory);
    }
    
    public void write(Path outputDirectory, Path basePath) {
        
        try {
            Path thisPath = Files.createDirectories(basePath.resolve(this.key));
            this.subFolders.values().forEach(docFolder -> docFolder.write(outputDirectory, thisPath));
            this.children.values().forEach(child -> child.write(outputDirectory.resolve(ROOT.key())));
        } catch(IOException e) {
            throw new RuntimeException("Error creating DocFolder directories!", e);
        }
        
    }
    
    public String key() {
        
        return key;
    }
    
    public String displayName() {
        
        return displayName;
    }
    
    public Map<String, DocFolder> subFolders() {
        
        return subFolders;
    }
    
    public Map<String, Page> children() {
        
        return children;
    }
    
    public void subFolder(final DocFolder subFolder) {
        
        this.subFolders.put(subFolder.key(), subFolder);
    }
    
    public Page child(final Page child) {
        
        this.children.put(child.key(), child);
        return child;
    }
    
    public Page child(String[] parentPaths, Page page) {
        
        DocFolder parentFolder = this;
        for(String parentPath : parentPaths) {
            parentFolder = parentFolder.subFolders()
                    .computeIfAbsent(parentPath, s -> new DocFolder(s.toLowerCase(), s, new TreeMap<>(), new TreeMap<>()));
        }
        return parentFolder.child(page);
    }
    
    @Override
    public int compareTo(@NotNull DocFolder o) {
        
        return this.key.compareTo(o.key);
    }
    
}
