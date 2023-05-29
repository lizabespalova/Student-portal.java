package com.studentportal.StudentPortal.Helpbot.service.mainclasses;


import com.studentportal.StudentPortal.Helpbot.config.HelpbotConfig;
import com.studentportal.StudentPortal.Helpbot.model.Customer;
import com.studentportal.StudentPortal.Helpbot.model.CustomerRepository;
import com.studentportal.StudentPortal.Helpbot.model.Performer;
import com.studentportal.StudentPortal.Helpbot.model.PerformerRepository;
import com.studentportal.StudentPortal.Helpbot.model.Post;
import com.studentportal.StudentPortal.Helpbot.model.PostRepository;
import com.studentportal.StudentPortal.Helpbot.model.Purchase;
import com.studentportal.StudentPortal.Helpbot.model.PurchaseRepository;
import com.studentportal.StudentPortal.Helpbot.model.Rooms;
import com.studentportal.StudentPortal.Helpbot.model.RoomsRepository;
import com.studentportal.StudentPortal.Helpbot.model.Thief;
import com.studentportal.StudentPortal.Helpbot.model.ThiefRepository;

import com.studentportal.StudentPortal.Helpbot.service.command.Command;
import com.studentportal.StudentPortal.Helpbot.service.command.Command.CommandFactory;
import com.studentportal.StudentPortal.Helpbot.service.command.callbackquerycommands.BotHasQueryCommand;
import com.studentportal.StudentPortal.Helpbot.service.command.hasmessagecommands.BotHasMessageCommand;

import com.studentportal.StudentPortal.Helpbot.service.command.hasnotnullmessagecommands.BotHasNotNullMessageCommand;
import com.studentportal.StudentPortal.Helpbot.service.consts.Quiz;
import com.studentportal.StudentPortal.Helpbot.service.consts.Subjects;
import com.studentportal.StudentPortal.Helpbot.service.consts.Text;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import org.telegram.telegrambots.meta.api.methods.AnswerPreCheckoutQuery;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.payments.PreCheckoutQuery;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class Helpbot extends TelegramLongPollingBot {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private PerformerRepository performerRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private RoomsRepository roomsRepository;
    @Autowired
    private PurchaseRepository purchaseRepository;
    @Autowired
    private ThiefRepository thiefRepository;
    @Autowired
    private CommandFactory commandFactory;

    //    private Chanels chanel;
    private Text const_text;

    final HelpbotConfig config;
    private String text;
    private Subjects subjects;
    private Quiz quiz;

    @Override
    public String getBotUsername() {
        return config.getUsername();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    public String getBotTokenPay() {
        return config.getTokenPay();
    }



    public Helpbot(HelpbotConfig helpbotConfig) {
        config = helpbotConfig;
        List<BotCommand> listofCommands = new ArrayList<>();
        text = ("Меню");
        listofCommands.add(new BotCommand("/start", text));
        text = EmojiParser.parseToUnicode(":globe_with_meridians:" + " " + "Мова");
        listofCommands.add(new BotCommand("/language", text));
        try {
            this.execute(new SetMyCommands(listofCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
//        const_text= new Text();
        if (update.hasCallbackQuery()) {
            commandFactory.getCommand(update, (byte) 1).resolve(update);
        }
        if (update.hasPreCheckoutQuery()) {
            PreCheckoutQuery preCheckoutQuery = update.getPreCheckoutQuery();
            AnswerPreCheckoutQuery answerPreCheckoutQuery = new AnswerPreCheckoutQuery(preCheckoutQuery.getId(), true);
            try {
                execute(answerPreCheckoutQuery);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }

        if (message != null) {
            String user_sms = message.getText();
            if (user_sms != null) {
                switch (user_sms) {
                    case "/start": {
                        set_main_menu(String.valueOf(message.getChatId()), message);
                        //редагування коду
                        Customer customer = customerRepository.findById(message.getChatId()).get();
                        customer.setAgreementsState(true);
                        customer.setThiefListState(0);
                        customer.setCheck_state(true);
                        customer.setPhotoLink(null);
                        customer.setFileLink(null);
                        customer.setPriceFlag(1);
                        customerRepository.save(customer);
                        break;
                    }
                }
                commandFactory.getCommand(update, (byte) 2).resolve(update);
            }
        }
        if (update.hasMessage()) {
            commandFactory.getCommand(update, (byte) 3).resolve(update);
        }
    }
    public void set_main_menu(String chatId, Message message){
        SendMessage main_menu_sms = new SendMessage();
        main_menu_sms.setChatId(chatId);
        main_menu_sms.setText(EmojiParser.parseToUnicode("Оберіть дію:" + ":blush:"));
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        List<KeyboardRow> menu = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add(Text.agreement_text);
        KeyboardRow row1 = new KeyboardRow();
        row1.add(Text.thiefText);
        KeyboardRow row2 = new KeyboardRow();
        row2.add( EmojiParser.parseToUnicode(Text.performerRegister));
        menu.add(row);
        menu.add(row1);
        menu.add(row2);
        keyboard.setKeyboard(menu);
        keyboard.setResizeKeyboard(true);
        keyboard.setOneTimeKeyboard(false);
        keyboard.setSelective(true);
        main_menu_sms.setReplyMarkup(keyboard);
        if(!customerRepository.findById(message.getChatId()).isEmpty()) {
            Customer customer = customerRepository.findById(message.getChatId()).get();
            customer.setAgreementsState(true);
            customer.setState(Quiz.MAINMENU.toString());
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
            customer.setState(Quiz.MAINMENU.toString());
            customerRepository.save(customer);
        }

        try {
            // Send the message
           execute(main_menu_sms);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}






















