package com.blockycraft.blockyhelp;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class BlockyHelp extends JavaPlugin {

    private Properties config;
    private int commandsPerPage = 4;
    private String header, footer, pageInvalid, errorNoCommand;

    @Override
    public void onEnable() {
        File configFile = new File(getDataFolder(), "config.properties");
        if (!configFile.exists()) {
            try {
                getDataFolder().mkdirs();
                InputStream in = getClass().getClassLoader().getResourceAsStream("config.properties");
                if (in != null) {
                    OutputStream out = new FileOutputStream(configFile);
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = in.read(buffer)) > 0) {
                        out.write(buffer, 0, len);
                    }
                    out.close();
                    in.close();
                } else {
                    configFile.createNewFile();
                }
            } catch (Exception e) {
                getServer().getLogger().severe("Erro ao gerar config.properties: " + e.getMessage());
            }
        }

        config = new Properties();
        try {
            InputStream input = new FileInputStream(configFile);
            config.load(input);
            input.close();
        } catch (Exception e) {
            getServer().getLogger().severe("Erro ao carregar config.properties: " + e.getMessage());
        }

        // Carrega configs
        commandsPerPage = parseInt("pagination.max", 4);
        header = color(config.getProperty("ajuda.header", ""));
        footer = config.getProperty("ajuda.footer", "");
        pageInvalid = color(config.getProperty("pagination.invalid", ""));
        errorNoCommand = color(config.getProperty("error.nocommand", ""));
    }

    @Override
    public void onDisable() {}

    private int parseInt(String key, int def) {
        try {
            return Integer.parseInt(config.getProperty(key, String.valueOf(def)));
        } catch (Exception e) {
            return def;
        }
    }

    private String color(String txt) {
        return txt.replace("&", "§");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Apenas jogadores podem usar este comando.");
            return true;
        }

        List<String> commands = new ArrayList<String>();
        int i = 1;
        while (true) {
            String nameLine = config.getProperty("command." + i + ".name");
            if (nameLine == null) break;
            commands.add(color(nameLine));
            i++;
        }

        int totalPages = Math.max(1, (int) Math.ceil((double) commands.size() / commandsPerPage));
        int page = 1;
        if (args.length > 0) {
            try {
                page = Integer.parseInt(args[0]);
                if (page < 1 || page > totalPages) {
                    sender.sendMessage(pageInvalid.replace("{total}", String.valueOf(totalPages)));
                    return true;
                }
            } catch (Exception e) {
                sender.sendMessage(pageInvalid.replace("{total}", String.valueOf(totalPages)));
                return true;
            }
        }

        if (commands.isEmpty()) {
            sender.sendMessage(errorNoCommand);
            return true;
        }

        // Header
        sender.sendMessage(header);

        // Comandos da página atual
        int start = (page - 1) * commandsPerPage;
        int end = Math.min(start + commandsPerPage, commands.size());
        for (int j = start; j < end; j++) {
            sender.sendMessage(commands.get(j));
        }

        // Footer formatado preenchendo pagina e total
        String outFooter = footer.replace("{pagina}", String.valueOf(page)).replace("{total}", String.valueOf(totalPages));
        sender.sendMessage(outFooter);

        return true;
    }
}
