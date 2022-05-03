package com.blamejared.crafttweaker.annotation.processor.validation.bracket;

import com.blamejared.crafttweaker.annotation.processor.util.StringSourceFile;
import io.toolisticon.cute.CompileTestBuilder;
import org.junit.jupiter.api.Test;

public class BracketHandlerValidationTest {
    
    @Test
    public void resolverIsValid() {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(BracketHandlerValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import javax.annotation.Nullable;
                        import com.blamejared.crafttweaker.api.annotation.BracketResolver;
                        import org.openzen.zencode.java.ZenCodeType.Method;
                        
                        public class Main {
                            
                            @Method
                            @BracketResolver("tokens")
                            public String getAt(String tokens) {
                                return tokens;
                            }
                            
                        }"""))
                .compilationShouldSucceed()
                .executeTest();
    }
    
    @Test
    public void resolverIsInvalidIfReturnsVoid() {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(BracketHandlerValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import javax.annotation.Nullable;
                        import com.blamejared.crafttweaker.api.annotation.BracketResolver;
                        import org.openzen.zencode.java.ZenCodeType.Method;
                        
                        public class Main {
                            
                            @Method
                            @BracketResolver("tokens")
                            public void getAt(String tokens) {
                            }
                            
                        }"""))
                .compilationShouldFail()
                .expectErrorMessageThatContains("Element is annotated with @BracketResolver but does not have a return type.")
                .executeTest();
    }
    
    @Test
    public void resolverIsInvalidIfReturnsVoidClass() {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(BracketHandlerValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import javax.annotation.Nullable;
                        import com.blamejared.crafttweaker.api.annotation.BracketResolver;
                        import org.openzen.zencode.java.ZenCodeType.Method;
                        
                        public class Main {
                            
                            @Method
                            @BracketResolver("tokens")
                            public Void getAt(String tokens) {
                                return null;
                            }
                            
                        }"""))
                .compilationShouldFail()
                .expectErrorMessageThatContains("Element is annotated with @BracketResolver but does not have a return type.")
                .executeTest();
    }
    
    @Test
    public void resolverIsInvalidIfNoParams() {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(BracketHandlerValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import javax.annotation.Nullable;
                        import com.blamejared.crafttweaker.api.annotation.BracketResolver;
                        import org.openzen.zencode.java.ZenCodeType.Method;
                        
                        public class Main {
                            
                            @Method
                            @BracketResolver("tokens")
                            public String getAt() {
                                return "tokens";
                            }
                            
                        }"""))
                .compilationShouldFail()
                .expectErrorMessageThatContains("Element is annotated with @BracketResolver but does not accept a String as its only parameter.")
                .executeTest();
    }
    
    @Test
    public void resolverIsInvalidIfExtraParams() {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(BracketHandlerValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import javax.annotation.Nullable;
                        import com.blamejared.crafttweaker.api.annotation.BracketResolver;
                        import org.openzen.zencode.java.ZenCodeType.Method;
                        
                        public class Main {
                            
                            @Method
                            @BracketResolver("tokens")
                            public String getAt(String tokens, boolean exact) {
                                return tokens;
                            }
                            
                        }"""))
                .compilationShouldFail()
                .expectErrorMessageThatContains("Element is annotated with @BracketResolver but does not accept a String as its only parameter.")
                .executeTest();
    }
    
    @Test
    public void resolverIsInvalidIfNotAMethod() {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(BracketHandlerValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import javax.annotation.Nullable;
                        import com.blamejared.crafttweaker.api.annotation.BracketResolver;
                        import org.openzen.zencode.java.ZenCodeType.Method;
                        
                        public class Main {
                            
                            @BracketResolver("tokens")
                            public String getAt(String tokens) {
                                return tokens;
                            }
                            
                        }"""))
                .compilationShouldFail()
                .expectErrorMessageThatContains("Elements annotated with @BracketResolver should also be annotated with @ZenCodeType.Method")
                .executeTest();
    }
    
}
