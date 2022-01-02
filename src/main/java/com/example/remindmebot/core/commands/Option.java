package com.example.remindmebot.core.commands;

import net.dv8tion.jda.api.interactions.commands.OptionType;

public record Option(OptionType type, String name, String description, boolean required) {
}
