package com.blamejared.crafttweaker.annotation.processor.util.codec;

import com.google.common.base.Suppliers;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;

import java.util.function.Function;
import java.util.function.Supplier;

public class RecursiveCodec<T> implements Codec<T> {
    
    private final Supplier<Codec<T>> wrapped;
    
    public RecursiveCodec(final Function<Codec<T>, Codec<T>> wrapped) {
        
        this.wrapped = Suppliers.memoize(() -> wrapped.apply(this));
    }
    
    @Override
    public <S> DataResult<Pair<T, S>> decode(final DynamicOps<S> ops, final S input) {
        
        return wrapped.get().decode(ops, input);
    }
    
    @Override
    public <S> DataResult<S> encode(final T input, final DynamicOps<S> ops, final S prefix) {
        
        return wrapped.get().encode(input, ops, prefix);
    }
    
}