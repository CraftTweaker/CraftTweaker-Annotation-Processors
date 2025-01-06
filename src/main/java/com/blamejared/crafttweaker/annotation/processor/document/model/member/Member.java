package com.blamejared.crafttweaker.annotation.processor.document.model.member;

import com.blamejared.crafttweaker.annotation.processor.document.model.comment.Comment;
import com.blamejared.crafttweaker.annotation.processor.document.model.extra.Extra;
import com.mojang.serialization.Codec;

import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class Member {
    
    public static final Codec<Member> CODEC = MemberKind.CODEC.dispatch("kind", Member::kind, MemberKind::codec);
    
    private final String key;
    private final String displayName;
    private final MemberKind kind;
    private final MemberOrigin origin;
    private final boolean isStatic;
    private final Optional<Comment> comment;
    private final Extra extra;
    
    public Member(String key, String displayName, MemberKind kind, MemberOrigin origin, boolean isStatic, Optional<Comment> comment, Extra extra) {
        
        this.key = key;
        this.displayName = displayName;
        this.kind = kind;
        this.origin = origin;
        this.isStatic = isStatic;
        this.comment = comment;
        this.extra = extra;
    }
    
    public String key() {
        
        return key;
    }
    
    public String displayName() {
        
        return displayName;
    }
    
    public MemberKind kind() {
        
        return kind;
    }
    
    public MemberOrigin origin() {
        
        return origin;
    }
    
    public boolean isStatic() {
        
        return isStatic;
    }
    
    public Optional<Comment> comment() {
        
        return comment;
    }
    
    public Extra extra() {
        
        return extra;
    }
    
    @Override
    public String toString() {
        
        final StringBuilder sb = new StringBuilder("Member{");
        sb.append("key='").append(key).append('\'');
        sb.append(", displayName='").append(displayName).append('\'');
        sb.append(", memberType=").append(kind);
        sb.append(", origin=").append(origin);
        sb.append(", isStatic=").append(isStatic);
        sb.append(", comment=").append(comment);
        sb.append(", extra=").append(extra);
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
        
        Member member = (Member) o;
        return isStatic() == member.isStatic() && key.equals(member.key) && displayName.equals(member.displayName) && kind == member.kind && origin == member.origin && comment.equals(member.comment) && extra.equals(member.extra);
    }
    
    @Override
    public int hashCode() {
        
        int result = key.hashCode();
        result = 31 * result + displayName.hashCode();
        result = 31 * result + kind.hashCode();
        result = 31 * result + origin.hashCode();
        result = 31 * result + Boolean.hashCode(isStatic());
        result = 31 * result + comment.hashCode();
        result = 31 * result + extra.hashCode();
        return result;
    }
    
}
