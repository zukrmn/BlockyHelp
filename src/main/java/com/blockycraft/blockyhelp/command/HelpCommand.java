package com.blockycraft.blockyhelp.command;

import com.blockycraft.blockyhelp.BlockyHelp;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HelpCommand implements CommandExecutor {

    private final BlockyHelp plugin;

    public HelpCommand(BlockyHelp plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        String lang = plugin.getGeoIPManager().getPlayerLanguage(player);

        List<String> commands = plugin.getLanguageManager().getCommandList(lang);
        int commandsPerPage = 5;
        try {
            commandsPerPage = Integer.parseInt(plugin.getProperties().getProperty("pagination.max", "5"));
        } catch (NumberFormatException e) {
            // ignore, use default
        }

        if (commands.isEmpty()) {
            sender.sendMessage(plugin.getLanguageManager().get(lang, "error.nocommand"));
            return true;
        }

        int totalPages = (int) Math.ceil((double) commands.size() / commandsPerPage);
        int page = 1;

        if (args.length > 0) {
            try {
                page = Integer.parseInt(args[0]);
                if (page < 1 || page > totalPages) {
                    Map<String, String> placeholders = new HashMap<>();
                    placeholders.put("total", String.valueOf(totalPages));
                    sender.sendMessage(plugin.getLanguageManager().get(lang, "pagination.invalid", placeholders));
                    return true;
                }
            } catch (NumberFormatException e) {
                Map<String, String> placeholders = new HashMap<>();
                placeholders.put("total", String.valueOf(totalPages));
                sender.sendMessage(plugin.getLanguageManager().get(lang, "pagination.invalid", placeholders));
                return true;
            }
        }

        Map<String, String> headerPlaceholders = new HashMap<>();
        headerPlaceholders.put("pagina", String.valueOf(page));
        headerPlaceholders.put("total", String.valueOf(totalPages));
        sender.sendMessage(plugin.getLanguageManager().get(lang, "ajuda.header", headerPlaceholders));

        int start = (page - 1) * commandsPerPage;
        int end = Math.min(start + commandsPerPage, commands.size());

        for (int i = start; i < end; i++) {
            sender.sendMessage(commands.get(i));
        }

        Map<String, String> footerPlaceholders = new HashMap<>();
        footerPlaceholders.put("pagina", String.valueOf(page));
        footerPlaceholders.put("total", String.valueOf(totalPages));
        sender.sendMessage(plugin.getLanguageManager().get(lang, "ajuda.footer", footerPlaceholders));

        return true;
    }
}