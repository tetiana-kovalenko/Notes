package com.example.notes.room;

import android.util.Log;

import com.example.notes.activity.App;
import com.example.notes.model.Note;

import java.util.List;

public class NoteHandler {

    public void addNote(Note note){
        App.db.noteDao().addNote(note);
    }

    public void addNote(String title, String text, String thumbLoc){
        App.db.noteDao().addNote(new Note(title, text, thumbLoc));
//        Log.d("addition", "Success!");
//
//        String noteId = String.valueOf(getAllNotes().get(0).getId());
//        Log.d("id", noteId);
    }

    public int updateNote(Note note){
        return App.db.noteDao().updateNote(note);
    }

    public void updateNote(long id, String title, String text, String thumbLoc){
        App.db.noteDao().updateNote(new Note(id, title, text, thumbLoc));
//        Log.d("updating", "Success!");
//
//        String noteId = String.valueOf(getAllNotes().get(0).getId());
//        Log.d("id", noteId);
    }

    public List<Note> getAllNotes() {
        return App.db.noteDao().getAllNotes();
    }

    public Note getNoteById(long id) {
        return App.db.noteDao().getNoteById(id);
    }

    public void deleteNote(Note note) {
        App.db.noteDao().deleteNote(note);
    }

    public void deleteNoteById(long id) {
        App.db.noteDao().deleteNoteById(id);
    }
}
