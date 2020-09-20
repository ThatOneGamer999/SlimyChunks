package io.gitlab.kyleafmine.minebucks.packet;



import net.minecraft.server.v1_16_R2.PacketPlayOutMapChunk;
import net.minecraft.server.v1_16_R2.PacketPlayOutUnloadChunk;
import net.minecraft.server.v1_16_R2.PlayerConnection;
import org.bukkit.Chunk;
import org.bukkit.craftbukkit.v1_16_R2.CraftChunk;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PacketWorldChunk {

    private PacketPlayOutMapChunk packet;
    private PacketPlayOutUnloadChunk unload;
    private boolean sendUnload;
    public PacketWorldChunk(final Chunk chunk, boolean u) {
        sendUnload =u;
        unload = new PacketPlayOutUnloadChunk(chunk.getX(), chunk.getZ());
        packet = new PacketPlayOutMapChunk(((CraftChunk) chunk).getHandle(), 65535);

    }
    public PacketWorldChunk(final Chunk chunk) {
        sendUnload = true;
        unload = new PacketPlayOutUnloadChunk(chunk.getX(), chunk.getZ());
        packet = new PacketPlayOutMapChunk(((CraftChunk) chunk).getHandle(), 65535);
    }

    public void send(final Player player) {
        PlayerConnection conn = ((CraftPlayer) player).getHandle().playerConnection;
        if (sendUnload) {
            conn.sendPacket(unload);
        }
        conn.sendPacket(packet);
    }

}
