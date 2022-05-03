package com.blamejared.crafttweaker.annotation.processor.document.page.page;

import com.blamejared.crafttweaker.annotation.processor.document.file.PageOutputWriter;
import com.blamejared.crafttweaker.annotation.processor.document.meta.DocumentMeta;
import com.blamejared.crafttweaker.annotation.processor.document.page.info.DocumentationPageInfo;
import com.blamejared.crafttweaker.annotation.processor.document.page.member.static_member.DocumentedStaticMembers;
import com.blamejared.crafttweaker.annotation.processor.document.page.member.virtual_member.DocumentedVirtualMembers;
import com.blamejared.crafttweaker.annotation.processor.document.page.type.AbstractTypeInfo;

public final class ExpansionPage extends DocumentationPage {
    
    private final AbstractTypeInfo expandedType;
    
    public ExpansionPage(AbstractTypeInfo expandedType, DocumentationPageInfo pageInfo, DocumentedVirtualMembers members, DocumentedStaticMembers staticMembers) {
        
        super(pageInfo, members, staticMembers);
        this.expandedType = expandedType;
    }
    
    @Override
    protected void writeTitle(PageOutputWriter writer) {
        
        writer.printf("# Expansion for %s%n%n", expandedType.getSimpleMarkdown());
    }
    
    @Override
    protected void writeOwnerModId(PageOutputWriter writer) {
        
        writer.printf("This expansion was added by a mod with mod-id `%s`. So you need to have this mod installed if you want to use this feature.%n%n", pageInfo.declaringModId);
    }
    
    @Override
    public void fillMeta(DocumentMeta meta) {
        
        meta.setOwnerModId(pageInfo.declaringModId);
        
    }
    
}
