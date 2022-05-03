package com.blamejared.crafttweaker.annotation.processor.util;

import org.openzen.zencode.java.ZenCodeType;

import java.util.Optional;

public class OperatorTypeParameterCountProvider {
    
    public static Optional<String> validateParameterCount(ZenCodeType.OperatorType operatorType, int actualParams) {
        
        switch(operatorType) {
            case ADD:
            case SUB:
            case MUL:
            case DIV:
            case MOD:
            case CAT:
            case OR:
            case AND:
            case XOR:
            case ADDASSIGN:
            case SUBASSIGN:
            case MULASSIGN:
            case DIVASSIGN:
            case MODASSIGN:
            case CATASSIGN:
            case ORASSIGN:
            case ANDASSIGN:
            case XORASSIGN:
            case SHLASSIGN:
            case SHRASSIGN:
            case CONTAINS:
            case COMPARE:
            case MEMBERGETTER:
            case EQUALS:
            case NOTEQUALS:
            case SHL:
            case SHR:
                return compareParams(operatorType, 1, actualParams);
            case NEG:
            case INVERT:
            case NOT:
                return compareParams(operatorType, 0, actualParams);
            case MEMBERSETTER:
                return compareParams(operatorType, 2, actualParams);
            case INDEXSET:
                if(actualParams < 2) {
                    return Optional.of("Operator '" + operatorType + "' requires a minimum of '2' parameters, but '" + actualParams + "' was given");
                }
                return Optional.empty();
            case INDEXGET:
                if(actualParams < 1) {
                    return Optional.of("Operator '" + operatorType + "' requires a minimum of '1' parameter, but '" + actualParams + "' was given");
                }
                return Optional.empty();
            default:
                throw new IllegalArgumentException();
        }
    }
    
    private static Optional<String> compareParams(ZenCodeType.OperatorType operatorType, int expectedParams, int actualParams) {
        
        return actualParams != expectedParams ? Optional.of("Operator '" + operatorType + "' requires '" + expectedParams + "' parameter" + (expectedParams == 1 ? "" : "s") + ", but '" + actualParams + "' was given.") : Optional.empty();
    }
    
    public static int getParameterCountFor(ZenCodeType.OperatorType operatorType) {
        
        switch(operatorType) {
            case ADD:
            case SUB:
            case MUL:
            case DIV:
            case MOD:
            case CAT:
            case OR:
            case AND:
            case XOR:
            case ADDASSIGN:
            case SUBASSIGN:
            case MULASSIGN:
            case DIVASSIGN:
            case MODASSIGN:
            case CATASSIGN:
            case ORASSIGN:
            case ANDASSIGN:
            case XORASSIGN:
            case SHLASSIGN:
            case SHRASSIGN:
            case INDEXGET:
            case CONTAINS:
            case COMPARE:
            case MEMBERGETTER:
            case EQUALS:
            case NOTEQUALS:
            case SHL:
            case SHR:
                return 1;
            case NEG:
            case INVERT:
            case NOT:
                return 0;
            case MEMBERSETTER:
                return 2;
            case INDEXSET:
                return 3;
            default:
                throw new IllegalArgumentException();
        }
    }
    
}
