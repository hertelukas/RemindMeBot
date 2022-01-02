package com.example.remindmebot.core.commands;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

import java.util.List;

public abstract class CommandHandler {

    private final String name;
    private final String description;
    private final boolean production;
    private final List<Option> options;

    public CommandHandler(String name, String description, boolean production, List<Option> options) {
        this.name = name;
        this.description = description;
        this.production = production;
        this.options = options;
    }

    public abstract boolean handle(SlashCommandEvent event);

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Option> getOptions() {
        return options;
    }

    public boolean isProduction() {
        return production;
    }
}
