@AnnotationWrapper(value = {
        Global.class,
        OptionalChar.class,
        OptionalBoolean.class,
        OptionalString.class,
        OptionalDouble.class,
        OptionalFloat.class,
        OptionalLong.class,
        OptionalInt.class,
        Optional.class,
        BorrowForThis.class,
        BorrowForCall.class,
        NullableUSize.class,
        USize.class,
        Unsigned.class,
        Nullable.class,
        NullableUSize.class,
        Caster.class,
        Setter.class,
        Getter.class,
        Operator.class,
        StaticExpansionMethod.class,
        Method.class,
        Constructor.class,
        Field.class,
        Struct.class,
        Name.class,
        Expansion.class
}, usePublicVisibility = true)
package org.openzen.zencode.java;

import io.toolisticon.aptk.annotationwrapper.api.AnnotationWrapper;

import static org.openzen.zencode.java.ZenCodeGlobals.Global;
import static org.openzen.zencode.java.ZenCodeType.BorrowForCall;
import static org.openzen.zencode.java.ZenCodeType.BorrowForThis;
import static org.openzen.zencode.java.ZenCodeType.Caster;
import static org.openzen.zencode.java.ZenCodeType.Constructor;
import static org.openzen.zencode.java.ZenCodeType.Expansion;
import static org.openzen.zencode.java.ZenCodeType.Field;
import static org.openzen.zencode.java.ZenCodeType.Getter;
import static org.openzen.zencode.java.ZenCodeType.Method;
import static org.openzen.zencode.java.ZenCodeType.Name;
import static org.openzen.zencode.java.ZenCodeType.Nullable;
import static org.openzen.zencode.java.ZenCodeType.NullableUSize;
import static org.openzen.zencode.java.ZenCodeType.Operator;
import static org.openzen.zencode.java.ZenCodeType.Optional;
import static org.openzen.zencode.java.ZenCodeType.OptionalBoolean;
import static org.openzen.zencode.java.ZenCodeType.OptionalChar;
import static org.openzen.zencode.java.ZenCodeType.OptionalDouble;
import static org.openzen.zencode.java.ZenCodeType.OptionalFloat;
import static org.openzen.zencode.java.ZenCodeType.OptionalInt;
import static org.openzen.zencode.java.ZenCodeType.OptionalLong;
import static org.openzen.zencode.java.ZenCodeType.OptionalString;
import static org.openzen.zencode.java.ZenCodeType.Setter;
import static org.openzen.zencode.java.ZenCodeType.StaticExpansionMethod;
import static org.openzen.zencode.java.ZenCodeType.Struct;
import static org.openzen.zencode.java.ZenCodeType.USize;
import static org.openzen.zencode.java.ZenCodeType.Unsigned;