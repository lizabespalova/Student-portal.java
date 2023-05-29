package com.studentportal.StudentPortal.Helpbot.service.command.hasnotnullmessagecommands;

import com.studentportal.StudentPortal.Helpbot.model.CustomerRepository;
import com.studentportal.StudentPortal.Helpbot.model.RoomsRepository;
import com.studentportal.StudentPortal.Helpbot.service.consts.Text;
import com.studentportal.StudentPortal.Helpbot.service.mainclasses.Helpbot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public class HasAgreementCommand extends HasNotNullMessageCommands{
    public HasAgreementCommand(Helpbot helpbot, CustomerRepository customerRepository, RoomsRepository roomsRepository) {
        super(helpbot, customerRepository, roomsRepository);
    }

    @Override
    public void resolve(Update update) {
        Message message = update.getMessage();
        end_stop_menu(String.valueOf(message.getChatId()));
        if (!customerRepository.findById(message.getChatId()).get().isCheck_state()) {
            set_post(String.valueOf(message.getChatId()), 0, update.getCallbackQuery().getMessage().getMessageId(), message);
            set_last_buttons(String.valueOf(message.getChatId()));
        } else set_subject_menu(String.valueOf(message.getChatId()), message);
    }

    @Override
    public boolean apply(Update update) {
        Message message = update.getMessage();
        String user_sms = message.getText();
        return user_sms.equals(Text.agreement_text);
    }
}
