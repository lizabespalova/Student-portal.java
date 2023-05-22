/*
package com.studentportal.StudentPortal.Helpbot.config;

import com.studentportal.StudentPortal.Helpbot.service.MainClasses.Helpbot;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Configuration
@EnableScheduling
@Data
@PropertySource("application.properties")
public class AppConfig {

    @Value("${telegram.help.bot.username}")
    private String username;

    @Value("${telegram.help.bot.token}")
    private String token;

    @Value("${telegram.help.bot.tokenpay}")
    private String tokenPay;


   @Bean
    public HelpbotConfig helpbotConfig() {
        HelpbotConfig config = new HelpbotConfig();
        config.setUsername(username);
        config.setToken(token);
        config.setTokenPay(tokenPay);
        return config;
    }

//   @Bean
//    public Helpbot registration(HelpbotConfig helpbotConfig) {
//
//         Helpbot bot = new Helpbot(helpbotConfig);
//        try {
//            new TelegramBotsApi(DefaultBotSession.class).registerBot(bot);
//            System.out.println("Бот @" + bot.getBotUsername() + " успешно запущен!!!");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return bot;
//    }
}
*/
