package net.syobonoon.plugin.miclosmagic.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class MagicCommandMaster implements TabExecutor {
    private Plugin plugin;
    private MagicCommandFactory magicCommandFactory;
    private List<String> magicOptionList = new ArrayList<>();

    public MagicCommandMaster(){
        this.magicCommandFactory = new MagicCommandFactory();
        this.magicOptionList.add("book");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        AbstractMagicCommand calledCommand;

        if ("magic".equals(command.getName())) {
            calledCommand = magicCommandFactory.createCommand(args[0]);
        }else{
            return false;
        }

        if (calledCommand == null) return false;
        calledCommand.onCommand(sender, args);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return magicOptionList;
    }
}
