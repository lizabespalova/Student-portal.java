package com.studentportal.StudentPortal.Helpbot.service;
import org.jetbrains.annotations.NotNull;
import org.telegram.telegrambots.meta.api.methods.groupadministration.ExportChatInviteLink;
import com.studentportal.StudentPortal.Helpbot.config.HelpbotConfig;
import com.studentportal.StudentPortal.Helpbot.model.*;
import com.vdurmont.emoji.EmojiParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.AnswerPreCheckoutQuery;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.invoices.CreateInvoiceLink;
import org.telegram.telegrambots.meta.api.methods.pinnedmessages.PinChatMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.payments.LabeledPrice;
import org.telegram.telegrambots.meta.api.objects.payments.PreCheckoutQuery;
import org.telegram.telegrambots.meta.api.objects.payments.SuccessfulPayment;
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
import java.util.Date;
import java.util.List;
import java.util.Random;


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
//    private boolean check_state = true;
 /*   private String performer_id;*/
    private Chanels chanel;
    private Text const_text;
    final HelpbotConfig config;
    private String text;
    private Subjects subjects;
    private Quiz quiz;
//    private long customers_id;
//    private String price;


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
//                register_to_data_base_customer(update.getCallbackQuery().getMessage());

                setBranchToTable(update.getCallbackQuery().getMessage(),strsubject);
                if(!customerRepository.findById(update.getCallbackQuery().getMessage().getChatId()).get().isCheck_state()/*.getCheck_state()*/){set_post(String.valueOf(chatID),0,update.getCallbackQuery().getMessage().getMessageId(),update.getCallbackQuery().getMessage()); set_last_buttons(String.valueOf(chatID));}
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

                int roomId=return_chat_link_and_show_sms_in_group(chatID, update);
               return_chat_link_and_show_sms_for_performer_in_group( performerID, postID, roomId);
                try {
                    set_sms_in_chat(roomId);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            /* else if (messagetext.contains(subjects.LINK.toString())) {
                String roomID="";
                for (int i = 0; i < messagetext.length(); i++) {
                 if(Character.isDigit(messagetext.charAt(i))){
                        roomID+=roomID+messagetext.charAt(i);
                    }
                }
                try {
                    set_sms_in_chat(roomID);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }*/
            else if (messagetext.equals(subjects.PERFORMER_REGISTER.toString())) {
                register_to_data_base_performer(update.getCallbackQuery().getFrom(), String.valueOf(chatID));
            }
            else if (messagetext.equals("PRICE")) {
                String Payload = "";
                for(int i=0; i<roomsRepository.count();i++){
                  if(roomsRepository.findById(i+1).get().getRoomID().equals(update.getCallbackQuery().getMessage().getChat().getId())){
                      Payload = roomsRepository.findById(i+1).get().getPayload();
                      break;
                  }
                }
              if(Payload==null||purchaseRepository.findById(Payload).get().getPriceToPerformer()==0) {
                    try {
                        fix_finish_text_price_customer(update.getCallbackQuery().getMessage());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }else{blockActionsWhilePayed(update);}
            }
            else if (messagetext.equals("CLOSE")) {
                setSurveyOrEnd(update);
            }
            else if (messagetext.equals("YES")) {
                String Payload = "";
                long performerID=0;
                for(int i=0; i<roomsRepository.count();i++){
                    if(roomsRepository.findById(i+1).get().getRoomID().equals(update.getCallbackQuery().getMessage().getChat().getId())){
                        Payload = roomsRepository.findById(i+1).get().getPayload();
                        performerID = roomsRepository.findById(i+1).get().getPerformerID();
                        break;
                    }
                }
                 if(performerID!=update.getCallbackQuery().getFrom().getId()){
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
                    sendMessage.setText("Ми просимо відповісти саме виконавця");
                    try{
                        execute(sendMessage);
                    }catch(TelegramApiException e){e.printStackTrace();}
                }else if(Payload==null||purchaseRepository.findById(Payload).get().getPriceToPerformer()==0) {
                     try {
                         sendPayment(update);
                     } catch (TelegramApiException e) {
                         throw new RuntimeException(e);
                     }
                 }
                else{
                    blockActionsWhilePayed(update);
                }
               /* try {
                    sendPayment(update);
                } catch (TelegramApiException e) {
                    throw new RuntimeException(e);
                }*/
            }
            else if (messagetext.equals("NO")) {
                String Payload = "";
                int price=0;
                for(int i=0; i<roomsRepository.count();i++){
                    if(roomsRepository.findById(i+1).get().getRoomID().equals(update.getCallbackQuery().getMessage().getChat().getId())){
                        Payload = roomsRepository.findById(i+1).get().getPayload();
                        price = roomsRepository.findById(i+1).get().getPrice();
                        break;
                    }
                }
                if(Payload==null||purchaseRepository.findById(Payload).get().getPriceToPerformer()==0) {
                    try {
                        check_performer_return_no(update,price);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }else{
                        blockActionsWhilePayed(update);
                }
               /* try {
                    check_performer_return_no(update,price);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }*/
            }else if (messagetext.equals("YESSURE")) {
                long customerID=0;
                long performerID=0;
                for(int i=0; i<roomsRepository.count();i++){
                    if(roomsRepository.findById(i+1).get().getRoomID().equals(update.getCallbackQuery().getMessage().getChat().getId())){
                        customerID = roomsRepository.findById(i+1).get().getCustomerID();
                        performerID = roomsRepository.findById(i+1).get().getPerformerID();
                    }
                }

                try {
                    //Генерувати нове посилання на кімнату
                    deleteMember(update.getCallbackQuery().getMessage().getChat().getId(), customerID);
                    deleteMember(update.getCallbackQuery().getMessage().getChat().getId(), performerID);
                    for(int i=0; i<roomsRepository.count();i++){
                        String inviteLink = "";
                        if(roomsRepository.findById(i+1).get().getRoomID().equals(update.getCallbackQuery().getMessage().getChat().getId())){
//                                    String newChatLink = "";
//                                    String chanels="";
//                                    chanels=postRepository.findById(Math.toIntExact(roomsRepository.findById(i + 1).get().getPostId())).get().getChanel();
//                                    inviteLink =roomsRepository.findById(i+1).get().getChatLink();
//                                    String urlStr = "https://api.telegram.org/bot%s/revokeChatInviteLink?chat_id=%s&invite_link=%s";
//                                    urlStr = String.format(urlStr, getBotToken(), chanels, inviteLink);
//                                    URL newurll = new URL(urlStr);
//                                    URLConnection con = newurll.openConnection();
//                                    StringBuilder sbb = new StringBuilder();
//                                    InputStream iss = new BufferedInputStream(con.getInputStream());
//                                    BufferedReader brr = new BufferedReader(new InputStreamReader(iss));
//                                    String inputLn = "";
//                                    while ((inputLn = brr.readLine()) != null) {
//                                        newChatLink = String.valueOf(sbb.append(inputLn));
//                                    }
                            String newChatLink = "";
//                            String urlStr = "https://api.telegram.org/bot%s/exportChatInviteLink?chat_id=%s";
//                            urlStr = String.format(urlStr, getBotToken(), chanels);
//                            URL newurll = new URL(urlStr);
//                            URLConnection con = newurll.openConnection();
//                            StringBuilder sbb = new StringBuilder();
//                            InputStream iss = new BufferedInputStream(con.getInputStream());
//                            BufferedReader brr = new BufferedReader(new InputStreamReader(iss));
//                            String inputLn = "";
//                            while ((inputLn = brr.readLine()) != null) {
//                                newChatLink = String.valueOf(sbb.append(inputLn));
//                            };
                            long chanels = roomsRepository.findById(i+1).get().getRoomID();
                            ExportChatInviteLink exportChatInviteLink = new ExportChatInviteLink();
                            exportChatInviteLink.setChatId(chanels);
                            try {
                                // Send the message
                                newChatLink = execute(exportChatInviteLink);
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }

                            Rooms rooms = new Rooms();
                            rooms.setIsFree(true);
                            rooms.setRoomID(roomsRepository.findById(i+1).get().getRoomID());
                            rooms.setRoomNumber(roomsRepository.findById(i+1).get().getRoomNumber());
                            rooms.setChatLink(newChatLink);
                            rooms.setStateInChat(0);
                            rooms.setFollowing(0);
                            roomsRepository.save(rooms);
                            break;
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            else if (messagetext.equals("NOSURE")) {
                setArrangeOnceMore(update);
            }
            else if (messagetext.equals("YESF")) {
               closeBargain(update);
                try {
                    setWarningToCleanRoom(update.getCallbackQuery().getMessage());
                } catch (IOException e) {throw new RuntimeException(e);}
//               cleanRoom(update);
            }
            else if (messagetext.equals("NOF")) {
                sendCard(update.getCallbackQuery().getMessage().getChat().getId());
            }else if(messagetext.equals("THIEFLIST")){
                showThiefList(update);
            }else if(messagetext.equals("THIEFADD")){

            }else if(messagetext.equals("THIEFCHECK")){

            }
//            else if (messagetext.equals("READY")) {
//                                for(int i=0;i<roomsRepository.count();i++){
//                    if(roomsRepository.findById(i+1).get().getRoomID().equals(message.getChat().getId())){
//                        if(roomsRepository.findById(i+1).get().getFollowing()==3){
//                            long card = purchaseRepository.findById(roomsRepository.findById(i+1).get().getPayload()).get().getPerformerCard();
//                            try {
//                                card = Long.parseLong(message.getText());
//                                String payload = roomsRepository.findById(i+1).get().getPayload();
//                                Purchase purchase = new Purchase();
//                                purchase.setPayloadID(purchaseRepository.findById(payload).get().getPayloadID());
//                                purchase.setCustomerID(purchaseRepository.findById(payload).get().getCustomerID());
//                                purchase.setPerformerID(purchaseRepository.findById(payload).get().getPerformerID());
//                                purchase.setChargeID(purchaseRepository.findById(payload).get().getChargeID());
//                                purchase.setFlag(purchaseRepository.findById(payload).get().isFlag());
//                                purchase.setPriceToPerformer(purchaseRepository.findById(payload).get().getPriceToPerformer());
//                                purchase.setRoomID(purchaseRepository.findById(payload).get().getRoomID());
//                                purchase.setMessageID(purchaseRepository.findById(payload).get().getMessageID());
//                                purchase.setSuccessfulBargain(purchaseRepository.findById(payload).get().isSuccessfulBargain());
//                                purchase.setPerformerCard(card);
//                                purchaseRepository.save(purchase);
//                                checkCard(message);
//                        }catch (NumberFormatException nfe){
//                                SendMessage sendMessage = new SendMessage();
//                                sendMessage.setText("Не співпадає з форматом картки");
//                                sendMessage.setChatId(message.getChat().getId());
//                                try {
//                                    // Send the message
//                                     execute(sendMessage);
//                                } catch (TelegramApiException e){e.printStackTrace();}
//                            }
//                            break;
//                        }
//                    }
//                }
//            }
        }
        if(update.hasPreCheckoutQuery()){
            PreCheckoutQuery preCheckoutQuery = update.getPreCheckoutQuery();

            AnswerPreCheckoutQuery answerPreCheckoutQuery = new AnswerPreCheckoutQuery(preCheckoutQuery.getId(),true);

            /*SendMessage sendMessage = new SendMessage();*/
            /*AnswerPreCheckoutQuery answerPreCheckoutQuery1 = new AnswerPreCheckoutQuery(String.valueOf(purchaseRepository.findById(preCheckoutQuery.getInvoicePayload()).get().getRoomID()),true);*/
            try {
              execute(answerPreCheckoutQuery);
              /*execute(answerPreCheckoutQuery1);*/
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
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
            if (update.getMessage().hasSuccessfulPayment()) {
                servePayment(update.getMessage().getSuccessfulPayment());
                blockPayment(update.getMessage().getSuccessfulPayment());
            }
            if(update.getMessage().getLeftChatMember()!=null){
                String payLoad="";
                for(int i=0; i<roomsRepository.count();i++){
                    if(update.getMessage().getChat().getId().equals(roomsRepository.findById(i+1).get().getRoomID())){
                        payLoad = roomsRepository.findById(i+1).get().getPayload();
                        break;
                    }
                }
                if(payLoad==null||!purchaseRepository.findById(payLoad).get().isFlag()){
                    try {
                        deleteChatMember(update);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }else if(payLoad!=null&&purchaseRepository.findById(payLoad).get().isFlag()){
                    for(int i=0; i<roomsRepository.count();i++){
                        if(roomsRepository.findById(i+1).get().getRoomID().equals(message.getChat().getId())){
                            if(roomsRepository.findById(i+1).get().getFollowing()==0)
                            setWarningRoguery(update);
                            break;
                        }
                    }

                }
            }
        }

        if(message!=null){
          /*  bot_chat_ID = String.valueOf(message.getChatId());*/
        String user_sms = message.getText();

        if(user_sms!=null) {
            const_text = new Text();
            if (user_sms.equals(const_text.getAgreement_text())) {

                end_stop_menu(String.valueOf(message.getChatId()));
                if (!customerRepository.findById(message.getChatId()).get().isCheck_state()/*.getCheck_state()*/ /*|| customerRepository.findById(message.getChatId()).get().getCheck_state()==null*/) {
                    set_post(String.valueOf(message.getChatId()), 0, update.getCallbackQuery().getMessage().getMessageId(), message);
                    set_last_buttons(String.valueOf(message.getChatId()));
                } else set_subject_menu(String.valueOf(message.getChatId()), message);
//
            } else if (user_sms.equals(const_text.getThiefText())) {
                setThiefList(update);
            }
            else if (user_sms.equals(const_text.getBack_text())) {
                if (customerRepository.findById(message.getChatId()).get().getState().equals(quiz.SUBJECTMENU.toString())) {
                    end_stop_menu(String.valueOf(message.getChatId()));
                    set_subject_menu(String.valueOf(message.getChatId()), message);

                } else if (customerRepository.findById(message.getChatId()).get().getState().equals(quiz.MAINMENU.toString())) {
                    set_main_menu(String.valueOf(message.getChatId()), message);
                } else if (customerRepository.findById(message.getChatId()).get().getState().equals(quiz.TEXTDESCRIPTION.toString())) {
                    set_text_description(message.getChatId(), message);
                } else if (customerRepository.findById(message.getChatId()).get().getState().equals(quiz.FILEDESCRIPTION.toString())) {
                    set_file_description(message.getChatId(), message);
                    set_ready_button(String.valueOf(message.getChatId()));
                } else if (customerRepository.findById(message.getChatId()).get().getState().equals(quiz.PRICE.toString()) || customerRepository.findById(message.getChatId()).get().getState().equals(quiz.AGREEMENTPRICE.toString()) || customerRepository.findById(message.getChatId()).get().getState().equals(quiz.FIXPRICE.toString())) {
                    set_price_description(String.valueOf(message.getChatId()), message);
                }
            }
            else if (user_sms.equals(const_text.getEnd_text())) {
                set_main_menu(String.valueOf(message.getChatId()), message);
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
                customer.setCheckDescriptionState(customerRepository.findById(message.getChatId()).get().getCheckDescriptionState());
                customer.setCheck_state(true);
                customer.setPriceFlag(1);
                customerRepository.save(customer);
                /* *//*check_state*//* = true;*/
            }
            else if (user_sms.equals(const_text.getReady_text()) && customerRepository.findById(message.getChatId()).get().getState().equals(quiz.TEXTDESCRIPTION.toString())) {
                end_stop_menu(String.valueOf(message.getChatId()));

                try {
                    if (customerRepository.findById(message.getChatId()).get().getPhotoLink() != null)
                        set_photo_to_channel_and_return_link(message);
                    if (customerRepository.findById(message.getChatId()).get().getFileLink() != null)
                        set_file_to_channel_and_return_link(message);
                    if (customerRepository.findById(message.getChatId()).get().getPhotoLink() == null)
                        customerRepository.findById(message.getChatId()).get().setPhotoLink(null);
                    if (customerRepository.findById(message.getChatId()).get().getFileLink() == null) /*customerData.setFile_url(null)*/
                        customerRepository.findById(message.getChatId()).get().setFileLink(null);
                    /*url_photo = null;
                    url_file = null;*/
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if (!customerRepository.findById(message.getChatId()).get().isCheck_state()/*getCheck_state()*//*check_state*/) {
                    set_post(String.valueOf(message.getChatId()), 0, message.getChatId(), message);
                    set_last_buttons(String.valueOf(message.getChatId()));
                } else set_price_description(String.valueOf(message.getChatId()), message);
            }
            else if (user_sms.equals(const_text.getChange_subject())) {
                set_subject_menu(String.valueOf(message.getChatId()), message);
                end_stop_menu(String.valueOf(message.getChatId()));
            }
            else if (user_sms.equals(const_text.getChange_description())) {
                set_text_description(message.getChatId(), message);
                end_stop_menu(String.valueOf(message.getChatId()));
            }
            else if (user_sms.equals(const_text.getChange_photo_or_file())) {
               /* List photo_list = new ArrayList();
                List file_list=new ArrayList<>();
                save_new_links(photo_list, file_list, message);*/
                Customer customer = new Customer();
                customer.setChatID(customerRepository.findById(message.getChatId()).get().getChatID());
                customer.setSurname(customerRepository.findById(message.getChatId()).get().getSurname());
                customer.setName(customerRepository.findById(message.getChatId()).get().getName());
                customer.setUser_nick(customerRepository.findById(message.getChatId()).get().getUser_nick());
                customer.setBranch(customerRepository.findById(message.getChatId()).get().getBranch());
                customer.setAgreementsState(true);
                customer.setDescription(customerRepository.findById(message.getChatId()).get().getDescription());
                customer.setPrice(customerRepository.findById(message.getChatId()).get().getPrice());
                customer.setPostLink(customerRepository.findById(message.getChatId()).get().getPostLink());
                customer.setState(customerRepository.findById(message.getChatId()).get().getState());
                customer.setCheckDescriptionState(customerRepository.findById(message.getChatId()).get().getCheckDescriptionState());
                customer.setCheck_state(customerRepository.findById(message.getChatId()).get().isCheck_state());
                customer.setPriceFlag(customerRepository.findById(message.getChatId()).get().getPriceFlag());
                customerRepository.save(customer);
                set_file_description(message.getChatId(), message);
                set_ready_button(String.valueOf(message.getChatId()));
            }
            else if (user_sms.equals(const_text.getChange_price())) {
                set_price_description(String.valueOf(message.getChatId()), message);
            } else if (user_sms.equals(const_text.getPerformerRegister())) {
                set_button_register_performer(String.valueOf(message.getChatId()));
            }  else if (user_sms.equals(const_text.getCleaning())) {
                try {
                    cleanRoom(update,true);
                } catch (IOException e) {throw new RuntimeException(e);}
            }
            else {
                if (!customerRepository.findById(message.getFrom().getId()).isEmpty()/*isEmpty()*/)/*customerRepository.findById(message.getFrom().getId()).get().getCheckDescriptionState()!=null*/ {
//                if (customerRepository.findById(message.getChatId()).get().getCheckDescriptionState()!=null) {
                    if (customerRepository.findById(message.getFrom().getId()).get().getCheckDescriptionState() == 1) {
                        get_text_description(message);
                        if (!customerRepository.findById(message.getChatId()).get().isCheck_state()/*getCheck_state()==false*/) {
                            set_last_buttons(String.valueOf(message.getChatId()));
                            set_post(String.valueOf(message.getChatId()), 0, message.getChatId(), message);
                        } else {
                            set_file_description(message.getChatId(), message);
                            set_ready_button(String.valueOf(message.getChatId()));
                        }
                    }
                }
                int roomNumb = 0;
                for (int i = 0; i < roomsRepository.count(); i++) {
                    if (message.getChat().getId().equals(roomsRepository.findById(i + 1).get().getRoomID())) {
                        roomNumb = roomsRepository.findById(i + 1).get().getRoomNumber();
                        break;
                    }
                }
                if (!roomsRepository.findById(roomNumb).isEmpty()) {
                    if (roomsRepository.findById(roomNumb).get().getStateInChat() != null) {
                        if (roomsRepository.findById(roomNumb).get().getStateInChat() == 0) {
//                            /*  if (customerRepository.findById(message.getFrom().getId()).get().getPriceFlag() == 1) {*/
//                    /*Customer customer = new Customer();
//                    customer.setChatID(customerRepository.findById(message.getFrom().getId()).get().getChatID());
//                    customer.setSurname(customerRepository.findById(message.getFrom().getId()).get().getSurname());
//                    customer.setName(customerRepository.findById(message.getFrom().getId()).get().getName());
//                    customer.setUser_nick(customerRepository.findById(message.getFrom().getId()).get().getUser_nick());
//                    customer.setBranch(customerRepository.findById(message.getFrom().getId()).get().getBranch());
//                    customer.setAgreementsState(true);
//                    customer.setDescription(customerRepository.findById(message.getFrom().getId()).get().getDescription());
//                    customer.setFileLink(customerRepository.findById(message.getFrom().getId()).get().getFileLink());
//                    customer.setPhotoLink(customerRepository.findById(message.getFrom().getId()).get().getPhotoLink());
//                    customer.setPrice(customerRepository.findById(message.getFrom().getId()).get().getPrice());
//                    customer.setPostLink(customerRepository.findById(message.getFrom().getId()).get().getPostLink());
//                    customer.setState(customerRepository.findById(message.getFrom().getId()).get().getState());
//                    customer.setPriceFlag(0);
//                    customerRepository.save(customer);*/
////                    Rooms rooms = new Rooms();
//                            /*   for(int i=0;i<roomsRepository.count();i++){*/
//                    /*if (message.getChat().getId().equals(roomNumb)) {
//                        rooms.setRoomID(roomsRepository.findById(roomNumb).get().getRoomID());
//                        rooms.setPostId(roomsRepository.findById(roomNumb).get().getPostId());
//                        rooms.setChatLink(roomsRepository.findById(roomNumb).get().getChatLink());
//                        rooms.setPerformerID(roomsRepository.findById(roomNumb).get().getPerformerID());
//                        rooms.setCustomerID(roomsRepository.findById(roomNumb).get().getCustomerID());
//                        rooms.setIsFree(roomsRepository.findById(roomNumb).get().isIsFree());
//                        rooms.setPrice(roomsRepository.findById(roomNumb).get().getPrice());
//                        rooms.setRoomNumber(roomsRepository.findById(roomNumb).get().getRoomNumber());
//                        rooms.setStateInChat(1);
//                        roomsRepository.save(rooms);
//                    }*/
                            try {
                                boolean check = check_customers_price(user_sms, message);
                                if(check){
                                    Rooms rooms = new Rooms();
                                    rooms.setRoomNumber(roomsRepository.findById(roomNumb).get().getRoomNumber());
                                    rooms.setIsFree(roomsRepository.findById(roomNumb).get().isIsFree());
                                    rooms.setChatLink(roomsRepository.findById(roomNumb).get().getChatLink());
                                    rooms.setCustomerID(roomsRepository.findById(roomNumb).get().getCustomerID());
                                    rooms.setDate(roomsRepository.findById(roomNumb).get().getDate());
                                    rooms.setPayload(roomsRepository.findById(roomNumb).get().getPayload());
                                    rooms.setPerformerID(roomsRepository.findById(roomNumb).get().getPerformerID());
                                    rooms.setPostId(roomsRepository.findById(roomNumb).get().getPostId());
                                    rooms.setPrice(Integer.valueOf(user_sms));
                                    rooms.setRoomID(roomsRepository.findById(roomNumb).get().getRoomID());
                                    rooms.setStateInChat(1);
                                }
//                                price = user_sms;
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                        if(user_sms.matches("^-?\\d+$")){
                            for(int i=0; i<roomsRepository.count(); i++){
                                if(roomsRepository.findById(i+1).get().getRoomID().equals(message.getChat().getId())&&roomsRepository.findById(i+1).get().getFollowing()==2){
                                    String payload = roomsRepository.findById(i+1).get().getPayload();
                                    Purchase purchase = new Purchase();
                                    purchase.setPayloadID(purchaseRepository.findById(payload).get().getPayloadID());
                                    purchase.setRoomID(purchaseRepository.findById(payload).get().getRoomID());
                                    purchase.setCustomerID(purchaseRepository.findById(payload).get().getCustomerID());
                                    purchase.setPerformerID(purchaseRepository.findById(payload).get().getPerformerID());
                                    purchase.setChargeID(purchaseRepository.findById(payload).get().getChargeID());
                                    purchase.setSuccessfulBargain(purchaseRepository.findById(payload).get().isSuccessfulBargain());
                                    purchase.setPriceToPerformer(purchaseRepository.findById(payload).get().getPriceToPerformer());
                                    purchase.setFlag(purchaseRepository.findById(payload).get().isFlag());
                                    purchase.setPerformerCard(Long.valueOf(user_sms));
                                    purchaseRepository.save(purchase);
                                    checkCard(message);
                                    break;
                                }
                            }

                        }
                    }
                }

            }
            if (!customerRepository.findById(message.getChatId()).isEmpty()){
               if(!customerRepository.findById(message.getChatId()).get().getState().isEmpty()) {
                    if (customerRepository.findById(message.getChatId()).get().getState().equals(quiz.FIXPRICE.toString())) {
                        boolean check_price = debug_fix_price(user_sms, message.getChatId(), message);
                        if (check_price) {
                            set_post(String.valueOf(message.getChatId()), 0, message.getChatId(), message);
                            set_last_buttons(String.valueOf(message.getChatId()));
//                            Rooms rooms = new Rooms();
//                            for (int i = 0; i < roomsRepository.count(); i++) {
//                                if (message.getChat().getId().equals(roomsRepository.findById(i + 1).get().getRoomID())) {
//                                    rooms.setRoomID(roomsRepository.findById(i + 1).get().getRoomID());
//                                    rooms.setPostId(roomsRepository.findById(i + 1).get().getPostId());
//                                    rooms.setChatLink(roomsRepository.findById(i + 1).get().getChatLink());
//                                    rooms.setPerformerID(roomsRepository.findById(i + 1).get().getPerformerID());
//                                    rooms.setCustomerID(roomsRepository.findById(i + 1).get().getCustomerID());
//                                    rooms.setIsFree(roomsRepository.findById(i + 1).get().isIsFree());
//                                    rooms.setPrice(Integer.valueOf(user_sms));
//                                    rooms.setRoomNumber(roomsRepository.findById(i + 1).get().getRoomNumber());
//                                    rooms.setDate(roomsRepository.findById(i + 1).get().getDate());
//                                    rooms.setPayload(roomsRepository.findById(i + 1).get().getPayload());
//                                    rooms.setStateInChat(0);
//                                    roomsRepository.save(rooms);
//                                    break;
//                                }
//                            }
                        } else {
                            set_fix_price_menu(String.valueOf(message.getChatId()), message);
                        }
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
                        customer.setCheckDescriptionState(customerRepository.findById(message.getChatId()).get().getCheckDescriptionState());
                        customer.setCheck_state(false);
                        customer.setPriceFlag(1);
                        customerRepository.save(customer);
//                        for (int i = 0; i < roomsRepository.count(); i++) {
//                            if (message.getChat().getId().equals(roomsRepository.findById(i + 1).get().getRoomID())) {
//                                rooms.setRoomID(roomsRepository.findById(i + 1).get().getRoomID());
//                                rooms.setPostId(roomsRepository.findById(i + 1).get().getPostId());
//                                rooms.setChatLink(roomsRepository.findById(i + 1).get().getChatLink());
//                                rooms.setPerformerID(roomsRepository.findById(i + 1).get().getPerformerID());
//                                rooms.setCustomerID(roomsRepository.findById(i + 1).get().getCustomerID());
//                                rooms.setIsFree(roomsRepository.findById(i + 1).get().isIsFree());
//                                rooms.setPrice(roomsRepository.findById(i + 1).get().getPrice());
//                                rooms.setRoomNumber(roomsRepository.findById(i + 1).get().getRoomNumber());
//                                rooms.setDate(roomsRepository.findById(i + 1).get().getDate());
//                                rooms.setPayload(roomsRepository.findById(i + 1).get().getPayload());
//                                rooms.setStateInChat(0);
//                                roomsRepository.save(rooms);
//                                break;
//                            }
//                        }
                    }
                }
            }
if(message.getFrom().getId()==782340442&&!customerRepository.findById(782340442L).isEmpty()){
    if(customerRepository.findById(782340442L).get().getThiefListState()==1){
        getThiefID(message);
        setThiefSurname(message);
        menuDeleteThief(message);
    }
    else if(update.getMessage().getText().equals(const_text.getDeleteThief())){
        setThiefIDtoAdmin(message);
    }
   else if(customerRepository.findById(782340442L).get().getThiefListState()==2){
        getThiefSurname(message);
        setThiefName(message);
        menuDeleteThief(message);
    }
    else if(customerRepository.findById(782340442L).get().getThiefListState()==3){
        getThiefName(message);
        setThiefNick(message);
        menuDeleteThief(message);
    }
    else if(customerRepository.findById(782340442L).get().getThiefListState()==4){
        long thiefId=getThiefNick(message);
        setThiefToFile(thiefId, message);
    }
    else if(customerRepository.findById(782340442L).get().getThiefListState()==10){
        deleteThiefRow(message);
    }
}
if(user_sms.matches("^-?\\d+$")){
                int estimate = Integer.parseInt(user_sms);
                long customerID=0;
                for(int i=0; i<roomsRepository.count(); i++){
                    if(roomsRepository.findById(i+1).get().getRoomID().equals(message.getChat().getId())){
//                       roomID = roomsRepository.findById(i+1).get().getRoomID();
                        customerID = roomsRepository.findById(i+1).get().getCustomerID();
                        if(roomsRepository.findById(i+1).get().getFollowing()==1){
                            checkCustomerEstimate(customerID, message,estimate);
                        }
                        break;
                    }
                }
            }
        }
            switch (user_sms) {
                    case "/start": {
                        set_main_menu(String.valueOf(message.getChatId()),message);
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
                        customer.setCheckDescriptionState(customerRepository.findById(message.getChatId()).get().getCheckDescriptionState());
                        customer.setCheck_state(true);
                        customer.setPriceFlag(1);
                        customerRepository.save(customer);
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
        row.add(const_text.getAgreement_text());
        KeyboardRow row1 = new KeyboardRow();
        row1.add(const_text.getThiefText());
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
//        save(message, quiz.MAINMENU.toString());
        /*customerData=new CustomerData();*/
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
        customer.setCheckDescriptionState(customerRepository.findById(message.getChatId()).get().getCheckDescriptionState());
        customer.setState(quiz.MAINMENU.toString());
        customer.setPriceFlag(0);
        customer.setCheck_state(customerRepository.findById(message.getChatId()).get().isCheck_state());
        customerRepository.save(customer);
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
//        save(message, quiz.SUBJECTMENU.toString());
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
        customer.setState(quiz.SUBJECTMENU.toString());
        customer.setPriceFlag(customerRepository.findById(message.getChatId()).get().getPriceFlag());
        customer.setCheck_state(customerRepository.findById(message.getChatId()).get().isCheck_state());
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
        customer.setCheck_state(customerRepository.findById(message.getChatId()).get().isCheck_state());
        customer.setState(customerRepository.findById(message.getChatId()).get().getState());
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
//        save(message, quiz.TEXTDESCRIPTION.toString());
       /* quiz=quiz.TEXTDESCRIPTION;*/
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
        customer.setCheckDescriptionState(customerRepository.findById(message.getChatId()).get().getCheckDescriptionState());
        customer.setState(quiz.TEXTDESCRIPTION.toString());
        customer.setPriceFlag(customerRepository.findById(message.getChatId()).get().getPriceFlag());
        customer.setCheck_state(customerRepository.findById(message.getChatId()).get().isCheck_state());
        customerRepository.save(customer);
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
        customer.setCheck_state(customerRepository.findById(update.getMessage().getChatId()).get().isCheck_state());
        customer.setCheckDescriptionState(customerRepository.findById(update.getMessage().getChatId()).get().getCheckDescriptionState());
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
        customer.setCheckDescriptionState(customerRepository.findById(message.getChatId()).get().getCheckDescriptionState());
        customer.setCheck_state(customerRepository.findById(message.getChatId()).get().isCheck_state());
        customer.setState(/*customerRepository.findById(message.getChatId()).get().getState()*/quiz.PRICE.toString());
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
        customer.setCheckDescriptionState(customerRepository.findById(update.getMessage().getChatId()).get().getCheckDescriptionState());
        customer.setPriceFlag(0);
        customer.setCheck_state(customerRepository.findById(update.getMessage().getChatId()).get().isCheck_state());
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
        customer.setCheckDescriptionState(customerRepository.findById(message.getChatId()).get().getCheckDescriptionState());
        customer.setState(customerRepository.findById(message.getChatId()).get().getState());
        customer.setPhotoLink(photo_url_list);
        customer.setCheck_state(customerRepository.findById(message.getChatId()).get().isCheck_state());
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
            if(check_num<10||check_num>10000){
                SendMessage sendMessage = new SendMessage();
                sendMessage.setText(const_text.getLittleMoney());
                sendMessage.setChatId(chatId);
                try {
                    // Send the message
                    execute(sendMessage);
                    return false;
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }

            }else {
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
                customer.setCheckDescriptionState(customerRepository.findById(message.getChatId()).get().getCheckDescriptionState());
                customer.setCheck_state(true);
                customer.setState(String.valueOf(quiz.FIXPRICE));
                customerRepository.save(customer);
                /*quiz=quiz.FIXPRICE;*/

            try {
                // Send the message
                execute(sendMessage);
                return true;
            } catch (TelegramApiException e) {
                e.printStackTrace();
              }
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
        customer.setCheckDescriptionState(customerRepository.findById(message.getChatId()).get().getCheckDescriptionState());
        customer.setState(String.valueOf(quiz.FIXPRICE));
        customer.setCheck_state(true);
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
            customer.setCheckDescriptionState(customerRepository.findById(message.getChatId()).get().getCheckDescriptionState());
            customer.setCheck_state(false);
            customer.setPriceFlag(1);
            customerRepository.save(customer);
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
            customer.setCheckDescriptionState(customerRepository.findById(message.getChatId()).get().getCheckDescriptionState());
            customer.setCheck_state(true);
            customer.setPriceFlag(1);
            customerRepository.save(customer);

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
        Customer customer = new Customer();
        customer.setChatID(customerRepository.findById(Long.valueOf(chatId)).get().getChatID());
        customer.setSurname(customerRepository.findById(Long.valueOf(chatId)).get().getSurname());
        customer.setName(customerRepository.findById(Long.valueOf(chatId)).get().getName());
        customer.setUser_nick(customerRepository.findById(Long.valueOf(chatId)).get().getUser_nick());
        customer.setBranch(customerRepository.findById(Long.valueOf(chatId)).get().getBranch());
        customer.setAgreementsState(customerRepository.findById(Long.valueOf(chatId)).get().isAgreementsState());
        customer.setDescription(customerRepository.findById(Long.valueOf(chatId)).get().getDescription());
        customer.setFileLink(customerRepository.findById(Long.valueOf(chatId)).get().getFileLink());
        customer.setPhotoLink(customerRepository.findById(Long.valueOf(chatId)).get().getPhotoLink());
        customer.setPrice(customerRepository.findById(Long.valueOf(chatId)).get().getPrice());
        customer.setPostLink(customerRepository.findById(Long.valueOf(chatId)).get().getPostLink());
        customer.setState(customerRepository.findById(Long.valueOf(chatId)).get().getState());
        customer.setCheckDescriptionState(customerRepository.findById(Long.valueOf(chatId)).get().getCheckDescriptionState());
        customer.setPriceFlag(customerRepository.findById(Long.valueOf(chatId)).get().getPriceFlag());
        customer.setCheck_state(false);
        customerRepository.save(customer);
        try {
            // Send the message
            execute(main_menu_sms);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
//    public void register_to_data_base_customer(Message message){
//            var chatID = message.getChatId();
//            var chat = message.getChat();
//            Customer customer = new Customer();
//            customer.setChatID(chatID);
//            customer.setName(chat.getFirstName());
//            customer.setSurname(chat.getLastName());
//            customer.setUser_nick(chat.getUserName());
//        customer.setBranch(customerRepository.findById(message.getChatId()).get().getBranch());
//        customer.setAgreementsState(true);
//        customer.setDescription(customerRepository.findById(message.getChatId()).get().getDescription());
//        customer.setFileLink(customerRepository.findById(message.getChatId()).get().getFileLink());
//        customer.setPhotoLink(customerRepository.findById(message.getChatId()).get().getPhotoLink());
//        customer.setPrice(customerRepository.findById(message.getChatId()).get().getPrice());
//        customer.setPostLink(customerRepository.findById(message.getChatId()).get().getPostLink());
//        customer.setState(customerRepository.findById(message.getChatId()).get().getState());
//        customer.setCheck_state(false);
//        customer.setCheckDescriptionState(customerRepository.findById(message.getChatId()).get().getCheckDescriptionState());
//        customer.setPriceFlag(1);
//            customerRepository.save(customer);
////           customers_id = chatID;
//      }
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
         /*   String performer_id = String.valueOf(user.getId());*/
            CustomerActions customerActions = new CustomerActions(customerRepository);
            String post = customerActions.get_customer_post_link_tostr(update,postRepository);
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatID);
            String userName = "";
            String userSurname = "";
            if(performerRepository.findById(user.getId()).get().getName()!=null){
                userName =performerRepository.findById(user.getId()).get().getName();
            }if(performerRepository.findById(user.getId()).get().getSurname()!=null){
                userSurname = performerRepository.findById(user.getId()).get().getSurname();
            }
            sendMessage.setText("Користувач " + userName + " " +  userSurname + ", з рейтингом: " + performerRepository.findById(user.getId()).get().getRating() + " та кількістю угод: " + performerRepository.findById(user.getId()).get().getBargain_amount() + ", готовий/а взятися за ваше завдання" + "\n" + post);
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
        editMessageText.setMessageId(update.getCallbackQuery().getMessage().getMessageId());
        editMessageText.setText("Спілку обірвано");
//        Customer customer = new Customer();
//        customer.setChatID(customerRepository.findById(update.getCallbackQuery().getMessage().getChatId()).get().getChatID());
//        customer.setSurname(customerRepository.findById(update.getCallbackQuery().getMessage().getChatId()).get().getSurname());
//        customer.setName(customerRepository.findById(update.getCallbackQuery().getMessage().getChatId()).get().getName());
//        customer.setUser_nick(customerRepository.findById(update.getCallbackQuery().getMessage().getChatId()).get().getUser_nick());
//        customer.setBranch(customerRepository.findById(update.getCallbackQuery().getMessage().getChatId()).get().getBranch());
//        customer.setAgreementsState(true);
//        customer.setDescription(customerRepository.findById(update.getCallbackQuery().getMessage().getChatId()).get().getDescription());
//        customer.setFileLink(customerRepository.findById(update.getCallbackQuery().getMessage().getChatId()).get().getFileLink());
//        customer.setPhotoLink(customerRepository.findById(update.getCallbackQuery().getMessage().getChatId()).get().getPhotoLink());
//        customer.setPrice(customerRepository.findById(update.getCallbackQuery().getMessage().getChatId()).get().getPrice());
//        customer.setPostLink(customerRepository.findById(update.getCallbackQuery().getMessage().getChatId()).get().getPostLink());
//        customer.setState(customerRepository.findById(update.getCallbackQuery().getMessage().getChatId()).get().getState());
//        customer.setCheckDescriptionState(customerRepository.findById(update.getMessage().getChatId()).get().getCheckDescriptionState());
//        customer.setCheck_state(true);
//        customer.setPriceFlag(1);
//        customerRepository.save(customer);
        try {
            // Send the message
            execute(editMessageText);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    public int  return_chat_link_and_show_sms_in_group(long chatID, Update update){
        int room_num = 0;
        String postUrl = null;
        for(int i=0;i<roomsRepository.count();i++) {
            if(roomsRepository.findById(i+1).get().isIsFree()) {
                postUrl = roomsRepository.findById(i + 1).get().getChatLink();
                room_num = i+1;
                Date date = new Date();
                String currentDate = String.valueOf(date);
                Rooms rooms = new Rooms();
                rooms.setIsFree(false);
                rooms.setRoomID(roomsRepository.findById(i+1).get().getRoomID());
                rooms.setRoomNumber(roomsRepository.findById(i+1).get().getRoomNumber());
                rooms.setChatLink(roomsRepository.findById(i+1).get().getChatLink());
                rooms.setCustomerID(update.getCallbackQuery().getMessage().getChatId());
                rooms.setDate(currentDate);
                rooms.setPayload(roomsRepository.findById(i+1).get().getPayload());
                rooms.setFollowing(0);
                roomsRepository.save(rooms);
                break;
            }
        }
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(chatID);
        editMessageText.setMessageId((int) update.getCallbackQuery().getMessage().getMessageId());
        editMessageText.setText("Посилання на чат з виконавцем");
        InlineKeyboardMarkup inline_keybord = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows_inline = new ArrayList<>();
        List<InlineKeyboardButton> row_inline=new ArrayList<>();
        var link_Button = new InlineKeyboardButton();
        link_Button.setText(const_text.getGo_chat());
        link_Button.setUrl(postUrl);
        link_Button.setCallbackData(subjects.LINK.toString()+","+room_num);
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
        return room_num;
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
    public void return_chat_link_and_show_sms_for_performer_in_group(String performerID, String postID, int roomID){
        Rooms rooms = new Rooms();
        rooms.setIsFree(false);
        rooms.setRoomID(roomsRepository.findById(roomID).get().getRoomID());
        rooms.setRoomNumber(roomsRepository.findById(roomID).get().getRoomNumber());
        rooms.setChatLink(roomsRepository.findById(roomID).get().getChatLink());
        rooms.setCustomerID(roomsRepository.findById(roomID).get().getCustomerID());
        rooms.setPerformerID(Long.valueOf(performerID));
        rooms.setPostId(Long.valueOf(postID));
        rooms.setDate(roomsRepository.findById(roomID).get().getDate());
        rooms.setPayload(roomsRepository.findById(roomID).get().getPayload());
        rooms.setFollowing(roomsRepository.findById(roomID).get().getFollowing());
        roomsRepository.save(rooms);
        String url = roomsRepository.findById(roomID).get().getChatLink();
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
        link_Button.setUrl(url);
        link_Button.setCallbackData(subjects.LINK.toString()+","+roomID);
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
    public void set_sms_in_chat(int roomID) throws IOException {
        CustomerActions customerActions=new CustomerActions(customerRepository);
        String post = customerActions.set_in_group_info_tostr();
        long chatId =roomsRepository.findById(roomID).get().getRoomID();

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setParseMode("HTML");
        sendMessage.setText(post);

        InlineKeyboardMarkup inline_keybord = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows_inline = new ArrayList<>();


        var endAgreementButton = new InlineKeyboardButton();
        endAgreementButton.setText(const_text.getEndBargain());
        endAgreementButton.setCallbackData("CLOSE");
        List<InlineKeyboardButton> row_inline=new ArrayList<>();
        var price_Button = new InlineKeyboardButton();
        price_Button.setText(const_text.getPriceChat());
        price_Button.setCallbackData("PRICE");

        row_inline.add(endAgreementButton);
        row_inline.add(price_Button);
        rows_inline.add(row_inline);
        inline_keybord.setKeyboard(rows_inline);
        sendMessage.setReplyMarkup(inline_keybord);
        try{
           Message messageID= execute(sendMessage);
//            PinChatMessage pinChatMessage = new PinChatMessage(roomsRepository.findById(roomID).get().getRoomID(),messageID.getMessageId());
            sendApiMethodAsync(PinChatMessage.builder()
                    .chatId(messageID.getChatId().toString())
                    .messageId(messageID.getMessageId())
                    .build());
        }catch(TelegramApiException e){
            e.printStackTrace();
        }
        /*String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&reply_markup=%s&parse_mode=%s&text=%s";
        long chatId =roomsRepository.findById(roomID).get().getRoomID();

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
        }*/
    }
    public void fix_finish_text_price_customer(Message message) throws IOException {
        /*Customer customer = new Customer();
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
        customerRepository.save(customer);*/
        int roomId = 0;
        for(int i=0; i<roomsRepository.count(); i++){
            if(roomsRepository.findById(i+1).get().getRoomID().equals(message.getChat().getId())){
//                customerID = roomsRepository.findById(i+1).get().getCustomerID();
                roomId = i+1;
                Rooms rooms=new Rooms();
                rooms.setRoomID(roomsRepository.findById(i+1).get().getRoomID());
                rooms.setPostId(roomsRepository.findById(i+1).get().getPostId());
                rooms.setChatLink(roomsRepository.findById(i+1).get().getChatLink());
                rooms.setPerformerID(roomsRepository.findById(i+1).get().getPerformerID());
                rooms.setCustomerID(roomsRepository.findById(i+1).get().getCustomerID());
                rooms.setIsFree(roomsRepository.findById(i+1).get().isIsFree());
                rooms.setPrice(roomsRepository.findById(i+1).get().getPrice());
                rooms.setRoomNumber(roomsRepository.findById(i+1).get().getRoomNumber());
                rooms.setDate(roomsRepository.findById(i+1).get().getDate());
                rooms.setFollowing(2);
                rooms.setStateInChat(0);
                roomsRepository.save(rooms);
                break;
            }
        }
//        Customer customer = new Customer();
//        customer.setChatID(customerRepository.findById(customerID).get().getChatID());
//        customer.setSurname(customerRepository.findById(customerID).get().getSurname());
//        customer.setName(customerRepository.findById(customerID).get().getName());
//        customer.setUser_nick(customerRepository.findById(customerID).get().getUser_nick());
//        customer.setBranch(customerRepository.findById(customerID).get().getBranch());
//        customer.setAgreementsState(true);
//        customer.setDescription(customerRepository.findById(customerID).get().getDescription());
//        customer.setFileLink(customerRepository.findById(customerID).get().getFileLink());
//        customer.setPhotoLink(customerRepository.findById(customerID).get().getPhotoLink());
//        customer.setPrice(customerRepository.findById(customerID).get().getPrice());
//        customer.setPostLink(customerRepository.findById(customerID).get().getPostLink());
//        customer.setState(customerRepository.findById(customerID).get().getState());
//        customer.setCheck_state(customerRepository.findById(customerID).get().getCheck_state());
//        customer.setPriceFlag(1);
//        customerRepository.save(customer);

        String post = "Користувач, напишіть ціну цифрою, без копійок";
        String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";
        long chatId = roomsRepository.findById(roomId).get().getRoomID();
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
        for(int i=0; i<roomsRepository.count(); i++){
            if(roomsRepository.findById(i+1).get().getRoomID().equals(message.getChat().getId()) && !message.getFrom().getId().equals(roomsRepository.findById(i+1).get().getCustomerID())/*&&customerRepository.findById(message.getFrom().getId()).get().getPriceFlag() != 1*/){
                String post = ("Ми просимо відправити смс саме користувача (людині, яка робила пост в канал)");
                String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";
                String chatId = String.valueOf(roomsRepository.findById(i+1).get().getRoomID());
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
        }
            try {
                int intParse =  Integer.parseInt(user_price);
                sms_to_performer(intParse, message);
                Rooms rooms = new Rooms();
                for (int i = 0; i < roomsRepository.count(); i++) {
                            if (message.getChat().getId().equals(roomsRepository.findById(i + 1).get().getRoomID())) {
                                rooms.setRoomID(roomsRepository.findById(i + 1).get().getRoomID());
                                rooms.setPostId(roomsRepository.findById(i + 1).get().getPostId());
                                rooms.setChatLink(roomsRepository.findById(i + 1).get().getChatLink());
                                rooms.setPerformerID(roomsRepository.findById(i + 1).get().getPerformerID());
                                rooms.setCustomerID(roomsRepository.findById(i + 1).get().getCustomerID());
                                rooms.setIsFree(roomsRepository.findById(i + 1).get().isIsFree());
                                rooms.setPrice(intParse);
                                rooms.setRoomNumber(roomsRepository.findById(i + 1).get().getRoomNumber());
                                rooms.setDate(roomsRepository.findById(i + 1).get().getDate());
                                rooms.setPayload(roomsRepository.findById(i + 1).get().getPayload());
                                rooms.setStateInChat(roomsRepository.findById(i + 1).get().getStateInChat());
                                rooms.setFollowing(roomsRepository.findById(i+1).get().getFollowing());
                                roomsRepository.save(rooms);
                                break;
                            }
                        }
//                sms_to_performer(user_price, message);
            } catch (NumberFormatException nfe) {
                for(int i=0; i<roomsRepository.count(); i++){
                if(roomsRepository.findById(i+1).get().getRoomID().equals(message.getChat().getId())){
                        String post = ("Ви відправили не число");
                        String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";
                        String chatId = String.valueOf(roomsRepository.findById(i+1).get().getRoomID());
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
                        return false;
                 }
                }
            }
        return true;
    }
    public void sms_to_performer(int user_price, Message message) throws IOException {
//        int check_num = Integer.parseInt(user_price);
        if (user_price < 10 || user_price > 10000) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText(const_text.getLittleMoney());
            sendMessage.setChatId(message.getChat().getId());
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

        } else {
//            for (int i = 0; i < roomsRepository.count(); i++) {
//                if (message.getChat().getId().equals(roomsRepository.findById(i + 1).get().getRoomID())) {
//                    Rooms rooms = new Rooms();
//                    rooms.setRoomID(roomsRepository.findById(i + 1).get().getRoomID());
//                    rooms.setPostId(roomsRepository.findById(i + 1).get().getPostId());
//                    rooms.setChatLink(roomsRepository.findById(i + 1).get().getChatLink());
//                    rooms.setPerformerID(roomsRepository.findById(i + 1).get().getPerformerID());
//                    rooms.setCustomerID(roomsRepository.findById(i + 1).get().getCustomerID());
//                    rooms.setIsFree(roomsRepository.findById(i + 1).get().isIsFree());
//                    rooms.setPrice(check_num);
//                    rooms.setRoomNumber(roomsRepository.findById(i + 1).get().getRoomNumber());
//                    rooms.setStateInChat(/*roomsRepository.findById(i+1).get().getStateInChat()*/1);
//                    rooms.setDate(roomsRepository.findById(i + 1).get().getDate());
//                    rooms.setPayload(roomsRepository.findById(i + 1).get().getPayload());
//                    roomsRepository.save(rooms);
//                }
//            }
            String post = ("Ціну зафіксовано, вона дорівнює: " + user_price + " грн, виконавець, ви згодні з цією ціною?");
            String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&reply_markup=%s&text=%s";
            String chatId = String.valueOf(message.getChat().getId());
        /*for(int i=0; i<roomsRepository.count(); i++){
            if(roomsRepository.findById(i+1).get().getRoomID().equals(message.getChat().getId())) {
                chatId  = String.valueOf(roomsRepository.findById(i+1).get().getRoomID());
            }
        }*/
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
    }
    public void check_performer_return_no(Update update, int user_price)throws IOException{
        long chatID = 0;
        long performerID=0;
        for (int i=0; i<roomsRepository.count();i++){
            if (update.getCallbackQuery().getMessage().getChat().getId().equals(roomsRepository.findById(i+1).get().getRoomID())){
                chatID=roomsRepository.findById(i+1).get().getRoomID();
                performerID=roomsRepository.findById(i+1).get().getPerformerID();
                break;
            }
        }
        if( performerID != update.getCallbackQuery().getFrom().getId()){
            String post = ("Ми просимо відповісти саме виконавця (людині, яка буде виконувати завдання)");
            String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";
            post = URLEncoder.encode(post, "UTF-8");
            urlString = String.format(urlString, getBotToken(), chatID, post);
            URL newurl = new URL(urlString);
            URLConnection conn = newurl.openConnection();
            StringBuilder sb = new StringBuilder();
            InputStream is = new BufferedInputStream(conn.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String inputLine = "";
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }
            sms_to_performer(user_price,update.getCallbackQuery().getMessage());
        }
        else{
            String post = ("Виконавець не згідний за ціною, спробуйте домовитися ще");
            String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";
            for (int i=0; i<roomsRepository.count();i++){
                if (update.getCallbackQuery().getMessage().getChat().getId().equals(roomsRepository.findById(i+1).get().getRoomID())){
                    chatID=roomsRepository.findById(i+1).get().getRoomID();
                    break;
                }
            }
            post = URLEncoder.encode(post, "UTF-8");
            urlString = String.format(urlString, getBotToken(), chatID, post);
            URL newurl = new URL(urlString);
            URLConnection conn = newurl.openConnection();
            StringBuilder sb = new StringBuilder();
            InputStream is = new BufferedInputStream(conn.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String inputLine = "";
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }
            Rooms rooms =new Rooms();
            for(int i=0;i<roomsRepository.count();i++) {
                if (update.getCallbackQuery().getMessage().getChat().getId().equals(roomsRepository.findById(i + 1).get().getRoomID())) {
                    rooms.setRoomID(roomsRepository.findById(i + 1).get().getRoomID());
                    rooms.setPostId(roomsRepository.findById(i + 1).get().getPostId());
                    rooms.setChatLink(roomsRepository.findById(i + 1).get().getChatLink());
                    rooms.setPerformerID(roomsRepository.findById(i + 1).get().getPerformerID());
                    rooms.setCustomerID(roomsRepository.findById(i + 1).get().getCustomerID());
                    rooms.setIsFree(roomsRepository.findById(i + 1).get().isIsFree());
                    rooms.setPrice(roomsRepository.findById(i + 1).get().getPrice());
                    rooms.setRoomNumber(roomsRepository.findById(i + 1).get().getRoomNumber());
                    rooms.setDate(roomsRepository.findById(i+1).get().getDate());
                    rooms.setPayload(roomsRepository.findById(i+1).get().getPayload());
                    rooms.setFollowing(roomsRepository.findById(i+1).get().getFollowing());
                    rooms.setStateInChat(1);
                    roomsRepository.save(rooms);
                    break;
                }
            }
//            Customer customer = new Customer();
//            customer.setChatID(customerRepository.findById(update.getCallbackQuery().getMessage().getFrom().getId()).get().getChatID());
//            customer.setSurname(customerRepository.findById(update.getCallbackQuery().getMessage().getFrom().getId()).get().getSurname());
//            customer.setName(customerRepository.findById(update.getCallbackQuery().getMessage().getFrom().getId()).get().getName());
//            customer.setUser_nick(customerRepository.findById(update.getCallbackQuery().getMessage().getFrom().getId()).get().getUser_nick());
//            customer.setBranch(customerRepository.findById(update.getCallbackQuery().getMessage().getFrom().getId()).get().getBranch());
//            customer.setAgreementsState(true);
//            customer.setDescription(customerRepository.findById(update.getCallbackQuery().getMessage().getFrom().getId()).get().getDescription());
//            customer.setFileLink(customerRepository.findById(update.getCallbackQuery().getMessage().getFrom().getId()).get().getFileLink());
//            customer.setPhotoLink(customerRepository.findById(update.getCallbackQuery().getMessage().getFrom().getId()).get().getPhotoLink());
//            customer.setPrice(customerRepository.findById(update.getCallbackQuery().getMessage().getFrom().getId()).get().getPrice());
//            customer.setPostLink(customerRepository.findById(update.getCallbackQuery().getMessage().getFrom().getId()).get().getPostLink());
//            customer.setState(customerRepository.findById(update.getCallbackQuery().getMessage().getFrom().getId()).get().getState());
//            customer.setPriceFlag(null);

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
            customer.setCheckDescriptionState(1);
            customer.setCheck_state(customerRepository.findById(message.getChatId()).get().isCheck_state());
            customer.setPriceFlag(0);
            customerRepository.save(customer);
        }/*else{
            register_to_data_base_customer(message);
            Customer customer = new Customer();
            customer.setChatID(customerRepository.findById(message.getChatId()).get().getChatID());
            customer.setSurname(customerRepository.findById(message.getChatId()).get().getSurname());
            customer.setName(customerRepository.findById(message.getChatId()).get().getName());
            customer.setUser_nick(customerRepository.findById(message.getChatId()).get().getUser_nick());
            customer.setBranch(strsubject);
            customer.setCheck_state(true);
            customerRepository.save(customer);
        }*/
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
            customer.setCheck_state(customerRepository.findById(update.getCallbackQuery().getMessage().getChatId()).get().isCheck_state());
            customer.setCheckDescriptionState(customerRepository.findById(update.getCallbackQuery().getMessage().getChatId()).get().getCheckDescriptionState());
            customer.setState(customerRepository.findById(update.getCallbackQuery().getMessage().getChatId()).get().getState());
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
        if(!customerRepository.findById(message.getChatId()).isEmpty()) {
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
            customer.setCheckDescriptionState(customerRepository.findById(message.getChatId()).get().getCheckDescriptionState());
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
    public void sendPayment(Update update) throws TelegramApiException {
        CustomerActions customerActions = new CustomerActions(customerRepository);
        int price = 0;
        int roomId=0;
        String payLoad="";
        for(int i=0; i<roomsRepository.count();i++) {
            if (roomsRepository.findById(i+1).get().getRoomID().equals(update.getCallbackQuery().getMessage().getChatId())){
                roomId = i+1;
                if(roomsRepository.findById(i+1).get().getPayload()==null){
                    char[] sAlphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();
                    int sLength = sAlphabet.length;
                    Random sRandom = new Random();
                    boolean flag = true;
                    int k=0;

                    while(flag){
                        payLoad+=sAlphabet[sRandom.nextInt(sLength)];
                        if(payLoad.length()==15&&!purchaseRepository.findById(payLoad).isEmpty()){
                            k=0;
                            flag = true;
                            payLoad = "";
                        } else if (payLoad.length()==15&&purchaseRepository.findById(payLoad).isEmpty()) {
                            break;
                        }
                        k++;
                    }
                    long customerId = 0;
                    for(int j=0; j<roomsRepository.count();j++){
                        if(roomsRepository.findById(j+1).get().getRoomID().equals(update.getCallbackQuery().getMessage().getChatId())){
                            customerId = roomsRepository.findById(j+1).get().getCustomerID();
                            price = roomsRepository.findById(j + 1).get().getPrice();
                            Rooms rooms = new Rooms();
                            rooms.setRoomID(roomsRepository.findById(j + 1).get().getRoomID());
                            rooms.setPostId(roomsRepository.findById(j + 1).get().getPostId());
                            rooms.setChatLink(roomsRepository.findById(j + 1).get().getChatLink());
                            rooms.setPerformerID(roomsRepository.findById(j + 1).get().getPerformerID());
                            rooms.setCustomerID(roomsRepository.findById(j + 1).get().getCustomerID());
                            rooms.setIsFree(roomsRepository.findById(j + 1).get().isIsFree());
                            rooms.setPrice(roomsRepository.findById(j + 1).get().getPrice());
                            rooms.setRoomNumber(roomsRepository.findById(j + 1).get().getRoomNumber());
                            rooms.setDate(roomsRepository.findById(j+1).get().getDate());
                            rooms.setStateInChat(roomsRepository.findById(j+1).get().getStateInChat());
                            rooms.setFollowing(roomsRepository.findById(i+1).get().getFollowing());
                            rooms.setPayload(payLoad);
                            roomsRepository.save(rooms);
                            break;
                        }
                    }

                    Purchase purchase = new Purchase();
                    purchase.setPayloadID(payLoad);
                    purchase.setRoomID(update.getCallbackQuery().getMessage().getChatId());
                    purchase.setCustomerID(customerId);
                    purchase.setPerformerID(update.getCallbackQuery().getFrom().getId());
/*
                    purchase.setMessageID(purchaseRepository.findById(payLoad).get().getMessageID());
*/             purchase.setSuccessfulBargain(false);
                    purchase.setPriceToPerformer(0);
                    purchase.setFlag(false);
                    purchaseRepository.save(purchase);
                }
                else{payLoad = roomsRepository.findById(i+1).get().getPayload();break;}
            }
        }

        /*link:for(int i=0; i<15; i++){
            payLoad+=sAlphabet[sRandom.nextInt(sLength)];
            if(purchaseRepository.findById(i+1).get().getPayloadID().equals(payLoad)){
                continue link;
            }
        }
*/

        int finishPrice = customerActions.finish_price_for_customer(price);
        CreateInvoiceLink createInvoiceLink = new CreateInvoiceLink(const_text.getTitle(), const_text.getFormDescription(), roomsRepository.findById(roomId).get().getPayload(), config.getTokenPay(), "UAH",
                List.of(new LabeledPrice("Вартість", finishPrice * 100)));
        String invoiceLink = execute(createInvoiceLink);
        SendMessage main_menu_sms = new SendMessage();
        main_menu_sms.setChatId(update.getCallbackQuery().getMessage().getChat().getId());
        main_menu_sms.setText(const_text.getInvocieDescription()+finishPrice);
        InlineKeyboardMarkup inline_keybord = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows_inline = new ArrayList<>();
        List<InlineKeyboardButton> row_inline=new ArrayList<>();
        var payButton = new InlineKeyboardButton();
        payButton.setText(const_text.getPay());
        payButton.setCallbackData("Сплатити");
        payButton.setUrl(invoiceLink);
        row_inline.add(payButton);
        rows_inline.add(row_inline);
        inline_keybord.setKeyboard(rows_inline);
        main_menu_sms.setReplyMarkup(inline_keybord);

        try{
           Message message = execute(main_menu_sms);
          /* int messageId= message.getMessageId();*/
            Purchase purchase = new Purchase();
            purchase.setPayloadID(purchaseRepository.findById(payLoad).get().getPayloadID());
            purchase.setRoomID(purchaseRepository.findById(payLoad).get().getRoomID());
            purchase.setCustomerID(purchaseRepository.findById(payLoad).get().getCustomerID());
            purchase.setPerformerID(purchaseRepository.findById(payLoad).get().getPerformerID());
            purchase.setFlag(purchaseRepository.findById(payLoad).get().isFlag());
            purchase.setMessageID(message.getMessageId());
            purchase.setPriceToPerformer(0);
            purchase.setSuccessfulBargain(false);
            purchaseRepository.save(purchase);
        }catch(TelegramApiException e){
            e.printStackTrace();
        }
        /*String urlString = "https://api.telegram.org/bot%s/sendInvoice?chat_id=%s&title=%s&description=%s&payload=%s&provider_token=%s&currency=%s&prices=%s&reply_markup=%s";
        String chatId = update.getCallbackQuery().getMessage().getChat().getId().toString();
        String reply = URLEncoder.encode("{\"inline_keyboard\":[[{\"text\":\"" + "Сплатити" + "\",\"callback_data\":\"Сплатити\"}]]}", "UTF-8");
        String parse = "HTML";
        post = URLEncoder.encode(post, "UTF-8");
        urlString = String.format(urlString, token, chatId, reply, parse, post);
        URL newurl = new URL(urlString);
        URLConnection conn = newurl.openConnection();
        StringBuilder sb = new StringBuilder();
        InputStream is = new BufferedInputStream(conn.getInputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String inputLine = "";
        String digits = "";
        int check = 0;
        while ((inputLine = br.readLine()) != null) {
            sb.append(inputLine);
        }*/
    }
    public void servePayment(SuccessfulPayment successfulPayment){
        long roomID = 0;
        int price =0;
            if(!purchaseRepository.findById(successfulPayment.getInvoicePayload()).isEmpty()){
                roomID=purchaseRepository.findById(successfulPayment.getInvoicePayload()).get().getRoomID();
            }
            for(int i=0; i<roomsRepository.count();i++){
              if(roomsRepository.findById(i+1).get().getRoomID()==roomID){
                  price = roomsRepository.findById(i+1).get().getPrice();
                  break;
              }
            }
            CustomerActions customerActions = new CustomerActions(customerRepository);
            int finishPrice = customerActions.finish_price_for_performer(price);
        Purchase purchase = new Purchase();
        purchase.setPayloadID(purchaseRepository.findById(successfulPayment.getInvoicePayload()).get().getPayloadID());
        purchase.setRoomID(purchaseRepository.findById(successfulPayment.getInvoicePayload()).get().getRoomID());
        purchase.setCustomerID(purchaseRepository.findById(successfulPayment.getInvoicePayload()).get().getCustomerID());
        purchase.setPerformerID(purchaseRepository.findById(successfulPayment.getInvoicePayload()).get().getPerformerID());
        purchase.setFlag(true);
        purchase.setPriceToPerformer(finishPrice);
        purchase.setChargeID(successfulPayment.getProviderPaymentChargeId());
        purchase.setMessageID(purchaseRepository.findById(successfulPayment.getInvoicePayload()).get().getMessageID());
        purchase.setSuccessfulBargain(purchaseRepository.findById(successfulPayment.getInvoicePayload()).get().isSuccessfulBargain());
        purchaseRepository.save(purchase);
        for(int i=0; i<roomsRepository.count();i++) {
            if (roomsRepository.findById(i + 1).get().getPayload() != null) {
                if (roomsRepository.findById(i + 1).get().getPayload().equals(successfulPayment.getInvoicePayload())) {
                    Rooms rooms = new Rooms();
                    rooms.setIsFree(roomsRepository.findById(i + 1).get().isIsFree());
                    rooms.setRoomID(roomsRepository.findById(i + 1).get().getRoomID());
                    rooms.setRoomNumber(roomsRepository.findById(i + 1).get().getRoomNumber());
                    rooms.setChatLink(roomsRepository.findById(i + 1).get().getChatLink());
                    rooms.setCustomerID(roomsRepository.findById(i + 1).get().getCustomerID());
                    rooms.setPerformerID(roomsRepository.findById(i + 1).get().getPerformerID());
                    rooms.setPostId(roomsRepository.findById(i + 1).get().getPostId());
                    rooms.setDate(roomsRepository.findById(i + 1).get().getDate());
                    rooms.setPayload(roomsRepository.findById(i + 1).get().getPayload());
                    rooms.setStateInChat(1);
                    rooms.setFollowing(1);
                    roomsRepository.save(rooms);
                    break;
                }
            }
        }
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(roomID);
        sendMessage.setText(const_text.getMsgPayYes());
        try {
            // Send the message
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
}
    public void blockPayment(@NotNull SuccessfulPayment successfulPayment){
        int messageId = 0;
        long chatId=0;
       if(!purchaseRepository.findById(successfulPayment.getInvoicePayload()).isEmpty()){
           messageId = purchaseRepository.findById(successfulPayment.getInvoicePayload()).get().getMessageID();
           chatId = purchaseRepository.findById(successfulPayment.getInvoicePayload()).get().getRoomID();

       }
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(chatId);
        editMessageText.setMessageId(messageId);
        editMessageText.setText(const_text.getIsPaid());

        try {
            // Send the message
            execute(editMessageText);

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }
    public void blockActionsWhilePayed(Update update){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
        sendMessage.setText(const_text.getBlockPay());
        try {
            // Send the message
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    //Зробити генерацію посилань на чати
    public void deleteChatMember(Update update) throws IOException {
        long customerID=0;
        long performerID=0;
        long roomID = 0;
        for(int i=0;i<roomsRepository.count();i++){
            if(update.getMessage().getChat().getId().equals(roomsRepository.findById(i+1).get().getRoomID())){
//                        String newChatLink = "";
//                        chanels = roomsRepository.findById(i+1).get().getRoomID();
//                        inviteLink =roomsRepository.findById(i+1).get().getChatLink();
//                        String urlStr = "https://api.telegram.org/bot%s/revokeChatInviteLink?chat_id=%s&invite_link=%s";
//                        urlStr = String.format(urlStr, getBotToken(), chanels, inviteLink);
//                        URL newurll = new URL(urlStr);
//                        URLConnection con = newurll.openConnection();
//                        StringBuilder sbb = new StringBuilder();
//                        InputStream iss = new BufferedInputStream(con.getInputStream());
//                        BufferedReader brr = new BufferedReader(new InputStreamReader(iss));
//                        String inputLn = "";
//                        while ((inputLn = brr.readLine()) != null) {
//                            newChatLink = String.valueOf(sbb.append(inputLn));
//                        String urlStr = "https://api.telegram.org/bot%s/exportChatInviteLink?chat_id=%s";
//                        urlStr = String.format(urlStr, getBotToken(), chanels);
//                        URL newurll = new URL(urlStr);
//                        URLConnection con = newurll.openConnection();
//                        StringBuilder sbb = new StringBuilder();
//                        InputStream iss = new BufferedInputStream(con.getInputStream());
//                        BufferedReader brr = new BufferedReader(new InputStreamReader(iss));
//                        String inputLn = "";
//                        while ((inputLn = brr.readLine()) != null) {
//                            newChatLink = String.valueOf(sbb.append(inputLn));
//                        }
                customerID = roomsRepository.findById(i+1).get().getCustomerID();
                performerID = roomsRepository.findById(i+1).get().getPerformerID();
                roomID = roomsRepository.findById(i+1).get().getRoomID();
//                Rooms rooms = new Rooms();
//                rooms.setRoomID(roomsRepository.findById(i+1).get().getRoomID());
//                rooms.setRoomNumber(roomsRepository.findById(i+1).get().getRoomNumber());
//                rooms.setIsFree(false);
//                rooms.setChatLink(newChatLink);
//                roomsRepository.save(rooms);
                break;
            }
        }
        if(update.getMessage().getLeftChatMember().getId().equals(customerID)){
          deleteMember(roomID,performerID);
        }
        if(update.getMessage().getLeftChatMember().getId().equals(performerID)) {
            deleteMember(roomID, customerID);
//            String urlString = "https://api.telegram.org/bot%s/unbanChatMember?chat_id=%s&user_id=%s";
//
//            urlString = String.format(urlString, getBotToken(), roomID, customerID);
//            URL newurl = new URL(urlString);
//            URLConnection conn = newurl.openConnection();
//            StringBuilder sb = new StringBuilder();
//            InputStream is = new BufferedInputStream(conn.getInputStream());
//            BufferedReader br = new BufferedReader(new InputStreamReader(is));
//            String inputLine = "";
//            String digits="";
//            int check=0;
//            while ((inputLine = br.readLine()) != null) {
//                sb.append(inputLine);
//            }
        }
        setWarningToCleanRoom(update.getMessage());
    }
    public void setWarningRoguery(Update update){
        for(int i=0; i<roomsRepository.count();i++){
            if(roomsRepository.findById(i+1).get().getRoomID().equals(update.getMessage().getChat().getId())){
                if(roomsRepository.findById(i+1).get().getPerformerID().equals(update.getMessage().getLeftChatMember().getId())){
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setChatId(update.getMessage().getChat().getId());
                    sendMessage.setText(const_text.getWarningToCustomer());
                    try {
                        // Send the message
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
                else if(roomsRepository.findById(i+1).get().getCustomerID().equals(update.getMessage().getLeftChatMember().getId())){
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setChatId(update.getMessage().getChat().getId());
                    sendMessage.setText(const_text.getWarningToPerformer());
                    try {
                        // Send the message
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    public void deleteMember(long roomID, long MemberId) throws IOException {
        String urlString = "https://api.telegram.org/bot%s/unbanChatMember?chat_id=%s&user_id=%s";
        urlString = String.format(urlString, getBotToken(), roomID, MemberId);
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
    public void setArrangeOnceMore(Update update){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getCallbackQuery().getMessage().getChat().getId());
        sendMessage.setText("Добре, спробуйте домовитися ще");
        try {
            // Send the message
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    public void setSure(Update update){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(update.getCallbackQuery().getMessage().getChat().getId());
        sendMessage.setText("ви не домовились, ви впевнені, що хочете завершити угоду?");
        InlineKeyboardMarkup inline_keybord = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows_inline = new ArrayList<>();



        List<InlineKeyboardButton> row_inline=new ArrayList<>();
        var yesButton = new InlineKeyboardButton();
        yesButton.setText("Так");
        yesButton.setCallbackData("YESSURE");
        var noButton = new InlineKeyboardButton();
        noButton.setText("Ні");
        noButton.setCallbackData("NOSURE");
        row_inline.add(yesButton);
        row_inline.add(noButton);
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
    public void setSurveyOrEnd(Update update){
        String payloadID="";
        int roomID=0;
        for(int i=0; i<roomsRepository.count();i++){
            if(roomsRepository.findById(i+1).get().getRoomID().equals(update.getCallbackQuery().getMessage().getChat().getId())){
                payloadID = roomsRepository.findById(i+1).get().getPayload();
                roomID = roomsRepository.findById(i+1).get().getRoomNumber();
//                customerID = roomsRepository.findById(i+1).get().getCustomerID();
//                performerID = roomsRepository.findById(i+1).get().getPerformerID();
            }
        }
        if(payloadID == null || !purchaseRepository.findById(payloadID).get().isFlag()){
            setSure(update);
        }
        else{
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(update.getCallbackQuery().getMessage().getChat().getId());
            sendMessage.setText("Користувач, оцініть , будь ласка виконавця за шкалою від 0 до 5. Надішліть у чат цифру. Виконавець побачить вашу оцінку, це не анонімно, але оцінюйте чесно.");
            Rooms rooms = new Rooms();
            rooms.setIsFree(roomsRepository.findById(roomID).get().isIsFree());
            rooms.setRoomID(roomsRepository.findById(roomID).get().getRoomID());
            rooms.setRoomNumber(roomsRepository.findById(roomID).get().getRoomNumber());
            rooms.setChatLink(roomsRepository.findById(roomID).get().getChatLink());
            rooms.setCustomerID(roomsRepository.findById(roomID).get().getCustomerID());
            rooms.setPerformerID(roomsRepository.findById(roomID).get().getPerformerID());
            rooms.setPostId(roomsRepository.findById(roomID).get().getPostId());
            rooms.setDate(roomsRepository.findById(roomID).get().getDate());
            rooms.setPayload(roomsRepository.findById(roomID).get().getPayload());
            rooms.setStateInChat(roomsRepository.findById(roomID).get().getStateInChat());
            rooms.setFollowing(1);
            roomsRepository.save(rooms);
            try {
                // Send the message
                execute(sendMessage);
            } catch (TelegramApiException e){e.printStackTrace();}
        }
    }
    public void checkCustomerEstimate(long customerID, Message message, int estimate) {
        if (customerID != message.getFrom().getId()) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText("Ми просимо відповісти самe користувача");
            sendMessage.setChatId(message.getChat().getId());
            try {
                // Send the message
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else if (estimate > 5 || estimate < 0) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText("Ви переоцінили або недооцінили виконавця");
            sendMessage.setChatId(message.getChat().getId());
            try {
                // Send the message
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText(const_text.getByeTxt());
            sendMessage.setChatId(customerID);
            try {
                // Send the message
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            try {
                deleteMember(message.getChat().getId(),customerID);
            } catch (IOException e) {throw new RuntimeException(e);}
           sendCard(message.getChat().getId());
            for(int i=0; i<roomsRepository.count();i++){
                if(roomsRepository.findById(i+1).get().getRoomID().equals(message.getChat().getId())){
                    long performerID = roomsRepository.findById(i+1).get().getPerformerID();
                    Rooms rooms = new Rooms();
                    rooms.setIsFree(roomsRepository.findById(i+1).get().isIsFree());
                    rooms.setRoomID(roomsRepository.findById(i+1).get().getRoomID());
                    rooms.setRoomNumber(roomsRepository.findById(i+1).get().getRoomNumber());
                    rooms.setChatLink(roomsRepository.findById(i+1).get().getChatLink());
                    rooms.setCustomerID(roomsRepository.findById(i+1).get().getCustomerID());
                    rooms.setPerformerID(roomsRepository.findById(i+1).get().getPerformerID());
                    rooms.setPostId(roomsRepository.findById(i+1).get().getPostId());
                    rooms.setDate(roomsRepository.findById(i+1).get().getDate());
                    rooms.setPayload(roomsRepository.findById(i+1).get().getPayload());
                    rooms.setStateInChat(roomsRepository.findById(i+1).get().getStateInChat());
                    rooms.setFollowing(2);
                    roomsRepository.save(rooms);
                    Performer performer = new Performer();
                    performer.setBargain_amount(performerRepository.findById(performerID).get().getBargain_amount()+1);
                    performer.setChatID(performerRepository.findById(performerID).get().getChatID());
                    performer.setSurname(performerRepository.findById(performerID).get().getSurname());
                    performer.setName(performerRepository.findById(performerID).get().getName());
                    performer.setUser_nick(performerRepository.findById(performerID).get().getUser_nick());
                   if(performerRepository.findById(performerID).get().getRating().equals(const_text.getFirst_performer())){
                       performer.setRating(message.getText());
                   }else{
                       float averageEstimate = Float.parseFloat(performerRepository.findById(performerID).get().getRating());
                       averageEstimate+=Float.parseFloat(message.getText());
                       averageEstimate/=performerRepository.findById(performerID).get().getBargain_amount()+1;
                       performer.setRating(String.valueOf(averageEstimate));
                   }
                   performerRepository.save(performer);
                    break;

                }
            }
        }
    }
    public void sendCard(long roomID){
        for(int i=0; i<roomsRepository.count();i++){
            if(roomID==roomsRepository.findById(i+1).get().getRoomID()){
                roomsRepository.findById(i+1).get().setFollowing(2);
                break;
            }
        }
        SendMessage sendMessage1 = new SendMessage();
        sendMessage1.setText("Користувач, надішліть, будь ласка, вашу картку");
        sendMessage1.setChatId(roomID);
//        InlineKeyboardMarkup inline_keybord = new InlineKeyboardMarkup();
//        List<List<InlineKeyboardButton>> rows_inline = new ArrayList<>();
//
//
//
//        List<InlineKeyboardButton> row_inline=new ArrayList<>();
//        var readyButton = new InlineKeyboardButton();
//        readyButton.setText(const_text.getMyCard());
//        readyButton.setCallbackData("READY");
//        row_inline.add(readyButton);
//        rows_inline.add(row_inline);
//        inline_keybord.setKeyboard(rows_inline);
//        sendMessage1.setReplyMarkup(inline_keybord);
        try {
            // Send the message
            execute(sendMessage1);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    public void checkCard(Message message){
        String payLoad="";
        for(int i=0;i<roomsRepository.count();i++){
            if(roomsRepository.findById(i+1).get().getRoomID().equals(message.getChat().getId())){
                payLoad = roomsRepository.findById(i+1).get().getPayload();
                break;
            }
        }
        long card = purchaseRepository.findById(payLoad).get().getPerformerCard();
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChat().getId());
        sendMessage.setText(const_text.getAreYouSure()+"\nКартка, яку ми зафіксували: "+card);
        InlineKeyboardMarkup inline_keybord = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows_inline = new ArrayList<>();

        List<InlineKeyboardButton> row_inline=new ArrayList<>();
        var yesButton = new InlineKeyboardButton();
        yesButton.setText("Так");
        yesButton.setCallbackData("YESF");
        var noButton = new InlineKeyboardButton();
        noButton.setText("Ні");
        noButton.setCallbackData("NOF");
        row_inline.add(yesButton);
        row_inline.add(noButton);
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
    public void closeBargain(Update update){
        long performerID=0;
        String payload="";
        long postID=0;
        long roomID=0;
        for(int i=0;i<roomsRepository.count();i++){
            if(update.getCallbackQuery().getMessage().getChat().getId().equals(roomsRepository.findById(i+1).get().getRoomID())){
                performerID = roomsRepository.findById(i+1).get().getPerformerID();
                payload = roomsRepository.findById(i+1).get().getPayload();
                postID = roomsRepository.findById(i+1).get().getPostId();
                roomID= roomsRepository.findById(i+1).get().getRoomID();
                break;
            }
        }
        Purchase purchase = new Purchase();
        purchase.setPayloadID(purchaseRepository.findById(payload).get().getPayloadID());
        purchase.setCustomerID(purchaseRepository.findById(payload).get().getCustomerID());
        purchase.setPerformerID(purchaseRepository.findById(payload).get().getPerformerID());
        purchase.setChargeID(purchaseRepository.findById(payload).get().getChargeID());
        purchase.setFlag(purchaseRepository.findById(payload).get().isFlag());
        purchase.setPriceToPerformer(purchaseRepository.findById(payload).get().getPriceToPerformer());
        purchase.setRoomID(purchaseRepository.findById(payload).get().getRoomID());
        purchase.setMessageID(purchaseRepository.findById(payload).get().getMessageID());
        purchase.setSuccessfulBargain(true);
        purchase.setPerformerCard(purchaseRepository.findById(payload).get().getPerformerCard());
        purchaseRepository.save(purchase);
        try {
            deleteMember(roomID, performerID);
        } catch (IOException e) {throw new RuntimeException(e);}
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(const_text.getByeTxtPerformer());
        sendMessage.setChatId(performerID);
        try {
            // Send the message
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(postRepository.findById((int) postID).get().getChanel());
        editMessageText.setMessageId((int) postID);
        editMessageText.setText("Виконано");
        try {
            // Send the message
          execute(editMessageText);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        Post newPost=new Post();
        newPost.setMessageID(postRepository.findById((int) postID).get().getMessageID());
        newPost.setLink(postRepository.findById((int) postID).get().getLink());
        newPost.setCustomer_id(postRepository.findById((int) postID).get().getCustomer_id());
        newPost.setChanel(postRepository.findById((int) postID).get().getChanel());
        newPost.setActive(false);
        postRepository.save(newPost);
    }
    public void setWarningToCleanRoom(Message message) throws IOException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Єлизавето, прибиральнице, видрій кімнату, сцуко!");
        sendMessage.setChatId(message.getChat().getId());
        try {
            // Send the message
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        for(int i=0; i<roomsRepository.count();i++){
            if (message.getChat().getId().equals(roomsRepository.findById(i + 1).get().getRoomID())) {
                String newChatLink = "";
                long chanels = roomsRepository.findById(i+1).get().getRoomID();
                ExportChatInviteLink exportChatInviteLink = new ExportChatInviteLink();
                exportChatInviteLink.setChatId(chanels);
                try {
                    // Send the message
                  newChatLink = execute(exportChatInviteLink);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
                Rooms rooms = new Rooms();
                rooms.setRoomID(roomsRepository.findById(i + 1).get().getRoomID());
                rooms.setRoomNumber(roomsRepository.findById(i + 1).get().getRoomNumber());
                rooms.setPostId(roomsRepository.findById(i + 1).get().getPostId());
                rooms.setIsFree(false);
                rooms.setDate("Тайм зробити клінінг");
                rooms.setChatLink(newChatLink);
                roomsRepository.save(rooms);
                break;
            }
        }

    }
    public void cleanRoom(Update update, boolean deletePost) throws IOException {
        long postID = 0;
        long chanels = 0;

        for(int i=0; i<roomsRepository.count();i++){
            if (update.getMessage().getChat().getId().equals(roomsRepository.findById(i + 1).get().getRoomID())) {
              postID = roomsRepository.findById(i+1).get().getPostId();
              chanels = roomsRepository.findById(i+1).get().getRoomID();
                break;
            }
        }
//        chanels = postRepository.findById((int) postID).get().getChanel();
        int messageID = update.getMessage().getMessageId();

        String urlString = "https://api.telegram.org/bot%s/deleteMessage?chat_id=%s&message_id=%s";
        urlString = String.format(urlString, getBotToken(), chanels, messageID);
        URL newurl = new URL(urlString);
        URLConnection conn = newurl.openConnection();
        StringBuilder sb = new StringBuilder();
        InputStream is = new BufferedInputStream(conn.getInputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        String inputLine = "";
        while ((inputLine = br.readLine()) != null) {
            sb.append(inputLine);
        }

        for(int i=0; i<roomsRepository.count();i++){
            if (update.getMessage().getChat().getId().equals(roomsRepository.findById(i + 1).get().getRoomID())) {
                Rooms rooms = new Rooms();
                rooms.setRoomID(roomsRepository.findById(i+1).get().getRoomID());
                rooms.setRoomNumber(roomsRepository.findById(i+1).get().getRoomNumber());
                rooms.setChatLink(roomsRepository.findById(i+1).get().getChatLink());
                rooms.setIsFree(true);
                roomsRepository.save(rooms);
                break;
            }
        }
        if(deletePost) {
            if (!postRepository.findById((int) postID).isEmpty() && !postRepository.findById((int) postID).get().isActive()) {
                Post post = postRepository.findById((int) postID).orElseThrow();
                postRepository.delete(post);
            }
        }
    }
    public void setThiefList(Update update){
        if(update.getMessage().getChatId()==782340442){
SendMessage sendMessage = new SendMessage();
sendMessage.setChatId(update.getMessage().getChatId());
sendMessage.setText(const_text.getThiefId());
            Customer customer = customerRepository.findById(update.getMessage().getChatId()).get();
            customer.setThiefListState(1);
            customerRepository.save(customer);
            try {
                // Send the message
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            menuDeleteThief(update.getMessage());
        }
        else{
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(update.getMessage().getChat().getId());
            sendMessage.setText(const_text.getThiefMenu());
            InlineKeyboardMarkup inline_keybord = new InlineKeyboardMarkup();
            List<List<InlineKeyboardButton>> rows_inline = new ArrayList<>();


            var thiefListButton = new InlineKeyboardButton();
            thiefListButton.setText(const_text.getThiefList());
            thiefListButton.setCallbackData("THIEFLIST");
            List<InlineKeyboardButton> row_inline=new ArrayList<>();
            var thiefAddButton = new InlineKeyboardButton();
            thiefAddButton.setText(const_text.getThiefAdd());
            thiefAddButton.setCallbackData("THIEFADD");
            var checkToThiefButton = new InlineKeyboardButton();
            checkToThiefButton.setText(const_text.getCheckThief());
            checkToThiefButton.setCallbackData("THIEFCHECK");

            row_inline.add(thiefListButton);
            row_inline.add(thiefAddButton);
            row_inline.add(checkToThiefButton);
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
    public void showThiefList(Update update){
        Customer customer = customerRepository.findById(update.getCallbackQuery().getFrom().getId()).get();
     customer.setThiefListState(1);
     customerRepository.save(customer);
      String list = "";
        try {
            FileReader fileReader = new FileReader("ThiefDataTableDownload");
            BufferedReader br = new BufferedReader(fileReader);

            for(int i=0; i<=10;i++){
             list += br.readLine();
            }
            fileReader.close();
            br.close();
           list=list.replace("null", "");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
     /* int leftSide= 0;
        int rightSide=customerRepository.findById(update.getCallbackQuery().getFrom().getId()).get().getThiefListState()*10;
      for(int i=leftSide; i<=rightSide;i++){
          list=thiefRepository.findAll().toString();
      }*/
      SendMessage sendMessage=new SendMessage();
      sendMessage.setChatId(update.getCallbackQuery().getFrom().getId());
      sendMessage.setText(list);
        try {
            // Send the message
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
   }
    public void menuDeleteThief(Message message){
        SendMessage main_menu_sms = new SendMessage();
        main_menu_sms.setText("Або - ,якщо нема, але не випадком з id)");
        main_menu_sms.setChatId(message.getChatId());
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        List<KeyboardRow> menu = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add(const_text.getDeleteThief());
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
   public void setThiefIDtoAdmin(Message message){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText(const_text.getDeleteThiefText());
        sendMessage.setChatId(message.getChatId());
        Customer customer = customerRepository.findById(message.getFrom().getId()).get();
        customer.setThiefListState(10);
        customerRepository.save(customer);
       try {
           // Send the message
           execute(sendMessage);
       } catch (TelegramApiException e) {
           e.printStackTrace();
       }
   }
   public void deleteThiefRow(Message message){
        try {
            Thief thief = thiefRepository.findById(Long.valueOf(message.getText())).orElseThrow();
            thiefRepository.delete(thief);
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText("Шахрая видалено");
            sendMessage.setChatId(message.getChatId());
            try {
                // Send the message
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }catch(Exception exception){
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText("Шахрая не було видалено");
            sendMessage.setChatId(message.getChatId());
            try {
                // Send the message
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
   }
   //Расскомментить нужный код
   public void getThiefID (Message message){
        if(!thiefRepository.findById(Long.valueOf(message.getText())).isEmpty()){
            SendMessage sendMessage = new SendMessage();
            sendMessage.setText("Цього шахрая вже зареєстровано");
            sendMessage.setChatId(message.getChat().getId());
            Customer customer = customerRepository.findById(message.getFrom().getId()).get();
            customer.setThiefListState(0);
            customerRepository.save(customer);
            try {
                // Send the message
                execute(sendMessage);
            } catch (TelegramApiException ex) {
                ex.printStackTrace();
            }
        }else {
            Thief thief = new Thief();
            thief.setThiefID(Long.valueOf(message.getText()));
            thiefRepository.save(thief);
            try {
                FileWriter fileWriter = new FileWriter("ForThief", false);
                fileWriter.write(message.getText());
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                SendMessage sendMessage = new SendMessage();
                sendMessage.setText("Помилка при записуванні ID");
                sendMessage.setChatId(message.getChat().getId());
                try {
                    // Send the message
                    execute(sendMessage);
                } catch (TelegramApiException ex) {
                    ex.printStackTrace();
                }
                throw new RuntimeException(e);
            }
        }
   }
   public void setThiefSurname(Message message){
       SendMessage sendMessage = new SendMessage();
       sendMessage.setText(const_text.getAddThiefSurname());
       sendMessage.setChatId(message.getChat().getId());
       Customer customer = customerRepository.findById(message.getFrom().getId()).get();
       customer.setThiefListState(2);
       customerRepository.save(customer);
       try {
           // Send the message
           execute(sendMessage);
       } catch (TelegramApiException e) {
           e.printStackTrace();
       }
   }
   public void getThiefSurname(Message message){
       String thiefID="";
       try {
           FileReader fileReader = new FileReader("ForThief");
          int count=0;
          while((count=fileReader.read())!=-1){
              thiefID += (char)count;
          }
          fileReader.close();
       } catch (IOException e) {
           SendMessage sendMessage = new SendMessage();
           sendMessage.setText("Помилка при зчитуванні ID");
           sendMessage.setChatId(message.getChat().getId());
           try {
               // Send the message
               execute(sendMessage);
           } catch (TelegramApiException ex) {
               ex.printStackTrace();
           }
           throw new RuntimeException(e);
       }
       Thief thief = thiefRepository.findById(Long.valueOf(thiefID)).get();
       thief.setSurname(message.getText());
       thiefRepository.save(thief);
   }
   public void setThiefName(Message message){

       SendMessage sendMessage = new SendMessage();
       sendMessage.setText(const_text.getAddThiefName());
       sendMessage.setChatId(message.getChat().getId());
       Customer customer = customerRepository.findById(message.getFrom().getId()).get();
       customer.setThiefListState(3);
       customerRepository.save(customer);
       try {
           // Send the message
           execute(sendMessage);
       } catch (TelegramApiException e) {
           e.printStackTrace();
       }
   }
   public void getThiefName(Message message){
       String thiefID="";
       try {
           FileReader fileReader = new FileReader("ForThief");
           int count=0;
           while((count=fileReader.read())!=-1){
               thiefID += (char)count;
           }
           fileReader.close();
       } catch (IOException e) {
           SendMessage sendMessage = new SendMessage();
           sendMessage.setText("Помилка при зчитуванні ID");
           sendMessage.setChatId(message.getChat().getId());
           try {
               // Send the message
               execute(sendMessage);
           } catch (TelegramApiException ex) {
               ex.printStackTrace();
           }
           throw new RuntimeException(e);
       }
       Thief thief = thiefRepository.findById(Long.valueOf(thiefID)).get();
       thief.setName(message.getText());
       thiefRepository.save(thief);
   }
   public void setThiefNick(Message message){
       SendMessage sendMessage = new SendMessage();
       sendMessage.setText(const_text.getAddThiefNick());
       sendMessage.setChatId(message.getChat().getId());
       Customer customer = customerRepository.findById(message.getFrom().getId()).get();
       customer.setThiefListState(4);
       customerRepository.save(customer);
       try {
           // Send the message
           execute(sendMessage);
       } catch (TelegramApiException e) {
           e.printStackTrace();
       }
   }
   public long getThiefNick(Message message){
       String thiefID="";
       try {
           FileReader fileReader = new FileReader("ForThief");
           int count=0;
           while((count=fileReader.read())!=-1){
               thiefID += (char)count;
           }
           fileReader.close();
       } catch (IOException e) {
           SendMessage sendMessage = new SendMessage();
           sendMessage.setText("Помилка при зчитуванні ID");
           sendMessage.setChatId(message.getChatId());
           try {
               // Send the message
               execute(sendMessage);
           } catch (TelegramApiException ex) {
               ex.printStackTrace();
           }
           throw new RuntimeException(e);
       }
       Thief thief = thiefRepository.findById(Long.valueOf(thiefID)).get();
       thief.setNick(message.getText()+",");
       thiefRepository.save(thief);
       SendMessage sendMessage = new SendMessage();
       sendMessage.setText(const_text.getReadyThief());
       sendMessage.setChatId(message.getChatId());
       try {
           // Send the message
           execute(sendMessage);
       } catch (TelegramApiException e) {
           e.printStackTrace();
       }
       Customer customer = customerRepository.findById(message.getFrom().getId()).get();
       customer.setThiefListState(0);
       customerRepository.save(customer);
       return Long.parseLong(thiefID);
   }
   public void setThiefToFile (long thiefId, Message message){
        Thief thief = thiefRepository.findById(thiefId).get();
       try {
           FileWriter fileWriter = new FileWriter("ThiefDataTableDownload",true);
           fileWriter.write(thief.toString());
           fileWriter.flush();
           fileWriter.close();
       } catch (IOException e) {
           SendMessage sendMessage = new SendMessage();
           sendMessage.setText("Помилка при записуванні шахрая до файла");
           sendMessage.setChatId(message.getChatId());
           try {
               // Send the message
               execute(sendMessage);
           } catch (TelegramApiException ex) {
               ex.printStackTrace();
           }
           throw new RuntimeException(e);
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
