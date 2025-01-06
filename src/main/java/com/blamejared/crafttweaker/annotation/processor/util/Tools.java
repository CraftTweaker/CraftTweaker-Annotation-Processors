package com.blamejared.crafttweaker.annotation.processor.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.source.util.Trees;

import javax.annotation.processing.ProcessingEnvironment;
import java.util.function.Function;

public class Tools {
    
    public static final Function<ProcessingEnvironment, Trees> TREES = Util.cacheLatest(Trees::instance);
    public static final Gson GSON = new GsonBuilder().create();
}
