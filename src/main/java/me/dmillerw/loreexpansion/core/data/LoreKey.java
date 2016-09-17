package me.dmillerw.loreexpansion.core.data;

import com.google.common.base.Objects;

public class LoreKey {

    private final String id;
    private final String category;

    public LoreKey(String id, String category) {
        this.id = id;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)
                .add("id", getId())
                .add("category", getCategory())
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoreKey)) return false;

        LoreKey loreKey = (LoreKey) o;

        if (getId() != null ? !getId().equals(loreKey.getId()) : loreKey.getId() != null) return false;
        return getCategory() != null ? getCategory().equals(loreKey.getCategory()) : loreKey.getCategory() == null;

    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getCategory() != null ? getCategory().hashCode() : 0);
        return result;
    }
}
