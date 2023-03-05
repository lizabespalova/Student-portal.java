package com.studentportal.StudentPortal.Helpbot.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.criteria.CriteriaBuilder;

@Data
@Entity
@Table(name = "post_data_table")
public class Post {
    @Id
    private Integer messageID;
    @Column(name = "link")
    private String link;
    @Column(nullable=true,name = "customer_id")
    private Long customer_id;
    @Column(nullable=true,name = "performer_id")
    private Long performer_id;
}
