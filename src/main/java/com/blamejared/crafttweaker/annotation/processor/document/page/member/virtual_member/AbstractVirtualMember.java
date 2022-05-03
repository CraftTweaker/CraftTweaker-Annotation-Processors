package com.blamejared.crafttweaker.annotation.processor.document.page.member.virtual_member;

import com.blamejared.crafttweaker.annotation.processor.document.page.comment.DocumentationComment;
import com.blamejared.crafttweaker.annotation.processor.document.page.member.header.MemberHeader;

public abstract class AbstractVirtualMember {
    
    public final MemberHeader header;
    private final DocumentationComment comment;
    
    protected AbstractVirtualMember(MemberHeader header, DocumentationComment comment) {
        
        this.header = header;
        this.comment = comment;
    }
    
    
    public DocumentationComment getComment() {
        
        return comment;
    }
    
}
