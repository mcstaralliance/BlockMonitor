package com.mcstaralliance.blockmonitor.listener;

import com.mcstaralliance.blockmonitor.BlockMonitor;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BlockPlaceListener implements Listener {
    BlockMonitor plugin = BlockMonitor.getInstance();

    public boolean isDebugging() {
        FileConfiguration config = plugin.getConfig();
        return config.getBoolean("debug");
    }

    public String getMessage(Player player, Block block) {
        String x = String.valueOf(block.getLocation().getBlockX());
        String y = String.valueOf(block.getLocation().getBlockY());
        String z = String.valueOf(block.getLocation().getBlockZ());
        String world = block.getWorld().getName();
        String pos = x + ',' + y + ',' + z + ',';
        return player.getName() + ':' + pos + ',' + pos + world + block;
    }

    public void notify(String message) {
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        for (Player p : players) {
            if (p.isOp()) {
                p.sendMessage(message);
            }
        }
    }

    public void write(String message) throws IOException {
        File file = new File(plugin.getDataFolder(), "monitor.log");
        try (FileWriter fileWriter = new FileWriter(file, true)) {
            fileWriter.write(message + "\n");
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) throws IOException {
        FileConfiguration config = plugin.getConfig();
        Player player = event.getPlayer();
        if (isDebugging()) {
            if (player.isOp()) {
                Block block = event.getBlockPlaced();
                String blockName = block.getType().name();
                player.sendMessage("BlockTypeName: " + blockName);
            }
        }
        List<String> blocks = config.getStringList("blocks");
        Block block = event.getBlockPlaced();
        String blockName = block.getType().name();
        for (String b : blocks) {
            if (b.equalsIgnoreCase(blockName)) {
                String message = getMessage(player, block);
                // 写入日志
                write(message);
                // 通知在线 OP
                notify(message);
            }
        }
    }
}
