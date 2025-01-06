/**
 * The 'key' of each type should be unique to it, per parent (member keys don't need to be unique across all members, just with the parent
 * 'keys' are used to group things together, if a page has multiple things like a setter, getter and method for 'burnTime', it shoudl be its own member group
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@FieldsAreNonnullByDefault
package com.blamejared.crafttweaker.annotation.processor.document.visitor;

import com.blamejared.crafttweaker.annotation.processor.util.annotations.FieldsAreNonnullByDefault;
import com.blamejared.crafttweaker.annotation.processor.util.annotations.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;