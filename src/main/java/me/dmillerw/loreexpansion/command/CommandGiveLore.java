package me.dmillerw.loreexpansion.command;

import com.google.common.collect.Lists;
import me.dmillerw.loreexpansion.LoreExpansion;
import me.dmillerw.loreexpansion.core.LoreLoader;
import me.dmillerw.loreexpansion.core.data.Lore;
import me.dmillerw.loreexpansion.core.data.LoreKey;
import me.dmillerw.loreexpansion.util.LoreUtil;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CommandGiveLore extends CommandBase {

    @Override
    public String getCommandName() {
        return "give";
    }

    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "/lore give <player> <lore_category> <lore_id> [useItem]";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length < 3)
            throw new WrongUsageException(getCommandUsage(sender));

        if (args[0].equalsIgnoreCase("~"))
            args[0] = sender.getName();

        EntityPlayer player = getPlayer(server, sender, args[0]);
        LoreKey loreKey = new LoreKey(args[2], args[1]);
        Lore lore = LoreLoader.getLore(loreKey);
        if (lore == null || lore.isNull())
            throw new CommandException("chat.loreexpansion.lore.given.fail");

        boolean useItem = false;
        if (args.length == 4)
            useItem = Boolean.parseBoolean(args[3]);

        if (useItem) { // Gives lore scrap item to player
            ItemStack stack = LoreUtil.attachLore(new ItemStack(LoreExpansion.LORE_PAGE), loreKey);

            boolean didGive = player.inventory.addItemStackToInventory(stack);
            if (didGive) {
                player.worldObj.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((player.getRNG().nextFloat() - player.getRNG().nextFloat()) * 0.7F + 1.0F) * 2.0F);
                player.inventoryContainer.detectAndSendChanges();
            }

            if (!didGive) {
                EntityItem entityitem = player.dropItem(stack, false);
                if (entityitem != null) {
                    entityitem.setNoPickupDelay();
                    entityitem.setOwner(player.getName());
                }
            }
        } else { // Gives lore directly to player data
            LoreUtil.provideLore(player, loreKey);
        }

        sender.addChatMessage(new TextComponentTranslation("chat.loreexpansion.lore.given", loreKey.toString(), args[0]));
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        switch (args.length) {
            case 1:
                return getListOfStringsMatchingLastWord(args, server.getAllUsernames());
            case 2:
                return getListOfStringsMatchingLastWord(args, LoreLoader.getCategories());
            case 3:
                return getListOfStringsMatchingLastWord(args, getLoreSetAsStrings(LoreLoader.getLoreForCategory(args[1])));
            case 4:
                return getListOfStringsMatchingLastWord(args, "true", "false");
            default:
                return Lists.newArrayList();
        }
    }

    private List<String> getLoreSetAsStrings(Collection<Lore> lores) {
        List<String> ret = new ArrayList<String>();
        for (Lore lore : lores)
            ret.add(lore.getKey().getId());
        return ret;
    }
}
