package com.studentportal.StudentPortal.Helpbot.service.command.callbackquerycommands;
import com.studentportal.StudentPortal.Helpbot.model.CustomerRepository;
import com.studentportal.StudentPortal.Helpbot.model.RoomsRepository;
import com.studentportal.StudentPortal.Helpbot.service.mainclasses.Helpbot;
import com.studentportal.StudentPortal.Helpbot.service.consts.Quiz;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

//@Component
public class FixpriceHasQueryCommand extends QueryCommands {
    public FixpriceHasQueryCommand(Helpbot helpbot, CustomerRepository customerRepository, RoomsRepository roomsRepository) {
        super(helpbot, customerRepository, roomsRepository);
    }
    @Override
    public void resolve(Update update) {
        var chatId = update.getCallbackQuery().getMessage().getChatId();
        set_fix_price_menu(String.valueOf(chatId),update.getCallbackQuery().getMessage());
    }

    @Override
    public boolean apply(Update update) {
        var messagetext = update.getMessage().getText();
        return messagetext.equals(Quiz.FIXPRICE.toString())
                && customerRepository.findById(update.getCallbackQuery().getMessage().getChatId()).get().getState().equals(Quiz.FILEDESCRIPTION.toString());
    }


}
