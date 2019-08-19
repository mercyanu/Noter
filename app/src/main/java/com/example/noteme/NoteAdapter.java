package com.example.noteme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//extend recyclerview.adapter and pass the innerclass we created in angle bracket
public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> {

    private List<Note> notes = new ArrayList<>();
    private Context context;
    private OnMyItemClickListener listener;

    public NoteAdapter(Context context) {
        this.notes = notes;
        this.context = context;
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.note_items, parent, false);
        return new NoteHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        Note currentNote = notes.get(position);
        holder.noteDesc.setText(currentNote.getDescription());
        holder.desc.setText(currentNote.getTitle());
        holder.priority.setText(String.valueOf(currentNote.getPriority()));
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }


    public void setNotes(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    public Note getNoteAt(int position) {
        return notes.get(position);
    }

    public class NoteHolder extends RecyclerView.ViewHolder {
        private TextView noteDesc;
        private TextView desc;
        private TextView priority;

        public NoteHolder(View itemView) {
            super(itemView);
            this.noteDesc = itemView.findViewById(R.id.note_text);
            this.desc = itemView.findViewById(R.id.desc_text);
            this.priority = itemView.findViewById(R.id.priority);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onMyItemClick(notes.get(position));
                    }
                }
            });
        }
    }

    public interface OnMyItemClickListener {
        void onMyItemClick(Note note);
    }

    public void setOnMyItemClickListener(OnMyItemClickListener listener) {
        this.listener = listener;
    }
}
