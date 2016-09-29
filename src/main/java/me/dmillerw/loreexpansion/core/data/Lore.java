package me.dmillerw.loreexpansion.core.data;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;
import me.dmillerw.loreexpansion.core.action.ActionData;
import me.dmillerw.loreexpansion.core.trigger.TriggerData;

import javax.annotation.Nullable;
import java.util.Set;

public final class Lore implements Comparable<Lore> {

    public static final Lore NULL_LORE = new Lore("NULL", "NULL", new Content("NULL", "NULL", "NULL", false), 0, Sets.newHashSet(new LoreKey("blah", "blah")), true, false, true, null, null);
    public static final String GLOBAL = "global";

    private final LoreKey key;
    private final Content content;
    private final int sortingIndex;
    private final Set<LoreKey> requirements;
    private final boolean autoAdd;
    private final boolean defaultLore;
    private final boolean notify;
    @Nullable
    private final TriggerData loreTrigger;
    @Nullable
    private final ActionData loreAction;

    public Lore(LoreKey key, Content content, int sortingIndex, Set<LoreKey> requirements, boolean autoAdd, boolean defaultLore, boolean notify, @Nullable TriggerData loreTrigger, @Nullable ActionData loreAction) {
        this.key = key;
        this.content = content;
        this.sortingIndex = sortingIndex;
        this.requirements = requirements;
        this.autoAdd = autoAdd;
        this.defaultLore = defaultLore;
        this.notify = notify;
        this.loreTrigger = loreTrigger;
        this.loreAction = loreAction;
    }

    public Lore(String id, String category, Content content, int sortingIndex, Set<LoreKey> requirements, boolean autoAdd, boolean defaultLore, boolean notify, @Nullable TriggerData loreTrigger, @Nullable ActionData loreAction) {
        this(new LoreKey(id, category), content, sortingIndex, requirements, autoAdd, defaultLore, notify, loreTrigger, loreAction);
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

    public boolean shouldAutoAdd() {
        return autoAdd;
    }

    public boolean isDefaultLore() {
        return defaultLore;
    }

    public boolean shouldNotify() {
        return notify;
    }

    @Nullable
    public TriggerData getLoreTrigger() {
        return loreTrigger;
    }

    @Nullable
    public ActionData getLoreAction() {
        return loreAction;
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
