package com.blamejared.crafttweaker_annotations.annotations;
import com.blamejared.crafttweaker_annotations.annotations.NativeConstructor.ConstructorParameter;

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
 * Wrapper class to read attribute values from Annotation ConstructorParameter.
 */
public class ConstructorParameterWrapper {

    private final Element annotatedElement;
    private final AnnotationMirror annotationMirror;

    /**
     * Private constructor.
     * Used to read annotation from Element.
     * @param annotatedElement the annotated Element to annotated with this wrapper annotation
     */
    private ConstructorParameterWrapper (Element annotatedElement) {
        this.annotatedElement = annotatedElement;
        this.annotationMirror = AnnotationUtils.getAnnotationMirror(annotatedElement, ConstructorParameter.class);
    }

    /**
     * Private constructor.
     * Mainly used for embedded annotations.
     * @param element the element related with the passed annotationMirror
     * @param annotationMirror the AnnotationMirror to wrap
     */
    private ConstructorParameterWrapper (Element element, AnnotationMirror annotationMirror) {
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
     * Gets the ConstructorParameter.type from wrapped annotation.
     * @return the attribute value as a TypeMirror
     */
    public TypeMirror typeAsTypeMirror() {
        return (TypeMirror)AnnotationUtils.getAnnotationValueOfAttributeWithDefaults(annotationMirror, "type").getValue();
    }

    /**
     * Gets the ConstructorParameter.type from wrapped annotation.
     * @return the attribute value as a TypeMirror
     */
    public TypeMirrorWrapper typeAsTypeMirrorWrapper() {
        return TypeMirrorWrapper.wrap((TypeMirror)AnnotationUtils.getAnnotationValueOfAttributeWithDefaults(annotationMirror, "type").getValue());
    }

    /**
     * Gets the ConstructorParameter.type from wrapped annotation.
     * @return the attribute value as a fqn
     */
    public String typeAsFqn() {
        return TypeUtils.TypeConversion.convertToFqn(typeAsTypeMirror());
    }



    /**
     * Gets the ConstructorParameter.name from wrapped annotation.
     * @return the attribute value
     */
    public String name() {
        return (String)AnnotationUtils.getAnnotationValueOfAttributeWithDefaults(annotationMirror, "name").getValue();
    }



    /**
     * Gets the ConstructorParameter.description from wrapped annotation.
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
      * Gets the ConstructorParameter.examples from wrapped annotation.
      * @return the attribute value
      */
     public String[] examples() {

         List<String> result = new ArrayList<>();
         for(AnnotationValue value : (List<AnnotationValue>)AnnotationUtils.getAnnotationValueOfAttributeWithDefaults(annotationMirror, "examples").getValue() ) {
              result.add((String)value.getValue());
         }

         return result.toArray(new String[result.size()]);
     }


    /**
     * Allows to check if attribute was explicitly set or if default value is used.
     * @return true, if default value is used, otherwise false
     */
    public boolean examplesIsDefaultValue(){
        return AnnotationUtils.getAnnotationValueOfAttribute(annotationMirror,"examples") == null;
    }



    /**
     * Checks if passed element is annotated with this wrapper annotation type : ConstructorParameter
     * @param element The element to check for wrapped annotation type
     * @return true, if passed element is annotated with ConstructorParameter annotation, otherwise false
     */
    public static boolean isAnnotated(Element element) {
        return element != null && element.getAnnotation(ConstructorParameter.class) != null;
    }

     /**
      * Gets the AnnotationMirror from passed element for this wrappers annotation type and creates a wrapper instance.
      * @param element The element to read the annotations from
      * @return The wrapped AnnotationMirror if Element is annotated with this wrappers annotation type, otherwise null.
      */
    public static ConstructorParameterWrapper wrap(Element element) {
        return isAnnotated(element) ? new ConstructorParameterWrapper(element) : null;
    }

    /**
     * Wraps an AnnotationMirror.
     * Throws an IllegalArgumentException if passed AnnotationMirror type doesn't match the wrapped annotation type.
     * @param annotationMirror The element annotated with the annotation to wrap
     * @return The wrapper instance
     */
    public static ConstructorParameterWrapper wrap(AnnotationMirror annotationMirror) {
        return new ConstructorParameterWrapper(null, annotationMirror);
    }

   /**
     * Wraps an AnnotationMirror.
     * Throws an IllegalArgumentException if passed AnnotationMirror type doesn't match the wrapped annotation type.
     * @param element the element bound to the usage of passed AnnotationMirror
     * @param annotationMirror The AnnotationMirror to wrap
     * @return The wrapper instance
     */
    public static ConstructorParameterWrapper wrap(Element element, AnnotationMirror annotationMirror) {
        return new ConstructorParameterWrapper(element, annotationMirror);
    }

}