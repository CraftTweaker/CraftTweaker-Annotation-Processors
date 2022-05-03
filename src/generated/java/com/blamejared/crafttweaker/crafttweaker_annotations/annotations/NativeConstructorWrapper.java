package com.blamejared.crafttweaker.crafttweaker_annotations.annotations;
import com.blamejared.crafttweaker_annotations.annotations.NativeConstructor;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import java.util.ArrayList;
import java.util.List;
import io.toolisticon.aptk.tools.AnnotationUtils;


/**
 * Wrapper class to read attribute values from Annotation NativeConstructor.
 */
public class NativeConstructorWrapper {

    private final Element annotatedElement;
    private final AnnotationMirror annotationMirror;

    /**
     * Private constructor.
     * Used to read annotation from Element.
     * @param annotatedElement the annotated Element to annotated with this wrapper annotation
     */
    private NativeConstructorWrapper (Element annotatedElement) {
        this.annotatedElement = annotatedElement;
        this.annotationMirror = AnnotationUtils.getAnnotationMirror(annotatedElement, NativeConstructor.class);
    }

    /**
     * Private constructor.
     * Mainly used for embedded annotations.
     * @param element the element related with the passed annotationMirror
     * @param annotationMirror the AnnotationMirror to wrap
     */
    private NativeConstructorWrapper (Element element, AnnotationMirror annotationMirror) {
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
     * Gets the NativeConstructor.value from wrapped annotation.
     * @return the attribute value
     */
    public AnnotationMirror[] valueAsAnnotationMirrorArray() {
        List<AnnotationMirror> result = new ArrayList<>();
        for(AnnotationValue value : (List<AnnotationValue>)AnnotationUtils.getAnnotationValueOfAttributeWithDefaults(annotationMirror, "value").getValue() ) {
            result.add( ((AnnotationMirror)value.getValue()));
        }

        return result.toArray(new AnnotationMirror[result.size()]);
    }





    /**
     * Gets the NativeConstructor.description from wrapped annotation.
     * @return the attribute value
     */
    public String description() {
        return (String)AnnotationUtils.getAnnotationValueOfAttributeWithDefaults(annotationMirror, "description").getValue();
    }


    /**
     * Allows to check if attribute was explicitly set or if default value is used.
     * @return true, if default value is used, otherwise false
     */
    public boolean descriptionIsDefaultValue(){
        return AnnotationUtils.getAnnotationValueOfAttribute(annotationMirror,"description") == null;
    }

    /**
     * Gets the NativeConstructor.deprecationMessage from wrapped annotation.
     * @return the attribute value
     */
    public String deprecationMessage() {
        return (String)AnnotationUtils.getAnnotationValueOfAttributeWithDefaults(annotationMirror, "deprecationMessage").getValue();
    }


    /**
     * Allows to check if attribute was explicitly set or if default value is used.
     * @return true, if default value is used, otherwise false
     */
    public boolean deprecationMessageIsDefaultValue(){
        return AnnotationUtils.getAnnotationValueOfAttribute(annotationMirror,"deprecationMessage") == null;
    }

    /**
     * Gets the NativeConstructor.getSinceVersion from wrapped annotation.
     * @return the attribute value
     */
    public String getSinceVersion() {
        return (String)AnnotationUtils.getAnnotationValueOfAttributeWithDefaults(annotationMirror, "getSinceVersion").getValue();
    }


    /**
     * Allows to check if attribute was explicitly set or if default value is used.
     * @return true, if default value is used, otherwise false
     */
    public boolean getSinceVersionIsDefaultValue(){
        return AnnotationUtils.getAnnotationValueOfAttribute(annotationMirror,"getSinceVersion") == null;
    }



    /**
     * Checks if passed element is annotated with this wrapper annotation type : NativeConstructor
     * @param element The element to check for wrapped annotation type
     * @return true, if passed element is annotated with NativeConstructor annotation, otherwise false
     */
    public static boolean isAnnotated(Element element) {
        return element != null && element.getAnnotation(NativeConstructor.class) != null;
    }

     /**
      * Gets the AnnotationMirror from passed element for this wrappers annotation type and creates a wrapper instance.
      * @param element The element to read the annotations from
      * @return The wrapped AnnotationMirror if Element is annotated with this wrappers annotation type, otherwise null.
      */
    public static NativeConstructorWrapper wrap(Element element) {
        return isAnnotated(element) ? new NativeConstructorWrapper(element) : null;
    }

    /**
     * Wraps an AnnotationMirror.
     * Throws an IllegalArgumentException if passed AnnotationMirror type doesn't match the wrapped annotation type.
     * @param annotationMirror The element annotated with the annotation to wrap
     * @return The wrapper instance
     */
    public static NativeConstructorWrapper wrap(AnnotationMirror annotationMirror) {
        return new NativeConstructorWrapper(null, annotationMirror);
    }

   /**
     * Wraps an AnnotationMirror.
     * Throws an IllegalArgumentException if passed AnnotationMirror type doesn't match the wrapped annotation type.
     * @param element the element bound to the usage of passed AnnotationMirror
     * @param annotationMirror The AnnotationMirror to wrap
     * @return The wrapper instance
     */
    public static NativeConstructorWrapper wrap(Element element, AnnotationMirror annotationMirror) {
        return new NativeConstructorWrapper(element, annotationMirror);
    }

}