package com.blamejared.crafttweaker.annotation.processor.document.model.extra;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public class RequiredMods {
    
    public static final Codec<RequiredMods> CODEC = Mod.CODEC.listOf().xmap(RequiredMods::new, RequiredMods::mods);
    
    private final List<Mod> mods;
    
    public RequiredMods(List<Mod> mods) {
        
        this.mods = mods;
    }
    
    public List<Mod> mods() {
        
        return mods;
    }
    
    @Override
    public String toString() {
        
        final StringBuilder sb = new StringBuilder("RequiredMods{");
        sb.append("mods=").append(mods);
        sb.append('}');
        return sb.toString();
    }
    
    @Override
    public boolean equals(Object o) {
        
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }
        
        RequiredMods that = (RequiredMods) o;
        return mods.equals(that.mods);
    }
    
    @Override
    public int hashCode() {
        
        return mods.hashCode();
    }
    
    public static class Mod {
        
        public static final Codec<Mod> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                        Codec.STRING.fieldOf("mod_id").forGetter(Mod::modId),
                        Codec.STRING.fieldOf("display_name").forGetter(Mod::displayName),
                        Codec.STRING.fieldOf("url").forGetter(Mod::url)
                )
                .apply(instance, Mod::new));
        
        private final String modId;
        private final String displayName;
        private final String url;
        
        public Mod(String modId, String displayName, String url) {
            
            this.modId = modId;
            this.displayName = displayName;
            this.url = url;
        }
        
        public String modId() {
            
            return modId;
        }
        
        public String displayName() {
            
            return displayName;
        }
        
        public String url() {
            
            return url;
        }
        
        @Override
        public String toString() {
            
            final StringBuilder sb = new StringBuilder("Mod{");
            sb.append("modId='").append(modId).append('\'');
            sb.append(", displayName='").append(displayName).append('\'');
            sb.append(", url='").append(url).append('\'');
            sb.append('}');
            return sb.toString();
        }
        
        @Override
        public boolean equals(Object o) {
            
            if(this == o) {
                return true;
            }
            if(o == null || getClass() != o.getClass()) {
                return false;
            }
            
            Mod mod = (Mod) o;
            return modId.equals(mod.modId) && displayName.equals(mod.displayName) && url.equals(mod.url);
        }
        
        @Override
        public int hashCode() {
            
            int result = modId.hashCode();
            result = 31 * result + displayName.hashCode();
            result = 31 * result + url.hashCode();
            return result;
        }
        
    }
    
}
