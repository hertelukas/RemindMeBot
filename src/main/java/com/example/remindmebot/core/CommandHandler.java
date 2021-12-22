package com.example.remindmebot.core;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;

public abstract class CommandHandler {

    private String name;

    public abstract boolean handle(SlashCommandEvent event);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
