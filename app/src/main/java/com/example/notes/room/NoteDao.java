package com.example.notes.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.notes.model.Note;

import java.util.List;

@Dao
public interface NoteDao {

    @Query("SELECT * FROM note ORDER BY id")
    List<Note> getAllNotes();

    @Query("SELECT * FROM note WHERE id=(:id)")
    Note getNoteById(long id);

    @Insert
    public long addNote(Note note);

    @Update
    public int updateNote(Note note);

    @Delete
    public void deleteNote(Note note);

    @Query("DELETE FROM note WHERE id=(:id)")
    public void deleteNoteById(long id);


}
