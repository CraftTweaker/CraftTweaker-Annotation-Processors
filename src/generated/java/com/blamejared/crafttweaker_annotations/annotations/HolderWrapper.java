package com.blamejared.crafttweaker_annotations.annotations;
import com.blamejared.crafttweaker_annotations.annotations.NativeMethod.Holder;
import com.blamejared.crafttweaker_annotations.annotations.NativeMethod;

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
 * Wrapper class to read attribute values from Annotation Holder.
 */
public class HolderWrapper {

    private final Element annotatedElement;
    private final AnnotationMirror annotationMirror;

    /**
     * Private constructor.
     * Used to read annotation from Element.
     * @param annotatedElement the annotated Element to annotated with this wrapper annotation
     */
    private HolderWrapper (Element annotatedElement) {
        this.annotatedElement = annotatedElement;
        this.annotationMirror = AnnotationUtils.getAnnotationMirror(annotatedElement, Holder.class);
    }

    /**
     * Private constructor.
     * Mainly used for embedded annotations.
     * @param element the element related with the passed annotationMirror
     * @param annotationMirror the AnnotationMirror to wrap
     */
    private HolderWrapper (Element element, AnnotationMirror annotationMirror) {
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
     * Gets the Holder.value from wrapped annotation.
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
     * Gets the Holder.value from wrapped annotation.
     * @return the attribute value
     */
    public NativeMethodWrapper[] value() {
        List<NativeMethodWrapper> result = new ArrayList<>();
        for(AnnotationValue value : (List<AnnotationValue>)AnnotationUtils.getAnnotationValueOfAttributeWithDefaults(annotationMirror, "value").getValue() ) {
            result.add( NativeMethodWrapper.wrap(this.annotatedElement, (AnnotationMirror)value.getValue()));
        }

        return result.toArray(new NativeMethodWrapper[result.size()]);
    }






    /**
     * Checks if passed element is annotated with this wrapper annotation type : Holder
     * @param element The element to check for wrapped annotation type
     * @return true, if passed element is annotated with Holder annotation, otherwise false
     */
    public static boolean isAnnotated(Element element) {
        return element != null && element.getAnnotation(Holder.class) != null;
    }

     /**
      * Gets the AnnotationMirror from passed element for this wrappers annotation type and creates a wrapper instance.
      * @param element The element to read the annotations from
      * @return The wrapped AnnotationMirror if Element is annotated with this wrappers annotation type, otherwise null.
      */
    public static HolderWrapper wrap(Element element) {
        return isAnnotated(element) ? new HolderWrapper(element) : null;
    }

    /**
     * Wraps an AnnotationMirror.
     * Throws an IllegalArgumentException if passed AnnotationMirror type doesn't match the wrapped annotation type.
     * @param annotationMirror The element annotated with the annotation to wrap
     * @return The wrapper instance
     */
    public static HolderWrapper wrap(AnnotationMirror annotationMirror) {
        return new HolderWrapper(null, annotationMirror);
    }

   /**
     * Wraps an AnnotationMirror.
     * Throws an IllegalArgumentException if passed AnnotationMirror type doesn't match the wrapped annotation type.
     * @param element the element bound to the usage of passed AnnotationMirror
     * @param annotationMirror The AnnotationMirror to wrap
     * @return The wrapper instance
     */
    public static HolderWrapper wrap(Element element, AnnotationMirror annotationMirror) {
        return new HolderWrapper(element, annotationMirror);
    }

}