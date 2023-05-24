package com.studentportal.StudentPortal.Helpbot.service.command;


import com.studentportal.StudentPortal.Helpbot.model.Customer;
import com.studentportal.StudentPortal.Helpbot.model.CustomerRepository;
import com.studentportal.StudentPortal.Helpbot.service.MainClasses.Helpbot;
import com.studentportal.StudentPortal.Helpbot.service.consts.Quiz;
import com.vdurmont.emoji.EmojiParser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@RequiredArgsConstructor
public class QuizFixpriceCommand implements BotCommand {
    private final Helpbot helpbot;

    private final CustomerRepository customerRepository;

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

    private void set_fix_price_menu(String chatId, Message message){
        SendMessage main_menu_sms = new SendMessage();
        main_menu_sms.setChatId(chatId);
        var text = EmojiParser.parseToUnicode("Напишіть вартість цифрою, без копійок:");
        main_menu_sms.setText(text);
        save(message, Quiz.FIXPRICE.toString());
        try {
            // Send the message
            helpbot.execute(main_menu_sms);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    void save(Message message, String state){
        if(!customerRepository.findById(message.getChatId()).isEmpty()) {
            Customer customer = customerRepository.findById(message.getChatId()).get();
            customer.setAgreementsState(true);
            customer.setState(state);
            customer.setPriceFlag(0);
            customer.setCheck_state(true);
            customerRepository.save(customer);
        }else{
            Customer customer = new Customer();
            customer.setChatID(message.getChatId());
            customer.setSurname(message.getFrom().getLastName());
            customer.setName(message.getFrom().getFirstName());
            customer.setUser_nick(message.getFrom().getUserName());
            customer.setCheckDescriptionState(0);
            customer.setCheck_state(true);
            customer.setState(state);
            customerRepository.save(customer);
        }
    }

}
