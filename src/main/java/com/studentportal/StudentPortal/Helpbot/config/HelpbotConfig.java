package com.studentportal.StudentPortal.Helpbot.config;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;



@Configuration
@EnableScheduling
@Data
@PropertySource("src/main/resources/application.properties")
public class HelpbotConfig {
    @Value("${telegram.help.bot.username}")
    private String username;

    @Value("${telegram.help.bot.token}")
    private String token;

    @Value("${telegram.help.bot.tokenpay}")
    private String tokenPay;
}
