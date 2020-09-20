package io.github.kyleafmine.slimychunks;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import io.github.kyleafmine.slimychunks.util.SlimeChunkPacketModifier;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class SlimyChunks extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("SlimyChunks by ThatOneGamer999");
        ProtocolLibrary.getProtocolManager().addPacketListener(new SlimeChunkPacketModifier(this));
        getCommand("slime").setExecutor(new SlimeCommand());
    }
    public void refreshChunksAsync(Player p, ArrayList<int[]> chunks) {
        World w = p.getWorld();
        BukkitRunnable r = new BukkitRunnable() {
            @Override
            public void run() {
                //Collections.shuffle(chunks);
                for (int i = 0; i < 4; i++) {
                    if (chunks.size() == 0) {
                        cancel();
                        return;
                    }
                    int[] a = chunks.get(0);
                    if (a == null) {
                        cancel();
                        return;
                    }
                    //Bukkit.getLogger().info("Sending chunk at x=" + a[0] + ";z=" + a[1] );
                    Chunk b = w.getChunkAt(a[0], a[1]);
                    chunks.remove(0);
                    new io.gitlab.kyleafmine.minebucks.packet.PacketWorldChunk(b).send(p);
                }
            }
        };
        r.runTaskTimer(this, 0L, 2L);

    }

}
