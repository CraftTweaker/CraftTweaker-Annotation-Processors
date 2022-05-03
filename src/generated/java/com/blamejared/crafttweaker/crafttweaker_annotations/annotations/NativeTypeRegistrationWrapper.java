package com.blamejared.crafttweaker.crafttweaker_annotations.annotations;
import com.blamejared.crafttweaker_annotations.annotations.NativeTypeRegistration;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;
import java.util.ArrayList;
import java.util.List;
import io.toolisticon.aptk.tools.AnnotationUtils;
import io.toolisticon.aptk.tools.TypeMirrorWrapper;
import io.toolisticon.aptk.tools.TypeUtils;


/**
 * Wrapper class to read attribute values from Annotation NativeTypeRegistration.
 */
public class NativeTypeRegistrationWrapper {

    private final Element annotatedElement;
    private final AnnotationMirror annotationMirror;

    /**
     * Private constructor.
     * Used to read annotation from Element.
     * @param annotatedElement the annotated Element to annotated with this wrapper annotation
     */
    private NativeTypeRegistrationWrapper (Element annotatedElement) {
        this.annotatedElement = annotatedElement;
        this.annotationMirror = AnnotationUtils.getAnnotationMirror(annotatedElement, NativeTypeRegistration.class);
    }

    /**
     * Private constructor.
     * Mainly used for embedded annotations.
     * @param element the element related with the passed annotationMirror
     * @param annotationMirror the AnnotationMirror to wrap
     */
    private NativeTypeRegistrationWrapper (Element element, AnnotationMirror annotationMirror) {
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
     * Gets the NativeTypeRegistration.value from wrapped annotation.
     * @return the attribute value as a TypeMirror
     */
    public TypeMirror valueAsTypeMirror() {
        return (TypeMirror)AnnotationUtils.getAnnotationValueOfAttributeWithDefaults(annotationMirror, "value").getValue();
    }

    /**
     * Gets the NativeTypeRegistration.value from wrapped annotation.
     * @return the attribute value as a TypeMirror
     */
    public TypeMirrorWrapper valueAsTypeMirrorWrapper() {
        return TypeMirrorWrapper.wrap((TypeMirror)AnnotationUtils.getAnnotationValueOfAttributeWithDefaults(annotationMirror, "value").getValue());
    }

    /**
     * Gets the NativeTypeRegistration.value from wrapped annotation.
     * @return the attribute value as a fqn
     */
    public String valueAsFqn() {
        return TypeUtils.TypeConversion.convertToFqn(valueAsTypeMirror());
    }



    /**
     * Gets the NativeTypeRegistration.zenCodeName from wrapped annotation.
     * @return the attribute value
     */
    public String zenCodeName() {
        return (String)AnnotationUtils.getAnnotationValueOfAttributeWithDefaults(annotationMirror, "zenCodeName").getValue();
    }



    /**
     * Gets the NativeTypeRegistration.constructors from wrapped annotation.
     * @return the attribute value
     */
    public AnnotationMirror[] constructorsAsAnnotationMirrorArray() {
        List<AnnotationMirror> result = new ArrayList<>();
        for(AnnotationValue value : (List<AnnotationValue>)AnnotationUtils.getAnnotationValueOfAttributeWithDefaults(annotationMirror, "constructors").getValue() ) {
            result.add( ((AnnotationMirror)value.getValue()));
        }

        return result.toArray(new AnnotationMirror[result.size()]);
    }

    /**
     * Gets the NativeTypeRegistration.constructors from wrapped annotation.
     * @return the attribute value
     */
    public NativeConstructorWrapper[] constructors() {
        List<NativeConstructorWrapper> result = new ArrayList<>();
        for(AnnotationValue value : (List<AnnotationValue>)AnnotationUtils.getAnnotationValueOfAttributeWithDefaults(annotationMirror, "constructors").getValue() ) {
            result.add( NativeConstructorWrapper.wrap(this.annotatedElement, (AnnotationMirror)value.getValue()));
        }

        return result.toArray(new NativeConstructorWrapper[result.size()]);
    }



    /**
     * Allows to check if attribute was explicitly set or if default value is used.
     * @return true, if default value is used, otherwise false
     */
    public boolean constructorsIsDefaultValue(){
        return AnnotationUtils.getAnnotationValueOfAttribute(annotationMirror,"constructors") == null;
    }



    /**
     * Checks if passed element is annotated with this wrapper annotation type : NativeTypeRegistration
     * @param element The element to check for wrapped annotation type
     * @return true, if passed element is annotated with NativeTypeRegistration annotation, otherwise false
     */
    public static boolean isAnnotated(Element element) {
        return element != null && element.getAnnotation(NativeTypeRegistration.class) != null;
    }

     /**
      * Gets the AnnotationMirror from passed element for this wrappers annotation type and creates a wrapper instance.
      * @param element The element to read the annotations from
      * @return The wrapped AnnotationMirror if Element is annotated with this wrappers annotation type, otherwise null.
      */
    public static NativeTypeRegistrationWrapper wrap(Element element) {
        return isAnnotated(element) ? new NativeTypeRegistrationWrapper(element) : null;
    }

    /**
     * Wraps an AnnotationMirror.
     * Throws an IllegalArgumentException if passed AnnotationMirror type doesn't match the wrapped annotation type.
     * @param annotationMirror The element annotated with the annotation to wrap
     * @return The wrapper instance
     */
    public static NativeTypeRegistrationWrapper wrap(AnnotationMirror annotationMirror) {
        return new NativeTypeRegistrationWrapper(null, annotationMirror);
    }

   /**
     * Wraps an AnnotationMirror.
     * Throws an IllegalArgumentException if passed AnnotationMirror type doesn't match the wrapped annotation type.
     * @param element the element bound to the usage of passed AnnotationMirror
     * @param annotationMirror The AnnotationMirror to wrap
     * @return The wrapper instance
     */
    public static NativeTypeRegistrationWrapper wrap(Element element, AnnotationMirror annotationMirror) {
        return new NativeTypeRegistrationWrapper(element, annotationMirror);
    }

}