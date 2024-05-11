package com.mcstaralliance.blockmonitor.listener;

import com.mcstaralliance.blockmonitor.BlockMonitor;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class BlockPlaceListener implements Listener {
    BlockMonitor plugin = BlockMonitor.getInstance();

    public boolean isDebugging() {
        FileConfiguration config = plugin.getConfig();
        return config.getBoolean("debug");
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
        List<String> items = config.getStringList("item");
        String block = event.getBlockPlaced().getBlockData().getAsString();
        for (String item : items) {
            if (item.equalsIgnoreCase(block)) {
                File file = new File(plugin.getDataFolder(), "monitor.log");
                FileWriter fileWriter = new FileWriter(file, true);
                String x = String.valueOf(event.getBlockPlaced().getLocation().getBlockX());
                String y = String.valueOf(event.getBlockPlaced().getLocation().getBlockY());
                String z = String.valueOf(event.getBlockPlaced().getLocation().getBlockZ());
                String world = event.getBlockPlaced().getWorld().getName();
                String pos = x + ',' + y + ',' + z + ',';
                String message = player.getName() + ':' + pos + ',' + pos + world + block;
                fileWriter.write(message + "\n");
            }
        }
    }
}
