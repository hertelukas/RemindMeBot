package com.example.remindmebot.core;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class MainListener extends ListenerAdapter {

    private final List<CommandHandler> commandHandlers;

    public MainListener(CommandHandler... commandHandlers) {
        this.commandHandlers = Arrays.asList(commandHandlers);
    }

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        if(!commandHandled(event)){
            event.reply("Command not found.").queue();
        }
    }

    private boolean commandHandled(@NotNull SlashCommandEvent event) {
        for (CommandHandler commandHandler : commandHandlers) {
            if (commandHandler.handle(event))
                return true;
        }
        return false;
    }
}
