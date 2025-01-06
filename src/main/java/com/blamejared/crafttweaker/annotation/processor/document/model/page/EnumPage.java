package com.blamejared.crafttweaker.annotation.processor.document.model.page;

import com.blamejared.crafttweaker.annotation.processor.document.model.comment.Comment;
import com.blamejared.crafttweaker.annotation.processor.document.model.extra.Extra;
import com.blamejared.crafttweaker.annotation.processor.document.model.member.MemberGroup;
import com.blamejared.crafttweaker.annotation.processor.document.model.type.Type;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class EnumPage extends Page {
    
    public static final Codec<EnumPage> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            PageVersion.CODEC.fieldOf("version").forGetter(Page::version),
            Codec.STRING.fieldOf("key").forGetter(Page::key),
            Codec.STRING.fieldOf("display_name").forGetter(Page::displayName),
            Comment.CODEC.optionalFieldOf("comment").forGetter(Page::comment),
            Extra.CODEC.fieldOf("extra").forGetter(Page::extra),
            Type.CODEC.fieldOf("type").forGetter(EnumPage::type),
            Codec.STRING.fieldOf("zen_code_name").forGetter(EnumPage::zenCodeName),
            Codec.unboundedMap(Codec.STRING, MemberGroup.CODEC).fieldOf("members").forGetter(EnumPage::members)
    ).apply(instance, EnumPage::new));
    
    protected final Type type;
    protected final String zenCodeName;
    protected final Map<String, MemberGroup> members;
    
    protected EnumPage(PageVersion version, String key, String displayName, PageKind kind, Optional<Comment> comment, Extra extra, Type type, String zenCodeName, Map<String, MemberGroup> members) {
        
        super(version, key, displayName, kind, comment, extra);
        this.type = type;
        this.zenCodeName = zenCodeName;
        this.members = members;
    }
    
    public EnumPage(PageVersion version, String key, String displayName, Optional<Comment> comment, Extra extra, Type type, String zenCodeName, Map<String, MemberGroup> members) {
        
        this(version, key, displayName, PageKind.ENUM, comment, extra, type, zenCodeName, members);
    }
    
    public Type type() {
        
        return type;
    }
    
    public String zenCodeName() {
        
        return zenCodeName;
    }
    
    public Map<String, MemberGroup> members() {
        
        return members;
    }
    
    @Override
    public String toString() {
        
        final StringBuilder sb = new StringBuilder("EnumPage{");
        sb.append("type=").append(type);
        sb.append(", zenCodeName='").append(zenCodeName).append('\'');
        sb.append(", members=").append(members);
        sb.append('}');
        return sb.toString();
    }
    
    @Override
    public boolean equals(Object o) {
        
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }
        if(!super.equals(o)) {
            return false;
        }
        
        EnumPage typePage = (EnumPage) o;
        return Objects.equals(type, typePage.type) && Objects.equals(zenCodeName, typePage.zenCodeName) && Objects.equals(members, typePage.members);
    }
    
    @Override
    public int hashCode() {
        
        int result = super.hashCode();
        result = 31 * result + Objects.hashCode(type);
        result = 31 * result + Objects.hashCode(zenCodeName);
        result = 31 * result + Objects.hashCode(members);
        return result;
    }
    
}
