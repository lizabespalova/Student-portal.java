package com.studentportal.StudentPortal.Helpbot.service.command.callbackquerycommands;

import com.studentportal.StudentPortal.Helpbot.model.Customer;
import com.studentportal.StudentPortal.Helpbot.model.CustomerRepository;
import com.studentportal.StudentPortal.Helpbot.model.RoomsRepository;
import com.studentportal.StudentPortal.Helpbot.service.consts.Quiz;
import com.studentportal.StudentPortal.Helpbot.service.mainclasses.Helpbot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class AgreementpriceHasQueryCommand extends QueryCommands {
    public AgreementpriceHasQueryCommand(Helpbot helpbot, CustomerRepository customerRepository, RoomsRepository roomsRepository) {
        super(helpbot, customerRepository,roomsRepository);
    }

    @Override
    public void resolve(Update update) {
        var chatId = update.getCallbackQuery().getMessage().getChatId();
        set_agreement_price_menu(String.valueOf(chatId), update.getCallbackQuery().getMessage());
        set_post(String.valueOf(chatId),0,update.getCallbackQuery().getMessage().getMessageId(), update.getCallbackQuery().getMessage());
        set_last_buttons(String.valueOf(chatId));
    }

    @Override
    public boolean apply(Update update) {
        var messagetext = update.getMessage().getText();
        return messagetext.equals(Quiz.AGREEMENTPRICE.toString()) && customerRepository.findById(update.getCallbackQuery().getMessage().getChatId()).get().getState().equals(Quiz.FILEDESCRIPTION.toString());
    }
    public void set_agreement_price_menu(String chatId, Message message){
        SendMessage main_menu_sms = new SendMessage();
        main_menu_sms.setChatId(chatId);
        main_menu_sms.setText("Добре, за ціною домовитеся з виконавцем");
        Customer customer = customerRepository.findById(message.getChatId()).get();
        customer.setAgreementsState(true);
        customer.setPrice("Домовлена");
        customer.setState(String.valueOf(Quiz.FIXPRICE));
        customer.setCheck_state(true);
        customerRepository.save(customer);
        try {
            // Send the message
            helpbot.execute(main_menu_sms);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
