package com.blamejared.crafttweaker.annotation.processor.validation.member;

import com.blamejared.crafttweaker.annotation.processor.util.StringSourceFile;
import com.blamejared.crafttweaker.annotation.processor.validation.virtual_type.VirtualTypeValidationProcessor;
import io.toolisticon.cute.CompileTestBuilder;
import org.junit.jupiter.api.Test;

public class MemberVisibilityValidationTest {
    
    @Test
    public void publicMethodIsValid() {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(VirtualTypeValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import org.openzen.zencode.java.ZenCodeType;
                        @ZenCodeType.Name("Main")
                        public class Main {
                            
                            @ZenCodeType.Method
                            public void test(){
                            }
                        }
                        """))
                .compilationShouldSucceed()
                .executeTest();
    }
    
    @Test
    public void publicConstructorIsValid() {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(VirtualTypeValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import org.openzen.zencode.java.ZenCodeType;
                        @ZenCodeType.Name("Main")
                        public class Main {
                            
                            @ZenCodeType.Constructor
                            public Main(){
                            }
                        }
                        """))
                .compilationShouldSucceed()
                .executeTest();
        
    }
    
    
}
