package io.github.kyleafmine.slimychunks.util;

import org.bukkit.entity.Player;

import java.util.HashSet;

public class SlimeManager {
    static HashSet<Player> findingSlims = new HashSet<>();
    public static void togglePlayer(Player p ) {
        if (findingSlims.contains(p)) {
            findingSlims.remove(p);
        } else {
            findingSlims.add(p);
        }
    }
    public static boolean isFindingSlimes(Player p) {
        return findingSlims.contains(p);
    }
}
