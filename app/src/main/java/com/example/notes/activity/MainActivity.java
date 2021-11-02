package com.example.notes.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.notes.model.Note;
import com.example.notes.adapter.NotesAdapter;
import com.example.notes.R;
import com.example.notes.room.NoteHandler;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity implements NotesAdapter.NoteClickable {

    RecyclerView recyclerView;
    NotesAdapter notesAdapter;
    ArrayList<Note> notes;
    ImageButton imgBtnAdd;
    ActivityResultLauncher<Intent> noteActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        notes = new ArrayList<>();
        imgBtnAdd = findViewById(R.id.btnAdd);

        loadNotes();

        ItemTouchHelper.SimpleCallback itemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                new NoteHandler().deleteNoteById(notes.get(viewHolder.getAdapterPosition()).getId());
                notes.remove(viewHolder.getAdapterPosition());
                notesAdapter.notifyItemRemoved(viewHolder.getAdapterPosition());

                // loadNotes();
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        noteActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == RESULT_OK){
                            loadNotes();
                        }
                        else if(result.getResultCode() == RESULT_CANCELED){
                            long noteToDeleteId = result.getData().getLongExtra("note_to_delete", 0);
                            if (noteToDeleteId != 0) {
                                new NoteHandler().deleteNoteById(noteToDeleteId);
                                loadNotes();
                            }
                        }
                    }
                });

        imgBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NoteView.class);
                // startActivityForResult(intent, RESULT_CANCELED);
                noteActivityResultLauncher.launch(intent);
            }
        });


    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (resultCode){
//            case RESULT_OK:
//                loadNotes();
//                break;
//            case RESULT_CANCELED:
//                long noteId = data.getLongExtra("note_id", 0);
//                if (noteId != 0) {
//                    new NoteHandler().deleteNoteById(noteId);
//                    loadNotes();
//                }
//                break;
//        }
//    }

    private void loadNotes() {
        notes = (ArrayList<Note>) new NoteHandler().getAllNotes();
        Collections.reverse(notes);
        notesAdapter = new NotesAdapter(notes, this, this);
//        GridLayoutManager layoutManager = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, true);
//        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(notesAdapter);
    }

    @Override
    public void onItemClick(int notePosition) {
        Note selectedNote = notes.get(notePosition);
        Intent intent = new Intent(MainActivity.this, NoteView.class);
//        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
//        intent.setType("image/*");
        Log.d("clicked", String.valueOf(selectedNote.getId()));
        intent.putExtra("note_id", selectedNote.getId());
//        intent.putExtra("thumbnail_path", selectedNote.getThumbLoc());
        // startActivityForResult(intent, 2);
        noteActivityResultLauncher.launch(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}