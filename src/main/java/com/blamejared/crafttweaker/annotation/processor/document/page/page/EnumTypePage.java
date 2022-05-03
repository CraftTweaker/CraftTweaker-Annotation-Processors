package com.blamejared.crafttweaker.annotation.processor.document.page.page;


import com.blamejared.crafttweaker.annotation.processor.document.file.PageOutputWriter;
import com.blamejared.crafttweaker.annotation.processor.document.meta.DocumentMeta;
import com.blamejared.crafttweaker.annotation.processor.document.page.info.TypePageInfo;
import com.blamejared.crafttweaker.annotation.processor.document.page.member.enum_constant.DocumentedEnumConstants;
import com.blamejared.crafttweaker.annotation.processor.document.page.member.header.DocumentedGenericParameter;
import com.blamejared.crafttweaker.annotation.processor.document.page.member.static_member.DocumentedStaticMembers;
import com.blamejared.crafttweaker.annotation.processor.document.page.member.virtual_member.DocumentedVirtualMembers;
import com.blamejared.crafttweaker.annotation.processor.document.page.type.AbstractTypeInfo;

import javax.annotation.Nullable;
import java.util.List;

public class EnumTypePage extends TypePage {
    
    private final DocumentedEnumConstants enumConstants;
    
    public EnumTypePage(TypePageInfo typePageInfo, DocumentedVirtualMembers members, @Nullable AbstractTypeInfo superType, List<AbstractTypeInfo> implementedTypes, DocumentedStaticMembers staticMembers, List<DocumentedGenericParameter> genericParameters, DocumentedEnumConstants enumConstants) {
        
        super(typePageInfo, members, superType, implementedTypes, staticMembers, genericParameters);
        this.enumConstants = enumConstants;
    }
    
    @Override
    protected void beforeWritingMembers(PageOutputWriter writer) {
        
        super.beforeWritingMembers(writer);
        enumConstants.write(writer);
    }
    
    @Override
    public void fillMeta(DocumentMeta meta) {
        
        super.fillMeta(meta);
        enumConstants.fillMeta(meta);
    }
    
}