package net.syobonoon.plugin.miclosmagic.command;

import org.bukkit.command.CommandSender;

public abstract class AbstractMagicCommand {
    public abstract boolean onCommand(CommandSender sender, String[] args);
}
