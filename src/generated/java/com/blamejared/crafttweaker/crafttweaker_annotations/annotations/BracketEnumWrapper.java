package com.blamejared.crafttweaker.crafttweaker_annotations.annotations;
import com.blamejared.crafttweaker_annotations.annotations.BracketEnum;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;

import io.toolisticon.aptk.tools.AnnotationUtils;


/**
 * Wrapper class to read attribute values from Annotation BracketEnum.
 */
public class BracketEnumWrapper {

    private final Element annotatedElement;
    private final AnnotationMirror annotationMirror;

    /**
     * Private constructor.
     * Used to read annotation from Element.
     * @param annotatedElement the annotated Element to annotated with this wrapper annotation
     */
    private BracketEnumWrapper (Element annotatedElement) {
        this.annotatedElement = annotatedElement;
        this.annotationMirror = AnnotationUtils.getAnnotationMirror(annotatedElement, BracketEnum.class);
    }

    /**
     * Private constructor.
     * Mainly used for embedded annotations.
     * @param element the element related with the passed annotationMirror
     * @param annotationMirror the AnnotationMirror to wrap
     */
    private BracketEnumWrapper (Element element, AnnotationMirror annotationMirror) {
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
     * Gets the BracketEnum.value from wrapped annotation.
     * @return the attribute value
     */
    public String value() {
        return (String)AnnotationUtils.getAnnotationValueOfAttributeWithDefaults(annotationMirror, "value").getValue();
    }





    /**
     * Checks if passed element is annotated with this wrapper annotation type : BracketEnum
     * @param element The element to check for wrapped annotation type
     * @return true, if passed element is annotated with BracketEnum annotation, otherwise false
     */
    public static boolean isAnnotated(Element element) {
        return element != null && element.getAnnotation(BracketEnum.class) != null;
    }

     /**
      * Gets the AnnotationMirror from passed element for this wrappers annotation type and creates a wrapper instance.
      * @param element The element to read the annotations from
      * @return The wrapped AnnotationMirror if Element is annotated with this wrappers annotation type, otherwise null.
      */
    public static BracketEnumWrapper wrap(Element element) {
        return isAnnotated(element) ? new BracketEnumWrapper(element) : null;
    }

    /**
     * Wraps an AnnotationMirror.
     * Throws an IllegalArgumentException if passed AnnotationMirror type doesn't match the wrapped annotation type.
     * @param annotationMirror The element annotated with the annotation to wrap
     * @return The wrapper instance
     */
    public static BracketEnumWrapper wrap(AnnotationMirror annotationMirror) {
        return new BracketEnumWrapper(null, annotationMirror);
    }

   /**
     * Wraps an AnnotationMirror.
     * Throws an IllegalArgumentException if passed AnnotationMirror type doesn't match the wrapped annotation type.
     * @param element the element bound to the usage of passed AnnotationMirror
     * @param annotationMirror The AnnotationMirror to wrap
     * @return The wrapper instance
     */
    public static BracketEnumWrapper wrap(Element element, AnnotationMirror annotationMirror) {
        return new BracketEnumWrapper(element, annotationMirror);
    }

}