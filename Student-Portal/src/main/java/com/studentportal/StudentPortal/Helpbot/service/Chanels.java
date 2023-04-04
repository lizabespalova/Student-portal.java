package com.studentportal.StudentPortal.Helpbot.service;

import com.studentportal.StudentPortal.Helpbot.config.HelpbotConfig;
import com.studentportal.StudentPortal.Helpbot.model.Customer;
import com.studentportal.StudentPortal.Helpbot.model.CustomerRepository;
import com.studentportal.StudentPortal.Helpbot.model.Post;
import com.studentportal.StudentPortal.Helpbot.model.PostRepository;
import org.springframework.context.event.ContextRefreshedEvent;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.w3c.dom.events.EventListener;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class Chanels {
    private Text const_text;
    private HelpbotConfig config;
    public String set_to_matem_chanal(CustomerRepository customerRepository, String token, Message message, PostRepository postRepository) throws IOException {

        CustomerActions customerActions = new CustomerActions(customerRepository);
        String post = customerActions.post_tostr(0,message);
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
       String urlList=("https://t.me/matem_vedmedyk/"+ digits);
        save(customerRepository, message, urlList);
        Post newPost = new Post();
        newPost.setMessageID(Integer.valueOf(digits));
        newPost.setLink(urlList);
        newPost.setCustomer_id(message.getChatId());
        postRepository.save(newPost);
     return urlList;

    }
    public String set_to_programm_chanal(CustomerRepository customerRepository, String token, Message message, PostRepository postRepository) throws IOException {
        CustomerActions customerActions = new CustomerActions(customerRepository);
        String post = customerActions.post_tostr(0,message);
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
        String urlList = "https://t.me/program_vedmedyk/"+ digits;
        save(customerRepository, message, urlList);
        Post newPost = new Post();
        newPost.setMessageID(Integer.valueOf(digits));
        newPost.setLink(urlList);
        newPost.setCustomer_id(message.getChatId());
        postRepository.save(newPost);
        return urlList;
    }
    public String set_to_chemistry_chanal(CustomerRepository customerRepository, String token,Message message, PostRepository postRepository) throws IOException {
        CustomerActions customerActions = new CustomerActions(customerRepository);
        String post = customerActions.post_tostr(0, message);
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
       String urlList=("https://t.me/chemistry_vedmedyk/" + digits);
        save(customerRepository, message, urlList);
        Post newPost = new Post();
        newPost.setMessageID(Integer.valueOf(digits));
        newPost.setLink(urlList);
        newPost.setCustomer_id(message.getChatId());
        postRepository.save(newPost);
        return urlList;
    }
    public String set_to_phylosophy_chanal(CustomerRepository customerRepository, String token, Message message, PostRepository postRepository) throws IOException {
        CustomerActions customerActions = new CustomerActions(customerRepository);
        String post = customerActions.post_tostr(0, message);
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
        String urlList="https://t.me/phylosophy_vedmedyk/"+ digits;
        save(customerRepository, message, urlList);
        Post newPost = new Post();
        newPost.setMessageID(Integer.valueOf(digits));
        newPost.setLink(urlList);
        newPost.setCustomer_id(message.getChatId());
        postRepository.save(newPost);
        return urlList;
    }
    public String set_to_main_chanal(CustomerRepository customerRepository, String token, Message message, PostRepository postRepository) throws IOException {
        CustomerActions customerActions = new CustomerActions(customerRepository);
        String post = customerActions.post_tostr(0, message);
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
        String urlList="https://t.me/main_vedmedyk/"+ digits;
        save(customerRepository, message, urlList);
        Post newPost = new Post();
        newPost.setMessageID(Integer.valueOf(digits));
        newPost.setLink(urlList);
        newPost.setCustomer_id(message.getChatId());
        postRepository.save(newPost);
        return urlList;
    }
    public String set_to_medic_chanal(CustomerRepository customerRepository, String token, Message message, PostRepository postRepository) throws IOException {
        CustomerActions customerActions = new CustomerActions(customerRepository);
        String post = customerActions.post_tostr(0,message);
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
        String urlList="https://t.me/medicine_vedmedyk/"+ digits;
        save(customerRepository, message, urlList);
        Post newPost = new Post();
        newPost.setMessageID(Integer.valueOf(digits));
        newPost.setLink(urlList);
        newPost.setCustomer_id(message.getChatId());
        postRepository.save(newPost);
        return urlList;
    }
    public String set_to_language_chanal(CustomerRepository customerRepository, String token, Message message, PostRepository postRepository) throws IOException {
        CustomerActions customerActions = new CustomerActions(customerRepository);
        String post = customerActions.post_tostr(0, message);
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
        String urlList="https://t.me/languages_vedmedyk/"+ digits;
        save(customerRepository, message, urlList);
        Post newPost = new Post();
        newPost.setMessageID(Integer.valueOf(digits));
        newPost.setLink(urlList);
        newPost.setCustomer_id(message.getChatId());
        postRepository.save(newPost);
        return urlList;
    }
    public String set_to_geography_chanal(CustomerRepository customerRepository, String token, Message message, PostRepository postRepository) throws IOException {
        CustomerActions customerActions = new CustomerActions(customerRepository);
        String post = customerActions.post_tostr(0, message);
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
        String urlList= "https://t.me/geogtaphy_vedmedyk/"+ digits;
        save(customerRepository, message, urlList);
        Post newPost = new Post();
        newPost.setMessageID(Integer.valueOf(digits));
        newPost.setLink(urlList);
        newPost.setCustomer_id(message.getChatId());
        postRepository.save(newPost);
        return urlList;
    }
    public void save(CustomerRepository customerRepository, Message message, String urlList){
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
        customer.setCheckDescriptionState(customerRepository.findById(message.getChatId()).get().getCheckDescriptionState());
        customer.setPostLink(urlList);
        customerRepository.save(customer);
    }
}
