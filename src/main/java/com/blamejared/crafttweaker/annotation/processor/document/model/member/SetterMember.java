package com.blamejared.crafttweaker.annotation.processor.document.model.member;

import com.blamejared.crafttweaker.annotation.processor.document.model.comment.Comment;
import com.blamejared.crafttweaker.annotation.processor.document.model.extra.Extra;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class SetterMember extends Member {
    
    public static final Codec<SetterMember> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    Codec.STRING.fieldOf("key").forGetter(Member::key),
                    Codec.STRING.fieldOf("display_name").forGetter(Member::displayName),
                    MemberOrigin.CODEC.fieldOf("origin").forGetter(Member::origin),
                    Codec.BOOL.fieldOf("static").forGetter(Member::isStatic),
                    Comment.CODEC.optionalFieldOf("comment").forGetter(Member::comment),
                    Extra.CODEC.fieldOf("extra").forGetter(Member::extra),
                    Parameter.CODEC.listOf().fieldOf("parameters").forGetter(SetterMember::parameters)
            )
            .apply(instance, SetterMember::new));
    
    private final List<Parameter> parameters;
    
    public SetterMember(String key, String displayName, MemberOrigin origin, boolean isStatic, Optional<Comment> comment, Extra extra, List<Parameter> parameters) {
        
        super(key, displayName, MemberKind.SETTER, origin, isStatic, comment, extra);
        this.parameters = parameters;
    }
    
    public List<Parameter> parameters() {
        
        return parameters;
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
        
        SetterMember that = (SetterMember) o;
        return parameters.equals(that.parameters);
    }
    
    @Override
    public int hashCode() {
        
        int result = super.hashCode();
        result = 31 * result + parameters.hashCode();
        return result;
    }
    
    @Override
    public String toString() {
        
        final StringBuilder sb = new StringBuilder("SetterMember{");
        sb.append("parameters=").append(parameters);
        sb.append('}');
        return sb.toString();
    }
    
}
