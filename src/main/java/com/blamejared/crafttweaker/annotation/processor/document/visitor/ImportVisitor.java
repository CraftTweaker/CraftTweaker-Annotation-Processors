package com.blamejared.crafttweaker.annotation.processor.document.visitor;

import com.sun.source.tree.CompilationUnitTree;
import com.sun.source.tree.ImportTree;
import com.sun.source.tree.MemberSelectTree;
import com.sun.source.util.SimpleTreeVisitor;
import io.toolisticon.aptk.common.ToolingProvider;
import io.toolisticon.aptk.tools.ElementUtils;

import javax.lang.model.element.PackageElement;
import java.util.Collection;
import java.util.Map;

public class ImportVisitor extends SimpleTreeVisitor<Map<String, String>, Map<String, String>> {
    
    @Override
    public Map<String, String> visitCompilationUnit(CompilationUnitTree node, Map<String, String> map) {
        
        node.getImports().forEach(importTree -> importTree.accept(this, map));
        return map;
    }
    
    @Override
    public Map<String, String> visitImport(ImportTree node, Map<String, String> map) {
        
        MemberSelectTree member = (MemberSelectTree) node.getQualifiedIdentifier();
        String importName = member.toString();
        if(member.getIdentifier().toString().equals("*")) {
            String substring = importName.substring(0, importName.lastIndexOf("."));
            ToolingProvider.getTooling()
                    .getElements()
                    .getAllPackageElements(substring)
                    .stream()
                    .map(PackageElement::getEnclosedElements)
                    .flatMap(Collection::stream)
                    .map(ElementUtils.CastElement::castToTypeElement)
                    .forEach(element -> map.putIfAbsent(element.getSimpleName().toString(), element.getQualifiedName().toString()));
        } else {
            map.putIfAbsent(member.getIdentifier().toString(), importName);
        }
        return map;
    }
    
}