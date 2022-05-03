package com.blamejared.crafttweaker.annotation.processor.validation.parameter.optional;

import com.blamejared.crafttweaker.annotation.processor.util.StringSourceFile;
import com.blamejared.crafttweaker.annotation.processor.validation.parameter.ParameterValidationProcessor;
import io.toolisticon.cute.CompileTestBuilder;
import org.junit.jupiter.api.Test;

public class OptionalParameterValidationTest {
    
    @Test
    public void optionalIsValid() {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(ParameterValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import javax.annotation.Nullable;
                        import org.openzen.zencode.java.ZenCodeType;
                        
                        public class Main {
                            
                            @ZenCodeType.Method
                            public void doThing(@ZenCodeType.Optional Object tokens) {}
                            
                        }"""))
                .compilationShouldSucceed()
                .executeTest();
    }
    
    @Test
    public void optionalLongIsValid() {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(ParameterValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import javax.annotation.Nullable;
                        import org.openzen.zencode.java.ZenCodeType;
                        
                        public class Main {
                            
                            @ZenCodeType.Method
                            public void doThing(@ZenCodeType.OptionalLong long number) {}
                            
                        }"""))
                .compilationShouldSucceed()
                .executeTest();
    }
    
    @Test
    public void optionalFloatIsValid() {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(ParameterValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import javax.annotation.Nullable;
                        import org.openzen.zencode.java.ZenCodeType;
                        
                        public class Main {
                            
                            @ZenCodeType.Method
                            public void doThing(@ZenCodeType.OptionalFloat float number) {}
                            
                        }"""))
                .compilationShouldSucceed()
                .executeTest();
    }
    
    @Test
    public void optionalStringIsValid() {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(ParameterValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import javax.annotation.Nullable;
                        import org.openzen.zencode.java.ZenCodeType;
                        
                        public class Main {
                            
                            @ZenCodeType.Method
                            public void doThing(@ZenCodeType.OptionalString String tokens) {}
                            
                        }"""))
                .compilationShouldSucceed()
                .executeTest();
    }
    
    @Test
    public void optionalDoubleIsValid() {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(ParameterValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import javax.annotation.Nullable;
                        import org.openzen.zencode.java.ZenCodeType;
                        
                        public class Main {
                            
                            @ZenCodeType.Method
                            public void doThing(@ZenCodeType.OptionalDouble double number) {}
                            
                        }"""))
                .compilationShouldSucceed()
                .executeTest();
    }
    
    @Test
    public void optionalBooleanIsValid() {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(ParameterValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import javax.annotation.Nullable;
                        import org.openzen.zencode.java.ZenCodeType;
                        
                        public class Main {
                            
                            @ZenCodeType.Method
                            public void doThing(@ZenCodeType.OptionalBoolean boolean valid) {}
                            
                        }"""))
                .compilationShouldSucceed()
                .executeTest();
    }
    
    @Test
    public void optionalCharIsValid() {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(ParameterValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import javax.annotation.Nullable;
                        import org.openzen.zencode.java.ZenCodeType;
                        
                        public class Main {
                            
                            @ZenCodeType.Method
                            public void doThing(@ZenCodeType.OptionalChar char character) {}
                            
                        }"""))
                .compilationShouldSucceed()
                .executeTest();
    }
    
    @Test
    public void mismatchedOptionalIsInvalid() {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(ParameterValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import javax.annotation.Nullable;
                        import org.openzen.zencode.java.ZenCodeType;
                        
                        public class Main {
                            
                            @ZenCodeType.Method
                            public void doThing(@ZenCodeType.OptionalString int number) {}
                            
                        }"""))
                .compilationShouldFail()
                .expectErrorMessageThatContains("Optional type should use OptionalInt annotation")
                .executeTest();
    }
    
    @Test
    public void optionalUsedOverSpecificOptionalIsInvalid() {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(ParameterValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import javax.annotation.Nullable;
                        import org.openzen.zencode.java.ZenCodeType;
                        
                        public class Main {
                            
                            @ZenCodeType.Method
                            public void doThing(@ZenCodeType.Optional int number) {}
                            
                        }"""))
                .compilationShouldFail()
                .expectErrorMessageThatContains("Optional type should use OptionalInt annotation")
                .executeTest();
    }
    
    @Test
    public void manyOptionalsAreValid() {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(ParameterValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import javax.annotation.Nullable;
                        import org.openzen.zencode.java.ZenCodeType;
                        
                        public class Main {
                            
                            @ZenCodeType.Method
                            public void doThing(@ZenCodeType.Optional Object tokens, @ZenCodeType.Optional Object escapes) {}
                            
                        }"""))
                .compilationShouldSucceed()
                .executeTest();
    }
    
    @Test
    public void optionalAtTheEndIsValid() {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(ParameterValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import javax.annotation.Nullable;
                        import org.openzen.zencode.java.ZenCodeType;
                        
                        public class Main {
                            
                            @ZenCodeType.Method
                            public void doThing(Object tokens, @ZenCodeType.Optional Object escapes) {}
                            
                        }"""))
                .compilationShouldSucceed()
                .executeTest();
    }
    
    @Test
    public void optionalNotAtTheEndIsInvalid() {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(ParameterValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import javax.annotation.Nullable;
                        import org.openzen.zencode.java.ZenCodeType;
                        
                        public class Main {
                            
                            @ZenCodeType.Method
                            public void doThing(Object tokens, @ZenCodeType.Optional Object escapes) {}
                            
                        }"""))
                .compilationShouldSucceed()
                .executeTest();
    }
    
    @Test
    public void optionalNotAtEndIsInvalid() {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(ParameterValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import javax.annotation.Nullable;
                        import org.openzen.zencode.java.ZenCodeType;
                        
                        public class Main {
                            
                            @ZenCodeType.Method
                            public void doThing(@ZenCodeType.Optional Object tokens, String name) {}
                            
                        }"""))
                .compilationShouldFail()
                .expectErrorMessageThatContains("Optional parameters must go last")
                .executeTest();
    }
    
    @Test
    public void optionalsAtStartAndEndIsInvalid() {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(ParameterValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import javax.annotation.Nullable;
                        import org.openzen.zencode.java.ZenCodeType;
                        
                        public class Main {
                            
                            @ZenCodeType.Method
                            public void doThing(@ZenCodeType.Optional Object tokens, String name, @ZenCodeType.Optional Object escapes) {}
                            
                        }"""))
                .compilationShouldFail()
                .expectErrorMessageThatContains("Optional parameters must go last")
                .executeTest();
    }
    
}
