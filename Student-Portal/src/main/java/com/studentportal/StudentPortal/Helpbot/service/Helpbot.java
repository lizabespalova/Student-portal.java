package com.studentportal.StudentPortal.Helpbot.service;

import com.studentportal.StudentPortal.Helpbot.config.HelpbotConfig;
import com.studentportal.StudentPortal.Helpbot.model.Customer;
import com.studentportal.StudentPortal.Helpbot.model.CustomerRepository;
import com.studentportal.StudentPortal.Helpbot.model.Performer;
import com.studentportal.StudentPortal.Helpbot.model.PerformerRepository;
import com.vdurmont.emoji.EmojiParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
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
    private boolean check_state = true;
    private Chanels chanel;
    final HelpbotConfig config;
    private String text;
    private List url_photo;
    private List url_file;
    private Subjects subjects;
    private String check_subj;
    private Quiz quiz;
    private long messageID;
    private Text const_text;
    private String bot_chat_ID;
    private CustomerData customerData = new CustomerData();
    private boolean check_txt_description=false;
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
            long chatID = update.getCallbackQuery().getMessage().getChatId();
            String messagetext = update.getCallbackQuery().getData();
            messageID = update.getCallbackQuery().getMessage().getMessageId();
            if(messagetext.equals(subjects.MATH.toString())||messagetext.equals(subjects.PROGRAMMING.toString())||
                    messagetext.equals(subjects.PHYLOSOPHY.toString())||messagetext.equals(subjects.GEOGRAPHY.toString())||
                    messagetext.equals(subjects.LANGUAGES.toString())||messagetext.equals(subjects.ANOTHER.toString())||
                    messagetext.equals(subjects.CHEMISTRY.toString())|| messagetext.equals(subjects.MEDICINE.toString())){
                check_subj=messagetext;
                Subjectgetters subjectgetters = new Subjectgetters();
                String strsubject = subjectgetters.getsubject(messagetext);
                customerData.setSubject(strsubject);
                if(!check_state){set_post(String.valueOf(chatID),0,messageID); set_last_buttons(String.valueOf(chatID));}
                else set_text_description(chatID);
            } else if (messagetext.equals(quiz.FIXPRICE.toString()) && quiz==quiz.FILEDESCRIPTION) {
                set_fix_price_menu(String.valueOf(chatID));
            }
            else if (messagetext.equals(quiz.AGREEMENTPRICE.toString()) && quiz==quiz.FILEDESCRIPTION) {
                set_agreement_price_menu(String.valueOf(chatID));
                set_post(String.valueOf(chatID),0,messageID);
                set_last_buttons(String.valueOf(chatID));
            } else if (messagetext.equals(subjects.CHANGE.toString())) {
                set_change_menu(String.valueOf(chatID));
            }
            else if (messagetext.equals(subjects.PUBLIC.toString())) {
                register_to_data_base_customer(update.getCallbackQuery().getMessage());
                chanel = new Chanels();
               if (check_subj==null){
                  set_warning(String.valueOf(chatID));
                }
               else if(check_subj.equals(subjects.MATH.toString())){
                    try {
                       String post_url = chanel.set_to_matem_chanal(customerData, getBotToken());
                       customerData.setPost_url(post_url);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
               }
               else if(check_subj.equals(subjects.PROGRAMMING.toString())){
                   try {
                       String post_url =  chanel.set_to_programm_chanal(customerData, getBotToken());
                       customerData.setPost_url(post_url);
                   } catch (IOException e) {
                       throw new RuntimeException(e);
                   }
               }else if(check_subj.equals(subjects.CHEMISTRY.toString())){
                   try {
                       String post_url = chanel.set_to_chemistry_chanal(customerData, getBotToken());
                       customerData.setPost_url(post_url);
                   } catch (IOException e) {
                       throw new RuntimeException(e);
                   }
               }else if(check_subj.equals(subjects.MEDICINE.toString())){
                   try {
                       String post_url = chanel.set_to_medic_chanal(customerData, getBotToken());
                       customerData.setPost_url(post_url);
                   } catch (IOException e) {
                       throw new RuntimeException(e);
                   }
               }else if(check_subj.equals(subjects.GEOGRAPHY.toString())){
                   try {
                       String post_url=chanel.set_to_geography_chanal(customerData, getBotToken());
                       customerData.setPost_url(post_url);
                   } catch (IOException e) {
                       throw new RuntimeException(e);
                   }
               }
               else if(check_subj.equals(subjects.PHYLOSOPHY.toString())){
                   try {
                       String post_url= chanel.set_to_phylosophy_chanal(customerData, getBotToken());
                       customerData.setPost_url(post_url);
                   } catch (IOException e) {
                       throw new RuntimeException(e);
                   }
               }
               else if(check_subj.equals(subjects.LANGUAGES.toString())){
                   try {
                       String post_url= chanel.set_to_language_chanal(customerData, getBotToken());
                       customerData.setPost_url(post_url);
                   } catch (IOException e) {
                       throw new RuntimeException(e);
                   }
               }else if(check_subj.equals(subjects.ANOTHER.toString())){
                   try {
                       String post_url= chanel.set_to_main_chanal(customerData, getBotToken());
                       customerData.setPost_url(post_url);
                   } catch (IOException e) {
                       throw new RuntimeException(e);
                   }
               }
               set_post(String.valueOf(chatID),1,messageID);
                set_main_menu(String.valueOf(chatID));
            }
            else if (messagetext.equals("Візьму")){
                register_to_data_base_performer(update.getCallbackQuery().getFrom());
                    set_information_about_performer(update.getCallbackQuery().getFrom(), bot_chat_ID);
            }
        }
        if(update.hasMessage()) {
            if (update.getMessage().hasDocument() && quiz == quiz.TEXTDESCRIPTION) {
                const_text = new Text();
                try {
                   get_file_description(update);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            } else if (update.hasMessage() && update.getMessage().hasPhoto() && quiz == quiz.TEXTDESCRIPTION) {
                get_photo_description(update);
            }
        }
        if(message!=null){
            bot_chat_ID = String.valueOf(message.getChatId());
        String user_sms = message.getText();
        if(user_sms!=null) {
            const_text = new Text();
            if (user_sms.equals(const_text.getAgreement_text())) {
                url_photo = new ArrayList();
                url_file = new ArrayList();
                end_stop_menu(String.valueOf(message.getChatId()));
                if (!check_state) {
                    set_post(String.valueOf(message.getChatId()),0,messageID);
                    set_last_buttons(String.valueOf(message.getChatId()));
                } else set_subject_menu(String.valueOf(message.getChatId()));
//
            } else if (user_sms.equals(const_text.getBack_text())) {
                if (quiz == quiz.SUBJECTMENU) {
                    end_stop_menu(String.valueOf(message.getChatId()));
                    set_subject_menu(String.valueOf(message.getChatId()));

                } else if (quiz == quiz.MAINMENU) {
                    set_main_menu(String.valueOf(message.getChatId()));
                } else if (quiz == quiz.TEXTDESCRIPTION) {
                    set_text_description(message.getChatId());
                } else if (quiz == quiz.FILEDESCRIPTION) {
                    set_file_description(message.getChatId());
                    set_ready_button(String.valueOf(message.getChatId()));
                } else if (quiz == quiz.PRICE || quiz == quiz.AGREEMENTPRICE || quiz == quiz.FIXPRICE) {
                    set_price_description(String.valueOf(message.getChatId()));
                }
            } else if (user_sms.equals(const_text.getEnd_text())) {
                set_main_menu(String.valueOf(message.getChatId()));
                check_state = true;
            } else if (check_txt_description == true) {
                get_text_description(message);
                if(!check_state){set_post(String.valueOf(message.getChatId()),0,messageID);set_last_buttons(String.valueOf(message.getChatId()));}
                else {
                    set_file_description(message.getChatId());
                    set_ready_button(String.valueOf(message.getChatId()));
                }
            } else if (user_sms.equals(const_text.getReady_text()) && quiz == quiz.TEXTDESCRIPTION) {
                end_stop_menu(String.valueOf(message.getChatId()));

                try {
                    if (url_photo != null) set_photo_to_channel_and_return_link();
                    if (url_file != null) set_file_to_channel_and_return_link();
                    if (url_photo == null) customerData.setPhoto_url(null);
                    if (url_file == null) customerData.setFile_url(null);
                    url_photo = null;
                    url_file = null;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if (!check_state) {
                    set_post(String.valueOf(message.getChatId()),0,messageID);
                    set_last_buttons(String.valueOf(message.getChatId()));
                } else set_price_description(String.valueOf(message.getChatId()));
            } else if (user_sms.equals(const_text.getChange_subject())) {
                set_subject_menu(String.valueOf(message.getChatId()));
                end_stop_menu(String.valueOf(message.getChatId()));
            } else if (user_sms.equals(const_text.getChange_description())) {
                    set_text_description(message.getChatId());
                    end_stop_menu(String.valueOf(message.getChatId()));
            } else if (user_sms.equals(const_text.getChange_photo_or_file())) {
                url_photo = new ArrayList();
                url_file = new ArrayList();
                set_file_description(message.getChatId());
                set_ready_button(String.valueOf(message.getChatId()));
            } else if (user_sms.equals(const_text.getChange_price())) {
                set_price_description(String.valueOf(message.getChatId()));
            }else if (quiz == quiz.FIXPRICE ) {
                boolean check_price=debug_fix_price(user_sms, message.getChatId());
                if(check_price) {
                    set_post(String.valueOf(message.getChatId()), 0, messageID);
                    set_last_buttons(String.valueOf(message.getChatId()));
                }else {set_fix_price_menu(String.valueOf(message.getChatId()));}
                check_state = false;
            }
        }
            switch (user_sms) {
                    case "/start": {
                        set_main_menu(String.valueOf(message.getChatId()));
                        check_state = true;
                        break;
                    }
                }
        }
    }

    public void set_main_menu(String chatId){
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
        quiz = quiz.MAINMENU;
        try {
            // Send the message
            execute(main_menu_sms);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    public void set_subject_menu(String chatId){
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
        quiz= quiz.MAINMENU;
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
    public void set_text_description(long chatId){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Опишіть своє завдання (без фото або файлів)");
        sendMessage.setChatId(chatId);
        quiz=quiz.SUBJECTMENU;
        check_txt_description = true;
        try{
            execute(sendMessage);
        }catch(TelegramApiException e){
            e.printStackTrace();
        }

    }
    public void get_text_description(Message message){
        String user_text_description = message.getText();
        SendMessage check = new SendMessage();
        check.setChatId(message.getChatId());
        check.setText(user_text_description);
        customerData.setDescription(user_text_description);
        check_txt_description=false;
/*
        try{
            execute(check);
        }catch(TelegramApiException e){
            e.printStackTrace();
        }*/
    }
    public void set_file_description(long chatId){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Якщо є фото або файл, то відправте");
        sendMessage.setChatId(chatId);
        quiz=quiz.TEXTDESCRIPTION;
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
        String urlString = "https://api.telegram.org/bot5814824968:AAHriiP3p-FlWNLHn7UN8_-8ntGp1LUMEFg/sendDocument?chat_id=@vedmedik_base&document="+file_id;
        String chatId = "@vedmedik_base";
        urlString = String.format(urlString, getBotToken(), chatId, file_id);
        url_file.add(urlString);
    }
    public void set_file_to_channel_and_return_link()throws IOException{
        List file_url_list = new ArrayList();
        for (int j=0; j<url_file.size();j++) {
            URL newurl = new URL((String) url_file.get(j));
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
        customerData.setFile_url(file_url_list);
    }
    public void get_photo_description(Update update) {
        String photo_id=update.getMessage().getPhoto().get(3).getFileId();
        String urlString = "https://api.telegram.org/bot5814824968:AAHriiP3p-FlWNLHn7UN8_-8ntGp1LUMEFg/sendPhoto?chat_id=@vedmedik_base&photo="+photo_id;
        String chatId = "@vedmedik_base";
        urlString = String.format(urlString, getBotToken(), chatId, photo_id);
        url_photo.add(urlString);
    }
    public void set_photo_to_channel_and_return_link()throws IOException{
        List photo_url_list = new ArrayList();
         for (int j=0; j<url_photo.size();j++) {
             URL newurl = new URL((String) url_photo.get(j));
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
        customerData.setPhoto_url(photo_url_list);
    }
    public void set_price_description(String chatId){
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
        quiz=quiz.FILEDESCRIPTION;
        try{
            execute(main_menu_sms);
        }catch(TelegramApiException e){
            e.printStackTrace();
        }
    }
    public void set_fix_price_menu(String chatId){
        SendMessage main_menu_sms = new SendMessage();
        main_menu_sms.setChatId(chatId);
        text = EmojiParser.parseToUnicode("Напишіть вартість цифрою, без копійок:");
        main_menu_sms.setText(text);
        quiz=quiz.FIXPRICE;
        try {
            // Send the message
            execute(main_menu_sms);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    public boolean debug_fix_price(String user_price, long chatId){

        try {
            int check_num = Integer.parseInt(user_price);
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText("Ціну зафіксовано, вона дорівнює: " + check_num);
            sendMessage.setChatId(chatId);
            customerData.setPrice(check_num);
            quiz=quiz.FIXPRICE;
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
    public void set_agreement_price_menu(String chatId){
        SendMessage main_menu_sms = new SendMessage();
        main_menu_sms.setChatId(chatId);
        text = EmojiParser.parseToUnicode("Добре, за ціною домовитеся з виконавцем");
        main_menu_sms.setText(text);
        text = "Домовлена";
        quiz=quiz.AGREEMENTPRICE;
        customerData.setPrice(text);
        try {
            // Send the message
            execute(main_menu_sms);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    public void set_post(String chatId, int check, long messageID){
        CustomerActions customerActions = new CustomerActions(customerData);
        String post;
        if(check == 0) {
           post = customerActions.post_tostr(0);
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
            post = customerActions.post_tostr(1);
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
        /*if(customerRepository.findById(message.getChatId()).isEmpty()) {*/
            var chatID = message.getChatId();
            var chat = message.getChat();
            Customer customer = new Customer();
            customer.setChatID(chatID);
            customer.setName(chat.getFirstName());
            customer.setSurname(chat.getLastName());
            customer.setUser_nick(chat.getUserName());
            customerRepository.save(customer);
//            customer.setPrice(20);
//            customer.setPaid(true);
//            customer.setPerformersAmount(5);
//            customer.setAgreementsState(true);
//            customerRepository.save(customer);
       /* }*/
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
    public void register_to_data_base_performer(User user){
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
        }
//            customer.setPrice(20);
//            customer.setPaid(true);
//            customer.setPerformersAmount(5);
//            customer.setAgreementsState(true);
//            customerRepository.save(customer);
    }
    public void set_information_about_performer(User user, String chatID)  {
        if(!performerRepository.findById(user.getId()).isEmpty()){
            CustomerActions customerActions = new CustomerActions(customerData);
            String post = customerActions.get_customer_post_link_tostr();
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatID);
            sendMessage.setText("Користувач " + performerRepository.findById(user.getId()).get().getName() + " " + performerRepository.findById(user.getId()).get().getSurname() + ", з рейтингом: " + performerRepository.findById(user.getId()).get().getRating() + "та кількістю угод: " + performerRepository.findById(user.getId()).get().getBargain_amount() + ", готовий/а взятися за ваше завдання" + "\n" + post);
            try {
                // Send the message
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
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