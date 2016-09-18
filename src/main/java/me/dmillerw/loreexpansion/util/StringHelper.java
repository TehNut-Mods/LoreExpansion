package me.dmillerw.loreexpansion.util;

import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class StringHelper {

    public static boolean isInteger(String str) {
        try {
            //noinspection ResultOfMethodCallIgnored
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    public static String indent(int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++)
            sb.append(" ");
        return sb.toString();
    }

    public static String pretty(String string) {
        StringBuilder stringBuilder = new StringBuilder();

        boolean toUpper = true;
        for (char c : string.toCharArray()) {
            if (toUpper) {
                stringBuilder.append(Character.toUpperCase(c));
                toUpper = false;
            } else if (Character.isDigit(c)) {
                stringBuilder.append(' ').append(c);
            } else if ((c == '-' || c == '_') || Character.isWhitespace(c)) {
                stringBuilder.append(' ');
                toUpper = true;
            } else {
                stringBuilder.append(Character.toLowerCase(c));
            }

        }

        return stringBuilder.toString();
    }

    @SideOnly(Side.CLIENT)
    public static String getLocalizedText(String text, Object... formatting) {
        return I18n.hasKey(text) ? I18n.format(text, formatting) : text;
    }
}
