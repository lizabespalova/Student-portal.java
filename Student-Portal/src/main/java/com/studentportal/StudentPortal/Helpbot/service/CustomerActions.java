package com.studentportal.StudentPortal.Helpbot.service;

import org.springframework.stereotype.Component;

@Component

public class CustomerActions {
   private CustomerData customerData;
   CustomerActions(CustomerData customerData){
       this.customerData =customerData;
   }
    public void finish_price(){}
    public StringBuilder set_customer_post() {
        StringBuilder sb = new StringBuilder(/*"<b>"*/);
        sb.append(  customerData.getText().getActive_state());
        sb.append("\n\n\n");
        sb.append("<b>Галузь: </b>"+  customerData.getSubject());
        sb.append("\n\n\n");
        sb.append("<b>Опис: </b>");
        sb.append(  customerData.getDescription());
        sb.append("\n\n\n");
        sb.append("<b>"+customerData.getText().getPrice_text()+"</b>"+": "+ customerData.getPrice());
        sb.append("\n");

        if(customerData.getFile_url()!=null) {
            for (int i=0; i<customerData.getFile_url().size();i++) {
                sb.append("<a href= \"");
                sb.append(customerData.getFile_url().get(i));
                sb.append("\">");
                sb.append("Прикріплений файл\n");
                sb.append("</a>");
            }
        }
        if(customerData.getPhoto_url()!=null) {
            for (int i=0; i<customerData.getPhoto_url().size();i++) {
                sb.append("<a href= \"");
                sb.append(customerData.getPhoto_url().get(i));
                sb.append("\">");
                sb.append("Прикріплене фото\n");
                sb.append("</a>");
            }
        }

        return sb;
    }
    public String post_tostr(int check){
       if(check==0) {
           return set_customer_post().toString();
       }else {
           return get_customer_post().toString();
       }
    }
    public StringBuilder get_customer_post(){
        StringBuilder sb = new StringBuilder(/*"<b>"*/);
        sb.append("Ось ваш пост у каналі:");
        sb.append("\n");
            sb.append("<a href= \"");
            sb.append(customerData.getPost_url());
            sb.append("\">");
            sb.append("Прикріплений пост\n");
            sb.append("</a>");
        return sb;
    }
    public StringBuilder get_customer_post_link(){
        StringBuilder sb = new StringBuilder();
        sb.append("Посилання на завдання:\n");
        sb.append(customerData.getPost_url());
        return sb;
    }
    public String get_customer_post_link_tostr(){
            return get_customer_post_link().toString();
    }
}
//        if(photo_url!=null){
//            SendMessage sendMessage = new SendMessage();
//            sendMessage.setParseMode("HTML");
//            sendMessage.setText("<b><b>");
        /*  InputMediaPhoto inputPhoto = new InputMediaPhoto();
        SendPhoto message = new SendPhoto();
        message.setPhoto(new InputMediaPhoto().setMedia(photo_url));*/
        /*    SendDocument sendDocument = new SendDocument();
            String urlDownload = "";
            sendDocument.setDocument(new InputFile().setMedia(urlDownload));*/
//        }
//        if(file_url!=null){
//
//        }
