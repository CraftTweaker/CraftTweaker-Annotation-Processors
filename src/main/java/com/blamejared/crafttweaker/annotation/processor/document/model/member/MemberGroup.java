package com.blamejared.crafttweaker.annotation.processor.document.model.member;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public class MemberGroup {

    public static final Codec<MemberGroup> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("key").forGetter(MemberGroup::key),
            Member.CODEC.listOf().fieldOf("members").forGetter(MemberGroup::members)
    ).apply(instance, MemberGroup::new));
    private final String key;
    private final List<Member> members;

    public MemberGroup(String key, List<Member> members) {
        this.key = key;
        this.members = members;
    }

    public String key() {
        return key;
    }

    public List<Member> members() {
        return members;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MemberGroup{");
        sb.append("key='").append(key).append('\'');
        sb.append(", members=").append(members);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MemberGroup that = (MemberGroup) o;
        return key.equals(that.key) && members.equals(that.members);
    }

    @Override
    public int hashCode() {
        int result = key.hashCode();
        result = 31 * result + members.hashCode();
        return result;
    }
}
