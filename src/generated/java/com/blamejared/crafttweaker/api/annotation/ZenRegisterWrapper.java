package com.blamejared.crafttweaker.api.annotation;
import com.blamejared.crafttweaker.api.annotation.ZenRegister;

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
 * Wrapper class to read attribute values from Annotation ZenRegister.
 */
public class ZenRegisterWrapper {

    private final Element annotatedElement;
    private final AnnotationMirror annotationMirror;

    /**
     * Private constructor.
     * Used to read annotation from Element.
     * @param annotatedElement the annotated Element to annotated with this wrapper annotation
     */
    private ZenRegisterWrapper (Element annotatedElement) {
        this.annotatedElement = annotatedElement;
        this.annotationMirror = AnnotationUtils.getAnnotationMirror(annotatedElement, ZenRegister.class);
    }

    /**
     * Private constructor.
     * Mainly used for embedded annotations.
     * @param element the element related with the passed annotationMirror
     * @param annotationMirror the AnnotationMirror to wrap
     */
    private ZenRegisterWrapper (Element element, AnnotationMirror annotationMirror) {
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
      * Gets the ZenRegister.modDeps from wrapped annotation.
      * @return the attribute value
      */
     public String[] modDeps() {

         List<String> result = new ArrayList<>();
         for(AnnotationValue value : (List<AnnotationValue>)AnnotationUtils.getAnnotationValueOfAttributeWithDefaults(annotationMirror, "modDeps").getValue() ) {
              result.add((String)value.getValue());
         }

         return result.toArray(new String[result.size()]);
     }


    /**
     * Allows to check if attribute was explicitly set or if default value is used.
     * @return true, if default value is used, otherwise false
     */
    public boolean modDepsIsDefaultValue(){
        return AnnotationUtils.getAnnotationValueOfAttribute(annotationMirror,"modDeps") == null;
    }

     /**
      * Gets the ZenRegister.loaders from wrapped annotation.
      * @return the attribute value
      */
     public String[] loaders() {

         List<String> result = new ArrayList<>();
         for(AnnotationValue value : (List<AnnotationValue>)AnnotationUtils.getAnnotationValueOfAttributeWithDefaults(annotationMirror, "loaders").getValue() ) {
              result.add((String)value.getValue());
         }

         return result.toArray(new String[result.size()]);
     }


    /**
     * Allows to check if attribute was explicitly set or if default value is used.
     * @return true, if default value is used, otherwise false
     */
    public boolean loadersIsDefaultValue(){
        return AnnotationUtils.getAnnotationValueOfAttribute(annotationMirror,"loaders") == null;
    }



    /**
     * Checks if passed element is annotated with this wrapper annotation type : ZenRegister
     * @param element The element to check for wrapped annotation type
     * @return true, if passed element is annotated with ZenRegister annotation, otherwise false
     */
    public static boolean isAnnotated(Element element) {
        return element != null && element.getAnnotation(ZenRegister.class) != null;
    }

     /**
      * Gets the AnnotationMirror from passed element for this wrappers annotation type and creates a wrapper instance.
      * @param element The element to read the annotations from
      * @return The wrapped AnnotationMirror if Element is annotated with this wrappers annotation type, otherwise null.
      */
    public static ZenRegisterWrapper wrap(Element element) {
        return isAnnotated(element) ? new ZenRegisterWrapper(element) : null;
    }

    /**
     * Wraps an AnnotationMirror.
     * Throws an IllegalArgumentException if passed AnnotationMirror type doesn't match the wrapped annotation type.
     * @param annotationMirror The element annotated with the annotation to wrap
     * @return The wrapper instance
     */
    public static ZenRegisterWrapper wrap(AnnotationMirror annotationMirror) {
        return new ZenRegisterWrapper(null, annotationMirror);
    }

   /**
     * Wraps an AnnotationMirror.
     * Throws an IllegalArgumentException if passed AnnotationMirror type doesn't match the wrapped annotation type.
     * @param element the element bound to the usage of passed AnnotationMirror
     * @param annotationMirror The AnnotationMirror to wrap
     * @return The wrapper instance
     */
    public static ZenRegisterWrapper wrap(Element element, AnnotationMirror annotationMirror) {
        return new ZenRegisterWrapper(element, annotationMirror);
    }

}