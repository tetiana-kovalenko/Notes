package com.example.notes.room;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.notes.model.Note;

@Database(entities = {Note.class}, version = 1)
public abstract class NotesDatabase extends RoomDatabase {
    public abstract NoteDao noteDao();
}
