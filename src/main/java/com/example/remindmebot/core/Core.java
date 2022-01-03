package com.example.remindmebot.core;

import com.example.remindmebot.core.commands.CommandHandler;
import com.example.remindmebot.core.commands.Option;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandCreateAction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.security.auth.login.LoginException;
import java.util.EnumSet;

@Configuration
public class Core {

    @Value("${token}")
    private String token;

    @Value("${testServer}")
    private String testServer;

    @Bean
    public JDA login(MainListener mainListener, CommandHandler... commandHandlers) throws LoginException {
        EnumSet<GatewayIntent> intents = EnumSet.of(
                GatewayIntent.GUILD_MESSAGES
        );
        JDA jda = JDABuilder.create(token, intents).addEventListeners(mainListener).build();

        jda.updateCommands().queue();

        try {
            for (CommandHandler cmd : commandHandlers) {
                CommandCreateAction tmp;

                // Initialize global commands
                if (cmd.isProduction()) {
                    tmp = jda.awaitReady().upsertCommand(cmd.getName(), cmd.getDescription());
                    for (Option option : cmd.getOptions()) {
                        tmp = tmp.addOption(option.type(), option.name(), option.description(), option.required());
                    }
                }
                // Initialize local (test) commands
                else {
                    tmp = jda.awaitReady().getGuildById(testServer).upsertCommand(cmd.getName(), cmd.getDescription());
                    for (Option option : cmd.getOptions()) {
                        tmp = tmp.addOption(option.type(), option.name(), option.description(), option.required());
                    }
                }

                //Create command
                tmp.queue();
            }
        } catch (InterruptedException e) {
            System.out.println("Initializing commands got interrupted: " + e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("Could not find guild " + testServer + ": " + e.getMessage());
        }

        return jda;
    }
}
