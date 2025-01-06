# CraftTweaker-Annotation-Processors

Java Annotation Processors used to validate and generate documentation for the CraftTweaker project.

## Introduction

CraftTweaker-Annotation-Processors is a Java Annotation Processor that validates and generates documentation for CraftTweaker related projects.

## Using the Annotation Processor

Builds are published to the [BlameJared maven](https://maven.blamejared.com), to use them in your build, first add the BlameJared maven to your `repositories` block in your build.gradle file like so:

```groovy
repositories {
    maven { 
        url = 'https://maven.blamejared.com'
        name = 'BlameJared Maven'
    }
}
```

Then add the Annotation Processor in your `dependencies` block like so:

```groovy
dependencies {
    annotationProcessor('com.blamejared.crafttweaker:Crafttweaker_Annotation_Processors:[VERSION]')
}
```

The latest version is current: [![Maven](https://img.shields.io/maven-metadata/v?color=C71A36&label=Latest%20version&logo=Latest%20version&metadataUrl=https%3A%2F%2Fmaven.blamejared.com%2Fcom%2Fblamejared%2Fcrafttweaker%2FCrafttweaker_Annotation_Processors%2Fmaven-metadata.xml&style=flat-square)](https://maven.blamejared.com/com/blamejared/crafttweaker/Crafttweaker_Annotation_Processors/)

You can then configure the annotation processor like so:
```groovy
compileJava {
    options.compilerArgs << "-Acrafttweaker.processor.document.output_directory=${file('docsOut')}"
}
```

If you are using a multiloader project such as the [MultiLoader-Template](https://github.com/jaredlll08/MultiLoader-Template), then it 
is advised to add the Annotation Processor to all sources and use the following options, which will handle merging the generated files.
```groovy
tasks {
    compileJava {
        options.compilerArgs << "-Acrafttweaker.processor.document.output_directory=${project.rootProject.file('docsOut')}"
        options.compilerArgs << "-Acrafttweaker.processor.document.multi_source=true"
    }
}
```

## Validation

Once you have the Annotation Processor in your project, it will automatically validate your ZenScript related classes and methods, ensuring it meets the runtime requirements of the annotation, such as:
- @BracketResolver methods has a string parameter and a non-void return type
- Exposed member names do not conflict with ZenScript keywords (such as `get` or `set`)
- @Optional parameters appear at the end of the parameters

## Automatic Documentation

The Annotation Processor will look for all classes with the `@Document` annotation and generate a file containing all the exposed members in the class, using their JavaDoc for extra comments.

The Annotation Processor also lets you provide examples for parameters using a new javadoc tag, `@docParam <param_name> <example>`, which can also go on the class itself using `this` as the `param_name`, like so: `@docParam this myObject`

Here is an example of what a documented class may look like:
```java
import com.blamejared.crafttweaker_annotations.annotations.Document;
import org.openzen.zencode.java.ZenCodeType;

import java.util.function.Predicate;

/**
 * @docParam this myTestObject
 */
@Document("vanilla/api/util/Test")
public class Test {
    
    /**
     * Does a thing with the provided parameters
     * @param first the first parameter
     * @param second the second parameter
     * @param third the third parameter
     *
     * @docParam first 1
     * @docParam second "hello world"
     * @docParam third (str) => str.length == 0
     */
    @ZenCodeType.Method
    public static void doThing(int first, String second, Predicate<String> third) {}
    
}
```

Most JavaDoc tags such as `@return`, `@see`, and `@deprecated` are supported and will be retained in the generated files.