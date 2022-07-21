package net.syobonoon.plugin.miclosmagic.command.submagiccommand;

import net.syobonoon.plugin.miclosmagic.MagicWorld;
import net.syobonoon.plugin.miclosmagic.command.AbstractMagicCommand;
import org.bukkit.command.CommandSender;

public class MagicCommandReload extends AbstractMagicCommand {

    public boolean onCommand(CommandSender sender, String[] args){
        if (!sender.hasPermission("magicworld.reload")) return false;
        if (args.length != 0) return false;
        MagicWorld.configManager.load_config();
        return true;
    }
}
