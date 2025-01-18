package com.blamejared.crafttweaker.annotation.processor.document.model.page;

import com.blamejared.crafttweaker.annotation.processor.document.model.comment.Comment;
import com.blamejared.crafttweaker.annotation.processor.document.model.extra.Extra;
import com.blamejared.crafttweaker.annotation.processor.document.model.member.MemberGroup;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Map;
import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class ExpansionPage extends Page {
    
    public static final Codec<ExpansionPage> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            PageVersion.CODEC.fieldOf("version").forGetter(Page::version),
            Codec.STRING.fieldOf("key").forGetter(Page::key),
            Codec.STRING.fieldOf("display_name").forGetter(Page::displayName),
            Comment.CODEC.optionalFieldOf("comment").forGetter(Page::comment),
            Extra.CODEC.fieldOf("extra").forGetter(Page::extra),
            Codec.STRING.optionalFieldOf("zen_code_name").forGetter(ExpansionPage::zenCodeName),
            Codec.unboundedMap(Codec.STRING, MemberGroup.CODEC).fieldOf("members").forGetter(ExpansionPage::members)
    ).apply(instance, ExpansionPage::new));
    
    protected final Optional<String> zenCodeName;
    protected final Map<String, MemberGroup> members;
    
    protected ExpansionPage(PageVersion version, String key, String displayName, PageKind kind, Optional<Comment> comment, Extra extra, Optional<String> zenCodeName, Map<String, MemberGroup> members) {
        
        super(version, key, displayName, kind, comment, extra);
        this.zenCodeName = zenCodeName;
        this.members = members;
    }
    
    public ExpansionPage(PageVersion version, String key, String displayName, Optional<Comment> comment, Extra extra, Optional<String> zenCodeName, Map<String, MemberGroup> members) {
        
        this(version, key, displayName, PageKind.EXPANSION, comment, extra, zenCodeName, members);
    }
    
    public Optional<String> zenCodeName() {
        
        return zenCodeName;
    }
    
    public Map<String, MemberGroup> members() {
        
        return members;
    }
    
    @Override
    public String toString() {
        
        final StringBuilder sb = new StringBuilder("ExpandedPage{");
        sb.append("zenCodeName=").append(zenCodeName);
        sb.append(", members=").append(members);
        sb.append('}');
        return sb.toString();
    }
    
    @Override
    public boolean equals(Object o) {
        
        if(o == null || getClass() != o.getClass()) {
            return false;
        }
        if(!super.equals(o)) {
            return false;
        }
        
        ExpansionPage that = (ExpansionPage) o;
        return zenCodeName.equals(that.zenCodeName) && members.equals(that.members);
    }
    
    @Override
    public int hashCode() {
        
        int result = super.hashCode();
        result = 31 * result + zenCodeName.hashCode();
        result = 31 * result + members.hashCode();
        return result;
    }
    
}
