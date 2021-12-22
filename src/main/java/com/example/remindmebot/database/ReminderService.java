package com.example.remindmebot.database;

import org.springframework.stereotype.Service;

@Service
public class ReminderService {
    ReminderRepository reminderRepository;

    public ReminderService(ReminderRepository reminderRepository) {
        this.reminderRepository = reminderRepository;
    }

    public void save(Reminder reminder) {
        reminderRepository.save(reminder);
    }
}
