package com.example.notes.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.notes.model.Note;
import com.example.notes.R;

import java.util.ArrayList;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteRowHolder> {

    ArrayList<Note> notesList;
    Context context;
    NoteClickable clickableInterface;

    public NotesAdapter(ArrayList<Note> notesList, Context context, NoteClickable clickableInterface){
        this.notesList = notesList;
        this.context = context;
        this.clickableInterface = clickableInterface;
    }

    @NonNull
    @Override
    public NoteRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.note_without_thumb, parent, false);

        return new NoteRowHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteRowHolder holder, int position) {
        String noteTitle = notesList.get(position).getNoteTitle();
        String noteText = notesList.get(position).getNoteText();

//        if(noteTitle.isEmpty() && !noteText.isEmpty()){
//            float titleTextWidth = holder.txtNoteTitle.getMeasuredWidth();
//
//            String titleRow = "";
//            float titleRowWidth = 0;
//            String[] wordsInNote = noteText.split(" ");
////            for(int i = 0; i < noteText.length(); i++){
////
////            }
//            //while (titleRowWidth <= titleTextWidth || !)
//            for(String word:wordsInNote){
//                titleRowWidth += holder.txtNoteTitle.getPaint().measureText(word);
//                if(titleRowWidth >= titleTextWidth || word.contains("\\n")){
//                    break;
//                }
//                titleRow += word + " ";
//            }
//            noteTitle = titleRow;
//        }

        if(!noteTitle.isEmpty()){
            holder.txtNoteTitle.setText(noteTitle);
            holder.txtNoteTitle.setVisibility(View.VISIBLE);
        }

        if(!noteText.isEmpty()){
            holder.txtNoteText.setText(noteText);
            holder.txtNoteText.setVisibility(View.VISIBLE);
        }


        String thumbLoc = notesList.get(position).getThumbLoc();
        if(thumbLoc != null) {
            Glide.with(context).load(thumbLoc).into(holder.imgNoteThumb);
        }
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    class NoteRowHolder extends RecyclerView.ViewHolder{

        TextView txtNoteTitle, txtNoteText;
        ImageView imgNoteThumb;

        public NoteRowHolder(@NonNull View itemView) {
            super(itemView);

            txtNoteTitle = itemView.findViewById(R.id.txtNoteTitle);
            txtNoteText = itemView.findViewById(R.id.txtNoteText);
            imgNoteThumb = itemView.findViewById(R.id.imgNoteThumb);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickableInterface.onItemClick(getAdapterPosition());
                }
            });
        }
    }

    public interface NoteClickable{
        void onItemClick(int notePosition);
    }
}
