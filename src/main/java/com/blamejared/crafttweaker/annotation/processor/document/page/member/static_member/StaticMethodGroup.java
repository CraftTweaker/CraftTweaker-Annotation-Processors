package com.blamejared.crafttweaker.annotation.processor.document.page.member.static_member;

import com.blamejared.crafttweaker.annotation.processor.document.file.PageOutputWriter;
import com.blamejared.crafttweaker.annotation.processor.document.meta.DocumentMeta;
import com.blamejared.crafttweaker.annotation.processor.document.meta.IFillMeta;
import com.blamejared.crafttweaker.annotation.processor.document.page.type.AbstractTypeInfo;

import java.util.Set;
import java.util.TreeSet;

public class StaticMethodGroup implements IFillMeta {
    
    private final String name;
    private final AbstractTypeInfo ownerType;
    private final Set<StaticMethodMember> staticMethods = new TreeSet<>();
    
    public StaticMethodGroup(String name, AbstractTypeInfo ownerType) {
        
        this.name = name;
        this.ownerType = ownerType;
    }
    
    public void addMethod(StaticMethodMember member) {
        
        this.staticMethods.add(member);
    }
    
    public void writeStaticMethods(PageOutputWriter writer) {
        
        for(StaticMethodMember method : staticMethods) {
            writer.group(name, method.getSince(), () -> method.write(writer, ownerType));
        }
    }
    
    @Override
    public void fillMeta(DocumentMeta meta) {
        
        for(StaticMethodMember virtualMethod : staticMethods) {
            virtualMethod.fillMeta(meta);
        }
    }
    
}
