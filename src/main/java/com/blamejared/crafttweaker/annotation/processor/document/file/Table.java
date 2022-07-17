package com.blamejared.crafttweaker.annotation.processor.document.file;

import com.blamejared.crafttweaker.annotation.processor.util.Pair;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class Table {
    
    private final List<String> keys = new ArrayList<>();
    private final List<Map<String, String>> rows = new ArrayList<>();
    
    private Map<String, String> currentRow = new HashMap<>();
    
    public Table() {
    
    }
    
    public Table key(String key) {
        
        this.keys.add(key);
        return this;
    }
    
    public Table rowEntry(String key, Object value) {
        
        if(value != null && !Objects.toString(value).isBlank()) {
            
            this.currentRow.put(key, Objects.toString(value).isBlank() ? null : Objects.toString(value));
        }
        
        return this;
    }
    
    public void endRow() {
        
        this.rows.add(currentRow);
        currentRow = new HashMap<>();
    }
    
    public String write() {
        
        final Collector<CharSequence, ?, String> tableJoiner = Collectors.joining(" | ", "| ", " |");
        final Set<String> usedKeys = rows.stream()
                .map(Map::keySet)
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
        final List<String> activeKeys = keys.stream().filter(usedKeys::contains).toList();
        final Map<String, List<String>> columns = new LinkedHashMap<>();
        
        activeKeys.forEach(key -> rows.forEach(row -> columns.computeIfAbsent(key, s -> new ArrayList<>())
                .add(row.getOrDefault(key, ""))));
        
        Map<String, Integer> lengthMap = columns.keySet()
                .stream()
                .map(key -> new Pair<>(key, columns.get(key)
                        .stream()
                        .map(s -> Math.max(key.length(), s.length()))
                        .max(Comparator.naturalOrder())
                        .orElse(0)))
                .collect(Collectors.toMap(Pair::first, Pair::second));
        final String header = columns.keySet()
                .stream()
                .map(key -> StringUtils.center(key, lengthMap.get(key)))
                .collect(tableJoiner);
        final String headerSep = header.replaceAll("[^|]", "-");
        final StringBuilder builder = new StringBuilder(header).append(System.lineSeparator()).append(headerSep);
        
        
        for(int i = 0; i < rows.size(); i++) {
            List<String> row = new ArrayList<>();
            for(String colKey : columns.keySet()) {
                List<String> values = columns.get(colKey);
                row.add(StringUtils.rightPad(values.get(i), lengthMap.get(colKey)));
            }
            builder.append(System.lineSeparator()).append(row.stream().collect(tableJoiner));
        }
        
        return builder.append(System.lineSeparator()).toString();
    }
    
    
}
