package com.blamejared.crafttweaker.annotation.processor.validation.keyword;

import com.blamejared.crafttweaker.annotation.processor.util.StringSourceFile;
import io.toolisticon.cute.CompileTestBuilder;
import org.junit.jupiter.api.Test;


public class KeywordValidationTest {
    
    // <editor-fold desc="Methods">
    @Test
    public void methodNameIsValid() {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(KeywordValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import javax.annotation.Nullable;
                        import org.openzen.zencode.java.ZenCodeType.Method;
                        
                        public class Main {
                            
                            @Method
                            public void getAt() {}
                            
                        }"""))
                .compilationShouldSucceed()
                .executeTest();
    }
    
    @Test
    public void overriddenMethodNameIsValid() {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(KeywordValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import javax.annotation.Nullable;
                        import org.openzen.zencode.java.ZenCodeType.Method;
                        
                        public class Main {
                            
                            @Method("getAt")
                            public void get() {}
                            
                        }"""))
                .compilationShouldSucceed()
                .executeTest();
    }
    
    @Test
    public void methodNameIsInvalid() {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(KeywordValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import javax.annotation.Nullable;
                        import org.openzen.zencode.java.ZenCodeType.Method;
                        
                        public class Main {
                            
                            @Method
                            public void get() {}
                            
                        }"""))
                .compilationShouldFail()
                .expectErrorMessageThatContains("Name 'get' is a ZenCode keyword!")
                .executeTest();
    }
    
    @Test
    public void overriddenMethodNameIsInvalid() {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(KeywordValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import javax.annotation.Nullable;
                        import org.openzen.zencode.java.ZenCodeType.Method;
                        
                        public class Main {
                            
                            @Method("get")
                            public void getThing() {}
                            
                        }"""))
                .compilationShouldFail()
                .expectErrorMessageThatContains("Name 'get' is a ZenCode keyword!")
                .executeTest();
    }
    
    // </editor-fold>
    // <editor-fold desc="Getters">
    @Test
    public void getterNameIsValid() {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(KeywordValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import javax.annotation.Nullable;
                        import org.openzen.zencode.java.ZenCodeType;
                        
                        public class Main {
                            
                            @ZenCodeType.Getter
                            public int getRandom() {
                                return 4;
                            }
                            
                        }"""))
                .compilationShouldSucceed()
                .executeTest();
    }
    
    @Test
    public void overriddenGetterNameIsValid() {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(KeywordValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import javax.annotation.Nullable;
                        import org.openzen.zencode.java.ZenCodeType;
                        
                        public class Main {
                            
                            @ZenCodeType.Getter("random")
                            public int get() {
                                return 4;
                            }
                            
                        }"""))
                .compilationShouldSucceed()
                .executeTest();
    }
    
    @Test
    public void getterNameIsInvalid() {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(KeywordValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import javax.annotation.Nullable;
                        import org.openzen.zencode.java.ZenCodeType;
                        
                        public class Main {
                            
                            @ZenCodeType.Getter
                            public int sbyte() {
                                return 4;
                            }
                            
                        }"""))
                .compilationShouldFail()
                .expectErrorMessageThatContains("Name 'sbyte' is a ZenCode keyword!")
                .executeTest();
    }
    
    @Test
    public void overriddenGetterNameIsInvalid() {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(KeywordValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import javax.annotation.Nullable;
                        import org.openzen.zencode.java.ZenCodeType;
                        
                        public class Main {
                            
                            @ZenCodeType.Getter("sbyte")
                            public int getSbyte() {
                                return 4;
                            }
                            
                        }"""))
                .compilationShouldFail()
                .expectErrorMessageThatContains("Name 'sbyte' is a ZenCode keyword!")
                .executeTest();
    }
    
    // </editor-fold>
    // <editor-fold desc="Setters">
    @Test
    public void setterNameIsValid() {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(KeywordValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import javax.annotation.Nullable;
                        import org.openzen.zencode.java.ZenCodeType;
                        
                        public class Main {
                            
                            @ZenCodeType.Setter
                            public void setThing(int number) {
                            
                            }
                            
                        }"""))
                .compilationShouldSucceed()
                .executeTest();
    }
    
    @Test
    public void overriddenSetterNameIsValid() {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(KeywordValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import javax.annotation.Nullable;
                        import org.openzen.zencode.java.ZenCodeType;
                        
                        public class Main {
                            
                            @ZenCodeType.Setter("thing")
                            public void set(int number) {
                            
                            }
                            
                        }"""))
                .compilationShouldSucceed()
                .executeTest();
    }
    
    @Test
    public void setterNameIsInvalid() {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(KeywordValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import javax.annotation.Nullable;
                        import org.openzen.zencode.java.ZenCodeType;
                        
                        public class Main {
                            
                            @ZenCodeType.Setter
                            public void set(int number) {
                            
                            }
                            
                        }"""))
                .compilationShouldFail()
                .expectErrorMessageThatContains("Name 'set' is a ZenCode keyword!")
                .executeTest();
    }
    
    @Test
    public void overriddenSetterNameIsInvalid() {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(KeywordValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import javax.annotation.Nullable;
                        import org.openzen.zencode.java.ZenCodeType;
                        
                        public class Main {
                            
                            @ZenCodeType.Setter("set")
                            public void setThing(int number) {
                            
                            }
                            
                        }"""))
                .compilationShouldFail()
                .expectErrorMessageThatContains("Name 'set' is a ZenCode keyword!")
                .executeTest();
    }
    
    // </editor-fold>
    // <editor-fold desc="Fields">
    @Test
    public void fieldNameIsValid() {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(KeywordValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import javax.annotation.Nullable;
                        import org.openzen.zencode.java.ZenCodeType;
                        
                        public class Main {
                            
                            @ZenCodeType.Field
                            public int random;
                            
                        }"""))
                .compilationShouldSucceed()
                .executeTest();
    }
    
    @Test
    public void overriddenFieldNameIsValid() {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(KeywordValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import javax.annotation.Nullable;
                        import org.openzen.zencode.java.ZenCodeType;
                        
                        public class Main {
                            
                            @ZenCodeType.Field("random")
                            public int get;
                            
                        }"""))
                .compilationShouldSucceed()
                .executeTest();
    }
    
    @Test
    public void fieldNameIsInvalid() {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(KeywordValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import javax.annotation.Nullable;
                        import org.openzen.zencode.java.ZenCodeType;
                        
                        public class Main {
                            
                            @ZenCodeType.Field
                            public int get;
                            
                        }"""))
                .compilationShouldFail()
                .expectErrorMessageThatContains("Name 'get' is a ZenCode keyword!")
                .executeTest();
    }
    
    @Test
    public void overriddenFieldNameIsInvalid() {
        
        CompileTestBuilder.compilationTest()
                .addProcessors(KeywordValidationProcessor.class)
                .addSources(new StringSourceFile("Main", """
                        import javax.annotation.Nullable;
                        import org.openzen.zencode.java.ZenCodeType;
                        
                        public class Main {
                            
                            @ZenCodeType.Field("get")
                            public int random;
                            
                        }"""))
                .compilationShouldFail()
                .expectErrorMessageThatContains("Name 'get' is a ZenCode keyword!")
                .executeTest();
    }
    // </editor-fold>
}
