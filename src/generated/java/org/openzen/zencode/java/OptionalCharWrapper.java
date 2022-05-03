package org.openzen.zencode.java;
import org.openzen.zencode.java.ZenCodeType.OptionalChar;

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
 * Wrapper class to read attribute values from Annotation OptionalChar.
 */
public class OptionalCharWrapper {

    private final Element annotatedElement;
    private final AnnotationMirror annotationMirror;

    /**
     * Private constructor.
     * Used to read annotation from Element.
     * @param annotatedElement the annotated Element to annotated with this wrapper annotation
     */
    private OptionalCharWrapper (Element annotatedElement) {
        this.annotatedElement = annotatedElement;
        this.annotationMirror = AnnotationUtils.getAnnotationMirror(annotatedElement, OptionalChar.class);
    }

    /**
     * Private constructor.
     * Mainly used for embedded annotations.
     * @param element the element related with the passed annotationMirror
     * @param annotationMirror the AnnotationMirror to wrap
     */
    private OptionalCharWrapper (Element element, AnnotationMirror annotationMirror) {
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
     * Gets the OptionalChar.value from wrapped annotation.
     * @return the attribute value
     */
    public char value() {
        return (char)AnnotationUtils.getAnnotationValueOfAttributeWithDefaults(annotationMirror, "value").getValue();
    }


    /**
     * Allows to check if attribute was explicitly set or if default value is used.
     * @return true, if default value is used, otherwise false
     */
    public boolean valueIsDefaultValue(){
        return AnnotationUtils.getAnnotationValueOfAttribute(annotationMirror,"value") == null;
    }



    /**
     * Checks if passed element is annotated with this wrapper annotation type : OptionalChar
     * @param element The element to check for wrapped annotation type
     * @return true, if passed element is annotated with OptionalChar annotation, otherwise false
     */
    public static boolean isAnnotated(Element element) {
        return element != null && element.getAnnotation(OptionalChar.class) != null;
    }

     /**
      * Gets the AnnotationMirror from passed element for this wrappers annotation type and creates a wrapper instance.
      * @param element The element to read the annotations from
      * @return The wrapped AnnotationMirror if Element is annotated with this wrappers annotation type, otherwise null.
      */
    public static OptionalCharWrapper wrap(Element element) {
        return isAnnotated(element) ? new OptionalCharWrapper(element) : null;
    }

    /**
     * Wraps an AnnotationMirror.
     * Throws an IllegalArgumentException if passed AnnotationMirror type doesn't match the wrapped annotation type.
     * @param annotationMirror The element annotated with the annotation to wrap
     * @return The wrapper instance
     */
    public static OptionalCharWrapper wrap(AnnotationMirror annotationMirror) {
        return new OptionalCharWrapper(null, annotationMirror);
    }

   /**
     * Wraps an AnnotationMirror.
     * Throws an IllegalArgumentException if passed AnnotationMirror type doesn't match the wrapped annotation type.
     * @param element the element bound to the usage of passed AnnotationMirror
     * @param annotationMirror The AnnotationMirror to wrap
     * @return The wrapper instance
     */
    public static OptionalCharWrapper wrap(Element element, AnnotationMirror annotationMirror) {
        return new OptionalCharWrapper(element, annotationMirror);
    }

}