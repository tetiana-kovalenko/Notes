package com.example.notes.activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.notes.R;
import com.example.notes.model.Note;
import com.example.notes.room.NoteHandler;


import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.io.IOException;

public class NoteView extends AppCompatActivity {

    ConstraintLayout constraintLayout;
    TextView txtNoteTitle, txtNoteText;
    ImageView imgThumbnail;
    Menu menu;

    boolean isChanged = false, isSaved = false;
    long noteId;
    String thumbPath;
    Note currentNote;

    ActivityResultLauncher<String[]> pickImageResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_view);

        constraintLayout = findViewById(R.id.constraintLayout);
        txtNoteTitle = findViewById(R.id.txtNoteTitle);
        txtNoteText = findViewById(R.id.txtNoteText);
        imgThumbnail = findViewById(R.id.imgThumbnail);

        txtNoteText.setShowSoftInputOnFocus(true);
        txtNoteTitle.setShowSoftInputOnFocus(true);

        Intent intent = getIntent();
        noteId = intent.getLongExtra("note_id", 0);

        if(noteId != 0){
            currentNote = new NoteHandler().getNoteById(noteId);
            txtNoteTitle.setText(currentNote.getNoteTitle());
            txtNoteText.setText(currentNote.getNoteText());

            thumbPath = currentNote.getThumbLoc();
            // Log.d("path", thumbPath);
            if(thumbPath != null) {
                 Glide.with(this).load(thumbPath).into(imgThumbnail);
            }
        }

        pickImageResultLauncher = registerForActivityResult(
                new ActivityResultContracts.OpenDocument(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        Glide.with(getBaseContext()).load(result).into(imgThumbnail);
                        final int takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                                | Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
                        getContentResolver().takePersistableUriPermission(result, takeFlags);
                        thumbPath = result.toString();
                        isChanged = true;
                    }
                }
        );

        txtNoteText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtNoteText.setFocusableInTouchMode(true);
                txtNoteText.requestFocus();
            }
        });

        if(txtNoteText.getText().length() == 0 && noteId == 0){
            txtNoteText.setFocusableInTouchMode(true);
            txtNoteText.requestFocus();
        }


        txtNoteText.addTextChangedListener(new Watcher());
        txtNoteTitle.addTextChangedListener(new Watcher());

        KeyboardVisibilityEvent.setEventListener(this, new KeyboardVisibilityEventListener() {
            @Override
            public void onVisibilityChanged(boolean b) {
                if(!b) {
                    txtNoteText.clearFocus();
                    txtNoteTitle.clearFocus();
                }

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        getMenuInflater().inflate(R.menu.note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
//            case R.id.itemSaveNote:
//                txtNoteText.clearFocus();
//                txtNoteTitle.clearFocus();
//                saveNote();
//
//                menu.findItem(R.id.itemSaveNote).setVisible(false);
//
//                break;
            case R.id.itemAddOrChangeThumb:
                pickImageResultLauncher.launch(new String[]{"image/*"});
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if(txtNoteTitle.getText().length() == 0 && txtNoteText.getText().length() == 0
                && thumbPath == null){
            getIntent().putExtra("note_to_delete", noteId);
        }
        else if(isChanged && !isSaved) {
             saveNote();
        }
        setResult(isSaved? RESULT_OK : RESULT_CANCELED, getIntent());
        super.onBackPressed();
    }

    public void saveNote(){
        if(noteId == 0) {
            new NoteHandler().addNote(txtNoteTitle.getText().toString(),
                    txtNoteText.getText().toString(),
                    thumbPath);
        }
        else {
            new NoteHandler().updateNote(noteId,
                    txtNoteTitle.getText().toString(),
                    txtNoteText.getText().toString(),
                    thumbPath);
        }
        isSaved = true;
    }

    class Watcher implements TextWatcher{

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            isChanged = true;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        @Override
        public void afterTextChanged(Editable s) { }
    }
}

//        txtNoteText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
////                if(menu != null) {
////                    menu.findItem(R.id.itemSaveNote).setVisible(hasFocus);
////                }
//
//                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//                if (hasFocus) {
//                    inputMethodManager.showSoftInput(v, 0);
//                } else {
//                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
//                }
//            }
//        });
//
//        txtNoteTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
////                if(menu != null) {
////                    menu.findItem(R.id.itemSaveNote).setVisible(hasFocus);
////                }
//
//                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//                if (hasFocus) {
//                    inputMethodManager.showSoftInput(v, 0);
//                } else {
//                    inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
//                }
//            }
//        });
//        InputMethodManager ii = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

