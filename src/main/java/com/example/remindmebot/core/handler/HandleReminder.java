package com.example.remindmebot.core.handler;

import com.example.remindmebot.core.commands.CommandHandler;
import com.example.remindmebot.core.MainListener;
import com.example.remindmebot.core.commands.Option;
import com.example.remindmebot.database.Reminder;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class HandleReminder extends CommandHandler {

    private HandleReminder() {
        super("remind", "Create a new reminder", true, options());
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

            String reply = getDuration(Duration.between(LocalDateTime.now(), temp));
            event.reply(reply).setEphemeral(true).queue();

            return true;

        } catch (DateTimeParseException e) {
            event.reply("Could not parse date. Please use this format: dd.MM.yyyy HH:mm").setEphemeral(true).queue();
            return true;
        } catch (NullPointerException e) {
            event.reply("Could not find a date").setEphemeral(true).queue();
            return true;
        }
    }

    private String getDuration(Duration duration) {
        StringBuilder reply = new StringBuilder();
        reply.append("Reminding you in ");

        long days = duration.get(ChronoUnit.DAYS);
        long hours = duration.get(ChronoUnit.HOURS);
        long minutes = duration.get(ChronoUnit.MINUTES);


        if (days > 0) {
            if (days > 1) {
                reply.append(days)
                        .append(" days, ");
            } else {
                reply.append(days).append(" day, ");
            }
        }

        if (hours == 1) {
            reply.append(hours).append(" hour, and ");
        } else if (hours > 0) {
            reply.append(hours).append(" hours, and ");
        }

        if (minutes == 1) {
            reply.append(minutes).append(" minute.");
        } else {
            reply.append(minutes).append(" minutes.");
        }


        return reply.toString();
    }

    private static List<Option> options() {
        List<Option> result = new ArrayList<>();
        result.add(new Option(OptionType.STRING, "date", "dd.MM.yyyy HH:mm", true));
        result.add(new Option(OptionType.STRING, "content", "Content of your reminder", true));
        return result;
    }
}
