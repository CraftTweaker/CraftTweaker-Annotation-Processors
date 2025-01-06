package com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.block;

import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.JavaDocContainerElement;
import com.blamejared.crafttweaker.annotation.processor.document.javadoc.element.visitor.JavadocElementVisitor;

public class JavadocUnknownBlockTag extends JavadocBlockTagBase {

    private final String name;

    public JavadocUnknownBlockTag(JavaDocContainerElement container, String name) {

        super(container);
        this.name = name;
    }

    @Override
    public String name() {

        return name;
    }

    @Override
    public String toString() {

        return "JavadocUnknownBlockTag{" +
               "name='" + name + '\'' +
               '}';
    }

    @Override
    public <T, C> T accept(JavadocElementVisitor<T, C> visitor, C context) {

        return visitor.visit(this, context);
    }

}
