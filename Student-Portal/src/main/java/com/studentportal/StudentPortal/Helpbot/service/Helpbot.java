package com.studentportal.StudentPortal.Helpbot.service;

import com.studentportal.StudentPortal.Helpbot.config.HelpbotConfig;
import com.studentportal.StudentPortal.Helpbot.model.*;
import com.vdurmont.emoji.EmojiParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;



@Component
public class Helpbot extends TelegramLongPollingBot {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private PerformerRepository performerRepository;
    @Autowired
    private PostRepository postRepository;
    private boolean check_state = true;
    private String performer_id;
    private Chanels chanel;
    private Text const_text;
    final HelpbotConfig config;
    private String text;
    private Subjects subjects;
    private Quiz quiz;
    private long customers_id;
    private String price;
    private long performers_id;

    @Override
    public String getBotUsername() {
        return config.getUsername();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    public Helpbot(HelpbotConfig config) {
        this.config = config;
        List<BotCommand> listofCommands = new ArrayList<>();
        text = ("Меню");
        listofCommands.add(new BotCommand("/start", text));
        text = EmojiParser.parseToUnicode(":globe_with_meridians:"+" "+"Мова");
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
        const_text= new Text();
        if (update.hasCallbackQuery()) {
          /*  Quiz quiz = null ;*/
            long chatID = update.getCallbackQuery().getMessage().getChatId();
            String messagetext = update.getCallbackQuery().getData();
/*
            messageID = update.getCallbackQuery().getMessage().getMessageId();
*/
            if(messagetext.equals(subjects.MATH.toString())||messagetext.equals(subjects.PROGRAMMING.toString())||
                    messagetext.equals(subjects.PHYLOSOPHY.toString())||messagetext.equals(subjects.GEOGRAPHY.toString())||
                    messagetext.equals(subjects.LANGUAGES.toString())||messagetext.equals(subjects.ANOTHER.toString())||
                    messagetext.equals(subjects.CHEMISTRY.toString())|| messagetext.equals(subjects.MEDICINE.toString())){
                Subjectgetters subjectgetters = new Subjectgetters();
                String strsubject = subjectgetters.getsubject(messagetext);
                register_to_data_base_customer(update.getCallbackQuery().getMessage());

                setBranchToTable(update.getCallbackQuery().getMessage(),strsubject);
                if(!check_state){set_post(String.valueOf(chatID),0,update.getCallbackQuery().getMessage().getMessageId(),update.getCallbackQuery().getMessage()); set_last_buttons(String.valueOf(chatID));}
                else set_text_description(chatID, update.getCallbackQuery().getMessage());
            }
            else if (messagetext.equals(quiz.FIXPRICE.toString()) && customerRepository.findById(update.getCallbackQuery().getMessage().getChatId()).get().getState().equals(quiz.FILEDESCRIPTION.toString())) {
                set_fix_price_menu(String.valueOf(chatID),update.getCallbackQuery().getMessage());
            }
            else if (messagetext.equals(quiz.AGREEMENTPRICE.toString()) && customerRepository.findById(update.getCallbackQuery().getMessage().getChatId()).get().getState().equals(quiz.FILEDESCRIPTION.toString())) {
                set_agreement_price_menu(String.valueOf(chatID), update.getCallbackQuery().getMessage());
                set_post(String.valueOf(chatID),0,update.getCallbackQuery().getMessage().getMessageId(), update.getCallbackQuery().getMessage());
                set_last_buttons(String.valueOf(chatID));
            }
            else if (messagetext.equals(subjects.CHANGE.toString())) {
                set_change_menu(String.valueOf(chatID));
            }
            else if (messagetext.equals(subjects.PUBLIC.toString())) {
/*
                register_to_data_base_customer(update.getCallbackQuery().getMessage());
*/
                chanel = new Chanels();
               if (customerRepository.findById(update.getCallbackQuery().getMessage().getChatId()).get().getBranch()==null){
                  set_warning(String.valueOf(chatID));
                }
               else if(customerRepository.findById(update.getCallbackQuery().getMessage().getChatId()).get().getBranch().equals(const_text.getMatem_text())){
                    try {
                        String post_url = chanel.set_to_matem_chanal(customerRepository, getBotToken(), update.getCallbackQuery().getMessage(), postRepository);

                        setPostLinkToTable(update,post_url);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
               }
               else if(customerRepository.findById(update.getCallbackQuery().getMessage().getChatId()).get().getBranch().equals(const_text.getProgramming_text())){
                   try {
                       String post_url =  chanel.set_to_programm_chanal(customerRepository, getBotToken(), update.getCallbackQuery().getMessage(), postRepository);

                       setPostLinkToTable(update,post_url);
                   } catch (IOException e) {
                       throw new RuntimeException(e);
                   }
               }
               else if(customerRepository.findById(update.getCallbackQuery().getMessage().getChatId()).get().getBranch().equals(const_text.getChemistry_text())){
                   try {
                       String post_url = chanel.set_to_chemistry_chanal(customerRepository, getBotToken(), update.getCallbackQuery().getMessage(), postRepository);

                       setPostLinkToTable(update,post_url);
                   } catch (IOException e) {
                       throw new RuntimeException(e);
                   }
               }
               else if(customerRepository.findById(update.getCallbackQuery().getMessage().getChatId()).get().getBranch().equals(const_text.getMedicine_text())){
                   try {
                       String post_url = chanel.set_to_medic_chanal(customerRepository, getBotToken(),update.getCallbackQuery().getMessage(), postRepository);
                       /*customerData.setPost_url(post_url);*/
                       setPostLinkToTable(update,post_url);
                   } catch (IOException e) {
                       throw new RuntimeException(e);
                   }
               }
               else if(customerRepository.findById(update.getCallbackQuery().getMessage().getChatId()).get().getBranch().equals(const_text.getGeographe_text())){
                   try {
                       String post_url=chanel.set_to_geography_chanal(customerRepository, getBotToken(), update.getCallbackQuery().getMessage(), postRepository);
                       /*customerData.setPost_url(post_url);*/
                       setPostLinkToTable(update,post_url);
                   } catch (IOException e) {
                       throw new RuntimeException(e);
                   }
               }
               else if(customerRepository.findById(update.getCallbackQuery().getMessage().getChatId()).get().getBranch().equals(const_text.getPhylosophy_text())){
                   try {
                       String post_url= chanel.set_to_phylosophy_chanal(customerRepository, getBotToken(), update.getCallbackQuery().getMessage(), postRepository);
                      /* customerData.setPost_url(post_url);*/
                       setPostLinkToTable(update,post_url);
                   } catch (IOException e) {
                       throw new RuntimeException(e);
                   }
               }
               else if(customerRepository.findById(update.getCallbackQuery().getMessage().getChatId()).get().getBranch().equals(const_text.getLanguages_text())){
                   try {
                       String post_url= chanel.set_to_language_chanal(customerRepository, getBotToken(), update.getCallbackQuery().getMessage(), postRepository);
                       /*customerData.setPost_url(post_url);*/
                       setPostLinkToTable(update,post_url);
                   } catch (IOException e) {
                       throw new RuntimeException(e);
                   }
               }
               else if(customerRepository.findById(update.getCallbackQuery().getMessage().getChatId()).get().getBranch().equals(const_text.getAnother_text())){
                   try {
                       String post_url= chanel.set_to_main_chanal(customerRepository, getBotToken(), update.getCallbackQuery().getMessage(), postRepository);
                      /* customerData.setPost_url(post_url);*/
                       setPostLinkToTable(update,post_url);
                   } catch (IOException e) {
                       throw new RuntimeException(e);
                   }
               }

               set_post(String.valueOf(chatID),1,update.getCallbackQuery().getMessage().getMessageId(), update.getCallbackQuery().getMessage());
                set_main_menu(String.valueOf(chatID), update.getCallbackQuery().getMessage());
            }
            else if (messagetext.equals("Візьму")){
               performer_check(update.getCallbackQuery().getFrom(),update.getCallbackQuery().getId());
                    set_information_about_performer(update.getCallbackQuery().getFrom(), postRepository.findById(update.getCallbackQuery().getMessage().getMessageId()).get().getCustomer_id(), update);
            }
            else if (messagetext.equals(subjects.CANCEL.toString())){
               bargain_cancel(chatID, update);
            }
            //Згода на угоду
            else if (Character.isDigit(messagetext.charAt(0))){
                boolean flag = true;
                String performerID="";
                String postID="";
                for (int i = 0; i < messagetext.length(); i++) {
                    char chrs = messagetext.charAt(i);
                    if (Character.isDigit(chrs) && flag) {
                        postID = postID + chrs;
                    }
                    if (Character.isDigit(chrs) && !flag) {
                        performerID = performerID + chrs;
                    }
                    if(chrs == ',') {
                        flag=false;
                    }

                }

                return_chat_link_and_show_sms_in_group(chatID, update);
                return_chat_link_and_show_sms_for_performer_in_group(update, performerID, postID);
                try {
                    set_sms_in_chat();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            else if (messagetext.equals(subjects.PERFORMER_REGISTER.toString())) {
                register_to_data_base_performer(update.getCallbackQuery().getFrom(), String.valueOf(chatID));
            }
            else if (messagetext.equals("PRICE")) {
                try {
                    fix_finish_text_price_customer(update.getCallbackQuery().getMessage());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            else if (messagetext.equals("CLOSE")) {
            }
            else if (messagetext.equals("YES")) {
            }
            else if (messagetext.equals("NO")) {
                try {
                    check_performer_return_no(update.getCallbackQuery().getFrom().getId(),price);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        if(update.hasMessage()) {
            if (update.getMessage().hasDocument() && customerRepository.findById(message.getChatId()).get().getState().equals( quiz.TEXTDESCRIPTION.toString())) {
                const_text = new Text();
                try {
                   get_file_description(update);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            } else if (update.hasMessage() && update.getMessage().hasPhoto() && customerRepository.findById(message.getChatId()).get().getState().equals( quiz.TEXTDESCRIPTION.toString())) {
                get_photo_description(update);
            }
        }
        if(message!=null){
          /*  bot_chat_ID = String.valueOf(message.getChatId());*/
        String user_sms = message.getText();

        if(user_sms!=null) {
            const_text = new Text();
            if (user_sms.equals(const_text.getAgreement_text())) {

                end_stop_menu(String.valueOf(message.getChatId()));
                if (!check_state) {
                    set_post(String.valueOf(message.getChatId()),0,update.getCallbackQuery().getMessage().getMessageId(), message);
                    set_last_buttons(String.valueOf(message.getChatId()));
                } else set_subject_menu(String.valueOf(message.getChatId()),message);
//
            } else if (user_sms.equals(const_text.getBack_text())) {
                if (customerRepository.findById(message.getChatId()).get().getState().equals(quiz.SUBJECTMENU.toString())) {
                    end_stop_menu(String.valueOf(message.getChatId()));
                    set_subject_menu(String.valueOf(message.getChatId()),message);

                } else if (customerRepository.findById(message.getChatId()).get().getState().equals(quiz.MAINMENU.toString())) {
                    set_main_menu(String.valueOf(message.getChatId()),message);
                } else if (customerRepository.findById(message.getChatId()).get().getState().equals(quiz.TEXTDESCRIPTION.toString())) {
                    set_text_description(message.getChatId(), message);
                } else if (customerRepository.findById(message.getChatId()).get().getState().equals(quiz.FILEDESCRIPTION.toString())) {
                    set_file_description(message.getChatId(), message);
                    set_ready_button(String.valueOf(message.getChatId()));
                } else if (customerRepository.findById(message.getChatId()).get().getState().equals(quiz.PRICE.toString()) || customerRepository.findById(message.getChatId()).get().getState().equals(quiz.AGREEMENTPRICE.toString()) || customerRepository.findById(message.getChatId()).get().getState().equals(quiz.FIXPRICE.toString())) {
                    set_price_description(String.valueOf(message.getChatId()), message);
                }
            } else if (user_sms.equals(const_text.getEnd_text())) {
                set_main_menu(String.valueOf(message.getChatId()), message);
                check_state = true;
            } else if (customerRepository.findById(message.getChatId()).get().getCheckDescriptionState()!=null) {
                if (customerRepository.findById(message.getChatId()).get().getCheckDescriptionState()==1){
                    get_text_description(message);
                if (!check_state) {
                    set_post(String.valueOf(message.getChatId()), 0, update.getCallbackQuery().getMessage().getMessageId(), message);
                    set_last_buttons(String.valueOf(message.getChatId()));
                } else {
                    set_file_description(message.getChatId(), message);
                    set_ready_button(String.valueOf(message.getChatId()));
                }
            }
            } else if (user_sms.equals(const_text.getReady_text()) && customerRepository.findById(message.getChatId()).get().getState().equals(quiz.TEXTDESCRIPTION.toString())) {
                end_stop_menu(String.valueOf(message.getChatId()));

                try {
                    if (customerRepository.findById(message.getChatId()).get().getPhotoLink()!= null) set_photo_to_channel_and_return_link(message);
                    if (customerRepository.findById(message.getChatId()).get().getFileLink() != null) set_file_to_channel_and_return_link(message);
                    if (customerRepository.findById(message.getChatId()).get().getPhotoLink() == null)  customerRepository.findById(message.getChatId()).get().setPhotoLink(null);
                    if (customerRepository.findById(message.getChatId()).get().getFileLink()  == null) /*customerData.setFile_url(null)*/customerRepository.findById(message.getChatId()).get().setFileLink(null);
                    /*url_photo = null;
                    url_file = null;*/
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if (!check_state) {
                    set_post(String.valueOf(message.getChatId()),0,update.getCallbackQuery().getMessage().getMessageId(), message);
                    set_last_buttons(String.valueOf(message.getChatId()));
                } else set_price_description(String.valueOf(message.getChatId()), message);
            } else if (user_sms.equals(const_text.getChange_subject())) {
                set_subject_menu(String.valueOf(message.getChatId()),message);
                end_stop_menu(String.valueOf(message.getChatId()));
            } else if (user_sms.equals(const_text.getChange_description())) {
                    set_text_description(message.getChatId(), message);
                    end_stop_menu(String.valueOf(message.getChatId()));
            } else if (user_sms.equals(const_text.getChange_photo_or_file())) {
                List photo_list = new ArrayList();
                List file_list=new ArrayList<>();
               save_new_links(photo_list, file_list, message);
                set_file_description(message.getChatId(), message);
                set_ready_button(String.valueOf(message.getChatId()));
            } else if (user_sms.equals(const_text.getChange_price())) {
                set_price_description(String.valueOf(message.getChatId()), message);
            }else if (user_sms.equals(const_text.getPerformerRegister())) {
                set_button_register_performer(String.valueOf(message.getChatId()));
            }
            else if (customerRepository.findById(message.getChatId()).get().getPriceFlag()!=null) {
                if (customerRepository.findById(message.getChatId()).get().getPriceFlag() == 1) {
/*
               flag=false;
*/
                    Customer customer = new Customer();
                    customer.setChatID(customerRepository.findById(message.getChatId()).get().getChatID());
                    customer.setSurname(customerRepository.findById(message.getChatId()).get().getSurname());
                    customer.setName(customerRepository.findById(message.getChatId()).get().getName());
                    customer.setUser_nick(customerRepository.findById(message.getChatId()).get().getUser_nick());
                    customer.setBranch(customerRepository.findById(message.getChatId()).get().getBranch());
                    customer.setAgreementsState(true);
                    customer.setDescription(customerRepository.findById(message.getChatId()).get().getDescription());
                    customer.setFileLink(customerRepository.findById(message.getChatId()).get().getFileLink());
                    customer.setPhotoLink(customerRepository.findById(message.getChatId()).get().getPhotoLink());
                    customer.setPrice(customerRepository.findById(message.getChatId()).get().getPrice());
                    customer.setPostLink(customerRepository.findById(message.getChatId()).get().getPostLink());
                    customer.setState(customerRepository.findById(message.getChatId()).get().getState());
                    customer.setPriceFlag(0);
                    customerRepository.save(customer);
                    try {
                        check_customers_price(user_sms, message);
                        price = user_sms;
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            else if (customerRepository.findById(message.getChatId()).get().getState()!=null) {
                if (customerRepository.findById(message.getChatId()).get().getState().equals(quiz.FIXPRICE.toString())) {
                boolean check_price = debug_fix_price(user_sms, message.getChatId(), message);
                if (check_price) {
                    set_post(String.valueOf(message.getChatId()), 0, update.getCallbackQuery().getMessage().getMessageId(), message);
                    set_last_buttons(String.valueOf(message.getChatId()));
                } else {
                    set_fix_price_menu(String.valueOf(message.getChatId()), message);
                }
                check_state = false;
            }
            }

        }
            switch (user_sms) {
                    case "/start": {
                        set_main_menu(String.valueOf(message.getChatId()),message);
                        check_state = true;
                        break;
                    }
                }
        }
    }
    public void set_main_menu(String chatId, Message message){
        SendMessage main_menu_sms = new SendMessage();
        main_menu_sms.setChatId(chatId);
        text = EmojiParser.parseToUnicode("Оберіть дію:" + ":blush:");
        main_menu_sms.setText(text);
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        List<KeyboardRow> menu = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        text = EmojiParser.parseToUnicode("Зробити оголошення"+":woman_student:");
        row.add(text);
        KeyboardRow row1 = new KeyboardRow();
        text = EmojiParser.parseToUnicode("Шахраї"+":octopus:");
        row1.add(text);
        text = EmojiParser.parseToUnicode("Активні виконавці"+":+1:");
        row1.add(text);
        KeyboardRow row2 = new KeyboardRow();
        text = EmojiParser.parseToUnicode(const_text.getPerformerRegister());
        row2.add(text);
        menu.add(row);
        menu.add(row1);
        menu.add(row2);
        keyboard.setKeyboard(menu);
        keyboard.setResizeKeyboard(true);
        keyboard.setOneTimeKeyboard(false);
        keyboard.setSelective(true);
        main_menu_sms.setReplyMarkup(keyboard);
       /* quiz = quiz.MAINMENU;*/

        save(message, quiz.MAINMENU.toString());
        try {
            // Send the message
            execute(main_menu_sms);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    public void set_subject_menu(String chatId, Message message){
      /*  const_text = new Text();*/
        SendMessage main_menu_sms = new SendMessage();
        main_menu_sms.setChatId(chatId);
        main_menu_sms.setText("Обери галузь:");
        InlineKeyboardMarkup inline_keybord = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows_inline = new ArrayList<>();



        List<InlineKeyboardButton> row_inline=new ArrayList<>();
        var programming_Button = new InlineKeyboardButton();
        programming_Button.setText(const_text.getProgramming_text());
        programming_Button.setCallbackData(subjects.PROGRAMMING.toString());
        var math_Button = new InlineKeyboardButton();
        math_Button.setText(const_text.getMatem_text());
        math_Button.setCallbackData(subjects.MATH.toString());
        row_inline.add(programming_Button);
        row_inline.add(math_Button);


        List<InlineKeyboardButton> row1_inline=new ArrayList<>();
        var medicine_button = new InlineKeyboardButton();
        medicine_button.setText(const_text.getMedicine_text());
        medicine_button.setCallbackData(subjects.MEDICINE.toString());
        var chemistry_button = new InlineKeyboardButton();
        chemistry_button.setText(const_text.getChemistry_text());
        chemistry_button.setCallbackData(subjects.CHEMISTRY.toString());
        row1_inline.add(medicine_button);
        row1_inline.add(chemistry_button);

        List<InlineKeyboardButton> row2_inline=new ArrayList<>();
        var phylosophy_button = new InlineKeyboardButton();
        phylosophy_button.setText(const_text.getPhylosophy_text());
        phylosophy_button.setCallbackData(subjects.PHYLOSOPHY.toString());
        var language_button = new InlineKeyboardButton();
        language_button.setText(const_text.getLanguages_text());
        language_button.setCallbackData(subjects.LANGUAGES.toString());
        row2_inline.add(phylosophy_button);
        row2_inline.add(language_button);


        List<InlineKeyboardButton> row3_inline=new ArrayList<>();
        var geography_button = new InlineKeyboardButton();
        geography_button.setText(const_text.getGeographe_text());
        geography_button.setCallbackData(subjects.GEOGRAPHY.toString());
        var another_button = new InlineKeyboardButton();
        another_button.setText(const_text.getAnother_text());
        another_button.setCallbackData(subjects.ANOTHER.toString());
        row3_inline.add(geography_button);
        row3_inline.add(another_button);

        rows_inline.add(row_inline);
        rows_inline.add(row1_inline);
        rows_inline.add(row2_inline);
        rows_inline.add(row3_inline);
        inline_keybord.setKeyboard(rows_inline);
        main_menu_sms.setReplyMarkup(inline_keybord);
       /* quiz= quiz.MAINMENU;*/
        save(message, quiz.MAINMENU.toString());
        /*customerData=new CustomerData();*/
        try{
            execute(main_menu_sms);
        }catch(TelegramApiException e){
            e.printStackTrace();
        }
    }
    public void end_stop_menu(String chatId){
        SendMessage main_menu_sms = new SendMessage();
        main_menu_sms.setChatId(chatId);
        text = EmojiParser.parseToUnicode(":+1:");
        main_menu_sms.setText(text);
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        List<KeyboardRow> menu = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        text = EmojiParser.parseToUnicode(":back:"+"Назад");
        row1.add(text);
        text = EmojiParser.parseToUnicode("Відмінити"+":x:");
        row1.add(text);
        menu.add(row1);
        keyboard.setKeyboard(menu);
        keyboard.setResizeKeyboard(true);
        keyboard.setOneTimeKeyboard(false);
        keyboard.setSelective(true);
        main_menu_sms.setReplyMarkup(keyboard);
        try {
            // Send the message
            execute(main_menu_sms);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    public void set_text_description(long chatId, Message message){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Опишіть своє завдання (без фото або файлів)");
        sendMessage.setChatId(chatId);
       /* quiz=quiz.SUBJECTMENU;*/
        save(message, quiz.SUBJECTMENU.toString());
        Customer customer = new Customer();
        customer.setChatID(customerRepository.findById(message.getChatId()).get().getChatID());
        customer.setSurname(customerRepository.findById(message.getChatId()).get().getSurname());
        customer.setName(customerRepository.findById(message.getChatId()).get().getName());
        customer.setUser_nick(customerRepository.findById(message.getChatId()).get().getUser_nick());
        customer.setBranch(customerRepository.findById(message.getChatId()).get().getBranch());
        customer.setAgreementsState(true);
        customer.setDescription(customerRepository.findById(message.getChatId()).get().getDescription());
        customer.setCheckDescriptionState(1);
        customer.setFileLink(customerRepository.findById(message.getChatId()).get().getFileLink());
        customer.setPhotoLink(customerRepository.findById(message.getChatId()).get().getPhotoLink());
        customer.setPrice(customerRepository.findById(message.getChatId()).get().getPrice());
        customer.setState(customerRepository.findById(message.getChatId()).get().getState());
        customer.setPriceFlag(customerRepository.findById(message.getChatId()).get().getPriceFlag());
        customerRepository.save(customer);
        try{
            execute(sendMessage);
        }catch(TelegramApiException e){
            e.printStackTrace();
        }
    }
    public void get_text_description(Message message){

        String user_text_description = message.getText();
        /*SendMessage check = new SendMessage();
        check.setChatId(message.getChatId());
        check.setText(user_text_description);*/
    /* *//*   customerData.setDescription(user_text_description);*//*
        customerRepository.findById(message.getChatId()).get().setDescription(user_text_description);*/
        Customer customer = new Customer();
        customer.setChatID(customerRepository.findById(message.getChatId()).get().getChatID());
        customer.setSurname(customerRepository.findById(message.getChatId()).get().getSurname());
        customer.setName(customerRepository.findById(message.getChatId()).get().getName());
        customer.setUser_nick(customerRepository.findById(message.getChatId()).get().getUser_nick());
        customer.setBranch(customerRepository.findById(message.getChatId()).get().getBranch());
        customer.setAgreementsState(true);
        customer.setPrice(customerRepository.findById(message.getChatId()).get().getPrice());
        customer.setDescription(user_text_description);
        customer.setCheckDescriptionState(0);
        customerRepository.save(customer);
        /*check_txt_description=false;*/
/*
        try{
            execute(check);
        }catch(TelegramApiException e){
            e.printStackTrace();
        }*/
    }
    public void set_file_description(long chatId, Message message){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Якщо є фото або файл, то відправте");
        sendMessage.setChatId(chatId);
        save(message, quiz.TEXTDESCRIPTION.toString());
       /* quiz=quiz.TEXTDESCRIPTION;*/
        try{
            execute(sendMessage);
        }catch(TelegramApiException e){
            e.printStackTrace();
        }

    }
    public void set_ready_button(String chatId){
        SendMessage main_menu_sms = new SendMessage();
        main_menu_sms.setChatId(chatId);
        text = "Коли відправите всі файли/фото, то натисніть <Готово>";
        main_menu_sms.setText(text);
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        List<KeyboardRow> menu = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add(const_text.getReady_text());
        menu.add(row1);
        keyboard.setKeyboard(menu);
        keyboard.setResizeKeyboard(true);
        keyboard.setOneTimeKeyboard(false);
        keyboard.setSelective(true);
        main_menu_sms.setReplyMarkup(keyboard);
        try {
            // Send the message
            execute(main_menu_sms);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    public void get_file_description(Update update) throws IOException {
        String file_id = update.getMessage().getDocument().getFileId();
        String urlString = "https://api.telegram.org/bot5814824968:AAEZRhb1emGeCJJ2cgDFTXwRF-d4fdbw1w8/sendDocument?chat_id=@vedmedik_base&document="+file_id;
        String chatId = "@vedmedik_base";
        urlString = String.format(urlString, getBotToken(), chatId, file_id);
        Customer customer = new Customer();
        customer.setChatID(customerRepository.findById(update.getMessage().getChatId()).get().getChatID());
        customer.setSurname(customerRepository.findById(update.getMessage().getChatId()).get().getSurname());
        customer.setName(customerRepository.findById(update.getMessage().getChatId()).get().getName());
        customer.setUser_nick(customerRepository.findById(update.getMessage().getChatId()).get().getUser_nick());
        customer.setBranch(customerRepository.findById(update.getMessage().getChatId()).get().getBranch());
        customer.setAgreementsState(true);
        customer.setDescription(customerRepository.findById(update.getMessage().getChatId()).get().getDescription());
        customer.setPhotoLink(customerRepository.findById(update.getMessage().getChatId()).get().getPhotoLink());
        List fileList = customerRepository.findById(update.getMessage().getChatId()).get().getFileLink();
        fileList.add(urlString);
        customer.setFileLink(fileList);
        customer.setPrice(customerRepository.findById(update.getMessage().getChatId()).get().getPrice());
        customer.setPostLink(customerRepository.findById(update.getMessage().getChatId()).get().getPostLink());
        customer.setState(customerRepository.findById(update.getMessage().getChatId()).get().getState());
        customer.setPriceFlag(0);
        customerRepository.save(customer);
    }
    public void set_file_to_channel_and_return_link(Message message)throws IOException{
        List file_url_list = new ArrayList();
        for (int j=0; j<customerRepository.findById(message.getChatId()).get().getFileLink().size();j++) {
            URL newurl = new URL((String) customerRepository.findById(message.getChatId()).get().getFileLink().get(j));
            URLConnection conn = newurl.openConnection();
            StringBuilder sb = new StringBuilder();
            InputStream is = new BufferedInputStream(conn.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String inputLine = "";
            String digits = "";
            int check = 0;
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
                for (int i = 0; i < inputLine.length(); i++) {
                    char chrs = inputLine.charAt(i);
                    if (Character.isDigit(chrs)) {
                        digits = digits + chrs;

                    }
                    if (chrs == ',') {
                        check++;
                    }
                    if (check == 2) {
                        break;
                    }
                }
                if (check == 2) {
                    break;
                }
            }

            String url = "https://t.me/vedmedik_base/" + digits;
            file_url_list.add(url);
        }
       /* customerData.setFile_url(file_url_list);*/
        Customer customer = new Customer();
        customer.setChatID(customerRepository.findById(message.getChatId()).get().getChatID());
        customer.setSurname(customerRepository.findById(message.getChatId()).get().getSurname());
        customer.setName(customerRepository.findById(message.getChatId()).get().getName());
        customer.setUser_nick(customerRepository.findById(message.getChatId()).get().getUser_nick());
        customer.setBranch(customerRepository.findById(message.getChatId()).get().getBranch());
        customer.setAgreementsState(true);
        customer.setPrice(customerRepository.findById(message.getChatId()).get().getPrice());
        customer.setDescription(customerRepository.findById(message.getChatId()).get().getDescription());
        customer.setFileLink(file_url_list);
        customer.setPhotoLink(customerRepository.findById(message.getChatId()).get().getPhotoLink());
       /* customerRepository.saveAll(file_url_list);*/
        customerRepository.save(customer);
    }
    public void get_photo_description(Update update) {
        String photo_id=update.getMessage().getPhoto().get(3).getFileId();
        String urlString = "https://api.telegram.org/bot5814824968:AAEZRhb1emGeCJJ2cgDFTXwRF-d4fdbw1w8/sendPhoto?chat_id=@vedmedik_base&photo="+photo_id;
        String chatId = "@vedmedik_base";
        urlString = String.format(urlString, getBotToken(), chatId, photo_id);
        Customer customer = new Customer();
        customer.setChatID(customerRepository.findById(update.getMessage().getChatId()).get().getChatID());
        customer.setSurname(customerRepository.findById(update.getMessage().getChatId()).get().getSurname());
        customer.setName(customerRepository.findById(update.getMessage().getChatId()).get().getName());
        customer.setUser_nick(customerRepository.findById(update.getMessage().getChatId()).get().getUser_nick());
        customer.setBranch(customerRepository.findById(update.getMessage().getChatId()).get().getBranch());
        customer.setAgreementsState(true);
        customer.setDescription(customerRepository.findById(update.getMessage().getChatId()).get().getDescription());
        customer.setFileLink(customerRepository.findById(update.getMessage().getChatId()).get().getFileLink());
        List photoList = customerRepository.findById(update.getMessage().getChatId()).get().getPhotoLink();
        photoList.add(urlString);
        customer.setPhotoLink(photoList);
        customer.setPrice(customerRepository.findById(update.getMessage().getChatId()).get().getPrice());
        customer.setPostLink(customerRepository.findById(update.getMessage().getChatId()).get().getPostLink());
        customer.setState(customerRepository.findById(update.getMessage().getChatId()).get().getState());
        customer.setPriceFlag(0);
        customerRepository.save(customer);
    }
    public void set_photo_to_channel_and_return_link(Message message)throws IOException{
        List photo_url_list = new ArrayList();
         for (int j=0; j<customerRepository.findById(message.getChatId()).get().getPhotoLink().size();j++) {
             URL newurl = new URL((String) customerRepository.findById(message.getChatId()).get().getPhotoLink().get(j));
             URLConnection conn = newurl.openConnection();
             StringBuilder sb = new StringBuilder();
             InputStream is = new BufferedInputStream(conn.getInputStream());
             BufferedReader br = new BufferedReader(new InputStreamReader(is));
             String inputLine = "";
             String digits = "";
             int check = 0;
             while ((inputLine = br.readLine()) != null) {
                 sb.append(inputLine);
                 for (int i = 0; i < inputLine.length(); i++) {
                     char chrs = inputLine.charAt(i);
                     if (Character.isDigit(chrs)) {
                         digits = digits + chrs;

                     }
                     if (chrs == ',') {
                         check++;
                     }
                     if (check == 2) {
                         break;
                     }
                 }
                 if (check == 2) {
                     break;
                 }
             }
             String url = "https://t.me/vedmedik_base/"+ digits;
             photo_url_list.add(url);

         }
       /* customerData.setPhoto_url(photo_url_list);*/
        Customer customer = new Customer();
        customer.setChatID(customerRepository.findById(message.getChatId()).get().getChatID());
        customer.setSurname(customerRepository.findById(message.getChatId()).get().getSurname());
        customer.setName(customerRepository.findById(message.getChatId()).get().getName());
        customer.setUser_nick(customerRepository.findById(message.getChatId()).get().getUser_nick());
        customer.setBranch(customerRepository.findById(message.getChatId()).get().getBranch());
        customer.setAgreementsState(true);
        customer.setDescription(customerRepository.findById(message.getChatId()).get().getDescription());
        customer.setFileLink(customerRepository.findById(message.getChatId()).get().getFileLink());
        customer.setPrice(customerRepository.findById(message.getChatId()).get().getPrice());
        customer.setPhotoLink(photo_url_list);
        customerRepository.save(customer);
    }
    public void set_price_description(String chatId, Message message){
        SendMessage main_menu_sms = new SendMessage();
        main_menu_sms.setChatId(chatId);
        main_menu_sms.setText("Укажіть як ви хочете зробити угоду - з фіксованою ціною або домовитися з виконавцем:");
        InlineKeyboardMarkup inline_keybord = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows_inline = new ArrayList<>();



        List<InlineKeyboardButton> row_inline=new ArrayList<>();
        var fix_Button = new InlineKeyboardButton();
        fix_Button.setText(const_text.getFix_price_text());
        fix_Button.setCallbackData(quiz.FIXPRICE.toString());
        var agreement_price_Button = new InlineKeyboardButton();
        agreement_price_Button.setText(const_text.getAgreement_price_text());
        agreement_price_Button.setCallbackData(quiz.AGREEMENTPRICE.toString());
        row_inline.add(fix_Button);
        row_inline.add(agreement_price_Button);
        rows_inline.add(row_inline);
        inline_keybord.setKeyboard(rows_inline);
        main_menu_sms.setReplyMarkup(inline_keybord);
        /*quiz=quiz.FILEDESCRIPTION;*/
        save(message, quiz.FILEDESCRIPTION.toString());
        try{
            execute(main_menu_sms);
        }catch(TelegramApiException e){
            e.printStackTrace();
        }
    }
    public void set_fix_price_menu(String chatId, Message message){
        SendMessage main_menu_sms = new SendMessage();
        main_menu_sms.setChatId(chatId);
        text = EmojiParser.parseToUnicode("Напишіть вартість цифрою, без копійок:");
        main_menu_sms.setText(text);
        /*quiz=quiz.FIXPRICE;*/
        save(message, quiz.FIXPRICE.toString());
        try {
            // Send the message
            execute(main_menu_sms);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    public boolean debug_fix_price(String user_price, long chatId, Message message){

        try {
            int check_num = Integer.parseInt(user_price);
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText("Ціну зафіксовано, вона дорівнює: " + check_num);
            sendMessage.setChatId(chatId);
           /* customerData.setPrice(check_num);*/
            Customer customer = new Customer();
            customer.setChatID(customerRepository.findById(message.getChatId()).get().getChatID());
            customer.setSurname(customerRepository.findById(message.getChatId()).get().getSurname());
            customer.setName(customerRepository.findById(message.getChatId()).get().getName());
            customer.setUser_nick(customerRepository.findById(message.getChatId()).get().getUser_nick());
            customer.setBranch(customerRepository.findById(message.getChatId()).get().getBranch());
            customer.setAgreementsState(true);
            customer.setDescription(customerRepository.findById(message.getChatId()).get().getDescription());
            customer.setFileLink(customerRepository.findById(message.getChatId()).get().getFileLink());
            customer.setPhotoLink(customerRepository.findById(message.getChatId()).get().getPhotoLink());
            customer.setPrice(String.valueOf(check_num));
            customerRepository.save(customer);
            /*quiz=quiz.FIXPRICE;*/
            customer.setState(String.valueOf(quiz.FIXPRICE));
            try {
                // Send the message
                execute(sendMessage);
                return true;
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }catch(NumberFormatException nfe){
         SendMessage sendMessage = new SendMessage();
            sendMessage.setText("Ви відправили не число");
            sendMessage.setChatId(chatId);
            try {
                // Send the message
                execute(sendMessage);
                return false;

            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
    public void set_agreement_price_menu(String chatId, Message message){
        SendMessage main_menu_sms = new SendMessage();
        main_menu_sms.setChatId(chatId);
        text = EmojiParser.parseToUnicode("Добре, за ціною домовитеся з виконавцем");
        main_menu_sms.setText(text);
        text = "Домовлена";
     /*   quiz=quiz.AGREEMENTPRICE;*/
        Customer customer = new Customer();
        customer.setChatID(customerRepository.findById(message.getChatId()).get().getChatID());
        customer.setSurname(customerRepository.findById(message.getChatId()).get().getSurname());
        customer.setName(customerRepository.findById(message.getChatId()).get().getName());
        customer.setUser_nick(customerRepository.findById(message.getChatId()).get().getUser_nick());
        customer.setBranch(customerRepository.findById(message.getChatId()).get().getBranch());
        customer.setAgreementsState(true);
        customer.setDescription(customerRepository.findById(message.getChatId()).get().getDescription());
        customer.setFileLink(customerRepository.findById(message.getChatId()).get().getFileLink());
        customer.setPhotoLink(customerRepository.findById(message.getChatId()).get().getPhotoLink());
        customer.setPrice(text);
        customer.setState(String.valueOf(quiz.FIXPRICE));
        customerRepository.save(customer);
       /* quiz=quiz.FIXPRICE;*/
        try {
            // Send the message
            execute(main_menu_sms);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    public void set_post(String chatId, int check, long messageID, Message message){
        CustomerActions customerActions = new CustomerActions(customerRepository);
        String post;
        if(check == 0) {
           post = customerActions.post_tostr(0, message);
            SendMessage sendMessage = new SendMessage();
            sendMessage.setParseMode("HTML");
            sendMessage.setChatId(chatId);
            sendMessage.setText(post);

            check_state = false;
            try {
                // Send the message
                execute(sendMessage);

            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

        }else {
            post = customerActions.post_tostr(1, message);
            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setChatId(chatId);
            editMessageText.setMessageId((int) messageID);
            editMessageText.setParseMode("HTML");
            editMessageText.setText(post);
            check_state = true;

            try {
                // Send the message
                execute(editMessageText);

            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

    }
    public void set_last_buttons(String chatId){
        const_text = new Text();
        SendMessage main_menu_sms = new SendMessage();
        main_menu_sms.setChatId(chatId);
        main_menu_sms.setText("Якщо хочете публікувати пост до каналу, натисніть <Публікувати>, якщо щось змінити, натисніть <Змінити>");
        InlineKeyboardMarkup inline_keybord = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows_inline = new ArrayList<>();
        List<InlineKeyboardButton> row_inline=new ArrayList<>();
        var change_Button = new InlineKeyboardButton();
        change_Button.setText(const_text.getChange_text());
        change_Button.setCallbackData(subjects.CHANGE.toString());
        var publish_Button = new InlineKeyboardButton();
        publish_Button.setText(const_text.getPublic_text());
        publish_Button.setCallbackData(subjects.PUBLIC.toString());
        row_inline.add(change_Button);
        row_inline.add(publish_Button);

        rows_inline.add(row_inline);

        inline_keybord.setKeyboard(rows_inline);
        main_menu_sms.setReplyMarkup(inline_keybord);
        /*customerData=new CustomerData();*/
        try{
            execute(main_menu_sms);
        }catch(TelegramApiException e){
            e.printStackTrace();
        }
    }
    public void set_change_menu(String chatId) {
        SendMessage main_menu_sms = new SendMessage();
        main_menu_sms.setChatId(chatId);
        text = EmojiParser.parseToUnicode("Оберіть на який крок треба повернутися: " + ":dancer:");
        main_menu_sms.setText(text);
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        List<KeyboardRow> menu = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add(const_text.getChange_subject());
        row.add(const_text.getChange_description());
        KeyboardRow row1 = new KeyboardRow();
        row1.add(const_text.getChange_photo_or_file());
        row1.add(const_text.getChange_price());
        menu.add(row);
        menu.add(row1);
        keyboard.setKeyboard(menu);
        keyboard.setResizeKeyboard(true);
        keyboard.setOneTimeKeyboard(false);
        keyboard.setSelective(true);
        main_menu_sms.setReplyMarkup(keyboard);
        try {
            // Send the message
            execute(main_menu_sms);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    public void register_to_data_base_customer(Message message){
            var chatID = message.getChatId();
            var chat = message.getChat();
            Customer customer = new Customer();
            customer.setChatID(chatID);
            customer.setName(chat.getFirstName());
            customer.setSurname(chat.getLastName());
            customer.setUser_nick(chat.getUserName());
        customer.setBranch(customerRepository.findById(message.getChatId()).get().getBranch());
        customer.setAgreementsState(true);
        customer.setDescription(customerRepository.findById(message.getChatId()).get().getDescription());
        customer.setFileLink(customerRepository.findById(message.getChatId()).get().getFileLink());
        customer.setPhotoLink(customerRepository.findById(message.getChatId()).get().getPhotoLink());
        customer.setPrice(customerRepository.findById(message.getChatId()).get().getPrice());
        customer.setPostLink(customerRepository.findById(message.getChatId()).get().getPostLink());
        customer.setState(customerRepository.findById(message.getChatId()).get().getState());
        customer.setPriceFlag(0);
            customerRepository.save(customer);
           customers_id = chatID;
      }
    public void set_warning(String chatId){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(const_text.getWarning());
        try {
            // Send the message
          execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    public void performer_check(User user, String chatID){
        if(performerRepository.findById(user.getId()).isEmpty()) {
            AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
            answerCallbackQuery.setCallbackQueryId(chatID);
            answerCallbackQuery.setText("Ви не зареєстровані, як виконавець. Зареєструйтеся спочатку у боті");
            answerCallbackQuery.setShowAlert(true);

            try {
                // Send the message
                execute(answerCallbackQuery);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }else{
            AnswerCallbackQuery answerCallbackQuery = new AnswerCallbackQuery();
            answerCallbackQuery.setCallbackQueryId(chatID);
            answerCallbackQuery.setText("Ваша заявка була відправлена користувачу на розгляд");
            answerCallbackQuery.setShowAlert(true);
          /*  Performer performer = new Performer();
            performer.setChatID(performerRepository.findById(user.getId()).get().getChatID());
            performer.setName(performerRepository.findById(user.getId()).get().getName());
            performer.setSurname(performerRepository.findById(user.getId()).get().getSurname());
            performer.setUser_nick(performerRepository.findById(user.getId()).get().getUser_nick());
            performer.setRating(performerRepository.findById(user.getId()).get().getRating());
            performer.setBargain_amount(performerRepository.findById(user.getId()).get().getBargain_amount());
            performerRepository.save(performer);*/
            performers_id = user.getId();
            try {
                // Send the message
                execute(answerCallbackQuery);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }

    }
    public void set_information_about_performer(User user, Long chatID, Update update)  {
        if(!performerRepository.findById(user.getId()).isEmpty()){
            performer_id = String.valueOf(user.getId());
            CustomerActions customerActions = new CustomerActions(customerRepository);
            String post = customerActions.get_customer_post_link_tostr(update,postRepository);
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatID);

            sendMessage.setText("Користувач " + performerRepository.findById(user.getId()).get().getName() + " " + performerRepository.findById(user.getId()).get().getSurname() + ", з рейтингом: " + performerRepository.findById(user.getId()).get().getRating() + "та кількістю угод: " + performerRepository.findById(user.getId()).get().getBargain_amount() + ", готовий/а взятися за ваше завдання" + "\n" + post);
            InlineKeyboardMarkup inline_keybord = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rows_inline = new ArrayList<>();



            List<InlineKeyboardButton> row_inline=new ArrayList<>();
            var agree_Button = new InlineKeyboardButton();
            agree_Button.setText(const_text.getAgree_text());
            agree_Button.setCallbackData(update.getCallbackQuery().getMessage().getMessageId()+","+update.getCallbackQuery().getFrom().getId());/*subjects.AGREE.toString()*/
            var cancel_Button = new InlineKeyboardButton();
            cancel_Button.setText(const_text.getCancel_text());
            cancel_Button.setCallbackData(subjects.CANCEL.toString());
            row_inline.add(agree_Button);
            row_inline.add(cancel_Button);
            rows_inline.add(row_inline);
            inline_keybord.setKeyboard(rows_inline);
            sendMessage.setReplyMarkup(inline_keybord);
            try {
                // Send the message
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
    private void bargain_cancel(long chatID, Update update) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(chatID);
        editMessageText.setMessageId((int) update.getCallbackQuery().getMessage().getMessageId());
        editMessageText.setText("Спілку обірвано");
        check_state = true;
        try {
            // Send the message
            execute(editMessageText);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    public void  return_chat_link_and_show_sms_in_group(long chatID, Update update){
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(chatID);
        editMessageText.setMessageId((int) update.getCallbackQuery().getMessage().getMessageId());
        editMessageText.setText("Посилання на чат з виконавцем");
        InlineKeyboardMarkup inline_keybord = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows_inline = new ArrayList<>();
        List<InlineKeyboardButton> row_inline=new ArrayList<>();
        var link_Button = new InlineKeyboardButton();
        link_Button.setText(const_text.getGo_chat());
        link_Button.setUrl("https://t.me/+g7sZc_AwchMyNDZi");
        link_Button.setCallbackData(subjects.LINK.toString());
        row_inline.add(link_Button);
        rows_inline.add(row_inline);
        inline_keybord.setKeyboard(rows_inline);
        editMessageText.setReplyMarkup(inline_keybord);
        try {
            // Send the message
            execute(editMessageText);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    public void register_to_data_base_performer(User user, String chatid){
        if(performerRepository.findById(user.getId()).isEmpty()) {
            var chatID = user.getId();
            Performer performer = new Performer();
            performer.setChatID(chatID);
            performer.setName(user.getFirstName());
            performer.setSurname(user.getLastName());
            performer.setUser_nick(user.getUserName());
            performer.setBargain_amount(0);
            performer.setRating(const_text.getFirst_performer());
            performerRepository.save(performer);
            /*performers_id = chatID;*/
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatid);
            sendMessage.setText("Вас було зареєйстровано як виконавець та видано рейтинг. Поки що він порожній");
            try {
                // Send the message
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }else{
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatid);
            sendMessage.setText("Ви вже зареєстровані, як виконавець");
            try {
                // Send the message
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
    public void set_button_register_performer(String ChatId){
        SendMessage main_menu_sms = new SendMessage();
        main_menu_sms.setChatId(ChatId);
        main_menu_sms.setText("Щоб зареєструватися, натисніть на кнопку \"Хочу зареєструватися\":");
        InlineKeyboardMarkup inline_keybord = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows_inline = new ArrayList<>();



        List<InlineKeyboardButton> row_inline=new ArrayList<>();
        var registrate_Button = new InlineKeyboardButton();
        registrate_Button.setText(const_text.getWant_registrate());
        registrate_Button.setCallbackData(subjects.PERFORMER_REGISTER.toString());
        row_inline.add(registrate_Button);
        rows_inline.add(row_inline);
        inline_keybord.setKeyboard(rows_inline);
        main_menu_sms.setReplyMarkup(inline_keybord);
        try{
            execute(main_menu_sms);
        }catch(TelegramApiException e){
            e.printStackTrace();
        }
    }
    public void return_chat_link_and_show_sms_for_performer_in_group(Update update, String performerID, String postID){
       CustomerActions customerActions = new CustomerActions(customerRepository);
       SendMessage sendMessage = new SendMessage();
       sendMessage.setChatId(performerID);
       sendMessage.setParseMode("HTML");
       sendMessage.setText("Посилання на чат з користувачем з посту:\n"+ postRepository.findById(Integer.valueOf(postID)).get().getLink() /*customerActions.get_customer_post_link_tostr(update,postRepository)*/);
        InlineKeyboardMarkup inline_keybord = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows_inline = new ArrayList<>();
        List<InlineKeyboardButton> row_inline=new ArrayList<>();
        var link_Button = new InlineKeyboardButton();
        link_Button.setText(const_text.getGo_chat());
        link_Button.setUrl("https://t.me/+g7sZc_AwchMyNDZi");
        link_Button.setCallbackData(subjects.LINK.toString());
        row_inline.add(link_Button);
        rows_inline.add(row_inline);
        inline_keybord.setKeyboard(rows_inline);
        sendMessage.setReplyMarkup(inline_keybord);
        try {
            // Send the message
            execute(sendMessage);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    public void set_sms_in_chat() throws IOException {
        CustomerActions customerActions=new CustomerActions(customerRepository);
        String post = customerActions.set_in_group_info_tostr();
        String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&reply_markup=%s&parse_mode=%s&text=%s";
        String chatId = "-811918442";
        String reply = URLEncoder.encode("{\"inline_keyboard\":[[{\"text\":\""+ "Вказати ціну"+"\",\"callback_data\":\"PRICE\"},{\"text\":\""+ "Закінчити угоду"+"\",\"callback_data\":\"CLOSE\"}]]}","UTF-8");
        String parse = "HTML";
        post = URLEncoder.encode(post,"UTF-8");
        urlString = String.format(urlString, getBotToken(), chatId, reply, parse, post);
        URL newurl = new URL(urlString);
        URLConnection conn = newurl.openConnection();
        StringBuilder sb = new StringBuilder();
        InputStream is = new BufferedInputStream(conn.getInputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String inputLine = "";
        while ((inputLine = br.readLine()) != null) {
            sb.append(inputLine);
        }
    }
    public void fix_finish_text_price_customer(Message message) throws IOException {
        Customer customer = new Customer();
        customer.setChatID(customerRepository.findById(message.getChatId()).get().getChatID());
        customer.setSurname(customerRepository.findById(message.getChatId()).get().getSurname());
        customer.setName(customerRepository.findById(message.getChatId()).get().getName());
        customer.setUser_nick(customerRepository.findById(message.getChatId()).get().getUser_nick());
        customer.setBranch(customerRepository.findById(message.getChatId()).get().getBranch());
        customer.setAgreementsState(true);
        customer.setDescription(customerRepository.findById(message.getChatId()).get().getDescription());
        customer.setFileLink(customerRepository.findById(message.getChatId()).get().getFileLink());
        customer.setPhotoLink(customerRepository.findById(message.getChatId()).get().getPhotoLink());
        customer.setPrice(customerRepository.findById(message.getChatId()).get().getPrice());
        customer.setPostLink(customerRepository.findById(message.getChatId()).get().getPostLink());
        customer.setState(customerRepository.findById(message.getChatId()).get().getState());
        customer.setPriceFlag(1);
        customerRepository.save(customer);
        String post = "Користувач, напишіть ціну цифрою, без копійок";
        String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";
        String chatId = "-811918442";
        post = URLEncoder.encode(post,"UTF-8");
        urlString = String.format(urlString, getBotToken(), chatId, post);
        URL newurl = new URL(urlString);
        URLConnection conn = newurl.openConnection();
        StringBuilder sb = new StringBuilder();
        InputStream is = new BufferedInputStream(conn.getInputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String inputLine = "";
        while ((inputLine = br.readLine()) != null) {
            sb.append(inputLine);
        }
    }
    public boolean check_customers_price(String user_price, Message message) throws IOException {
        if(customers_id != message.getFrom().getId()){
            String post = ("Ми просимо відправити смс саме користувача (людині, яка робила пост в канал)");
            String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";
            String chatId = "-811918442";
            post = URLEncoder.encode(post, "UTF-8");
            urlString = String.format(urlString, getBotToken(), chatId, post);
            URL newurl = new URL(urlString);
            URLConnection conn = newurl.openConnection();
            StringBuilder sb = new StringBuilder();
            InputStream is = new BufferedInputStream(conn.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String inputLine = "";
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }
            fix_finish_text_price_customer(message);
            return false;
        }
        else {
            try {
                sms_to_performer(user_price);
            } catch (NumberFormatException nfe) {
                String post = ("Ви відправили не число");
                String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";
                String chatId = "-811918442";
                post = URLEncoder.encode(post, "UTF-8");
                urlString = String.format(urlString, getBotToken(), chatId, post);
                URL newurl = new URL(urlString);
                URLConnection conn = newurl.openConnection();
                StringBuilder sb = new StringBuilder();
                InputStream is = new BufferedInputStream(conn.getInputStream());
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String inputLine = "";
                while ((inputLine = br.readLine()) != null) {
                    sb.append(inputLine);
                }
            }
        }
        return true;
    }
    public void sms_to_performer(String user_price) throws IOException {
        int check_num = Integer.parseInt(user_price);
        String post = ("Ціну зафіксовано, вона дорівнює: " + check_num + " грн, виконавець, ви згодні з цією ціною?");
        String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&reply_markup=%s&text=%s";
        String chatId = "-811918442";
        String reply = URLEncoder.encode("{\"inline_keyboard\":[[{\"text\":\"" + "Так" + "\",\"callback_data\":\"YES\"},{\"text\":\"" + "Ні" + "\",\"callback_data\":\"NO\"}]]}", "UTF-8");
        post = URLEncoder.encode(post, "UTF-8");
        urlString = String.format(urlString, getBotToken(), chatId, reply, post);
        URL newurl = new URL(urlString);
        URLConnection conn = newurl.openConnection();
        StringBuilder sb = new StringBuilder();
        InputStream is = new BufferedInputStream(conn.getInputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String inputLine = "";
        while ((inputLine = br.readLine()) != null) {
            sb.append(inputLine);
        }
    }
    public void check_performer_return_no(Long message, String user_price)throws IOException{
        if(performers_id != message){
            String post = ("Ми просимо відповісти саме виконавця (людині, яка буде виконувати завдання)");
            String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";
            String chatId = "-811918442";
            post = URLEncoder.encode(post, "UTF-8");
            urlString = String.format(urlString, getBotToken(), chatId, post);
            URL newurl = new URL(urlString);
            URLConnection conn = newurl.openConnection();
            StringBuilder sb = new StringBuilder();
            InputStream is = new BufferedInputStream(conn.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String inputLine = "";
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }
            sms_to_performer(user_price);
        }
        else{
            String post = ("Виконавець не згідний за ціною, спробуйте домовитися ще");
            String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";
            String chatId = "-811918442";
            post = URLEncoder.encode(post, "UTF-8");
            urlString = String.format(urlString, getBotToken(), chatId, post);
            URL newurl = new URL(urlString);
            URLConnection conn = newurl.openConnection();
            StringBuilder sb = new StringBuilder();
            InputStream is = new BufferedInputStream(conn.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String inputLine = "";
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }

        }
    }
    public void setBranchToTable(Message message, String strsubject){
      /*  User user = message.getFrom();*/
        if(!customerRepository.findById(message.getChatId()).isEmpty()) {
            Customer customer = new Customer();
            customer.setChatID(customerRepository.findById(message.getChatId()).get().getChatID());
            customer.setSurname(customerRepository.findById(message.getChatId()).get().getSurname());
            customer.setName(customerRepository.findById(message.getChatId()).get().getName());
            customer.setUser_nick(customerRepository.findById(message.getChatId()).get().getUser_nick());
            customer.setBranch(strsubject);
            customer.setAgreementsState(true);
            customer.setDescription(customerRepository.findById(message.getChatId()).get().getDescription());
            customer.setFileLink(customerRepository.findById(message.getChatId()).get().getFileLink());
            customer.setPhotoLink(customerRepository.findById(message.getChatId()).get().getPhotoLink());
            customer.setPrice(customerRepository.findById(message.getChatId()).get().getPrice());
            customer.setPostLink(customerRepository.findById(message.getChatId()).get().getPostLink());
            customer.setState(customerRepository.findById(message.getChatId()).get().getState());
            customer.setPriceFlag(0);
            customerRepository.save(customer);
        }else{
            register_to_data_base_customer(message);
            Customer customer = new Customer();
            customer.setChatID(customerRepository.findById(message.getChatId()).get().getChatID());
            customer.setSurname(customerRepository.findById(message.getChatId()).get().getSurname());
            customer.setName(customerRepository.findById(message.getChatId()).get().getName());
            customer.setUser_nick(customerRepository.findById(message.getChatId()).get().getUser_nick());
            customer.setBranch(strsubject);
            customerRepository.save(customer);
        }
    }
    public void setPostLinkToTable(Update update, String postUrl){
        if(!customerRepository.findById(update.getCallbackQuery().getMessage().getChatId()).isEmpty()) {
            Customer customer = new Customer();
            customer.setChatID(customerRepository.findById(update.getCallbackQuery().getMessage().getChatId()).get().getChatID());
            customer.setSurname(customerRepository.findById(update.getCallbackQuery().getMessage().getChatId()).get().getSurname());
            customer.setName(customerRepository.findById(update.getCallbackQuery().getMessage().getChatId()).get().getName());
            customer.setUser_nick(customerRepository.findById(update.getCallbackQuery().getMessage().getChatId()).get().getUser_nick());
            customer.setAgreementsState(true);
            customer.setBranch(customerRepository.findById(update.getCallbackQuery().getMessage().getChatId()).get().getBranch());
            customer.setDescription(customerRepository.findById(update.getCallbackQuery().getMessage().getChatId()).get().getDescription());
            customer.setPrice(customerRepository.findById(update.getCallbackQuery().getMessage().getChatId()).get().getPrice());
            customer.setPhotoLink(customerRepository.findById(update.getCallbackQuery().getMessage().getChatId()).get().getPhotoLink());
            customer.setFileLink(customerRepository.findById(update.getCallbackQuery().getMessage().getChatId()).get().getFileLink());
            customer.setPostLink(postUrl);
            customerRepository.save(customer);
        }/*else{
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(message.getChatId());
            sendMessage.setText("ERROR");
            try {
                // Send the message
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }*/
    }
    public void save(Message message, String state){
        Customer customer = new Customer();
        customer.setChatID(customerRepository.findById(message.getChatId()).get().getChatID());
        customer.setSurname(customerRepository.findById(message.getChatId()).get().getSurname());
        customer.setName(customerRepository.findById(message.getChatId()).get().getName());
        customer.setUser_nick(customerRepository.findById(message.getChatId()).get().getUser_nick());
        customer.setBranch(customerRepository.findById(message.getChatId()).get().getBranch());
        customer.setAgreementsState(true);
        customer.setDescription(customerRepository.findById(message.getChatId()).get().getDescription());
        customer.setFileLink(customerRepository.findById(message.getChatId()).get().getFileLink());
        customer.setPhotoLink(customerRepository.findById(message.getChatId()).get().getPhotoLink());
        customer.setPrice(customerRepository.findById(message.getChatId()).get().getPrice());
        customer.setPostLink(customerRepository.findById(message.getChatId()).get().getPostLink());
        customer.setState(state);
        customer.setPriceFlag(0);
        customerRepository.save(customer);
    }
    public void save_new_links(List photo_list, List file_list, Message message){
        Customer customer = new Customer();
        customer.setChatID(customerRepository.findById(message.getChatId()).get().getChatID());
        customer.setSurname(customerRepository.findById(message.getChatId()).get().getSurname());
        customer.setName(customerRepository.findById(message.getChatId()).get().getName());
        customer.setUser_nick(customerRepository.findById(message.getChatId()).get().getUser_nick());
        customer.setBranch(customerRepository.findById(message.getChatId()).get().getBranch());
        customer.setAgreementsState(true);
        customer.setDescription(customerRepository.findById(message.getChatId()).get().getDescription());
        customer.setFileLink(file_list);
        customer.setPhotoLink(photo_list);
        customer.setPrice(customerRepository.findById(message.getChatId()).get().getPrice());
        customer.setPostLink(customerRepository.findById(message.getChatId()).get().getPostLink());
        customer.setState(customerRepository.findById(message.getChatId()).get().getState());
        customer.setPriceFlag(0);
        customerRepository.save(customer);;
    }
}
   /* public void set_last_menu(String chatId){
        SendMessage main_menu_sms = new SendMessage();
        main_menu_sms.setChatId(chatId);
        text = EmojiParser.parseToUnicode("Оберіть дію:" + ":blush:");
        main_menu_sms.setText(text);
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        List<KeyboardRow> menu = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        text = EmojiParser.parseToUnicode("Зробити оголошення"+":woman_student:");
        row.add(text);
        KeyboardRow row1 = new KeyboardRow();
        text = EmojiParser.parseToUnicode("Шахраї"+":octopus:");
        row1.add(text);
        text = EmojiParser.parseToUnicode("Активні виконавці"+":+1:");
        row1.add(text);
        menu.add(row);
        menu.add(row1);
        keyboard.setKeyboard(menu);
        keyboard.setResizeKeyboard(true);
        keyboard.setOneTimeKeyboard(false);
        keyboard.setSelective(true);
        main_menu_sms.setReplyMarkup(keyboard);
        quiz = quiz.PRICE;
        try {
            // Send the message
            execute(main_menu_sms);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
       }
    }*/
   /*
            String doc_id = update.getMessage().getDocument().getFileId();
            String doc_name = update.getMessage().getDocument().getFileName();
            String doc_mine = update.getMessage().getDocument().getMimeType();
            long doc_size = update.getMessage().getDocument().getFileSize();
            String getID = String.valueOf(update.getMessage().getFrom().getId());

            Document document = new Document();
            document.setMimeType(doc_mine);
            document.setFileName(doc_name);
            document.setFileSize(doc_size);
            document.setFileId(doc_id);

            GetFile getFile = new GetFile();
            getFile.setFileId(document.getFileId());
            try {
                String filepath = execute(getFile).getFilePath();
               File file= downloadFile(filepath, new File("E:\\Java\\lesson3\\Student-Portal\\src\\main\\resources\\userfile"));
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }*/
  /* URL url = new URL("https://api.telegram.org/bot"+getBotToken()+"/getFile?file_id="+file_id);
        BufferedReader in = new BufferedReader(new InputStreamReader( url.openStream()));
        String res = in.readLine();
        JSONObject jresult = new JSONObject(res);
        JSONObject path = jresult.getJSONObject("result");
        String file_path = path.getString("file_path");
        URL downoload = new URL("https://api.telegram.org/file/bot" + getBotToken() + "/" + file_path);
        String upPath = "E:\\Java\\lesson3\\Student-Portal\\src\\main\\resources\\user-file\\userfile";
        FileOutputStream fos = new FileOutputStream(upPath + update.getMessage().getDocument().getFileName());
        ReadableByteChannel rbc = Channels.newChannel(downoload.openStream());
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
        rbc.close();*/
 /* URL url = new URL("https://api.telegram.org/bot"+getBotToken()+"/getFile?file_id="+file_id);
        BufferedReader in = new BufferedReader(new InputStreamReader( url.openStream()));
        String res = in.readLine();
        JSONObject jresult = new JSONObject(res);
        JSONObject path = jresult.getJSONObject("result");
        String file_path = path.getString("file_path");
        URL downoload = new URL("https://api.telegram.org/file/bot" + getBotToken() + "/" + file_path);
        String upPath = "E:\\Java\\lesson3\\Student-Portal\\src\\main\\resources\\user-file\\userfile";
        FileOutputStream fos = new FileOutputStream(upPath + file_id);
        ReadableByteChannel rbc = Channels.newChannel(downoload.openStream());
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
        rbc.close();
*/
/*  BufferedReader in = new BufferedReader(new InputStreamReader( url.openStream()));
        String res = in.readLine();
        JSONObject jresult = new JSONObject(res);
        JSONObject path = jresult.getJSONObject("result");
        String file_path = path.getString("file_path");
        String downoload = "https://api.telegram.org/file/bot" + getBotToken() + "/" + file_path;
        customerData.setFile_url(downoload);*/