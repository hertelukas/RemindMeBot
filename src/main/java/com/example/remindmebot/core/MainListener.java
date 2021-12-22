package com.example.remindmebot.core;

import com.example.remindmebot.database.Reminder;
import com.example.remindmebot.database.ReminderService;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class MainListener extends ListenerAdapter {

    static ReminderService reminderService;

    private final List<Guild> guilds;

    private final List<CommandHandler> commandHandlers;

    public MainListener(ReminderService reminderService, CommandHandler... commandHandlers) {
        this.commandHandlers = Arrays.asList(commandHandlers);
        MainListener.reminderService = reminderService;
        guilds = new ArrayList<>();

        // Setup notifier
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                List<Reminder> reminders = reminderService.getCurrentTimers();
                System.out.println("Got " + reminders.size() + " reminders");
                for (Reminder reminder : reminders) {
                    // TODO: 12/22/21 Don't send twice, delete database entry
                    for (Guild guild : guilds) {
                        try {
                            Objects.requireNonNull(guild.getMemberById(reminder.getUser()))
                                    .getUser()
                                    .openPrivateChannel()
                                    .queue(
                                            (c) -> c.sendMessage(reminder.getContent()).queue() //Send the message
                                    );
                        } catch (NullPointerException ignored) {
                            System.out.println("Member not found");
                        }
                    }
                }
            }
        }, 0, 1, TimeUnit.MINUTES);
    }

    public static ReminderService getReminderService() {
        return reminderService;
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        guilds.add(event.getGuild());
    }

    @Override
    public void onSlashCommand(@NotNull SlashCommandEvent event) {
        if (!commandHandled(event)) {
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
