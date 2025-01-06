package com.blamejared.crafttweaker.annotation.processor.document.model.extra;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;
import java.util.function.BiFunction;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class Extra {
    
    //TODO Support @TaggableElement
    
    public static final Extra EMPTY = new Extra(
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty()
    );
    
    public static final Codec<Extra> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BracketEnum.CODEC.optionalFieldOf("bracket_enum").forGetter(Extra::bracketEnum),
            Deprecated.CODEC.optionalFieldOf("deprecated").forGetter(Extra::deprecated),
            EventParent.CODEC.optionalFieldOf("event_parent").forGetter(Extra::eventParent),
            Examples.CODEC.optionalFieldOf("examples").forGetter(Extra::examples),
            Loaders.CODEC.optionalFieldOf("loaders").forGetter(Extra::loaders),
            Obtention.CODEC.optionalFieldOf("obtention").forGetter(Extra::obtention),
            ParameterComments.CODEC.optionalFieldOf("parameter_comment").forGetter(Extra::parameterComment),
            RequiredMods.CODEC.optionalFieldOf("required_mods").forGetter(Extra::requiredMods),
            Returns.CODEC.optionalFieldOf("returns").forGetter(Extra::returns),
            See.CODEC.optionalFieldOf("see").forGetter(Extra::see),
            Since.CODEC.optionalFieldOf("since").forGetter(Extra::since)
    ).apply(instance, Extra::new));
    
    private final Optional<BracketEnum> bracketEnum;
    private final Optional<Deprecated> deprecated;
    private final Optional<EventParent> eventParent;
    private final Optional<Examples> examples;
    private final Optional<Loaders> loaders;
    private final Optional<Obtention> obtention;
    private final Optional<ParameterComments> parameterComment;
    private final Optional<RequiredMods> requiredMods;
    private final Optional<Returns> returns;
    private final Optional<See> see;
    private final Optional<Since> since;
    
    public Extra(Optional<BracketEnum> bracketEnum, Optional<Deprecated> deprecated, Optional<EventParent> eventParent, Optional<Examples> examples, Optional<Loaders> loaders, Optional<Obtention> obtention, Optional<ParameterComments> parameterComment, Optional<RequiredMods> requiredMods, Optional<Returns> returns, Optional<See> see, Optional<Since> since) {
        
        this.bracketEnum = bracketEnum;
        this.deprecated = deprecated;
        this.eventParent = eventParent;
        this.examples = examples;
        this.loaders = loaders;
        this.obtention = obtention;
        this.parameterComment = parameterComment;
        this.requiredMods = requiredMods;
        this.returns = returns;
        this.see = see;
        this.since = since;
    }
    
    public Optional<BracketEnum> bracketEnum() {
        
        return bracketEnum;
    }
    
    public Optional<Deprecated> deprecated() {
        
        return deprecated;
    }
    
    public Optional<EventParent> eventParent() {
        
        return eventParent;
    }
    
    public Optional<Examples> examples() {
        
        return examples;
    }
    
    public Optional<Loaders> loaders() {
        
        return loaders;
    }
    
    public Optional<Obtention> obtention() {
        
        return obtention;
    }
    
    public Optional<ParameterComments> parameterComment() {
        
        return parameterComment;
    }
    
    public Optional<RequiredMods> requiredMods() {
        
        return requiredMods;
    }
    
    public Optional<Returns> returns() {
        
        return returns;
    }
    
    public Optional<See> see() {
        
        return see;
    }
    
    public Optional<Since> since() {
        
        return since;
    }
    
    private <T> Optional<T> update(Optional<T> originalT, T newT, BiFunction<T, T, T> updater) {
        
        return originalT.map(t -> updater.apply(t, newT)).or(() -> Optional.of(newT));
    }
    
    public Extra withBracketEnum(BracketEnum bracketEnum) {
        
        return new Extra(Optional.of(bracketEnum), this.deprecated, this.eventParent, this.examples, this.loaders, this.obtention, this.parameterComment, this.requiredMods, this.returns, this.see, this.since);
    }
    
    public Extra withDeprecated(Deprecated deprecated) {
        
        return new Extra(this.bracketEnum, Optional.of(deprecated), this.eventParent, this.examples, this.loaders, this.obtention, this.parameterComment, this.requiredMods, this.returns, this.see, this.since);
    }
    
    public Extra withDeprecated(Deprecated deprecated, BiFunction<Deprecated, Deprecated, Deprecated> updater) {
        
        return new Extra(this.bracketEnum, update(this.deprecated, deprecated, updater), this.eventParent, this.examples, this.loaders, this.obtention, this.parameterComment, this.requiredMods, this.returns, this.see, this.since);
    }
    
    public Extra withEventParent(EventParent eventParent) {
        
        return new Extra(this.bracketEnum, this.deprecated, Optional.of(eventParent), this.examples, this.loaders, this.obtention, this.parameterComment, this.requiredMods, this.returns, this.see, this.since);
    }
    
    public Extra withExamples(Examples examples) {
        
        return new Extra(this.bracketEnum, this.deprecated, this.eventParent, Optional.of(examples), this.loaders, this.obtention, this.parameterComment, this.requiredMods, this.returns, this.see, this.since);
    }
    
    public Extra withLoaders(Loaders loaders) {
        
        return new Extra(this.bracketEnum, this.deprecated, this.eventParent, this.examples, Optional.of(loaders), this.obtention, this.parameterComment, this.requiredMods, this.returns, this.see, this.since);
    }
    
    public Extra withObtention(Obtention obtention) {
        
        return new Extra(this.bracketEnum, this.deprecated, this.eventParent, this.examples, this.loaders, Optional.of(obtention), this.parameterComment, this.requiredMods, this.returns, this.see, this.since);
    }
    
    public Extra withParameterComment(ParameterComments parameterComment) {
        
        return new Extra(this.bracketEnum, this.deprecated, this.eventParent, this.examples, this.loaders, this.obtention, Optional.of(parameterComment), this.requiredMods, this.returns, this.see, this.since);
    }
    
    public Extra withRequiredMods(RequiredMods requiredMods) {
        
        return new Extra(this.bracketEnum, this.deprecated, this.eventParent, this.examples, this.loaders, this.obtention, this.parameterComment, Optional.of(requiredMods), this.returns, this.see, this.since);
    }
    
    public Extra withReturns(Returns returns) {
        
        return new Extra(this.bracketEnum, this.deprecated, this.eventParent, this.examples, this.loaders, this.obtention, this.parameterComment, this.requiredMods, Optional.of(returns), this.see, this.since);
    }
    
    public Extra withSee(See see) {
        
        return new Extra(this.bracketEnum, this.deprecated, this.eventParent, this.examples, this.loaders, this.obtention, this.parameterComment, this.requiredMods, this.returns, Optional.of(see), this.since);
    }
    
    public Extra withSee(See see, BiFunction<See, See, See> updater) {
        
        return new Extra(this.bracketEnum, this.deprecated, this.eventParent, this.examples, this.loaders, this.obtention, this.parameterComment, this.requiredMods, this.returns, update(this.see, see, updater), this.since);
    }
    
    public Extra withSince(Since since) {
        
        return new Extra(this.bracketEnum, this.deprecated, this.eventParent, this.examples, this.loaders, this.obtention, this.parameterComment, this.requiredMods, this.returns, this.see, Optional.of(since));
    }
    
    @Override
    public String toString() {
        
        final StringBuilder sb = new StringBuilder("Extra{");
        sb.append("bracketEnum=").append(bracketEnum);
        sb.append(", deprecated=").append(deprecated);
        sb.append(", eventParent=").append(eventParent);
        sb.append(", examples=").append(examples);
        sb.append(", loaders=").append(loaders);
        sb.append(", obtention=").append(obtention);
        sb.append(", parameterComment=").append(parameterComment);
        sb.append(", requiredMods=").append(requiredMods);
        sb.append(", returns=").append(returns);
        sb.append(", see=").append(see);
        sb.append(", since=").append(since);
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
        
        Extra extra = (Extra) o;
        return bracketEnum.equals(extra.bracketEnum) && deprecated.equals(extra.deprecated) && eventParent.equals(extra.eventParent) && examples.equals(extra.examples) && loaders.equals(extra.loaders) && obtention.equals(extra.obtention) && parameterComment.equals(extra.parameterComment) && requiredMods.equals(extra.requiredMods) && returns.equals(extra.returns) && see.equals(extra.see) && since.equals(extra.since);
    }
    
    @Override
    public int hashCode() {
        
        int result = bracketEnum.hashCode();
        result = 31 * result + deprecated.hashCode();
        result = 31 * result + eventParent.hashCode();
        result = 31 * result + examples.hashCode();
        result = 31 * result + loaders.hashCode();
        result = 31 * result + obtention.hashCode();
        result = 31 * result + parameterComment.hashCode();
        result = 31 * result + requiredMods.hashCode();
        result = 31 * result + returns.hashCode();
        result = 31 * result + see.hashCode();
        result = 31 * result + since.hashCode();
        return result;
    }
    
}
