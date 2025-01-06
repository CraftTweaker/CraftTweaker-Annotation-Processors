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
public class EventPage extends TypePage {
    
    public static final Codec<EventPage> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            PageVersion.CODEC.fieldOf("version").forGetter(Page::version),
            Codec.STRING.fieldOf("key").forGetter(Page::key),
            Codec.STRING.fieldOf("display_name").forGetter(Page::displayName),
            Comment.CODEC.optionalFieldOf("comment").forGetter(Page::comment),
            Extra.CODEC.fieldOf("extra").forGetter(Page::extra),
            Type.CODEC.fieldOf("type").forGetter(EventPage::type),
            Codec.STRING.fieldOf("zen_code_name").forGetter(EventPage::zenCodeName),
            Codec.unboundedMap(Codec.STRING, MemberGroup.CODEC).fieldOf("members").forGetter(EventPage::members),
            Comment.CODEC.optionalFieldOf("canceled_info").forGetter(EventPage::canceledInfo),
            Comment.CODEC.optionalFieldOf("not_canceled_info").forGetter(EventPage::notCanceledInfo),
            Comment.CODEC.optionalFieldOf("allow_info").forGetter(EventPage::allowInfo),
            Comment.CODEC.optionalFieldOf("default_info").forGetter(EventPage::defaultInfo),
            Comment.CODEC.optionalFieldOf("deny_info").forGetter(EventPage::denyInfo),
            Codec.BOOL.fieldOf("has_result").forGetter(EventPage::hasResult),
            Codec.BOOL.fieldOf("cancelable").forGetter(EventPage::cancelable)
    ).apply(instance, EventPage::new));
    
    private final Optional<Comment> canceledInfo;
    private final Optional<Comment> notCanceledInfo;
    private final Optional<Comment> allowInfo;
    private final Optional<Comment> defaultInfo;
    private final Optional<Comment> denyInfo;
    
    private final boolean hasResult;
    private final boolean cancelable;
    
    public EventPage(PageVersion version, String key, String displayName, Optional<Comment> comment, Extra extra, Type type, String zenCodeName, Map<String, MemberGroup> members, Optional<Comment> canceledInfo, Optional<Comment> notCanceledInfo, Optional<Comment> allowInfo, Optional<Comment> defaultInfo, Optional<Comment> denyInfo, boolean hasResult, boolean cancelable) {
        
        super(version, key, displayName, PageKind.EVENT, comment, extra, type, zenCodeName, members);
        this.canceledInfo = canceledInfo;
        this.notCanceledInfo = notCanceledInfo;
        this.allowInfo = allowInfo;
        this.defaultInfo = defaultInfo;
        this.denyInfo = denyInfo;
        this.hasResult = hasResult;
        this.cancelable = cancelable;
    }
    
    
    public Optional<Comment> canceledInfo() {
        
        return canceledInfo;
    }
    
    public Optional<Comment> notCanceledInfo() {
        
        return notCanceledInfo;
    }
    
    public Optional<Comment> allowInfo() {
        
        return allowInfo;
    }
    
    public Optional<Comment> defaultInfo() {
        
        return defaultInfo;
    }
    
    public Optional<Comment> denyInfo() {
        
        return denyInfo;
    }
    
    public boolean hasResult() {
        
        return hasResult;
    }
    
    public boolean cancelable() {
        
        return cancelable;
    }
    
    @Override
    public String toString() {
        
        final StringBuilder sb = new StringBuilder("EventPage{");
        sb.append("canceledInfo=").append(canceledInfo);
        sb.append(", notCanceledInfo=").append(notCanceledInfo);
        sb.append(", allowInfo=").append(allowInfo);
        sb.append(", defaultInfo=").append(defaultInfo);
        sb.append(", denyInfo=").append(denyInfo);
        sb.append(", hasResult=").append(hasResult);
        sb.append(", cancelable=").append(cancelable);
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
        
        EventPage eventPage = (EventPage) o;
        return hasResult == eventPage.hasResult && cancelable == eventPage.cancelable && Objects.equals(canceledInfo, eventPage.canceledInfo) && Objects.equals(notCanceledInfo, eventPage.notCanceledInfo) && Objects.equals(allowInfo, eventPage.allowInfo) && Objects.equals(defaultInfo, eventPage.defaultInfo) && Objects.equals(denyInfo, eventPage.denyInfo);
    }
    
    @Override
    public int hashCode() {
        
        int result = super.hashCode();
        result = 31 * result + Objects.hashCode(canceledInfo);
        result = 31 * result + Objects.hashCode(notCanceledInfo);
        result = 31 * result + Objects.hashCode(allowInfo);
        result = 31 * result + Objects.hashCode(defaultInfo);
        result = 31 * result + Objects.hashCode(denyInfo);
        result = 31 * result + Boolean.hashCode(hasResult);
        result = 31 * result + Boolean.hashCode(cancelable);
        return result;
    }
    
}
