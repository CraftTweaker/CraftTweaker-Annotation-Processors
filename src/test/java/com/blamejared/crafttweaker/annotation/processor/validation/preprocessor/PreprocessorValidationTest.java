package com.blamejared.crafttweaker.annotation.processor.validation.preprocessor;

import com.blamejared.crafttweaker.annotation.processor.util.StringSourceFile;
import io.toolisticon.cute.CompileTestBuilder;
import org.junit.jupiter.api.Test;

public class PreprocessorValidationTest {
    
    @Test
    public void implicitDefaultConstructorIsValid() {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(PreprocessorAnnotationValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import javax.annotation.Nullable;
                        import com.blamejared.crafttweaker.api.annotation.BracketResolver;
                        import com.blamejared.crafttweaker.api.annotation.Preprocessor;
                        import com.blamejared.crafttweaker.api.zencode.IPreprocessor;
                        
                        @Preprocessor
                        public class Main implements IPreprocessor {
                        
                        }"""))
                .compilationShouldSucceed()
                .executeTest();
    }
    
    @Test
    public void defaultConstructorIsValid() {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(PreprocessorAnnotationValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import javax.annotation.Nullable;
                        import com.blamejared.crafttweaker.api.annotation.BracketResolver;
                        import com.blamejared.crafttweaker.api.annotation.Preprocessor;
                        import com.blamejared.crafttweaker.api.zencode.IPreprocessor;
                        
                        @Preprocessor
                        public class Main implements IPreprocessor  {
                            
                            public Main() {}
                        }"""))
                .compilationShouldSucceed()
                .executeTest();
    }
    
    @Test
    public void instanceFieldIsValid() {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(PreprocessorAnnotationValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import javax.annotation.Nullable;
                        import com.blamejared.crafttweaker.api.annotation.BracketResolver;
                        import com.blamejared.crafttweaker.api.annotation.Preprocessor;
                        import com.blamejared.crafttweaker.api.zencode.IPreprocessor;
                        
                        @Preprocessor
                        public class Main implements IPreprocessor  {
                            
                            public static final Main INSTANCE = new Main();
                            
                        }"""))
                .compilationShouldSucceed()
                .executeTest();
    }
    
    @Test
    public void privateConstructorAndInstanceFieldIsValid() {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(PreprocessorAnnotationValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import javax.annotation.Nullable;
                        import com.blamejared.crafttweaker.api.annotation.BracketResolver;
                        import com.blamejared.crafttweaker.api.annotation.Preprocessor;
                        import com.blamejared.crafttweaker.api.zencode.IPreprocessor;
                        
                        @Preprocessor
                        public class Main implements IPreprocessor  {
                            
                            public static final Main INSTANCE = new Main();
                            
                            private Main() {}
                        }"""))
                .compilationShouldSucceed()
                .executeTest();
    }
    
    @Test
    public void privateConstructorWithParamsAndInstanceFieldIsValid() {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(PreprocessorAnnotationValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import javax.annotation.Nullable;
                        import com.blamejared.crafttweaker.api.annotation.BracketResolver;
                        import com.blamejared.crafttweaker.api.annotation.Preprocessor;
                        import com.blamejared.crafttweaker.api.zencode.IPreprocessor;
                        
                        @Preprocessor
                        public class Main implements IPreprocessor  {
                            
                            public static final Main INSTANCE = new Main("name");
                            
                            private Main(String name) {}
                        }"""))
                .compilationShouldSucceed()
                .executeTest();
    }
    
    @Test
    public void privateConstructorIsInvalid() {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(PreprocessorAnnotationValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        
                        import com.blamejared.crafttweaker.api.annotation.Preprocessor;
                        import com.blamejared.crafttweaker.api.zencode.IPreprocessor;
                        
                        @Preprocessor
                        public class Main implements IPreprocessor {
                            private Main() {
                            }
                        }"""))
                .compilationShouldFail()
                .expectErrorMessageThatContains("Element is annotated as Preprocessor but has no INSTANCE field nor a public no-arg constructor!")
                .executeTest();
    }
    
    @Test
    public void publicConstructorWithParamsIsInvalid() {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(PreprocessorAnnotationValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        
                        import com.blamejared.crafttweaker.api.annotation.Preprocessor;
                        import com.blamejared.crafttweaker.api.zencode.IPreprocessor;
                        
                        @Preprocessor
                        public class Main implements IPreprocessor {
                            public Main(String name) {
                            }
                        }"""))
                .compilationShouldFail()
                .expectErrorMessageThatContains("Element is annotated as Preprocessor but has no INSTANCE field nor a public no-arg constructor!")
                .executeTest();
    }
    
}
