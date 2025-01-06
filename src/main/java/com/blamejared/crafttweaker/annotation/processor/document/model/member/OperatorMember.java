package com.blamejared.crafttweaker.annotation.processor.document.model.member;

import com.blamejared.crafttweaker.annotation.processor.document.model.comment.Comment;
import com.blamejared.crafttweaker.annotation.processor.document.model.extra.Extra;
import com.blamejared.crafttweaker.annotation.processor.document.model.type.Type;
import com.blamejared.crafttweaker.annotation.processor.util.Util;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import org.openzen.zencode.java.ZenCodeType;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class OperatorMember extends Member {
    
    public static final Codec<OperatorMember> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    Codec.STRING.fieldOf("key").forGetter(Member::key),
                    Codec.STRING.fieldOf("display_name").forGetter(Member::displayName),
                    MemberOrigin.CODEC.fieldOf("origin").forGetter(Member::origin),
                    Codec.BOOL.fieldOf("static").forGetter(Member::isStatic),
                    Comment.CODEC.optionalFieldOf("comment").forGetter(Member::comment),
                    Extra.CODEC.fieldOf("extra").forGetter(Member::extra),
                    Parameter.CODEC.listOf().fieldOf("parameters").forGetter(OperatorMember::parameters),
                    Type.CODEC.fieldOf("return_type").forGetter(OperatorMember::returnType),
                    Codec.unboundedMap(Codec.STRING, Type.CODEC)
                            .fieldOf("type_parameters")
                            .forGetter(OperatorMember::typeParameters),
                    Codec.STRING.xmap(s -> ZenCodeType.OperatorType.valueOf(s.toUpperCase()), operatorType -> operatorType.name().toLowerCase()).fieldOf("operator").forGetter(OperatorMember::operator)
            )
            .apply(instance, OperatorMember::new));
    
    private final List<Parameter> parameters;
    private final Type returnType;
    private final Map<String, Type> typeParameters;
    private final ZenCodeType.OperatorType operator;
    
    public OperatorMember(String key, String displayName, MemberOrigin origin, boolean isStatic, Optional<Comment> comment, Extra extra, List<Parameter> parameters, Type returnType, Map<String, Type> typeParameters, ZenCodeType.OperatorType operator) {
        
        super(key, displayName, MemberKind.OPERATOR, origin, isStatic, comment, extra);
        this.parameters = parameters;
        this.returnType = returnType;
        this.typeParameters = typeParameters;
        this.operator = operator;
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
    
    public ZenCodeType.OperatorType operator() {
        
        return operator;
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
        
        OperatorMember that = (OperatorMember) o;
        return parameters.equals(that.parameters) && returnType.equals(that.returnType) && typeParameters.equals(that.typeParameters) && operator == that.operator;
    }
    
    @Override
    public int hashCode() {
        
        int result = super.hashCode();
        result = 31 * result + parameters.hashCode();
        result = 31 * result + returnType.hashCode();
        result = 31 * result + typeParameters.hashCode();
        result = 31 * result + operator.hashCode();
        return result;
    }
    
    @Override
    public String toString() {
        
        final StringBuilder sb = new StringBuilder("OperatorMember{");
        sb.append("parameters=").append(parameters);
        sb.append(", returnType=").append(returnType);
        sb.append(", typeParameters=").append(typeParameters);
        sb.append(", operator=").append(operator);
        sb.append('}');
        return sb.toString();
    }
    
}
