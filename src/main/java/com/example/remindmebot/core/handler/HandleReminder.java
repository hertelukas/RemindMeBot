package com.example.remindmebot.core.handler;

import com.example.remindmebot.core.CommandHandler;
import com.example.remindmebot.core.MainListener;
import com.example.remindmebot.database.Reminder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

@Service
public class HandleReminder extends CommandHandler {

    private HandleReminder() {
        setName("remind");
    }

    @Override
    public boolean handle(SlashCommandEvent event) {
        if (!event.getName().equals(getName())) return false;

        long user = event.getUser().getIdLong();
        try {
            String date = Objects.requireNonNull(event.getOption("date")).getAsString();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
            LocalDateTime temp = LocalDateTime.from(formatter.parse(date));

            // Reminder should be in the future
            if (temp.isBefore(LocalDateTime.now())) {
                event.reply("Reminder is in the past, please provide a date in the future")
                        .setEphemeral(true)
                        .queue();
                return true;
            }

            Reminder reminder = new Reminder(
                    user,
                    Objects.requireNonNull(event.getOption("content")).getAsString(),
                    Timestamp.valueOf(temp)
            );

            MainListener.getReminderService().save(reminder);

        } catch (DateTimeParseException e) {
            event.reply("Could not parse date. Please use this format: dd.MM.yyyy HH:mm").setEphemeral(true).queue();
            return true;
        } catch (NullPointerException e) {
            event.reply("Could not find a date").setEphemeral(true).queue();
            return true;
        }


        event.reply("Reminder saved!").setEphemeral(true).queue();
        return true;
    }
}
