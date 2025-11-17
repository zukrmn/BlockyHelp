package com.blockycraft.blockyhelp;

import com.blockycraft.blockyhelp.command.HelpCommand;
import com.blockycraft.blockyhelp.geoip.GeoIPManager;
import com.blockycraft.blockyhelp.lang.LanguageManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class BlockyHelp extends JavaPlugin {

    private LanguageManager languageManager;
    private GeoIPManager geoIPManager;
    private Properties properties;
    private File configFile;

    @Override
    public void onEnable() {
        // Carrega a config.properties padrão
        saveDefaultConfig();
        reloadProperties();

        // Inicializa os managers
        languageManager = new LanguageManager(this);
        geoIPManager = new GeoIPManager();

        // Registra o comando
        getCommand("ajuda").setExecutor(new HelpCommand(this));

        getServer().getLogger().info("BlockyHelp has been enabled!");
    }

    @Override
    public void onDisable() {
        getServer().getLogger().info("BlockyHelp has been disabled!");
    }

    public LanguageManager getLanguageManager() {
        return languageManager;
    }

    public GeoIPManager getGeoIPManager() {
        return geoIPManager;
    }

    public Properties getProperties() {
        if (properties == null) {
            reloadProperties();
        }
        return properties;
    }

    public void reloadProperties() {
        if (configFile == null) {
            configFile = new File(getDataFolder(), "config.properties");
        }
        properties = new Properties();
        try (InputStream input = new FileInputStream(configFile)) {
            properties.load(input);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    public void saveDefaultConfig() {
        if (configFile == null) {
            configFile = new File(getDataFolder(), "config.properties");
        }
        if (!configFile.exists()) {
            try (InputStream in = getClass().getClassLoader().getResourceAsStream("config.properties")) {
                if (in != null) {
                    if (!getDataFolder().exists()) {
                        getDataFolder().mkdirs();
                    }
                    java.nio.file.Files.copy(in, configFile.toPath());
                }
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
    }
}
