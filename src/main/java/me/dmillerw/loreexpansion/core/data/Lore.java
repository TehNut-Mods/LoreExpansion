package me.dmillerw.loreexpansion.core.data;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;

import java.util.Set;

public class Lore {

    public static final Lore NULL_LORE = new Lore("NULL", "NULL", new Content("NULL", "NULL", "NULL"), Sets.<Reference>newHashSet());

    private final String id;
    private final String category;
    private final Content content;
    private final Set<Reference> requirements;

    public Lore(String id, String category, Content content, Set<Reference> requirements) {
        this.id = id;
        this.category = category;
        this.content = content;
        this.requirements = requirements;
    }

    public boolean isNull() {
        return this.equals(NULL_LORE) || getId().equalsIgnoreCase("NULL") || getCategory().equalsIgnoreCase("NULL");
    }

    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public Content getContent() {
        return content;
    }

    public Set<Reference> getRequirements() {
        return requirements;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", getId())
                .add("category", getCategory())
                .add("content", getContent())
                .add("requirements", getRequirements())
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Lore)) return false;

        Lore lore = (Lore) o;

        if (getId() != null ? !getId().equals(lore.getId()) : lore.getId() != null) return false;
        if (getCategory() != null ? !getCategory().equals(lore.getCategory()) : lore.getCategory() != null)
            return false;
        if (getContent() != null ? !getContent().equals(lore.getContent()) : lore.getContent() != null) return false;
        return getRequirements() != null ? getRequirements().equals(lore.getRequirements()) : lore.getRequirements() == null;

    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getCategory() != null ? getCategory().hashCode() : 0);
        result = 31 * result + (getContent() != null ? getContent().hashCode() : 0);
        result = 31 * result + (getRequirements() != null ? getRequirements().hashCode() : 0);
        return result;
    }
}
