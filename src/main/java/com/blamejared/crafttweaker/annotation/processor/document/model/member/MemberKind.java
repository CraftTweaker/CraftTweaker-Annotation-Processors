package com.blamejared.crafttweaker.annotation.processor.document.model.member;

import com.blamejared.crafttweaker.annotation.processor.util.Keyable;
import com.blamejared.crafttweaker.annotation.processor.util.Util;
import com.mojang.serialization.Codec;

import java.util.function.Supplier;

public enum MemberKind implements Keyable {
    METHOD("method", () -> MethodMember.CODEC),
    GETTER("getter", () -> GetterMember.CODEC),
    SETTER("setter", () -> SetterMember.CODEC),
    FIELD("field", () -> FieldMember.CODEC),
    ENUM_CONSTANT("enum_constant", () -> EnumConstantMember.CODEC),
    CASTER("caster", () -> CasterMember.CODEC),
    OPERATOR("operator", () -> OperatorMember.CODEC),
    CONSTRUCTOR("constructor", () -> ConstructorMember.CODEC);
    
    public static final Codec<MemberKind> CODEC = Codec.STRING.xmap(Util.enumLookup(MemberKind.values()), MemberKind::key);
    
    private final String key;
    private final Supplier<Codec<? extends Member>> codec;
    
    MemberKind(String key, Supplier<Codec<? extends Member>> codec) {
        
        this.key = key;
        this.codec = codec;
    }
    
    public String key() {
        
        return key;
    }
    
    public Codec<? extends Member> codec() {
        
        return codec.get();
    }
    
    @Override
    public String toString() {
        
        return "MemberKind{" +
                "key='" + key + '\'' +
                '}';
    }
}
