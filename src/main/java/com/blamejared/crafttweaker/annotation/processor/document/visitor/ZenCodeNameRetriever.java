package com.blamejared.crafttweaker.annotation.processor.document.visitor;

import com.blamejared.crafttweaker_annotations.annotations.NativeTypeRegistrationWrapper;
import org.openzen.zencode.java.NameWrapper;

import javax.lang.model.element.TypeElement;
import javax.lang.model.util.SimpleElementVisitor8;
import java.util.Optional;

public class ZenCodeNameRetriever extends SimpleElementVisitor8<Optional<String>, Void> {
    
    public static final ZenCodeNameRetriever INSTANCE = new ZenCodeNameRetriever();
    
    public ZenCodeNameRetriever() {
        
        super(Optional.empty());
    }
    
    @Override
    public Optional<String> visitType(TypeElement e, Void unused) {
        
        if(NameWrapper.isAnnotated(e)) {
            return Optional.ofNullable(NameWrapper.wrap(e)).map(NameWrapper::value);
        }
        if(NativeTypeRegistrationWrapper.isAnnotated(e)) {
            return Optional.ofNullable(NativeTypeRegistrationWrapper.wrap(e))
                    .map(NativeTypeRegistrationWrapper::zenCodeName);
        }
        return super.visitType(e, unused);
    }
    
}
