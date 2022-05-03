package com.blamejared.crafttweaker_annotations.annotations;
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
 * Wrapper class to read attribute values from Annotation NativeMethod.
 */
public class NativeMethodWrapper {

    private final Element annotatedElement;
    private final AnnotationMirror annotationMirror;

    /**
     * Private constructor.
     * Used to read annotation from Element.
     * @param annotatedElement the annotated Element to annotated with this wrapper annotation
     */
    private NativeMethodWrapper (Element annotatedElement) {
        this.annotatedElement = annotatedElement;
        this.annotationMirror = AnnotationUtils.getAnnotationMirror(annotatedElement, NativeMethod.class);
    }

    /**
     * Private constructor.
     * Mainly used for embedded annotations.
     * @param element the element related with the passed annotationMirror
     * @param annotationMirror the AnnotationMirror to wrap
     */
    private NativeMethodWrapper (Element element, AnnotationMirror annotationMirror) {
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
     * Gets the NativeMethod.name from wrapped annotation.
     * @return the attribute value
     */
    public String name() {
        return (String)AnnotationUtils.getAnnotationValueOfAttributeWithDefaults(annotationMirror, "name").getValue();
    }



    /**
     * Gets the NativeMethod.parameters from wrapped annotation.
     * @return the attribute value as a TypeMirror
     */
    public TypeMirror[] parametersAsTypeMirror() {

        List<TypeMirror> result = new ArrayList<>();
        for(AnnotationValue value : (List<AnnotationValue>)AnnotationUtils.getAnnotationValueOfAttributeWithDefaults(annotationMirror, "parameters").getValue() ) {
            result.add( ((TypeMirror)value.getValue()));
        }

        return result.toArray(new TypeMirror[result.size()]);
    }

    /**
     * Gets the NativeMethod.parameters from wrapped annotation.
     * @return the attribute value as a TypeMirror
     */
    public TypeMirrorWrapper[] parametersAsTypeMirrorWrapper() {

        List<TypeMirrorWrapper> result = new ArrayList<>();
        for(AnnotationValue value : (List<AnnotationValue>)AnnotationUtils.getAnnotationValueOfAttributeWithDefaults(annotationMirror, "parameters").getValue() ) {
            result.add(TypeMirrorWrapper.wrap((TypeMirror)value.getValue()));
        }

        return result.toArray(new TypeMirrorWrapper[result.size()]);
    }

    /**
     * Gets the NativeMethod.parameters from wrapped annotation.
     * @return the attribute value as a fqn
     */
    public String[] parametersAsFqn() {

        List<String> result = new ArrayList<>();
        for(AnnotationValue value : (List<AnnotationValue>)AnnotationUtils.getAnnotationValueOfAttributeWithDefaults(annotationMirror, "parameters").getValue() ) {
            result.add( TypeUtils.TypeConversion.convertToFqn((TypeMirror)value.getValue()));
        }

        return result.toArray(new String[result.size()]);
    }



    /**
     * Gets the NativeMethod.getterName from wrapped annotation.
     * @return the attribute value
     */
    public String getterName() {
        return (String)AnnotationUtils.getAnnotationValueOfAttributeWithDefaults(annotationMirror, "getterName").getValue();
    }


    /**
     * Allows to check if attribute was explicitly set or if default value is used.
     * @return true, if default value is used, otherwise false
     */
    public boolean getterNameIsDefaultValue(){
        return AnnotationUtils.getAnnotationValueOfAttribute(annotationMirror,"getterName") == null;
    }

    /**
     * Gets the NativeMethod.setterName from wrapped annotation.
     * @return the attribute value
     */
    public String setterName() {
        return (String)AnnotationUtils.getAnnotationValueOfAttributeWithDefaults(annotationMirror, "setterName").getValue();
    }


    /**
     * Allows to check if attribute was explicitly set or if default value is used.
     * @return true, if default value is used, otherwise false
     */
    public boolean setterNameIsDefaultValue(){
        return AnnotationUtils.getAnnotationValueOfAttribute(annotationMirror,"setterName") == null;
    }



    /**
     * Checks if passed element is annotated with this wrapper annotation type : NativeMethod
     * @param element The element to check for wrapped annotation type
     * @return true, if passed element is annotated with NativeMethod annotation, otherwise false
     */
    public static boolean isAnnotated(Element element) {
        return element != null && element.getAnnotation(NativeMethod.class) != null;
    }

     /**
      * Gets the AnnotationMirror from passed element for this wrappers annotation type and creates a wrapper instance.
      * @param element The element to read the annotations from
      * @return The wrapped AnnotationMirror if Element is annotated with this wrappers annotation type, otherwise null.
      */
    public static NativeMethodWrapper wrap(Element element) {
        return isAnnotated(element) ? new NativeMethodWrapper(element) : null;
    }

    /**
     * Wraps an AnnotationMirror.
     * Throws an IllegalArgumentException if passed AnnotationMirror type doesn't match the wrapped annotation type.
     * @param annotationMirror The element annotated with the annotation to wrap
     * @return The wrapper instance
     */
    public static NativeMethodWrapper wrap(AnnotationMirror annotationMirror) {
        return new NativeMethodWrapper(null, annotationMirror);
    }

   /**
     * Wraps an AnnotationMirror.
     * Throws an IllegalArgumentException if passed AnnotationMirror type doesn't match the wrapped annotation type.
     * @param element the element bound to the usage of passed AnnotationMirror
     * @param annotationMirror The AnnotationMirror to wrap
     * @return The wrapper instance
     */
    public static NativeMethodWrapper wrap(Element element, AnnotationMirror annotationMirror) {
        return new NativeMethodWrapper(element, annotationMirror);
    }

}