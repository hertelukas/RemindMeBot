package com.example.remindmebot.core.handler;

import com.example.remindmebot.core.CommandHandler;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.springframework.stereotype.Service;

@Service
public class HandleReminder extends CommandHandler {

    private HandleReminder(){
        setName("remind");
    }
    @Override
    public boolean handle(SlashCommandEvent event) {
        if(!event.getName().equals(getName())) return false;

        event.reply("Reminder saved!").setEphemeral(true).queue();
        return true;
    }
}
