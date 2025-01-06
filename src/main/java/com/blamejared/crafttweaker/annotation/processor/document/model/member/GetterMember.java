package com.blamejared.crafttweaker.annotation.processor.document.model.member;

import com.blamejared.crafttweaker.annotation.processor.document.model.comment.Comment;
import com.blamejared.crafttweaker.annotation.processor.document.model.extra.Extra;
import com.blamejared.crafttweaker.annotation.processor.document.model.type.Type;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class GetterMember extends Member {
    
    public static final Codec<GetterMember> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    Codec.STRING.fieldOf("key").forGetter(Member::key),
                    Codec.STRING.fieldOf("display_name").forGetter(Member::displayName),
                    MemberOrigin.CODEC.fieldOf("origin").forGetter(Member::origin),
                    Codec.BOOL.fieldOf("static").forGetter(Member::isStatic),
                    Comment.CODEC.optionalFieldOf("comment").forGetter(Member::comment),
                    Extra.CODEC.fieldOf("extra").forGetter(Member::extra),
                    Type.CODEC.fieldOf("type").forGetter(GetterMember::type)
            )
            .apply(instance, GetterMember::new));
    
    private final Type type;
    
    public GetterMember(String key, String displayName, MemberOrigin origin, boolean isStatic, Optional<Comment> comment, Extra extra, Type type) {
        
        super(key, displayName, MemberKind.GETTER, origin, isStatic, comment, extra);
        this.type = type;
    }
    
    public Type type() {
        
        return type;
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
        
        GetterMember that = (GetterMember) o;
        return type.equals(that.type);
    }
    
    @Override
    public int hashCode() {
        
        int result = super.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }
    
    
    @Override
    public String toString() {
        
        final StringBuilder sb = new StringBuilder("GetterMember{");
        sb.append("type=").append(type);
        sb.append('}');
        return sb.toString();
    }
    
}
