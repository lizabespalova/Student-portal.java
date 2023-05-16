package com.studentportal.StudentPortal.Helpbot.config;

import com.studentportal.StudentPortal.Helpbot.service.MainClasses.Helpbot;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
public class AppConfig {
    final HelpbotConfig helpbotConfig;

    public AppConfig(HelpbotConfig helpbotConfig) {
        this.helpbotConfig = helpbotConfig;
    }

    @Bean
    public Helpbot registration(){
        Helpbot bot = new Helpbot(helpbotConfig);
        try {
            new TelegramBotsApi(DefaultBotSession.class).registerBot(bot);
            System.out.println("Бот @"+bot.getBotUsername()+" успешно запущен!!!");
        } catch (Exception e){
            e.printStackTrace();
        }
        return bot;
    }
}
