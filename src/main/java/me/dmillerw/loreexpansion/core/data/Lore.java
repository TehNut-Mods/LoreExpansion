package me.dmillerw.loreexpansion.core.data;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;

import java.util.Set;

public class Lore implements Comparable<Lore> {

    public static final Lore NULL_LORE = new Lore("NULL", "NULL", new Content("NULL", "NULL", "NULL"), 0, Sets.newHashSet(new LoreKey("blah", "blah")));
    public static final String GLOBAL = "global";

    private final LoreKey key;
    private final Content content;
    private final int sortingIndex;
    private final Set<LoreKey> requirements;

    private boolean hidden;
    private boolean autoplay;
    private boolean notify;

    public Lore(LoreKey key, Content content, int sortingIndex, Set<LoreKey> requirements) {
        this.key = key;
        this.content = content;
        this.sortingIndex = sortingIndex;
        this.requirements = requirements;
    }

    public Lore(String id, String category, Content content, int sortingIndex, Set<LoreKey> requirements) {
        this(new LoreKey(id, category), content, sortingIndex, requirements);
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

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isAutoplay() {
        return autoplay;
    }

    public void setAutoplay(boolean autoplay) {
        this.autoplay = autoplay;
    }

    public boolean isNotify() {
        return notify;
    }

    public void setNotify(boolean notify) {
        this.notify = notify;
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
