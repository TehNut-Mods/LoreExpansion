package me.dmillerw.loreexpansion.json.data;

import com.google.common.base.Objects;

public class Reference {

    private final String id;
    private final String category;

    public Reference(String id, String category) {
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
        if (!(o instanceof Reference)) return false;

        Reference reference = (Reference) o;

        if (getId() != null ? !getId().equals(reference.getId()) : reference.getId() != null) return false;
        return getCategory() != null ? getCategory().equals(reference.getCategory()) : reference.getCategory() == null;

    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getCategory() != null ? getCategory().hashCode() : 0);
        return result;
    }
}
