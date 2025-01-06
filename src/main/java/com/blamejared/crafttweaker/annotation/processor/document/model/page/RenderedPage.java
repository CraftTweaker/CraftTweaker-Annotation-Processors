package com.blamejared.crafttweaker.annotation.processor.document.model.page;

import com.blamejared.crafttweaker.annotation.processor.document.model.extra.Extra;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;

public class RenderedPage extends Page {
    
    public static final Codec<RenderedPage> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            PageVersion.CODEC.fieldOf("version").forGetter(Page::version),
            Codec.STRING.fieldOf("key").forGetter(Page::key),
            Codec.STRING.fieldOf("display_name").forGetter(Page::displayName),
            Codec.STRING.fieldOf("content").forGetter(RenderedPage::content),
            Codec.STRING.fieldOf("raw_content").forGetter(RenderedPage::rawContent)
    ).apply(instance, RenderedPage::new));
    
    protected final String content;
    protected final String rawContent;
    
    protected RenderedPage(PageVersion version, String key, String displayName, PageKind kind, String content, String rawContent) {
        
        super(version, key, displayName, kind, Optional.empty(), Extra.EMPTY);
        this.content = content;
        this.rawContent = rawContent;
    }
    
    public RenderedPage(PageVersion version, String key, String displayName, String content, String rawContent) {
        
        this(version, key, displayName, PageKind.RENDERED, content, rawContent);
    }
    
    public String content() {
        
        return content;
    }
    
    public String rawContent() {
        
        return rawContent;
    }
    
    @Override
    public boolean equals(Object o) {
        
        if(o == null || getClass() != o.getClass()) {
            return false;
        }
        if(!super.equals(o)) {
            return false;
        }
        
        RenderedPage that = (RenderedPage) o;
        return content.equals(that.content);
    }
    
    @Override
    public int hashCode() {
        
        int result = super.hashCode();
        result = 31 * result + content.hashCode();
        return result;
    }
    
    
    @Override
    public String toString() {
        
        final StringBuilder sb = new StringBuilder("MarkdownPage{");
        sb.append("content='").append(content).append('\'');
        sb.append('}');
        return sb.toString();
    }
    
}
