package io.github.kyleafmine.slimychunks.util;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import io.github.kyleafmine.slimychunks.packet.ChunkSection;
import io.github.kyleafmine.slimychunks.packet.FakeChunkData;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

public class SlimeChunkPacketModifier extends PacketAdapter {
    public static final int LIME_STAINED_GLASS = 9447;//4100;
    public static final int GRAY_STAINED_GLASS = 9457;//4102;
    public static final ArrayList<Integer> blacklist = new ArrayList<>();
    static {
        blacklist.add(0); // air
        blacklist.add(9666); // cave_air
        blacklist.add(1342); // grass
        blacklist.add(9665); // void_air
        blacklist.add(7893); // tall grass - upper half
        blacklist.add(7894); // tall grass - lower half
        blacklist.add(49); // water - 15
    }
    public SlimeChunkPacketModifier(Plugin plugin) {
        super(plugin, PacketType.Play.Server.MAP_CHUNK);
    }
    @Override
    public void onPacketSending(PacketEvent e) {
        if (!SlimeManager.isFindingSlimes(e.getPlayer())) {
            return;
        }
        FakeChunkData c = FakeChunkData.fromPacket(e.getPacket());
        if (c == null) {
            return;
        }
        boolean isSlime = e.getPlayer().getWorld().getChunkAt(e.getPacket().getIntegers().read(0), e.getPacket().getIntegers().read(1)).isSlimeChunk();
        for (int y = 0; y < 256; y++) {
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    ChunkSection s = c.getSection((int) Math.floor(y / 16));
                    if (!blacklist.contains(s.getBlock(x, y % 16, z))) {
                        if ((x == 15 || z ==15) || (x == 0 || z == 0)) {
                            s.setBlock(x, y % 16, z, isSlime ? LIME_STAINED_GLASS : GRAY_STAINED_GLASS);
                        }
                    }
                }
            }
        }
        e.getPacket().getByteArrays().write(0, c.toBytes());
    }
}
