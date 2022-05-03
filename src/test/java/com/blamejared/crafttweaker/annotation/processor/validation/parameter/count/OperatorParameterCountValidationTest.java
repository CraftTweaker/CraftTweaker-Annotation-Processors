package com.blamejared.crafttweaker.annotation.processor.validation.parameter.count;

import com.blamejared.crafttweaker.annotation.processor.util.StringSourceFile;
import com.blamejared.crafttweaker.annotation.processor.validation.virtual_type.VirtualTypeValidationProcessor;
import io.toolisticon.cute.CompileTestBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class OperatorParameterCountValidationTest {
    
    @ParameterizedTest
    @ValueSource(strings = {"ADD", "SUB", "MUL", "DIV", "MOD", "CAT", "OR", "AND", "XOR", "ADDASSIGN", "SUBASSIGN", "MULASSIGN", "DIVASSIGN", "MODASSIGN", "CATASSIGN", "ORASSIGN", "ANDASSIGN", "XORASSIGN", "SHLASSIGN", "SHRASSIGN", "CONTAINS", "COMPARE", "MEMBERGETTER", "EQUALS", "NOTEQUALS", "SHL", "SHR"})
    public void singleCorrectCountIsValid(String operatorName) {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(VirtualTypeValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import javax.annotation.Nullable;
                        import org.openzen.zencode.java.ZenCodeType;
                        
                        @ZenCodeType.Name("Main")
                        public class Main {
                            
                            @ZenCodeType.Operator(ZenCodeType.OperatorType.%s)
                            public String getAt(String other) {
                                return other;
                            }
                            
                        }""".formatted(operatorName)))
                .compilationShouldSucceed()
                .executeTest();
    }
    
    @Test
    public void doubleCorrectCountIsValid() {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(VirtualTypeValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import javax.annotation.Nullable;
                        import org.openzen.zencode.java.ZenCodeType;
                        
                        @ZenCodeType.Name("Main")
                        public class Main {
                            
                            @ZenCodeType.Operator(ZenCodeType.OperatorType.MEMBERSETTER)
                            public String getAt(String key, String value) {
                                return "other";
                            }
                            
                        }"""))
                .compilationShouldSucceed()
                .executeTest();
    }
    
    @Test
    public void minOneExactlyCorrectCountIsValid() {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(VirtualTypeValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import javax.annotation.Nullable;
                        import org.openzen.zencode.java.ZenCodeType;
                        
                        @ZenCodeType.Name("Main")
                        public class Main {
                            
                            @ZenCodeType.Operator(ZenCodeType.OperatorType.INDEXGET)
                            public String getAt(String key) {
                                return "other";
                            }
                            
                        }"""))
                .compilationShouldSucceed()
                .executeTest();
    }
    
    @Test
    public void minOneCorrectCountIsValid() {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(VirtualTypeValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import javax.annotation.Nullable;
                        import org.openzen.zencode.java.ZenCodeType;
                        
                        @ZenCodeType.Name("Main")
                        public class Main {
                            
                            @ZenCodeType.Operator(ZenCodeType.OperatorType.INDEXGET)
                            public String getAt(String first, String second) {
                                return "other";
                            }
                            
                        }"""))
                .compilationShouldSucceed()
                .executeTest();
    }
    
    @Test
    public void minDoubleExactlyCorrectCountIsValid() {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(VirtualTypeValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import javax.annotation.Nullable;
                        import org.openzen.zencode.java.ZenCodeType;
                        
                        @ZenCodeType.Name("Main")
                        public class Main {
                            
                            @ZenCodeType.Operator(ZenCodeType.OperatorType.INDEXSET)
                            public String getAt(String key, String value) {
                                return "other";
                            }
                            
                        }"""))
                .compilationShouldSucceed()
                .executeTest();
    }
    
    @Test
    public void minDoubleCorrectCountIsValid() {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(VirtualTypeValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import javax.annotation.Nullable;
                        import org.openzen.zencode.java.ZenCodeType;
                        
                        @ZenCodeType.Name("Main")
                        public class Main {
                            
                            @ZenCodeType.Operator(ZenCodeType.OperatorType.INDEXSET)
                            public String getAt(String first, String second, String value) {
                                return "other";
                            }
                            
                        }"""))
                .compilationShouldSucceed()
                .executeTest();
    }
    
    @ParameterizedTest
    @ValueSource(strings = {"NEG", "INVERT", "NOT"})
    public void emptyCorrectCountIsValid(String operatorName) {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(VirtualTypeValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import javax.annotation.Nullable;
                        import org.openzen.zencode.java.ZenCodeType;
                        
                        @ZenCodeType.Name("Main")
                        public class Main {
                            
                            @ZenCodeType.Operator(ZenCodeType.OperatorType.%s)
                            public String getAt() {
                                return "other";
                            }
                            
                        }""".formatted(operatorName)))
                .compilationShouldSucceed()
                .executeTest();
    }
    
    @ParameterizedTest
    @ValueSource(strings = {"ADD", "SUB", "MUL", "DIV", "MOD", "CAT", "OR", "AND", "XOR", "ADDASSIGN", "SUBASSIGN", "MULASSIGN", "DIVASSIGN", "MODASSIGN", "CATASSIGN", "ORASSIGN", "ANDASSIGN", "XORASSIGN", "SHLASSIGN", "SHRASSIGN", "CONTAINS", "COMPARE", "MEMBERGETTER", "EQUALS", "NOTEQUALS", "SHL", "SHR"})
    public void singleIncorrectCountIsInvalid(String operatorName) {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(VirtualTypeValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import javax.annotation.Nullable;
                        import org.openzen.zencode.java.ZenCodeType;
                        
                        @ZenCodeType.Name("Main")
                        public class Main {
                            
                            @ZenCodeType.Operator(ZenCodeType.OperatorType.%s)
                            public String getAt() {
                                return "other";
                            }
                            
                        }""".formatted(operatorName)))
                .compilationShouldFail()
                .expectErrorMessageThatContains("Operator '%s' requires '1' parameter, but '0' was given".formatted(operatorName))
                .executeTest();
    }
    
    @ParameterizedTest
    @ValueSource(strings = {"NEG", "INVERT", "NOT"})
    public void emptyIncorrectCountIsInvalid(String operatorName) {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(VirtualTypeValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import javax.annotation.Nullable;
                        import org.openzen.zencode.java.ZenCodeType;
                        
                        @ZenCodeType.Name("Main")
                        public class Main {
                            
                            @ZenCodeType.Operator(ZenCodeType.OperatorType.%s)
                            public String getAt(String other) {
                                return other;
                            }
                            
                        }""".formatted(operatorName)))
                .compilationShouldFail()
                .expectErrorMessageThatContains("Operator '%s' requires '0' parameters, but '1' was given".formatted(operatorName))
                .executeTest();
    }
    
    @Test
    public void doubleIncorrectCountIsInvalid() {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(VirtualTypeValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import javax.annotation.Nullable;
                        import org.openzen.zencode.java.ZenCodeType;
                        
                        @ZenCodeType.Name("Main")
                        public class Main {
                            
                            @ZenCodeType.Operator(ZenCodeType.OperatorType.MEMBERSETTER)
                            public String getAt(String other) {
                                return other;
                            }
                            
                        }"""))
                .compilationShouldFail()
                .expectErrorMessageThatContains("Operator 'MEMBERSETTER' requires '2' parameters, but '1' was given")
                .executeTest();
    }
    
    @Test
    public void minOneIncorrectCountIsInvalid() {
    
        CompileTestBuilder.compilationTest()
                .addProcessors(VirtualTypeValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import javax.annotation.Nullable;
                        import org.openzen.zencode.java.ZenCodeType;
                        
                        @ZenCodeType.Name("Main")
                        public class Main {
                            
                            @ZenCodeType.Operator(ZenCodeType.OperatorType.INDEXGET)
                            public String getAt() {
                                return "other";
                            }
                            
                        }"""))
                .compilationShouldFail()
                .expectErrorMessageThatContains("Operator 'INDEXGET' requires a minimum of '1' parameter, but '0' was given")
                .executeTest();
    }
    
    @Test
    public void minTwoIncorrectCountIsInvalid() {
    
        CompileTestBuilder.compilationTest()
                .addProcessors(VirtualTypeValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import javax.annotation.Nullable;
                        import org.openzen.zencode.java.ZenCodeType;
                        
                        @ZenCodeType.Name("Main")
                        public class Main {
                            
                            @ZenCodeType.Operator(ZenCodeType.OperatorType.INDEXSET)
                            public String getAt(String key) {
                                return "other";
                            }
                            
                        }"""))
                .compilationShouldFail()
                .expectErrorMessageThatContains("Operator 'INDEXSET' requires a minimum of '2' parameters, but '1' was given")
                .executeTest();
    }
    
}
