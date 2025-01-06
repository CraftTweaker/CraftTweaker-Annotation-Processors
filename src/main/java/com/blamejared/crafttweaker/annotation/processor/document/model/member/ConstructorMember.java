package com.blamejared.crafttweaker.annotation.processor.document.model.member;

import com.blamejared.crafttweaker.annotation.processor.document.model.comment.Comment;
import com.blamejared.crafttweaker.annotation.processor.document.model.extra.Extra;
import com.blamejared.crafttweaker.annotation.processor.document.model.type.Type;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class ConstructorMember extends Member {
    
    public static final Codec<ConstructorMember> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    MemberOrigin.CODEC.fieldOf("origin").forGetter(Member::origin),
                    Comment.CODEC.optionalFieldOf("comment").forGetter(Member::comment),
                    Extra.CODEC.fieldOf("extra").forGetter(Member::extra),
                    Parameter.CODEC.listOf().fieldOf("parameters").forGetter(ConstructorMember::parameters),
                    Codec.unboundedMap(Codec.STRING, Type.CODEC)
                            .fieldOf("type_parameters")
                            .forGetter(ConstructorMember::typeParameters)
            )
            .apply(instance, ConstructorMember::new));
    
    private final List<Parameter> parameters;
    private final Map<String, Type> typeParameters;
    
    public ConstructorMember(MemberOrigin origin, Optional<Comment> comment, Extra extra, List<Parameter> parameters, Map<String, Type> typeParameters) {
        
        super("new", "new", MemberKind.CONSTRUCTOR, origin, false, comment, extra);
        this.parameters = parameters;
        this.typeParameters = typeParameters;
    }
    
    public List<Parameter> parameters() {
        
        return parameters;
    }
    
    public Map<String, Type> typeParameters() {
        
        return typeParameters;
    }
    
    @Override
    public String toString() {
        
        final StringBuilder sb = new StringBuilder("ConstructorMember{");
        sb.append("parameters=").append(parameters);
        sb.append(", typeParameters=").append(typeParameters);
        sb.append('}');
        return sb.toString();
    }
    
}
