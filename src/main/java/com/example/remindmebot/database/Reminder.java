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

    @Column(name = "user")
    private long user;

    @Column(name = "content")
    private String content;

    @Column(name = "date")
    private Timestamp date;

    public Reminder(){

    }

    public Reminder(long user, String content, Timestamp date){
        this.user = user;
        this.content = content;
        this.date = date;
    }

    public long getUser() {
        return user;
    }

    public String getContent() {
        return content;
    }
}
