package com.example.notes.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "note")
public class Note {

    @PrimaryKey(autoGenerate = true)
    long id;
    String noteTitle;
    String noteText;
    String thumbLoc;

    public Note(){
        noteTitle = "";
        noteText = "";
        thumbLoc = "";
    }

    public Note(String noteTitle, String noteText, String thumbLoc){
        this.noteTitle = noteTitle;
        this.noteText = noteText;
        this.thumbLoc = thumbLoc;
    }

    public Note(long id, String noteTitle, String noteText, String thumbLoc){
        this.id = id;
        this.noteTitle = noteTitle;
        this.noteText = noteText;
        this.thumbLoc = thumbLoc;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public String getThumbLoc() {
        return thumbLoc;
    }

    public void setThumbLoc(String thumbLoc) {
        this.thumbLoc = thumbLoc;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
