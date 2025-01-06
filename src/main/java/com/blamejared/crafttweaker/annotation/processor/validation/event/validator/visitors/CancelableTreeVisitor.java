package com.blamejared.crafttweaker.annotation.processor.validation.event.validator.visitors;

import com.sun.source.tree.IdentifierTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.tree.MethodInvocationTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.SimpleTreeVisitor;

public class CancelableTreeVisitor extends SimpleTreeVisitor<Boolean, Void> {
    
    public static final CancelableTreeVisitor INSTANCE = new CancelableTreeVisitor();
    
    private CancelableTreeVisitor() {
    
    }
    
    public boolean visit(Tree tree) {
        
        return tree.accept(this, null);
    }
    
    @Override
    public Boolean visitVariable(VariableTree node, Void object) {
        
        return node.getInitializer().accept(this, object);
    }
    
    @Override
    public Boolean visitMethodInvocation(MethodInvocationTree node, Void object) {
        
        return node.getMethodSelect().accept(this, object);
    }
    
    @Override
    public Boolean visitMemberSelect(MemberSelectTree node, Void object) {
        
        return node.getExpression().accept(this, object) && node.getIdentifier().contentEquals("cancelable");
    }
    
    @Override
    public Boolean visitIdentifier(IdentifierTree node, Void unused) {
        
        return node.getName().contentEquals("IEventBus");
    }
    
    
}
