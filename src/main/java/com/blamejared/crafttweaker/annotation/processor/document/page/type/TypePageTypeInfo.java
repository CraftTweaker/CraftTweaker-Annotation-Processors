package com.blamejared.crafttweaker.annotation.processor.document.page.type;


import com.blamejared.crafttweaker.annotation.processor.document.page.info.TypeName;
import com.blamejared.crafttweaker.annotation.processor.document.page.info.TypePageInfo;

public class TypePageTypeInfo extends AbstractTypeInfo {
    
    private final TypePageInfo pageInfo;
    
    public TypePageTypeInfo(TypePageInfo pageInfo) {
        
        this.pageInfo = pageInfo;
    }
    
    @Override
    public String getDisplayName() {
        
        return pageInfo.zenCodeName.getSimpleName();
    }
    
    @Override
    public String getClickableMarkdown() {
        
        return String.format("[%s](/%s)", pageInfo.getSimpleName(), pageInfo.getOutputPath());
    }
    
    public TypeName getZenCodeName() {
        
        return pageInfo.zenCodeName;
    }
    
}
