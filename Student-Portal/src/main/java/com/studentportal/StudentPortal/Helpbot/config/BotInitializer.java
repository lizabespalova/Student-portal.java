package com.studentportal.StudentPortal.Helpbot.config;


import com.studentportal.StudentPortal.Helpbot.service.MainClasses.Helpbot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Slf4j
@Configuration
public class BotInitializer {
    // можно удалить весь класс
//    @Bean
//    public TelegramBotsApi telegramBotsApi(Helpbot bot) throws TelegramApiException {
//        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
//        try {
//            telegramBotsApi.registerBot(bot);
//            System.out.println("Бот @" + bot.getBotUsername() + " успешно запущен!!!");
//        } catch (TelegramApiException e) {
//            log.error("Error occurred: " + e.getMessage());
//        }
//        return telegramBotsApi;
//    }

}
