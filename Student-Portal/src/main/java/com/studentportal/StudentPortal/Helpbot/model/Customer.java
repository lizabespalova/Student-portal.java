package com.studentportal.StudentPortal.Helpbot.model;
import lombok.Data;
import javax.persistence.*;
@Data
@Entity
@Table(name = "customer_data_table")
public class Customer {
    @Id
    /*@GeneratedValue(strategy = GenerationType.AUTO)*/
    private Long chatID;
    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "user_nick")
    private String user_nick;
    @Column(name = "price")
    private int price;
    @Column(name = "is_paid")
    private boolean isPaid;
    @Column(name = "customer_performer")
    private String customersPerformer;
    @Column(name = "agreement_state")
    private boolean agreementsState; //(true - завдання виконано, false - не виконано)
    @Column(name = "performers_amount")
    private int performersAmount;

}

