package com.blamejared.crafttweaker.annotation.processor.document.page.member.static_member;

import com.blamejared.crafttweaker.annotation.processor.document.file.PageOutputWriter;
import com.blamejared.crafttweaker.annotation.processor.document.meta.DocumentMeta;
import com.blamejared.crafttweaker.annotation.processor.document.meta.IFillMeta;
import com.blamejared.crafttweaker.annotation.processor.document.page.member.PropertyMember;
import com.blamejared.crafttweaker.annotation.processor.document.page.type.AbstractTypeInfo;

import java.util.Map;
import java.util.TreeMap;

public class DocumentedStaticMembers implements IFillMeta {
    
    protected final Map<String, PropertyMember> properties = new TreeMap<>();
    protected final Map<String, StaticMethodGroup> methodGroups = new TreeMap<>();
    
    public void write(PageOutputWriter writer) {
        
        writeStaticMethods(writer);
        writeProperties(writer);
    }
    
    
    private void writeStaticMethods(PageOutputWriter writer) {
        
        if(methodGroups.isEmpty()) {
            return;
        }
        
        writer.printf("## Static Methods%n%n");
        
        for(StaticMethodGroup value : methodGroups.values()) {
            value.writeStaticMethods(writer);
        }
    }
    
    protected void writeProperties(PageOutputWriter writer) {
        
        if(properties.isEmpty()) {
            return;
        }
        
        // TODO("Support deprecation and since properly, along with better documentation format")
        writer.printf("## Static Properties%n%n");
        writer.newTable().key("Name").key("Type").key("Has Getter").key("Has Setter").key("Description");
        for(PropertyMember value : properties.values()) {
            value.writeTableRow(writer);
        }
        writer.println(writer.currentTable().write());
    }
    
    public void addProperty(PropertyMember propertyMember) {
        
        properties.put(propertyMember.getName(), propertyMember);
    }
    
    public void addMethod(StaticMethodMember staticMethodMember, AbstractTypeInfo ownerType) {
        
        final StaticMethodGroup group = methodGroups.computeIfAbsent(staticMethodMember.getName(), name -> new StaticMethodGroup(name, ownerType));
        group.addMethod(staticMethodMember);
    }
    
    @Override
    public void fillMeta(DocumentMeta meta) {
        
        
        for(PropertyMember value : properties.values()) {
            value.fillMeta(meta);
        }
        
        for(StaticMethodGroup value : methodGroups.values()) {
            value.fillMeta(meta);
        }
        
    }
    
}
