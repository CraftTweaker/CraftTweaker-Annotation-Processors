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
public class MethodMember extends Member {
    
    public static final Codec<MethodMember> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    Codec.STRING.fieldOf("key").forGetter(Member::key),
                    Codec.STRING.fieldOf("display_name").forGetter(Member::displayName),
                    MemberOrigin.CODEC.fieldOf("origin").forGetter(Member::origin),
                    Codec.BOOL.fieldOf("static").forGetter(Member::isStatic),
                    Comment.CODEC.optionalFieldOf("comment").forGetter(Member::comment),
                    Extra.CODEC.fieldOf("extra").forGetter(Member::extra),
                    Parameter.CODEC.listOf().fieldOf("parameters").forGetter(MethodMember::parameters),
                    Type.CODEC.fieldOf("return_type").forGetter(MethodMember::returnType),
                    Codec.unboundedMap(Codec.STRING, Type.CODEC)
                            .fieldOf("type_parameters")
                            .forGetter(MethodMember::typeParameters)
            )
            .apply(instance, MethodMember::new));
    
    private final List<Parameter> parameters;
    private final Type returnType;
    private final Map<String, Type> typeParameters;
    
    public MethodMember(String key, String displayName, MemberOrigin origin, boolean isStatic, Optional<Comment> comment, Extra extra, List<Parameter> parameters, Type returnType, Map<String, Type> typeParameters) {
        
        super(key, displayName, MemberKind.METHOD, origin, isStatic, comment, extra);
        this.parameters = parameters;
        this.returnType = returnType;
        this.typeParameters = typeParameters;
    }
    
    public List<Parameter> parameters() {
        
        return parameters;
    }
    
    public Type returnType() {
        
        return returnType;
    }
    
    public Map<String, Type> typeParameters() {
        
        return typeParameters;
    }
    
    @Override
    public String toString() {
        
        final StringBuilder sb = new StringBuilder("MethodMember{");
        sb.append("parameters=").append(parameters);
        sb.append(", returnType=").append(returnType);
        sb.append(", typeParameters=").append(typeParameters);
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
        
        MethodMember that = (MethodMember) o;
        return parameters.equals(that.parameters) && returnType.equals(that.returnType) && typeParameters.equals(that.typeParameters);
    }
    
    @Override
    public int hashCode() {
        
        int result = super.hashCode();
        result = 31 * result + parameters.hashCode();
        result = 31 * result + returnType.hashCode();
        result = 31 * result + typeParameters.hashCode();
        return result;
    }
    
}
