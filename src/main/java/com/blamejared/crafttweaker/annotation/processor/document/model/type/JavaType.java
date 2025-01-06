package com.blamejared.crafttweaker.annotation.processor.document.model.type;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class JavaType extends Type {
    
    public static final Codec<JavaType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                    Codec.STRING.fieldOf("key").forGetter(Type::key),
                    Codec.STRING.fieldOf("display_name").forGetter(Type::displayName),
                    Codec.BOOL.fieldOf("nullable").forGetter(Type::nullable),
                    Codec.STRING.fieldOf("package_name").forGetter(JavaType::packageName),
                    Codec.STRING.fieldOf("class_name").forGetter(JavaType::className),
                    Codec.unboundedMap(Codec.STRING, Type.CODEC).fieldOf("type_parameters").forGetter(JavaType::typeParameters),
                    Type.CODEC.optionalFieldOf("super_type").forGetter(JavaType::superType),
                    Type.CODEC.listOf().fieldOf("interfaces").forGetter(JavaType::interfaces)
            )
            .apply(instance, JavaType::new));
    
    private final String packageName;
    private final String className;
    private final Map<String, Type> typeParameters;
    private final Optional<Type> superType;
    private final List<Type> interfaces;
    
    public JavaType(String key, String displayName, boolean nullable, String packageName, String className, Map<String, Type> typeParameters, Optional<Type> superType, List<Type> interfaces) {
        
        super(key, displayName, TypeKind.JAVA, nullable);
        this.packageName = packageName;
        this.className = className;
        this.typeParameters = typeParameters;
        this.superType = superType;
        this.interfaces = interfaces;
    }
    
    public String packageName() {
        
        return packageName;
    }
    
    public String className() {
        
        return className;
    }
    
    public Map<String, Type> typeParameters() {
        
        return typeParameters;
    }
    
    public Optional<Type> superType() {
        
        return superType;
    }
    
    public List<Type> interfaces() {
        
        return interfaces;
    }
    
    
    @Override
    public String toString() {
        
        final StringBuilder sb = new StringBuilder("JavaType{");
        sb.append("packageName='").append(packageName).append('\'');
        sb.append(", className='").append(className).append('\'');
        sb.append(", typeParameters=").append(typeParameters);
        sb.append(", superType=").append(superType);
        sb.append(", interfaces=").append(interfaces);
        sb.append('}');
        return sb.toString();
    }
    
    
    @Override
    public boolean equals(Object o) {
        
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }
        if(!super.equals(o)) {
            return false;
        }
        
        JavaType javaType = (JavaType) o;
        return packageName.equals(javaType.packageName) && className.equals(javaType.className) && typeParameters.equals(javaType.typeParameters) && superType.equals(javaType.superType) && interfaces.equals(javaType.interfaces);
    }
    
    @Override
    public int hashCode() {
        
        int result = super.hashCode();
        result = 31 * result + packageName.hashCode();
        result = 31 * result + className.hashCode();
        result = 31 * result + typeParameters.hashCode();
        result = 31 * result + superType.hashCode();
        result = 31 * result + interfaces.hashCode();
        return result;
    }
    
}
