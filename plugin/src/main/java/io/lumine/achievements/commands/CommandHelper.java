package io.lumine.achievements.commands;

import io.lumine.achievements.config.Scope;
import io.lumine.mythic.bukkit.utils.adventure.text.Component;
import io.lumine.mythic.bukkit.utils.config.properties.Property;
import io.lumine.mythic.bukkit.utils.config.properties.types.StringProp;
import io.lumine.mythic.bukkit.utils.text.Text;
import org.bukkit.command.CommandSender;

public class CommandHelper {

    public static final StringProp COMMAND_HEADER = Property.String(Scope.CONFIG,
            "Configuration.Language.Command-Header",
            "<yellow><strikethrough>------------<gold>=====</strikethrough> <bold><gradient:#dc0c1f:#ed858f>MCCosmetics</gradient></bold> <strikethrough><gold>=====<yellow>------------</strikethrough>");

    public static final StringProp COMMAND_FOOTER = Property.String(Scope.CONFIG,
            "Configuration.Language.Command-Footer",
            "<yellow><strikethrough>--------------------------------------</strikethrough>");
    public static final StringProp COMMAND_PREFIX = Property.String(Scope.CONFIG,
            "Configuration.Language.Command-Prefix",
            "<bold><white>[<#dc0c1f>MCCosmetics<white>]</bold> ");
    
    public static void sendCommandHeader(CommandSender sender) {
        Text.sendMessage(sender, Text.parse(COMMAND_HEADER.get()));
    }
    
    public static void sendCommandFooter(CommandSender sender)    {
        Text.sendMessage(sender, Text.parse(COMMAND_FOOTER.get()));
    }
    
    public static void sendSuccess(CommandSender sender, String message)    {
        Text.sendMessage(sender, Text.parse(COMMAND_PREFIX.get()).append(Text.parse("<green>" + message)));
    }
    
    public static void sendError(CommandSender sender, String message)  {
        Text.sendMessage(sender, Text.parse(COMMAND_PREFIX.get()).append(Text.parse("<red>" + message)));
    }
    

    public static void sendCommandMessage(CommandSender player, String[]... args) {
        sendCommandHeader(player);
        player.sendMessage(" ");
        for(String[] s : args) {
            for(String ss : s) {
                Text.sendMessage(player, ss);
            }
        }
        player.sendMessage(" ");
        sendCommandFooter(player);
    }
    
    public static void sendCommandMessage(CommandSender player, Component[]... args) {
        sendCommandHeader(player);
        player.sendMessage(" ");
        for(Component[] s : args) {
            for(Component ss : s) {
                Text.sendMessage(player, ss);
            }
        }
        player.sendMessage(" ");
        sendCommandFooter(player);
    }
}
