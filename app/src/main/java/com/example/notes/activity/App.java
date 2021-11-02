package com.example.notes.activity;

import android.app.Application;
import androidx.room.RoomDatabase;
import androidx.room.Room;

import com.example.notes.room.NotesDatabase;

public class App extends Application {

    public static NotesDatabase db;

    @Override
    public void onCreate() {
        super.onCreate();
        db = Room.databaseBuilder(getApplicationContext(),
                NotesDatabase.class, "notes").allowMainThreadQueries().build();
    }
}
    //    class BuildDatabase extends AsyncTask<String, Void, Void>{
//
//        @Override
//        protected Void doInBackground(String... strings) {
//            NotesDatabase db = Room.databaseBuilder(getApplicationContext(),
//                    NotesDatabase.class, "database-name").build();
//        }
//    }

