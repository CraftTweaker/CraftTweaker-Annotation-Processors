package com.blamejared.crafttweaker_annotations.annotations;
import com.blamejared.crafttweaker_annotations.annotations.TypedExpansion;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.List;
import io.toolisticon.aptk.tools.AnnotationUtils;
import io.toolisticon.aptk.tools.TypeMirrorWrapper;
import io.toolisticon.aptk.tools.TypeUtils;


/**
 * Wrapper class to read attribute values from Annotation TypedExpansion.
 */
public class TypedExpansionWrapper {

    private final Element annotatedElement;
    private final AnnotationMirror annotationMirror;

    /**
     * Private constructor.
     * Used to read annotation from Element.
     * @param annotatedElement the annotated Element to annotated with this wrapper annotation
     */
    private TypedExpansionWrapper (Element annotatedElement) {
        this.annotatedElement = annotatedElement;
        this.annotationMirror = AnnotationUtils.getAnnotationMirror(annotatedElement, TypedExpansion.class);
    }

    /**
     * Private constructor.
     * Mainly used for embedded annotations.
     * @param element the element related with the passed annotationMirror
     * @param annotationMirror the AnnotationMirror to wrap
     */
    private TypedExpansionWrapper (Element element, AnnotationMirror annotationMirror) {
        this.annotatedElement = element;
        this.annotationMirror = annotationMirror;
    }

    /**
     * Gets the element on which the wrapped annotation is used.
     */
    public Element _annotatedElement() {
        return this.annotatedElement;
    }

    /**
     * Gets the wrapped AnnotationMirror.
     */
     public AnnotationMirror _annotationMirror() {
        return this.annotationMirror;
     }

    /**
     * Gets the TypedExpansion.value from wrapped annotation.
     * @return the attribute value as a TypeMirror
     */
    public TypeMirror valueAsTypeMirror() {
        return (TypeMirror)AnnotationUtils.getAnnotationValueOfAttributeWithDefaults(annotationMirror, "value").getValue();
    }

    /**
     * Gets the TypedExpansion.value from wrapped annotation.
     * @return the attribute value as a TypeMirror
     */
    public TypeMirrorWrapper valueAsTypeMirrorWrapper() {
        return TypeMirrorWrapper.wrap((TypeMirror)AnnotationUtils.getAnnotationValueOfAttributeWithDefaults(annotationMirror, "value").getValue());
    }

    /**
     * Gets the TypedExpansion.value from wrapped annotation.
     * @return the attribute value as a fqn
     */
    public String valueAsFqn() {
        return TypeUtils.TypeConversion.convertToFqn(valueAsTypeMirror());
    }





    /**
     * Checks if passed element is annotated with this wrapper annotation type : TypedExpansion
     * @param element The element to check for wrapped annotation type
     * @return true, if passed element is annotated with TypedExpansion annotation, otherwise false
     */
    public static boolean isAnnotated(Element element) {
        return element != null && element.getAnnotation(TypedExpansion.class) != null;
    }

     /**
      * Gets the AnnotationMirror from passed element for this wrappers annotation type and creates a wrapper instance.
      * @param element The element to read the annotations from
      * @return The wrapped AnnotationMirror if Element is annotated with this wrappers annotation type, otherwise null.
      */
    public static TypedExpansionWrapper wrap(Element element) {
        return isAnnotated(element) ? new TypedExpansionWrapper(element) : null;
    }

    /**
     * Wraps an AnnotationMirror.
     * Throws an IllegalArgumentException if passed AnnotationMirror type doesn't match the wrapped annotation type.
     * @param annotationMirror The element annotated with the annotation to wrap
     * @return The wrapper instance
     */
    public static TypedExpansionWrapper wrap(AnnotationMirror annotationMirror) {
        return new TypedExpansionWrapper(null, annotationMirror);
    }

   /**
     * Wraps an AnnotationMirror.
     * Throws an IllegalArgumentException if passed AnnotationMirror type doesn't match the wrapped annotation type.
     * @param element the element bound to the usage of passed AnnotationMirror
     * @param annotationMirror The AnnotationMirror to wrap
     * @return The wrapper instance
     */
    public static TypedExpansionWrapper wrap(Element element, AnnotationMirror annotationMirror) {
        return new TypedExpansionWrapper(element, annotationMirror);
    }

}