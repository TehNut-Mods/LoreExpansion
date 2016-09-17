package me.dmillerw.loreexpansion.core.data;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;

import java.util.Set;

public class Lore {

    public static final Lore NULL_LORE = new Lore("NULL", "NULL", new Content("NULL", "NULL", "NULL"), Sets.<LoreKey>newHashSet());

    private final LoreKey key;
    private final Content content;
    private final Set<LoreKey> requirements;

    public Lore(LoreKey key, Content content, Set<LoreKey> requirements) {
        this.key = key;
        this.content = content;
        this.requirements = requirements;
    }

    public Lore(String id, String category, Content content, Set<LoreKey> requirements) {
        this(new LoreKey(id, category), content, requirements);
    }

    public boolean isNull() {
        return this.equals(NULL_LORE) || getKey().getId().equalsIgnoreCase("NULL") || getKey().getCategory().equalsIgnoreCase("NULL");
    }

    public LoreKey getKey() {
        return key;
    }

    public Content getContent() {
        return content;
    }

    public Set<LoreKey> getRequirements() {
        return requirements;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("key", getKey())
                .add("content", getContent())
                .add("requirements", getRequirements())
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lore)) return false;

        Lore lore = (Lore) o;

        if (getKey() != null ? !getKey().equals(lore.getKey()) : lore.getKey() != null) return false;
        if (getContent() != null ? !getContent().equals(lore.getContent()) : lore.getContent() != null) return false;
        return getRequirements() != null ? getRequirements().equals(lore.getRequirements()) : lore.getRequirements() == null;

    }

    @Override
    public int hashCode() {
        int result = getKey() != null ? getKey().hashCode() : 0;
        result = 31 * result + (getContent() != null ? getContent().hashCode() : 0);
        result = 31 * result + (getRequirements() != null ? getRequirements().hashCode() : 0);
        return result;
    }
}
