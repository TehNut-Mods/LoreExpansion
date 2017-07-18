package me.dmillerw.loreexpansion.core.data;

import net.minecraft.nbt.NBTTagCompound;
import org.apache.commons.lang3.builder.ToStringBuilder;

public final class LoreKey {

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

    public LoreKey copy() {
        return new LoreKey(getId(), getCategory());
    }

    public NBTTagCompound serialize() {
        NBTTagCompound tagCompound = new NBTTagCompound();
        tagCompound.setString("id", getId());
        tagCompound.setString("category", getCategory());
        return tagCompound;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", id)
                .append("category", category)
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

    public static LoreKey deserialize(NBTTagCompound tagCompound) {
        String id = tagCompound.getString("id");
        String category = tagCompound.getString("category");
        return new LoreKey(id, category);
    }
}
