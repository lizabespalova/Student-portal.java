package com.studentportal.StudentPortal.Helpbot.service.command.hasnotnullmessagecommands;

import com.studentportal.StudentPortal.Helpbot.model.CustomerRepository;
import com.studentportal.StudentPortal.Helpbot.model.RoomsRepository;
import com.studentportal.StudentPortal.Helpbot.service.consts.Quiz;
import com.studentportal.StudentPortal.Helpbot.service.consts.Text;
import com.studentportal.StudentPortal.Helpbot.service.mainclasses.Helpbot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

public class HasBackTextCommand extends HasNotNullMessageCommands{
    public HasBackTextCommand(Helpbot helpbot, CustomerRepository customerRepository, RoomsRepository roomsRepository) {
        super(helpbot, customerRepository, roomsRepository);
    }

    @Override
    public void resolve(Update update) {
        Message message = update.getMessage();
        if (customerRepository.findById(message.getChatId()).get().getState().equals(Quiz.SUBJECTMENU.toString())) {
            end_stop_menu(String.valueOf(message.getChatId()));
            set_subject_menu(String.valueOf(message.getChatId()), message);
        } else if (customerRepository.findById(message.getChatId()).get().getState().equals(Quiz.MAINMENU.toString())) {
            helpbot.set_main_menu(String.valueOf(message.getChatId()), message);
        } else if (customerRepository.findById(message.getChatId()).get().getState().equals(Quiz.TEXTDESCRIPTION.toString())) {
            set_text_description(message.getChatId(), message);
        } else if (customerRepository.findById(message.getChatId()).get().getState().equals(Quiz.FILEDESCRIPTION.toString())) {
            set_file_description(message.getChatId(), message);
            set_ready_button(String.valueOf(message.getChatId()));
        } else if (customerRepository.findById(message.getChatId()).get().getState().equals(Quiz.PRICE.toString()) || customerRepository.findById(message.getChatId()).get().getState().equals(Quiz.AGREEMENTPRICE.toString()) || customerRepository.findById(message.getChatId()).get().getState().equals(Quiz.FIXPRICE.toString())) {
            set_price_description(String.valueOf(message.getChatId()), message);
        }
    }

    @Override
    public boolean apply(Update update) {
        Message message = update.getMessage();
        String user_sms = message.getText();
        return user_sms.equals(Text.back_text);
    }
}
