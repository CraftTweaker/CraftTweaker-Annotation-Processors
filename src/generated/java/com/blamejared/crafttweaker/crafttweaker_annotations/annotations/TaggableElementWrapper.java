package com.blamejared.crafttweaker.crafttweaker_annotations.annotations;
import com.blamejared.crafttweaker_annotations.annotations.TaggableElement;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;

import io.toolisticon.aptk.tools.AnnotationUtils;


/**
 * Wrapper class to read attribute values from Annotation TaggableElement.
 */
public class TaggableElementWrapper {

    private final Element annotatedElement;
    private final AnnotationMirror annotationMirror;

    /**
     * Private constructor.
     * Used to read annotation from Element.
     * @param annotatedElement the annotated Element to annotated with this wrapper annotation
     */
    private TaggableElementWrapper (Element annotatedElement) {
        this.annotatedElement = annotatedElement;
        this.annotationMirror = AnnotationUtils.getAnnotationMirror(annotatedElement, TaggableElement.class);
    }

    /**
     * Private constructor.
     * Mainly used for embedded annotations.
     * @param element the element related with the passed annotationMirror
     * @param annotationMirror the AnnotationMirror to wrap
     */
    private TaggableElementWrapper (Element element, AnnotationMirror annotationMirror) {
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
     * Gets the TaggableElement.value from wrapped annotation.
     * @return the attribute value
     */
    public String value() {
        return (String)AnnotationUtils.getAnnotationValueOfAttributeWithDefaults(annotationMirror, "value").getValue();
    }





    /**
     * Checks if passed element is annotated with this wrapper annotation type : TaggableElement
     * @param element The element to check for wrapped annotation type
     * @return true, if passed element is annotated with TaggableElement annotation, otherwise false
     */
    public static boolean isAnnotated(Element element) {
        return element != null && element.getAnnotation(TaggableElement.class) != null;
    }

     /**
      * Gets the AnnotationMirror from passed element for this wrappers annotation type and creates a wrapper instance.
      * @param element The element to read the annotations from
      * @return The wrapped AnnotationMirror if Element is annotated with this wrappers annotation type, otherwise null.
      */
    public static TaggableElementWrapper wrap(Element element) {
        return isAnnotated(element) ? new TaggableElementWrapper(element) : null;
    }

    /**
     * Wraps an AnnotationMirror.
     * Throws an IllegalArgumentException if passed AnnotationMirror type doesn't match the wrapped annotation type.
     * @param annotationMirror The element annotated with the annotation to wrap
     * @return The wrapper instance
     */
    public static TaggableElementWrapper wrap(AnnotationMirror annotationMirror) {
        return new TaggableElementWrapper(null, annotationMirror);
    }

   /**
     * Wraps an AnnotationMirror.
     * Throws an IllegalArgumentException if passed AnnotationMirror type doesn't match the wrapped annotation type.
     * @param element the element bound to the usage of passed AnnotationMirror
     * @param annotationMirror The AnnotationMirror to wrap
     * @return The wrapper instance
     */
    public static TaggableElementWrapper wrap(Element element, AnnotationMirror annotationMirror) {
        return new TaggableElementWrapper(element, annotationMirror);
    }

}