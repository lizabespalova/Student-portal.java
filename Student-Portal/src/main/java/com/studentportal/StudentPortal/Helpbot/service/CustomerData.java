package com.studentportal.StudentPortal.Helpbot.service;

import com.pengrad.telegrambot.model.request.InputMediaPhoto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;
@Component
public class CustomerData <T> implements Serializable {
    private static final long serialVersionUID = -7875692315562148863L;
    @Setter
    @Getter
    private String subject;
    @Getter
    @Setter
    private String description;
    @Getter
    @Setter
    private List file_url;
    @Getter
    @Setter
    private List photo_url;
    @Getter
    @Setter
    private T price;
    @Getter
    @Setter
    private String post_url;
    @Setter
    private boolean post_state;
    @Getter
    private Text text = new Text();
}
