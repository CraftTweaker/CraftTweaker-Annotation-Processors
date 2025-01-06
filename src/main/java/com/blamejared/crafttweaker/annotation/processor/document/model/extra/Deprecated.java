package com.blamejared.crafttweaker.annotation.processor.document.model.extra;

import com.blamejared.crafttweaker.annotation.processor.document.model.comment.Comment;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class Deprecated {
    
    public static final Codec<Deprecated> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Comment.CODEC.optionalFieldOf("reason").forGetter(Deprecated::reason),
            Comment.CODEC.optionalFieldOf("since").forGetter(Deprecated::since),
            Codec.BOOL.fieldOf("for_removal").forGetter(Deprecated::forRemoval)
    ).apply(instance, Deprecated::new));
    
    private final Optional<Comment> reason;
    private final Optional<Comment> since;
    private final boolean forRemoval;
    
    public Deprecated(Optional<Comment> reason, Optional<Comment> since, boolean forRemoval) {
        
        this.reason = reason;
        this.since = since;
        this.forRemoval = forRemoval;
    }
    
    public Optional<Comment> reason() {
        
        return reason;
    }
    
    public Optional<Comment> since() {
        
        return since;
    }
    
    public boolean forRemoval() {
        
        return forRemoval;
    }
    
    
    public Deprecated merge(Deprecated other) {
        
        return new Deprecated(
                reason.isPresent() ? reason : other.reason,
                since.isPresent() ? since : other.since,
                forRemoval || other.forRemoval
        );
    }
    
    @Override
    public String toString() {
        
        final StringBuilder sb = new StringBuilder("Deprecated{");
        sb.append("reason=").append(reason);
        sb.append(", since=").append(since);
        sb.append(", forRemoval=").append(forRemoval);
        sb.append('}');
        return sb.toString();
    }
    
}
