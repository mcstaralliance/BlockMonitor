package com.mcstaralliance.blockmonitor;

import com.mcstaralliance.blockmonitor.listener.BlockPlaceListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;

public final class BlockMonitor extends JavaPlugin {
    private static BlockMonitor instance;
    public static BlockMonitor getInstance() {
        return instance;
    }
    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        Bukkit.getServer().getPluginManager().registerEvents(new BlockPlaceListener(), this);
        File file = new File(getDataFolder(), "monitor.log");
        try {
            if (file.createNewFile()) {
                getLogger().info("BlockMonitor 日志文件已创建。");
            }
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "无法创建日志文件", e);
        }


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
