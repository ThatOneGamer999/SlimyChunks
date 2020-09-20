package io.github.kyleafmine.slimychunks;

import io.github.kyleafmine.slimychunks.util.SlimeManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class SlimeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("Only a player may execute this command");
        }
        Player p = (Player) commandSender;
        if (!p.hasPermission("slimeychunks.use")) {
            p.spigot().sendMessage(new ComponentBuilder("No permission!").color(ChatColor.GREEN).create());
            return true;
        }
        Chunk pc = p.getLocation().getChunk();
        SlimeManager.togglePlayer(p);
        ArrayList<int[]> chunks = new ArrayList<>();
        for (int cx = (int) (Math.ceil(p.getClientViewDistance() - 1 ) * -1); cx < p.getClientViewDistance() -1; cx++) {
            for (int cy = (int) (Math.ceil(p.getClientViewDistance()  - 1) * -1); cy < p.getClientViewDistance() -1; cy++) {
                //chunks.add(p.getWorld().getChunkAt(pc.getX() + cx, pc.getZ() + cy));
                int[] c = new int[2];
                c[0] = pc.getX() + cx;
                c[1] = pc.getZ() + cy;
                chunks.add(c);
            }
        }
        if (SlimeManager.isFindingSlimes(p)) {
            p.spigot().sendMessage(new ComponentBuilder("Turned on the slime chunk locator, please wait while your chunks are being reloaded...").color(ChatColor.GREEN).create());
        } else {
            p.spigot().sendMessage(new ComponentBuilder("Turned off slime chunk locator, please wait while your chunks are being reloaded...").color(ChatColor.RED).create());
        }
        ((SlimyChunks) Bukkit.getServer().getPluginManager().getPlugin("MineBucks")).refreshChunksAsync(p, chunks);

        return true;
    }
}
