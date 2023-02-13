package com.studentportal.StudentPortal.Helpbot.service;

import org.springframework.context.event.ContextRefreshedEvent;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class Chanels {
    private Text const_text;
    public String set_to_matem_chanal(CustomerData customerData, String token) throws IOException {
//       /* CustomerActions customerActions = new CustomerActions(customerData);
//        String post = customerActions.post_tostr();
//        SendMessage sendMessage = new SendMessage("@matem_vedmedyk", "channel by id message");
//        sendMessage.setParseMode("HTML");
//        sendMessage.setChatId(chatId);
//        sendMessage.setText(post);*/
//        /*
//        try {
//            // Send the message
//            execute(sendMessage);
//
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
//*/
//       /* URL newurl = new URL(urlString);
//        URLConnection conn = newurl.openConnection();
//        StringBuilder sb = new StringBuilder();
//        InputStream is = new BufferedInputStream(conn.getInputStream());
//        BufferedReader br = new BufferedReader(new InputStreamReader(is));
//        String inputLine = "";
//        String digits="";
//        int check=0;
//        while ((inputLine = br.readLine()) != null) {
//            sb.append(inputLine);
//            for (int i = 0; i < inputLine.length(); i++) {
//                char chrs = inputLine.charAt(i);
//                if (Character.isDigit(chrs)) {
//                    digits = digits + chrs;
//
//                }
//                if(chrs == ',') {
//                    check++;
//                }
//                if(check==2){
//                    break;
//                }
//            }
//            if(check==2){
//                break;
//            }
//        }
//        String url = "https://t.me/matem_vedmedyk"+ digits;
//        customerData.setPhoto_url(url);
//        try {
//            // Send the message
//            execute(sendMessage);
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//    }*/
//        /*TelegramBot bot = new TelegramBot(getBotToken());*/
//     /*   SendMessage sendMessage = new SendMessage();
//        sendMessage.setParseMode("HTML");
//        sendMessage.setChatId(chatId);
//        sendMessage.setText(post);
//        InlineKeyboardMarkup inline_keybord = new InlineKeyboardMarkup();
//        List<List<InlineKeyboardButton>> rows_inline = new ArrayList<>();
//
//
//
//        List<InlineKeyboardButton> row_inline=new ArrayList<>();
//        var take_Button = new InlineKeyboardButton();
//        take_Button.setText("Беру");
//        take_Button.setCallbackData(subjects.TAKE.toString());
//        row_inline.add(take_Button);
//        rows_inline.add(row_inline);
//
//        inline_keybord.setKeyboard(rows_inline);
//        sendMessage.setReplyMarkup(inline_keybord);*/
//
//       /* String charsToRemove = "\n";
//
//        for (char c : charsToRemove.toCharArray()) {
//            post = post.replace(String.valueOf(c), "");
//        }*/
// /*       TelegramBot bot = new TelegramBot(getBotToken());
//        bot.execute(sendMessage);*/
//       /* CustomerActions customerActions = new CustomerActions(customerData);
//        String post = customerActions.post_tostr();
//        String urlString = "https://api.telegram.org/bot5814824968:AAHriiP3p-FlWNLHn7UN8_-8ntGp1LUMEFg/sendMessage?chat_id=@matem_vedmedyk&reply_markup={\"inline_keyboard\":[[{\"text\":\""+ "BI3bMY"+"\",\"callback_data\":\"HI\"}]]}&parse_mode=HTML&text="+post;
//*/
//
///*
//        String urlString = "https://api.telegram.org/bot5814824968:AAHriiP3p-FlWNLHn7UN8_-8ntGp1LUMEFg/forwardMessage?chat_id=@matem_vedmedyk&from_chat_id=@helpstudentportal_bot&message_id="+chatId;
//*/
//
//       /* String channel_name = "@matem_vedmedyk";
//        urlString = String.format(urlString, getBotToken(), channel_name, post);
//        URL url = new URL(urlString);
//        URLConnection conn = url.openConnection();
//        StringBuilder sb = new StringBuilder();
//        InputStream is = new BufferedInputStream(conn.getInputStream());
//        BufferedReader br = new BufferedReader(new InputStreamReader(is));
//        String inputLine = "";
//        while ((inputLine = br.readLine()) != null) {
//            sb.append(inputLine);
//        }*/
        CustomerActions customerActions = new CustomerActions(customerData);
        String post = customerActions.post_tostr(0);
        /*String charsToRemove = "\n";*/

       /* for (char c : charsToRemove.toCharArray()) {
            post = post.replace(String.valueOf(c), "");
        }*/

        String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&reply_markup=%s&parse_mode=%s&text=%s";
        String chatId = "@matem_vedmedyk";
        String reply = URLEncoder.encode("{\"inline_keyboard\":[[{\"text\":\""+ "Візьму"+"\",\"callback_data\":\"Візьму\"}]]}","UTF-8");
        String parse = "HTML";
        post = URLEncoder.encode(post,"UTF-8");
        urlString = String.format(urlString, token, chatId, reply, parse, post);
        URL newurl = new URL(urlString);
        URLConnection conn = newurl.openConnection();
        StringBuilder sb = new StringBuilder();
        InputStream is = new BufferedInputStream(conn.getInputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String inputLine = "";
        String digits="";
        int check=0;
        while ((inputLine = br.readLine()) != null) {
            sb.append(inputLine);
            for (int i = 0; i < inputLine.length(); i++) {
                char chrs = inputLine.charAt(i);
                if (Character.isDigit(chrs)) {
                    digits = digits + chrs;

                }
                if(chrs == ',') {
                    check++;
                }
                if(check==2){
                    break;
                }
            }
            if(check==2){
                break;
            }
        }
        String url = "https://t.me/matem_vedmedyk/"+ digits;

     return url;

    }
    public String set_to_programm_chanal(CustomerData customerData, String token) throws IOException {
//       /* CustomerActions customerActions = new CustomerActions(customerData);
//        String post = customerActions.post_tostr();
//        SendMessage sendMessage = new SendMessage("@matem_vedmedyk", "channel by id message");
//        sendMessage.setParseMode("HTML");
//        sendMessage.setChatId(chatId);
//        sendMessage.setText(post);*/
//        /*
//        try {
//            // Send the message
//            execute(sendMessage);
//
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
//*/
//       /* URL newurl = new URL(urlString);
//        URLConnection conn = newurl.openConnection();
//        StringBuilder sb = new StringBuilder();
//        InputStream is = new BufferedInputStream(conn.getInputStream());
//        BufferedReader br = new BufferedReader(new InputStreamReader(is));
//        String inputLine = "";
//        String digits="";
//        int check=0;
//        while ((inputLine = br.readLine()) != null) {
//            sb.append(inputLine);
//            for (int i = 0; i < inputLine.length(); i++) {
//                char chrs = inputLine.charAt(i);
//                if (Character.isDigit(chrs)) {
//                    digits = digits + chrs;
//
//                }
//                if(chrs == ',') {
//                    check++;
//                }
//                if(check==2){
//                    break;
//                }
//            }
//            if(check==2){
//                break;
//            }
//        }
//        String url = "https://t.me/matem_vedmedyk"+ digits;
//        customerData.setPhoto_url(url);
//        try {
//            // Send the message
//            execute(sendMessage);
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//    }*/
//        /*TelegramBot bot = new TelegramBot(getBotToken());*/
//     /*   SendMessage sendMessage = new SendMessage();
//        sendMessage.setParseMode("HTML");
//        sendMessage.setChatId(chatId);
//        sendMessage.setText(post);
//        InlineKeyboardMarkup inline_keybord = new InlineKeyboardMarkup();
//        List<List<InlineKeyboardButton>> rows_inline = new ArrayList<>();
//
//
//
//        List<InlineKeyboardButton> row_inline=new ArrayList<>();
//        var take_Button = new InlineKeyboardButton();
//        take_Button.setText("Беру");
//        take_Button.setCallbackData(subjects.TAKE.toString());
//        row_inline.add(take_Button);
//        rows_inline.add(row_inline);
//
//        inline_keybord.setKeyboard(rows_inline);
//        sendMessage.setReplyMarkup(inline_keybord);*/
//
//       /* String charsToRemove = "\n";
//
//        for (char c : charsToRemove.toCharArray()) {
//            post = post.replace(String.valueOf(c), "");
//        }*/
// /*       TelegramBot bot = new TelegramBot(getBotToken());
//        bot.execute(sendMessage);*/
//       /* CustomerActions customerActions = new CustomerActions(customerData);
//        String post = customerActions.post_tostr();
//        String urlString = "https://api.telegram.org/bot5814824968:AAHriiP3p-FlWNLHn7UN8_-8ntGp1LUMEFg/sendMessage?chat_id=@matem_vedmedyk&reply_markup={\"inline_keyboard\":[[{\"text\":\""+ "BI3bMY"+"\",\"callback_data\":\"HI\"}]]}&parse_mode=HTML&text="+post;
//*/
//
///*
//        String urlString = "https://api.telegram.org/bot5814824968:AAHriiP3p-FlWNLHn7UN8_-8ntGp1LUMEFg/forwardMessage?chat_id=@matem_vedmedyk&from_chat_id=@helpstudentportal_bot&message_id="+chatId;
//*/
//
//       /* String channel_name = "@matem_vedmedyk";
//        urlString = String.format(urlString, getBotToken(), channel_name, post);
//        URL url = new URL(urlString);
//        URLConnection conn = url.openConnection();
//        StringBuilder sb = new StringBuilder();
//        InputStream is = new BufferedInputStream(conn.getInputStream());
//        BufferedReader br = new BufferedReader(new InputStreamReader(is));
//        String inputLine = "";
//        while ((inputLine = br.readLine()) != null) {
//            sb.append(inputLine);
//        }*/
        CustomerActions customerActions = new CustomerActions(customerData);
        String post = customerActions.post_tostr(0);
        /*String charsToRemove = "\n";*/

       /* for (char c : charsToRemove.toCharArray()) {
            post = post.replace(String.valueOf(c), "");
        }*/

        String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&reply_markup=%s&parse_mode=%s&text=%s";
        String chatId = "@program_vedmedyk";
        String reply = URLEncoder.encode("{\"inline_keyboard\":[[{\"text\":\""+ "Візьму"+"\",\"callback_data\":\"Візьму\"}]]}","UTF-8");
        String parse = "HTML";
        post = URLEncoder.encode(post,"UTF-8");
        urlString = String.format(urlString, token, chatId, reply, parse, post);
        URL newurl = new URL(urlString);
        URLConnection conn = newurl.openConnection();
        StringBuilder sb = new StringBuilder();
        InputStream is = new BufferedInputStream(conn.getInputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String inputLine = "";
        String digits="";
        int check=0;
        while ((inputLine = br.readLine()) != null) {
            sb.append(inputLine);
            for (int i = 0; i < inputLine.length(); i++) {
                char chrs = inputLine.charAt(i);
                if (Character.isDigit(chrs)) {
                    digits = digits + chrs;

                }
                if(chrs == ',') {
                    check++;
                }
                if(check==2){
                    break;
                }
            }
            if(check==2){
                break;
            }
        }
        String url = "https://t.me/program_vedmedyk/"+ digits;

        return url;
    }
    public String set_to_chemistry_chanal(CustomerData customerData, String token) throws IOException {
//       /* CustomerActions customerActions = new CustomerActions(customerData);
//        String post = customerActions.post_tostr();
//        SendMessage sendMessage = new SendMessage("@matem_vedmedyk", "channel by id message");
//        sendMessage.setParseMode("HTML");
//        sendMessage.setChatId(chatId);
//        sendMessage.setText(post);*/
//        /*
//        try {
//            // Send the message
//            execute(sendMessage);
//
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
//*/
//       /* URL newurl = new URL(urlString);
//        URLConnection conn = newurl.openConnection();
//        StringBuilder sb = new StringBuilder();
//        InputStream is = new BufferedInputStream(conn.getInputStream());
//        BufferedReader br = new BufferedReader(new InputStreamReader(is));
//        String inputLine = "";
//        String digits="";
//        int check=0;
//        while ((inputLine = br.readLine()) != null) {
//            sb.append(inputLine);
//            for (int i = 0; i < inputLine.length(); i++) {
//                char chrs = inputLine.charAt(i);
//                if (Character.isDigit(chrs)) {
//                    digits = digits + chrs;
//
//                }
//                if(chrs == ',') {
//                    check++;
//                }
//                if(check==2){
//                    break;
//                }
//            }
//            if(check==2){
//                break;
//            }
//        }
//        String url = "https://t.me/matem_vedmedyk"+ digits;
//        customerData.setPhoto_url(url);
//        try {
//            // Send the message
//            execute(sendMessage);
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//    }*/
//        /*TelegramBot bot = new TelegramBot(getBotToken());*/
//     /*   SendMessage sendMessage = new SendMessage();
//        sendMessage.setParseMode("HTML");
//        sendMessage.setChatId(chatId);
//        sendMessage.setText(post);
//        InlineKeyboardMarkup inline_keybord = new InlineKeyboardMarkup();
//        List<List<InlineKeyboardButton>> rows_inline = new ArrayList<>();
//
//
//
//        List<InlineKeyboardButton> row_inline=new ArrayList<>();
//        var take_Button = new InlineKeyboardButton();
//        take_Button.setText("Беру");
//        take_Button.setCallbackData(subjects.TAKE.toString());
//        row_inline.add(take_Button);
//        rows_inline.add(row_inline);
//
//        inline_keybord.setKeyboard(rows_inline);
//        sendMessage.setReplyMarkup(inline_keybord);*/
//
//       /* String charsToRemove = "\n";
//
//        for (char c : charsToRemove.toCharArray()) {
//            post = post.replace(String.valueOf(c), "");
//        }*/
// /*       TelegramBot bot = new TelegramBot(getBotToken());
//        bot.execute(sendMessage);*/
//       /* CustomerActions customerActions = new CustomerActions(customerData);
//        String post = customerActions.post_tostr();
//        String urlString = "https://api.telegram.org/bot5814824968:AAHriiP3p-FlWNLHn7UN8_-8ntGp1LUMEFg/sendMessage?chat_id=@matem_vedmedyk&reply_markup={\"inline_keyboard\":[[{\"text\":\""+ "BI3bMY"+"\",\"callback_data\":\"HI\"}]]}&parse_mode=HTML&text="+post;
//*/
//
///*
//        String urlString = "https://api.telegram.org/bot5814824968:AAHriiP3p-FlWNLHn7UN8_-8ntGp1LUMEFg/forwardMessage?chat_id=@matem_vedmedyk&from_chat_id=@helpstudentportal_bot&message_id="+chatId;
//*/
//
//       /* String channel_name = "@matem_vedmedyk";
//        urlString = String.format(urlString, getBotToken(), channel_name, post);
//        URL url = new URL(urlString);
//        URLConnection conn = url.openConnection();
//        StringBuilder sb = new StringBuilder();
//        InputStream is = new BufferedInputStream(conn.getInputStream());
//        BufferedReader br = new BufferedReader(new InputStreamReader(is));
//        String inputLine = "";
//        while ((inputLine = br.readLine()) != null) {
//            sb.append(inputLine);
//        }*/
        CustomerActions customerActions = new CustomerActions(customerData);
        String post = customerActions.post_tostr(0);
        /*String charsToRemove = "\n";*/

       /* for (char c : charsToRemove.toCharArray()) {
            post = post.replace(String.valueOf(c), "");
        }*/

        String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&reply_markup=%s&parse_mode=%s&text=%s";
        String chatId = "@chemistry_vedmedyk";
        String reply = URLEncoder.encode("{\"inline_keyboard\":[[{\"text\":\""+ "Візьму"+"\",\"callback_data\":\"Візьму\"}]]}","UTF-8");
        String parse = "HTML";
        post = URLEncoder.encode(post,"UTF-8");
        urlString = String.format(urlString, token, chatId, reply, parse, post);
        URL newurl = new URL(urlString);
        URLConnection conn = newurl.openConnection();
        StringBuilder sb = new StringBuilder();
        InputStream is = new BufferedInputStream(conn.getInputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String inputLine = "";
        String digits="";
        int check=0;
        while ((inputLine = br.readLine()) != null) {
            sb.append(inputLine);
            for (int i = 0; i < inputLine.length(); i++) {
                char chrs = inputLine.charAt(i);
                if (Character.isDigit(chrs)) {
                    digits = digits + chrs;

                }
                if(chrs == ',') {
                    check++;
                }
                if(check==2){
                    break;
                }
            }
            if(check==2){
                break;
            }
        }
        String url = "https://t.me/chemistry_vedmedyk/"+ digits;

        return url;

    }
    public String set_to_phylosophy_chanal(CustomerData customerData, String token) throws IOException {
//       /* CustomerActions customerActions = new CustomerActions(customerData);
//        String post = customerActions.post_tostr();
//        SendMessage sendMessage = new SendMessage("@matem_vedmedyk", "channel by id message");
//        sendMessage.setParseMode("HTML");
//        sendMessage.setChatId(chatId);
//        sendMessage.setText(post);*/
//        /*
//        try {
//            // Send the message
//            execute(sendMessage);
//
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
//*/
//       /* URL newurl = new URL(urlString);
//        URLConnection conn = newurl.openConnection();
//        StringBuilder sb = new StringBuilder();
//        InputStream is = new BufferedInputStream(conn.getInputStream());
//        BufferedReader br = new BufferedReader(new InputStreamReader(is));
//        String inputLine = "";
//        String digits="";
//        int check=0;
//        while ((inputLine = br.readLine()) != null) {
//            sb.append(inputLine);
//            for (int i = 0; i < inputLine.length(); i++) {
//                char chrs = inputLine.charAt(i);
//                if (Character.isDigit(chrs)) {
//                    digits = digits + chrs;
//
//                }
//                if(chrs == ',') {
//                    check++;
//                }
//                if(check==2){
//                    break;
//                }
//            }
//            if(check==2){
//                break;
//            }
//        }
//        String url = "https://t.me/matem_vedmedyk"+ digits;
//        customerData.setPhoto_url(url);
//        try {
//            // Send the message
//            execute(sendMessage);
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//    }*/
//        /*TelegramBot bot = new TelegramBot(getBotToken());*/
//     /*   SendMessage sendMessage = new SendMessage();
//        sendMessage.setParseMode("HTML");
//        sendMessage.setChatId(chatId);
//        sendMessage.setText(post);
//        InlineKeyboardMarkup inline_keybord = new InlineKeyboardMarkup();
//        List<List<InlineKeyboardButton>> rows_inline = new ArrayList<>();
//
//
//
//        List<InlineKeyboardButton> row_inline=new ArrayList<>();
//        var take_Button = new InlineKeyboardButton();
//        take_Button.setText("Беру");
//        take_Button.setCallbackData(subjects.TAKE.toString());
//        row_inline.add(take_Button);
//        rows_inline.add(row_inline);
//
//        inline_keybord.setKeyboard(rows_inline);
//        sendMessage.setReplyMarkup(inline_keybord);*/
//
//       /* String charsToRemove = "\n";
//
//        for (char c : charsToRemove.toCharArray()) {
//            post = post.replace(String.valueOf(c), "");
//        }*/
// /*       TelegramBot bot = new TelegramBot(getBotToken());
//        bot.execute(sendMessage);*/
//       /* CustomerActions customerActions = new CustomerActions(customerData);
//        String post = customerActions.post_tostr();
//        String urlString = "https://api.telegram.org/bot5814824968:AAHriiP3p-FlWNLHn7UN8_-8ntGp1LUMEFg/sendMessage?chat_id=@matem_vedmedyk&reply_markup={\"inline_keyboard\":[[{\"text\":\""+ "BI3bMY"+"\",\"callback_data\":\"HI\"}]]}&parse_mode=HTML&text="+post;
//*/
//
///*
//        String urlString = "https://api.telegram.org/bot5814824968:AAHriiP3p-FlWNLHn7UN8_-8ntGp1LUMEFg/forwardMessage?chat_id=@matem_vedmedyk&from_chat_id=@helpstudentportal_bot&message_id="+chatId;
//*/
//
//       /* String channel_name = "@matem_vedmedyk";
//        urlString = String.format(urlString, getBotToken(), channel_name, post);
//        URL url = new URL(urlString);
//        URLConnection conn = url.openConnection();
//        StringBuilder sb = new StringBuilder();
//        InputStream is = new BufferedInputStream(conn.getInputStream());
//        BufferedReader br = new BufferedReader(new InputStreamReader(is));
//        String inputLine = "";
//        while ((inputLine = br.readLine()) != null) {
//            sb.append(inputLine);
//        }*/
        CustomerActions customerActions = new CustomerActions(customerData);
        String post = customerActions.post_tostr(0);
        /*String charsToRemove = "\n";*/

       /* for (char c : charsToRemove.toCharArray()) {
            post = post.replace(String.valueOf(c), "");
        }*/

        String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&reply_markup=%s&parse_mode=%s&text=%s";
        String chatId = "@phylosophy_vedmedyk";
        String reply = URLEncoder.encode("{\"inline_keyboard\":[[{\"text\":\""+ "Візьму"+"\",\"callback_data\":\"Візьму\"}]]}","UTF-8");
        String parse = "HTML";
        post = URLEncoder.encode(post,"UTF-8");
        urlString = String.format(urlString, token, chatId, reply, parse, post);
        URL newurl = new URL(urlString);
        URLConnection conn = newurl.openConnection();
        StringBuilder sb = new StringBuilder();
        InputStream is = new BufferedInputStream(conn.getInputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String inputLine = "";
        String digits="";
        int check=0;
        while ((inputLine = br.readLine()) != null) {
            sb.append(inputLine);
            for (int i = 0; i < inputLine.length(); i++) {
                char chrs = inputLine.charAt(i);
                if (Character.isDigit(chrs)) {
                    digits = digits + chrs;

                }
                if(chrs == ',') {
                    check++;
                }
                if(check==2){
                    break;
                }
            }
            if(check==2){
                break;
            }
        }
        String url = "https://t.me/phylosophy_vedmedyk/"+ digits;

        return url;
    }
    public String set_to_main_chanal(CustomerData customerData, String token) throws IOException {
//       /* CustomerActions customerActions = new CustomerActions(customerData);
//        String post = customerActions.post_tostr();
//        SendMessage sendMessage = new SendMessage("@matem_vedmedyk", "channel by id message");
//        sendMessage.setParseMode("HTML");
//        sendMessage.setChatId(chatId);
//        sendMessage.setText(post);*/
//        /*
//        try {
//            // Send the message
//            execute(sendMessage);
//
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
//*/
//       /* URL newurl = new URL(urlString);
//        URLConnection conn = newurl.openConnection();
//        StringBuilder sb = new StringBuilder();
//        InputStream is = new BufferedInputStream(conn.getInputStream());
//        BufferedReader br = new BufferedReader(new InputStreamReader(is));
//        String inputLine = "";
//        String digits="";
//        int check=0;
//        while ((inputLine = br.readLine()) != null) {
//            sb.append(inputLine);
//            for (int i = 0; i < inputLine.length(); i++) {
//                char chrs = inputLine.charAt(i);
//                if (Character.isDigit(chrs)) {
//                    digits = digits + chrs;
//
//                }
//                if(chrs == ',') {
//                    check++;
//                }
//                if(check==2){
//                    break;
//                }
//            }
//            if(check==2){
//                break;
//            }
//        }
//        String url = "https://t.me/matem_vedmedyk"+ digits;
//        customerData.setPhoto_url(url);
//        try {
//            // Send the message
//            execute(sendMessage);
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//    }*/
//        /*TelegramBot bot = new TelegramBot(getBotToken());*/
//     /*   SendMessage sendMessage = new SendMessage();
//        sendMessage.setParseMode("HTML");
//        sendMessage.setChatId(chatId);
//        sendMessage.setText(post);
//        InlineKeyboardMarkup inline_keybord = new InlineKeyboardMarkup();
//        List<List<InlineKeyboardButton>> rows_inline = new ArrayList<>();
//
//
//
//        List<InlineKeyboardButton> row_inline=new ArrayList<>();
//        var take_Button = new InlineKeyboardButton();
//        take_Button.setText("Беру");
//        take_Button.setCallbackData(subjects.TAKE.toString());
//        row_inline.add(take_Button);
//        rows_inline.add(row_inline);
//
//        inline_keybord.setKeyboard(rows_inline);
//        sendMessage.setReplyMarkup(inline_keybord);*/
//
//       /* String charsToRemove = "\n";
//
//        for (char c : charsToRemove.toCharArray()) {
//            post = post.replace(String.valueOf(c), "");
//        }*/
// /*       TelegramBot bot = new TelegramBot(getBotToken());
//        bot.execute(sendMessage);*/
//       /* CustomerActions customerActions = new CustomerActions(customerData);
//        String post = customerActions.post_tostr();
//        String urlString = "https://api.telegram.org/bot5814824968:AAHriiP3p-FlWNLHn7UN8_-8ntGp1LUMEFg/sendMessage?chat_id=@matem_vedmedyk&reply_markup={\"inline_keyboard\":[[{\"text\":\""+ "BI3bMY"+"\",\"callback_data\":\"HI\"}]]}&parse_mode=HTML&text="+post;
//*/
//
///*
//        String urlString = "https://api.telegram.org/bot5814824968:AAHriiP3p-FlWNLHn7UN8_-8ntGp1LUMEFg/forwardMessage?chat_id=@matem_vedmedyk&from_chat_id=@helpstudentportal_bot&message_id="+chatId;
//*/
//
//       /* String channel_name = "@matem_vedmedyk";
//        urlString = String.format(urlString, getBotToken(), channel_name, post);
//        URL url = new URL(urlString);
//        URLConnection conn = url.openConnection();
//        StringBuilder sb = new StringBuilder();
//        InputStream is = new BufferedInputStream(conn.getInputStream());
//        BufferedReader br = new BufferedReader(new InputStreamReader(is));
//        String inputLine = "";
//        while ((inputLine = br.readLine()) != null) {
//            sb.append(inputLine);
//        }*/
        CustomerActions customerActions = new CustomerActions(customerData);
        String post = customerActions.post_tostr(0);
        /*String charsToRemove = "\n";*/

       /* for (char c : charsToRemove.toCharArray()) {
            post = post.replace(String.valueOf(c), "");
        }*/

        String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&reply_markup=%s&parse_mode=%s&text=%s";
        String chatId = "@main_vedmedyk";
        String reply = URLEncoder.encode("{\"inline_keyboard\":[[{\"text\":\""+ "Візьму"+"\",\"callback_data\":\"Візьму\"}]]}","UTF-8");
        String parse = "HTML";
        post = URLEncoder.encode(post,"UTF-8");
        urlString = String.format(urlString, token, chatId, reply, parse, post);
        URL newurl = new URL(urlString);
        URLConnection conn = newurl.openConnection();
        StringBuilder sb = new StringBuilder();
        InputStream is = new BufferedInputStream(conn.getInputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String inputLine = "";
        String digits="";
        int check=0;
        while ((inputLine = br.readLine()) != null) {
            sb.append(inputLine);
            for (int i = 0; i < inputLine.length(); i++) {
                char chrs = inputLine.charAt(i);
                if (Character.isDigit(chrs)) {
                    digits = digits + chrs;

                }
                if(chrs == ',') {
                    check++;
                }
                if(check==2){
                    break;
                }
            }
            if(check==2){
                break;
            }
        }
        String url = "https://t.me/main_vedmedyk/"+ digits;

        return url;
    }
    public String set_to_medic_chanal(CustomerData customerData, String token) throws IOException {
//       /* CustomerActions customerActions = new CustomerActions(customerData);
//        String post = customerActions.post_tostr();
//        SendMessage sendMessage = new SendMessage("@matem_vedmedyk", "channel by id message");
//        sendMessage.setParseMode("HTML");
//        sendMessage.setChatId(chatId);
//        sendMessage.setText(post);*/
//        /*
//        try {
//            // Send the message
//            execute(sendMessage);
//
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
//*/
//       /* URL newurl = new URL(urlString);
//        URLConnection conn = newurl.openConnection();
//        StringBuilder sb = new StringBuilder();
//        InputStream is = new BufferedInputStream(conn.getInputStream());
//        BufferedReader br = new BufferedReader(new InputStreamReader(is));
//        String inputLine = "";
//        String digits="";
//        int check=0;
//        while ((inputLine = br.readLine()) != null) {
//            sb.append(inputLine);
//            for (int i = 0; i < inputLine.length(); i++) {
//                char chrs = inputLine.charAt(i);
//                if (Character.isDigit(chrs)) {
//                    digits = digits + chrs;
//
//                }
//                if(chrs == ',') {
//                    check++;
//                }
//                if(check==2){
//                    break;
//                }
//            }
//            if(check==2){
//                break;
//            }
//        }
//        String url = "https://t.me/matem_vedmedyk"+ digits;
//        customerData.setPhoto_url(url);
//        try {
//            // Send the message
//            execute(sendMessage);
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//    }*/
//        /*TelegramBot bot = new TelegramBot(getBotToken());*/
//     /*   SendMessage sendMessage = new SendMessage();
//        sendMessage.setParseMode("HTML");
//        sendMessage.setChatId(chatId);
//        sendMessage.setText(post);
//        InlineKeyboardMarkup inline_keybord = new InlineKeyboardMarkup();
//        List<List<InlineKeyboardButton>> rows_inline = new ArrayList<>();
//
//
//
//        List<InlineKeyboardButton> row_inline=new ArrayList<>();
//        var take_Button = new InlineKeyboardButton();
//        take_Button.setText("Беру");
//        take_Button.setCallbackData(subjects.TAKE.toString());
//        row_inline.add(take_Button);
//        rows_inline.add(row_inline);
//
//        inline_keybord.setKeyboard(rows_inline);
//        sendMessage.setReplyMarkup(inline_keybord);*/
//
//       /* String charsToRemove = "\n";
//
//        for (char c : charsToRemove.toCharArray()) {
//            post = post.replace(String.valueOf(c), "");
//        }*/
// /*       TelegramBot bot = new TelegramBot(getBotToken());
//        bot.execute(sendMessage);*/
//       /* CustomerActions customerActions = new CustomerActions(customerData);
//        String post = customerActions.post_tostr();
//        String urlString = "https://api.telegram.org/bot5814824968:AAHriiP3p-FlWNLHn7UN8_-8ntGp1LUMEFg/sendMessage?chat_id=@matem_vedmedyk&reply_markup={\"inline_keyboard\":[[{\"text\":\""+ "BI3bMY"+"\",\"callback_data\":\"HI\"}]]}&parse_mode=HTML&text="+post;
//*/
//
///*
//        String urlString = "https://api.telegram.org/bot5814824968:AAHriiP3p-FlWNLHn7UN8_-8ntGp1LUMEFg/forwardMessage?chat_id=@matem_vedmedyk&from_chat_id=@helpstudentportal_bot&message_id="+chatId;
//*/
//
//       /* String channel_name = "@matem_vedmedyk";
//        urlString = String.format(urlString, getBotToken(), channel_name, post);
//        URL url = new URL(urlString);
//        URLConnection conn = url.openConnection();
//        StringBuilder sb = new StringBuilder();
//        InputStream is = new BufferedInputStream(conn.getInputStream());
//        BufferedReader br = new BufferedReader(new InputStreamReader(is));
//        String inputLine = "";
//        while ((inputLine = br.readLine()) != null) {
//            sb.append(inputLine);
//        }*/
        CustomerActions customerActions = new CustomerActions(customerData);
        String post = customerActions.post_tostr(0);
        /*String charsToRemove = "\n";*/

       /* for (char c : charsToRemove.toCharArray()) {
            post = post.replace(String.valueOf(c), "");
        }*/

        String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&reply_markup=%s&parse_mode=%s&text=%s";
        String chatId = "@medicine_vedmedyk";
        String reply = URLEncoder.encode("{\"inline_keyboard\":[[{\"text\":\""+ "Візьму"+"\",\"callback_data\":\"Візьму\"}]]}","UTF-8");
        String parse = "HTML";
        post = URLEncoder.encode(post,"UTF-8");
        urlString = String.format(urlString, token, chatId, reply, parse, post);
        URL newurl = new URL(urlString);
        URLConnection conn = newurl.openConnection();
        StringBuilder sb = new StringBuilder();
        InputStream is = new BufferedInputStream(conn.getInputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String inputLine = "";
        String digits="";
        int check=0;
        while ((inputLine = br.readLine()) != null) {
            sb.append(inputLine);
            for (int i = 0; i < inputLine.length(); i++) {
                char chrs = inputLine.charAt(i);
                if (Character.isDigit(chrs)) {
                    digits = digits + chrs;

                }
                if(chrs == ',') {
                    check++;
                }
                if(check==2){
                    break;
                }
            }
            if(check==2){
                break;
            }
        }
        String url = "https://t.me/medicine_vedmedyk/"+ digits;

        return url;
    }
    public String set_to_language_chanal(CustomerData customerData, String token) throws IOException {
//       /* CustomerActions customerActions = new CustomerActions(customerData);
//        String post = customerActions.post_tostr();
//        SendMessage sendMessage = new SendMessage("@matem_vedmedyk", "channel by id message");
//        sendMessage.setParseMode("HTML");
//        sendMessage.setChatId(chatId);
//        sendMessage.setText(post);*/
//        /*
//        try {
//            // Send the message
//            execute(sendMessage);
//
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
//*/
//       /* URL newurl = new URL(urlString);
//        URLConnection conn = newurl.openConnection();
//        StringBuilder sb = new StringBuilder();
//        InputStream is = new BufferedInputStream(conn.getInputStream());
//        BufferedReader br = new BufferedReader(new InputStreamReader(is));
//        String inputLine = "";
//        String digits="";
//        int check=0;
//        while ((inputLine = br.readLine()) != null) {
//            sb.append(inputLine);
//            for (int i = 0; i < inputLine.length(); i++) {
//                char chrs = inputLine.charAt(i);
//                if (Character.isDigit(chrs)) {
//                    digits = digits + chrs;
//
//                }
//                if(chrs == ',') {
//                    check++;
//                }
//                if(check==2){
//                    break;
//                }
//            }
//            if(check==2){
//                break;
//            }
//        }
//        String url = "https://t.me/matem_vedmedyk"+ digits;
//        customerData.setPhoto_url(url);
//        try {
//            // Send the message
//            execute(sendMessage);
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//    }*/
//        /*TelegramBot bot = new TelegramBot(getBotToken());*/
//     /*   SendMessage sendMessage = new SendMessage();
//        sendMessage.setParseMode("HTML");
//        sendMessage.setChatId(chatId);
//        sendMessage.setText(post);
//        InlineKeyboardMarkup inline_keybord = new InlineKeyboardMarkup();
//        List<List<InlineKeyboardButton>> rows_inline = new ArrayList<>();
//
//
//
//        List<InlineKeyboardButton> row_inline=new ArrayList<>();
//        var take_Button = new InlineKeyboardButton();
//        take_Button.setText("Беру");
//        take_Button.setCallbackData(subjects.TAKE.toString());
//        row_inline.add(take_Button);
//        rows_inline.add(row_inline);
//
//        inline_keybord.setKeyboard(rows_inline);
//        sendMessage.setReplyMarkup(inline_keybord);*/
//
//       /* String charsToRemove = "\n";
//
//        for (char c : charsToRemove.toCharArray()) {
//            post = post.replace(String.valueOf(c), "");
//        }*/
// /*       TelegramBot bot = new TelegramBot(getBotToken());
//        bot.execute(sendMessage);*/
//       /* CustomerActions customerActions = new CustomerActions(customerData);
//        String post = customerActions.post_tostr();
//        String urlString = "https://api.telegram.org/bot5814824968:AAHriiP3p-FlWNLHn7UN8_-8ntGp1LUMEFg/sendMessage?chat_id=@matem_vedmedyk&reply_markup={\"inline_keyboard\":[[{\"text\":\""+ "BI3bMY"+"\",\"callback_data\":\"HI\"}]]}&parse_mode=HTML&text="+post;
//*/
//
///*
//        String urlString = "https://api.telegram.org/bot5814824968:AAHriiP3p-FlWNLHn7UN8_-8ntGp1LUMEFg/forwardMessage?chat_id=@matem_vedmedyk&from_chat_id=@helpstudentportal_bot&message_id="+chatId;
//*/
//
//       /* String channel_name = "@matem_vedmedyk";
//        urlString = String.format(urlString, getBotToken(), channel_name, post);
//        URL url = new URL(urlString);
//        URLConnection conn = url.openConnection();
//        StringBuilder sb = new StringBuilder();
//        InputStream is = new BufferedInputStream(conn.getInputStream());
//        BufferedReader br = new BufferedReader(new InputStreamReader(is));
//        String inputLine = "";
//        while ((inputLine = br.readLine()) != null) {
//            sb.append(inputLine);
//        }*/
        CustomerActions customerActions = new CustomerActions(customerData);
        String post = customerActions.post_tostr(0);
        /*String charsToRemove = "\n";*/

       /* for (char c : charsToRemove.toCharArray()) {
            post = post.replace(String.valueOf(c), "");
        }*/

        String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&reply_markup=%s&parse_mode=%s&text=%s";
        String chatId = "@languages_vedmedyk";
        String reply = URLEncoder.encode("{\"inline_keyboard\":[[{\"text\":\""+ "Візьму"+"\",\"callback_data\":\"Візьму\"}]]}","UTF-8");
        String parse = "HTML";
        post = URLEncoder.encode(post,"UTF-8");
        urlString = String.format(urlString, token, chatId, reply, parse, post);
        URL newurl = new URL(urlString);
        URLConnection conn = newurl.openConnection();
        StringBuilder sb = new StringBuilder();
        InputStream is = new BufferedInputStream(conn.getInputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String inputLine = "";
        String digits="";
        int check=0;
        while ((inputLine = br.readLine()) != null) {
            sb.append(inputLine);
            for (int i = 0; i < inputLine.length(); i++) {
                char chrs = inputLine.charAt(i);
                if (Character.isDigit(chrs)) {
                    digits = digits + chrs;

                }
                if(chrs == ',') {
                    check++;
                }
                if(check==2){
                    break;
                }
            }
            if(check==2){
                break;
            }
        }
        String url = "https://t.me/languages_vedmedyk/"+ digits;

        return url;
    }
    public String set_to_geography_chanal(CustomerData customerData, String token) throws IOException {
//       /* CustomerActions customerActions = new CustomerActions(customerData);
//        String post = customerActions.post_tostr();
//        SendMessage sendMessage = new SendMessage("@matem_vedmedyk", "channel by id message");
//        sendMessage.setParseMode("HTML");
//        sendMessage.setChatId(chatId);
//        sendMessage.setText(post);*/
//        /*
//        try {
//            // Send the message
//            execute(sendMessage);
//
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
//*/
//       /* URL newurl = new URL(urlString);
//        URLConnection conn = newurl.openConnection();
//        StringBuilder sb = new StringBuilder();
//        InputStream is = new BufferedInputStream(conn.getInputStream());
//        BufferedReader br = new BufferedReader(new InputStreamReader(is));
//        String inputLine = "";
//        String digits="";
//        int check=0;
//        while ((inputLine = br.readLine()) != null) {
//            sb.append(inputLine);
//            for (int i = 0; i < inputLine.length(); i++) {
//                char chrs = inputLine.charAt(i);
//                if (Character.isDigit(chrs)) {
//                    digits = digits + chrs;
//
//                }
//                if(chrs == ',') {
//                    check++;
//                }
//                if(check==2){
//                    break;
//                }
//            }
//            if(check==2){
//                break;
//            }
//        }
//        String url = "https://t.me/matem_vedmedyk"+ digits;
//        customerData.setPhoto_url(url);
//        try {
//            // Send the message
//            execute(sendMessage);
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//    }*/
//        /*TelegramBot bot = new TelegramBot(getBotToken());*/
//     /*   SendMessage sendMessage = new SendMessage();
//        sendMessage.setParseMode("HTML");
//        sendMessage.setChatId(chatId);
//        sendMessage.setText(post);
//        InlineKeyboardMarkup inline_keybord = new InlineKeyboardMarkup();
//        List<List<InlineKeyboardButton>> rows_inline = new ArrayList<>();
//
//
//
//        List<InlineKeyboardButton> row_inline=new ArrayList<>();
//        var take_Button = new InlineKeyboardButton();
//        take_Button.setText("Беру");
//        take_Button.setCallbackData(subjects.TAKE.toString());
//        row_inline.add(take_Button);
//        rows_inline.add(row_inline);
//
//        inline_keybord.setKeyboard(rows_inline);
//        sendMessage.setReplyMarkup(inline_keybord);*/
//
//       /* String charsToRemove = "\n";
//
//        for (char c : charsToRemove.toCharArray()) {
//            post = post.replace(String.valueOf(c), "");
//        }*/
// /*       TelegramBot bot = new TelegramBot(getBotToken());
//        bot.execute(sendMessage);*/
//       /* CustomerActions customerActions = new CustomerActions(customerData);
//        String post = customerActions.post_tostr();
//        String urlString = "https://api.telegram.org/bot5814824968:AAHriiP3p-FlWNLHn7UN8_-8ntGp1LUMEFg/sendMessage?chat_id=@matem_vedmedyk&reply_markup={\"inline_keyboard\":[[{\"text\":\""+ "BI3bMY"+"\",\"callback_data\":\"HI\"}]]}&parse_mode=HTML&text="+post;
//*/
//
///*
//        String urlString = "https://api.telegram.org/bot5814824968:AAHriiP3p-FlWNLHn7UN8_-8ntGp1LUMEFg/forwardMessage?chat_id=@matem_vedmedyk&from_chat_id=@helpstudentportal_bot&message_id="+chatId;
//*/
//
//       /* String channel_name = "@matem_vedmedyk";
//        urlString = String.format(urlString, getBotToken(), channel_name, post);
//        URL url = new URL(urlString);
//        URLConnection conn = url.openConnection();
//        StringBuilder sb = new StringBuilder();
//        InputStream is = new BufferedInputStream(conn.getInputStream());
//        BufferedReader br = new BufferedReader(new InputStreamReader(is));
//        String inputLine = "";
//        while ((inputLine = br.readLine()) != null) {
//            sb.append(inputLine);
//        }*/
        CustomerActions customerActions = new CustomerActions(customerData);
        String post = customerActions.post_tostr(0);
        /*String charsToRemove = "\n";*/

       /* for (char c : charsToRemove.toCharArray()) {
            post = post.replace(String.valueOf(c), "");
        }*/

        String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&reply_markup=%s&parse_mode=%s&text=%s";
        String chatId = "@geogtaphy_vedmedyk";
        String reply = URLEncoder.encode("{\"inline_keyboard\":[[{\"text\":\""+ "Візьму"+"\",\"callback_data\":\"Візьму\"}]]}","UTF-8");
        String parse = "HTML";
        post = URLEncoder.encode(post,"UTF-8");
        urlString = String.format(urlString, token, chatId, reply, parse, post);
        URL newurl = new URL(urlString);
        URLConnection conn = newurl.openConnection();
        StringBuilder sb = new StringBuilder();
        InputStream is = new BufferedInputStream(conn.getInputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String inputLine = "";
        String digits="";
        int check=0;
        while ((inputLine = br.readLine()) != null) {
            sb.append(inputLine);
            for (int i = 0; i < inputLine.length(); i++) {
                char chrs = inputLine.charAt(i);
                if (Character.isDigit(chrs)) {
                    digits = digits + chrs;

                }
                if(chrs == ',') {
                    check++;
                }
                if(check==2){
                    break;
                }
            }
            if(check==2){
                break;
            }
        }
        String url = "https://t.me/geogtaphy_vedmedyk/"+ digits;

        return url;
    }
}
