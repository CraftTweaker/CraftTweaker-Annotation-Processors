package com.blamejared.crafttweaker.annotation.processor.document.page.member.virtual_member;

import com.blamejared.crafttweaker.annotation.processor.document.file.PageOutputWriter;
import com.blamejared.crafttweaker.annotation.processor.document.meta.DocumentMeta;
import com.blamejared.crafttweaker.annotation.processor.document.meta.IFillMeta;
import com.blamejared.crafttweaker.annotation.processor.document.page.member.PropertyMember;
import com.blamejared.crafttweaker.annotation.processor.document.page.type.AbstractTypeInfo;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class DocumentedVirtualMembers implements IFillMeta {
    
    protected final Set<CasterMember> casters = new TreeSet<>();
    protected final Map<String, VirtualMethodGroup> methodGroups = new TreeMap<>();
    protected final Set<OperatorMember> operators = new TreeSet<>();
    protected final Map<String, PropertyMember> properties = new TreeMap<>();
    
    public DocumentedVirtualMembers() {
    
    }
    
    public void addCaster(CasterMember casterMember) {
        
        casters.add(casterMember);
    }
    
    public void addMethod(VirtualMethodMember methodMember, AbstractTypeInfo ownerType) {
        
        final VirtualMethodGroup group = methodGroups.computeIfAbsent(methodMember.getName(), name -> new VirtualMethodGroup(name, ownerType));
        group.addMethod(methodMember);
    }
    
    public void addOperator(OperatorMember operatorMember) {
        
        operators.add(operatorMember);
    }
    
    public void addProperty(PropertyMember propertyMember) {
        
        properties.merge(propertyMember.getName(), propertyMember, PropertyMember::merge);
    }
    
    public void write(PageOutputWriter writer) {
        
        writeCasters(writer);
        writeMethods(writer);
        writeOperators(writer);
        writeProperties(writer);
    }
    
    @Override
    public void fillMeta(DocumentMeta meta) {
        
        for(PropertyMember value : properties.values()) {
            value.fillMeta(meta);
        }
        
        for(VirtualMethodGroup value : methodGroups.values()) {
            value.fillMeta(meta);
        }
        
    }
    
    protected void writeCasters(PageOutputWriter writer) {
        
        if(casters.isEmpty()) {
            return;
        }
        
        // TODO("Support deprecation and since properly, along with better documentation format")
        writer.printf("## Casters%n%n");
        writer.newTable().key("Result Type").key("Is Implicit");
        for(CasterMember caster : casters) {
            caster.writeTableRow(writer);
        }
        writer.println(writer.currentTable().write());
    }
    
    protected void writeMethods(PageOutputWriter writer) {
        
        if(methodGroups.isEmpty()) {
            return;
        }
        writer.printf("## Methods%n%n");
        
        for(VirtualMethodGroup value : methodGroups.values()) {
            value.writeVirtualMethods(writer);
        }
        writer.println();
    }
    
    protected void writeOperators(PageOutputWriter writer) {
        
        if(operators.isEmpty()) {
            return;
        }
        
        writer.printf("## Operators%n%n");
        for(OperatorMember operator : operators) {
            operator.write(writer);
        }
        writer.println();
    }
    
    protected void writeProperties(PageOutputWriter writer) {
        
        if(properties.isEmpty()) {
            return;
        }
        
        // TODO("Support deprecation and since properly, along with better documentation format")
        writer.printf("## Properties%n%n");
        writer.newTable().key("Name").key("Type").key("Has Getter").key("Has Setter").key("Description");
        for(PropertyMember value : properties.values()) {
            value.writeTableRow(writer);
        }
        writer.println(writer.currentTable().write());
    }
    
    
}
