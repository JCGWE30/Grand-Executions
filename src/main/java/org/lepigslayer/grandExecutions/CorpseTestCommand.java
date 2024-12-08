package org.lepigslayer.grandExecutions;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.util.Vector;

public class CorpseTestCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        double x = Double.parseDouble(strings[0]);
        double y = Double.parseDouble(strings[1]);
        double z = Double.parseDouble(strings[2]);
        DeadBody.getFirst().setOffset(new Vector(x, y, z));
        return true;
    }
}
