package com.example.remindmebot.database;

import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ReminderService {
    ReminderRepository reminderRepository;

    public ReminderService(ReminderRepository reminderRepository) {
        this.reminderRepository = reminderRepository;
    }

    public void save(Reminder reminder) {
        reminderRepository.save(reminder);
    }

    public List<Reminder> getCurrentTimers() {
        return reminderRepository.getRemindersByDateBetween(Timestamp.valueOf(LocalDateTime.now()), Timestamp.valueOf(LocalDateTime.now().plus(1, ChronoUnit.MINUTES)));
    }
}
