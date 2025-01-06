package com.blamejared.crafttweaker.annotation.processor.document.model.member;

import com.blamejared.crafttweaker.annotation.processor.document.model.comment.Comment;
import com.blamejared.crafttweaker.annotation.processor.document.model.extra.Extra;
import com.blamejared.crafttweaker.annotation.processor.document.model.type.Type;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class CasterMember extends Member {
    
    public static final Codec<CasterMember> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    Codec.STRING.fieldOf("key").forGetter(Member::key),
                    Codec.STRING.fieldOf("display_name").forGetter(Member::displayName),
                    MemberOrigin.CODEC.fieldOf("origin").forGetter(Member::origin),
                    Codec.BOOL.fieldOf("static").forGetter(Member::isStatic),
                    Comment.CODEC.optionalFieldOf("comment").forGetter(Member::comment),
                    Extra.CODEC.fieldOf("extra").forGetter(Member::extra),
                    Type.CODEC.fieldOf("to").forGetter(CasterMember::to),
                    Codec.BOOL.fieldOf("implicit").forGetter(CasterMember::implicit)
            )
            .apply(instance, CasterMember::new));
    
    private final Type to;
    private final boolean implicit;
    
    public CasterMember(String key, String displayName, MemberOrigin origin, boolean isStatic, Optional<Comment> comment, Extra extra, Type type, boolean implicit) {
        
        super(key, displayName, MemberKind.CASTER, origin, isStatic, comment, extra);
        to = type;
        this.implicit = implicit;
    }
    
    public Type to() {
        
        return to;
    }
    
    public boolean implicit() {
        
        return implicit;
    }
    
}
