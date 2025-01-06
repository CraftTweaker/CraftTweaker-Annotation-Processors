package com.blamejared.crafttweaker.annotation.processor.document.model.comment;

import com.blamejared.crafttweaker.annotation.processor.util.Keyable;
import com.blamejared.crafttweaker.annotation.processor.util.Util;
import com.mojang.serialization.Codec;

import java.util.function.Supplier;

public enum CommentKind implements Keyable {
    ROOT("root", () -> RootComment.CODEC),
    BOLD("bold", () -> BoldComment.CODEC),
    CODE("code", () -> CodeComment.CODEC),
    ITALICS("italics", () -> ItalicsComment.CODEC),
    LINK("link", () -> LinkComment.CODEC),
    NEW_LINE("new_line", () -> NewlineComment.CODEC),
    PARAGRAPH("paragraph", () -> ParagraphComment.CODEC),
    PLAINTEXT("plaintext", () -> PlaintextComment.CODEC),
    LIST("list", () -> ListComment.CODEC),
    LIST_ITEM("list_item", () -> ListItemComment.CODEC);
    
    public static final Codec<CommentKind> CODEC = Codec.STRING.xmap(Util.enumLookup(CommentKind.values()), CommentKind::key);
    private final String key;
    private final Supplier<Codec<? extends Comment>> codec;
    
    CommentKind(String key, Supplier<Codec<? extends Comment>> codec) {
        
        this.key = key;
        this.codec = codec;
    }
    
    public String key() {
        
        return key;
    }
    
    public Codec<? extends Comment> codec() {
        
        return codec.get();
    }
    
    @Override
    public String toString() {
        
        return "CommentKind{" +
                "key='" + key + '\'' +
                '}';
    }
}
