package org.lepigslayer.grandExecutions;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ToggleCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player) return true;
        boolean antiState = !GrandExecutions.enabled;
        commandSender.sendMessage("Enabled state is now "+antiState);
        GrandExecutions.enabled = antiState;
        return true;
    }
}
