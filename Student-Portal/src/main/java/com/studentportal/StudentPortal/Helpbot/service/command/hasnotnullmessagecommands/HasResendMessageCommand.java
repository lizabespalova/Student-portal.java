package com.studentportal.StudentPortal.Helpbot.service.command.hasnotnullmessagecommands;

import com.studentportal.StudentPortal.Helpbot.model.CustomerRepository;
import com.studentportal.StudentPortal.Helpbot.model.RoomsRepository;
import com.studentportal.StudentPortal.Helpbot.service.command.Commands;
import com.studentportal.StudentPortal.Helpbot.service.mainclasses.Helpbot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public class HasResendMessageCommand extends HasNotNullMessageCommands {
    public HasResendMessageCommand(Helpbot helpbot, CustomerRepository customerRepository, RoomsRepository roomsRepository) {
        super(helpbot, customerRepository, roomsRepository);
    }

    @Override
    public void resolve(Update update) {
        Message message = update.getMessage();
        if (customerRepository.findById(message.getFrom().getId()).get().getCheckDescriptionState() == 1) {
            get_text_description(message);
            if (!customerRepository.findById(message.getChatId()).get().isCheck_state()) {
                set_last_buttons(String.valueOf(message.getChatId()));
                set_post(String.valueOf(message.getChatId()), 0, message.getChatId(), message);
            } else {
                set_file_description(message.getChatId(), message);
                set_ready_button(String.valueOf(message.getChatId()));
            }
        }
    }

    @Override
    public boolean apply(Update update) {
        Message message = update.getMessage();
        return !customerRepository.findById(message.getFrom().getId()).isEmpty();
    }
}
