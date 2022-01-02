package com.example.remindmebot.core;

import com.example.remindmebot.core.commands.CommandHandler;
import com.example.remindmebot.database.Reminder;
import com.example.remindmebot.database.ReminderService;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class MainListener extends ListenerAdapter {

    static ReminderService reminderService;

    private final Set<Guild> guilds;

    private final List<CommandHandler> commandHandlers;

    public MainListener(ReminderService reminderService, CommandHandler... commandHandlers) {
        this.commandHandlers = Arrays.asList(commandHandlers);
        MainListener.reminderService = reminderService;
        guilds = new HashSet<>();

        // Setup notifier
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            List<Reminder> reminders = reminderService.getCurrentTimers();
            System.out.println("Got " + reminders.size() + " reminders");
            for (Reminder reminder : reminders) {
                for (Guild guild : guilds) {
                    try {
                        User user = Objects.requireNonNull(guild.getMemberById(reminder.getUser())).getUser();
                        user.openPrivateChannel().queue(
                                (c) -> c.sendMessage(reminder.getContent()).queue()
                        );
                        break;
                    } catch (NullPointerException ignored) {
                        System.out.println("Member not found");
                    }
                }
            }
        }, 0, 1, TimeUnit.MINUTES);
    }

    public static ReminderService getReminderService() {
        return reminderService;
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        guilds.addAll(event.getJDA().getGuilds());
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
