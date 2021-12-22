package com.example.remindmebot.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, String> {
    @Query
    List<Reminder> getRemindersByDateBetween(Timestamp date, Timestamp until);
}
