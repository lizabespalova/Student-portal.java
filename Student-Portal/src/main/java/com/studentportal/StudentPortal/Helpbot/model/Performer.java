package com.studentportal.StudentPortal.Helpbot.model;
import lombok.Data;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "perfomer_data_table")
public class Performer {
    @Id
    /*@GeneratedValue(strategy = GenerationType.AUTO)*/
    private Long chatID;
    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "user_nick")
    private String user_nick;
    @Column(name = "rating")
    private String rating;
    @Column(name = "bargain_amount") //Кількість угод
    private int bargain_amount;
   /* @Column(name = "postLinks")
    private List<String> postLinks = new ArrayList<>(); //(посилання на пости, які були взяті)*/
}

