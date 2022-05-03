package com.blamejared.crafttweaker.annotation.processor.validation.parameter.count;

import com.blamejared.crafttweaker.annotation.processor.util.StringSourceFile;
import com.blamejared.crafttweaker.annotation.processor.validation.virtual_type.VirtualTypeValidationProcessor;
import io.toolisticon.cute.CompileTestBuilder;
import org.junit.jupiter.api.Test;

public class ParameterCountValidationTest {
    
    @Test
    public void noParameterGetterIsCorrect() {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(VirtualTypeValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import javax.annotation.Nullable;
                        import org.openzen.zencode.java.ZenCodeType;
                        
                        @ZenCodeType.Name("Main")
                        public class Main {
                            
                            @ZenCodeType.Getter
                            public String getAt() {
                                return "other";
                            }
                            
                        }"""))
                .compilationShouldSucceed()
                .executeTest();
    }
    
    @Test
    public void singleParameterSetterIsCorrect() {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(VirtualTypeValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import javax.annotation.Nullable;
                        import org.openzen.zencode.java.ZenCodeType;
                        
                        @ZenCodeType.Name("Main")
                        public class Main {
                            
                            @ZenCodeType.Setter
                            public void setAt(String name) {
                            }
                            
                        }"""))
                .compilationShouldSucceed()
                .executeTest();
    }
    
    @Test
    public void noParameterCasterIsCorrect() {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(VirtualTypeValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import javax.annotation.Nullable;
                        import org.openzen.zencode.java.ZenCodeType;
                        
                        @ZenCodeType.Name("Main")
                        public class Main {
                            
                            @ZenCodeType.Caster
                            public String getAt() {
                                return "other";
                            }
                            
                        }"""))
                .compilationShouldSucceed()
                .executeTest();
    }
    
    @Test
    public void getterWithParametersIsInvalid() {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(VirtualTypeValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import javax.annotation.Nullable;
                        import org.openzen.zencode.java.ZenCodeType;
                        
                        @ZenCodeType.Name("Main")
                        public class Main {
                            
                            @ZenCodeType.Getter
                            public String getAt(String name) {
                                return "other";
                            }
                            
                        }"""))
                .compilationShouldFail()
                .expectErrorMessageThatContains("Expected '0' parameters for Getter method but received '1'")
                .executeTest();
    }
    
    @Test
    public void setterWithNoParametersIsInvalid() {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(VirtualTypeValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import javax.annotation.Nullable;
                        import org.openzen.zencode.java.ZenCodeType;
                        
                        @ZenCodeType.Name("Main")
                        public class Main {
                            
                            @ZenCodeType.Setter
                            public void getAt() {
                            }
                            
                        }"""))
                .compilationShouldFail()
                .expectErrorMessageThatContains("Expected '1' parameter for Setter method but received '0'")
                .executeTest();
    }
    
    @Test
    public void casterWithParametersIsInvalid() {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(VirtualTypeValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import javax.annotation.Nullable;
                        import org.openzen.zencode.java.ZenCodeType;
                        
                        @ZenCodeType.Name("Main")
                        public class Main {
                            
                            @ZenCodeType.Caster
                            public String getAt(String name) {
                                return "other";
                            }
                            
                        }"""))
                .compilationShouldFail()
                .expectErrorMessageThatContains("Expected '0' parameters for Caster method but received '1'")
                .executeTest();
    }
    
}
