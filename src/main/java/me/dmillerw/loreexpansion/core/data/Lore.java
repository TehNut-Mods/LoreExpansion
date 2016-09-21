package me.dmillerw.loreexpansion.core.data;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import me.dmillerw.loreexpansion.core.trigger.TriggerData;

import javax.annotation.Nullable;
import java.util.Set;

public class Lore implements Comparable<Lore> {

    public static final Lore NULL_LORE = new Lore("NULL", "NULL", new Content("NULL", "NULL", "NULL", false), 0, Sets.newHashSet(new LoreKey("blah", "blah")), null);
    public static final String GLOBAL = "global";

    private final LoreKey key;
    private final Content content;
    private final int sortingIndex;
    private final Set<LoreKey> requirements;
    @Nullable
    private final TriggerData loreTrigger;

    public Lore(LoreKey key, Content content, int sortingIndex, Set<LoreKey> requirements, @Nullable TriggerData loreTrigger) {
        this.key = key;
        this.content = content;
        this.sortingIndex = sortingIndex;
        this.requirements = requirements;
        this.loreTrigger = loreTrigger;
    }

    public Lore(String id, String category, Content content, int sortingIndex, Set<LoreKey> requirements, @Nullable TriggerData loreTrigger) {
        this(new LoreKey(id, category), content, sortingIndex, requirements, loreTrigger);
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

    public int getSortingIndex() {
        return sortingIndex;
    }

    public Set<LoreKey> getRequirements() {
        return requirements;
    }

    @Nullable
    public TriggerData getLoreTrigger() {
        return loreTrigger;
    }

    @Override
    public int compareTo(Lore o) {
        if (getSortingIndex() > o.getSortingIndex())
            return 1;

        if (getSortingIndex() < o.getSortingIndex())
            return -1;

        return 0;
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
