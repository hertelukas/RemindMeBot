package com.example.remindmebot.core;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.security.auth.login.LoginException;
import java.util.EnumSet;

@Configuration
public class Core {

    @Value("${token}")
    private String token;

    @Bean
    public JDA login(MainListener mainListener) throws LoginException {
        EnumSet<GatewayIntent> intents = EnumSet.of(
                GatewayIntent.GUILD_MESSAGES
        );
        JDA jda = JDABuilder.create(token, intents).addEventListeners(mainListener).build();

        // TODO: 12/22/21 Create global Command
        try {
            jda.awaitReady().getGuildById("725998620284944394").upsertCommand("remind", "Create a reminder").queue();
        }catch (Exception e){
            System.out.println(e);
        }
        return jda;
    }
}
