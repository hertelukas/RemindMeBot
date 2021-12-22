package com.example.remindmebot.database;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "Reminder")
public class Reminder {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "content")
    private String content;

    @Column(name = "date")
    private Timestamp date;

    public Reminder(){

    }
}
