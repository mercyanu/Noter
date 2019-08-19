package com.example.noteme;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NoteViewModel noteViewModel;
    public static final int ADD_NOTE_RREQUEST = 1;
    public static final int EDIT_NOTE_RREQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(this, "Oncreate method started", Toast.LENGTH_SHORT).show();

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);

        final NoteAdapter noteAdapter = new NoteAdapter(this);
        recyclerView.setAdapter(noteAdapter);

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.selectAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                //this method triggers whenever changes occur is our LiveData object
                noteAdapter.setNotes(notes);
            }
        });

        FloatingActionButton addNewNote = findViewById(R.id.fab_add_note);
        addNewNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AddEditActivity.class);
                startActivityForResult(i, ADD_NOTE_RREQUEST);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewModel.delete(noteAdapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Note Deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        noteAdapter.setOnMyItemClickListener(new NoteAdapter.OnMyItemClickListener() {
            @Override
            public void onMyItemClick(Note note) {
                Intent i = new Intent(MainActivity.this, AddEditActivity.class);
                i.putExtra(AddEditActivity.EXTRA_TITLE, note.getTitle());
                i.putExtra(AddEditActivity.EXTRA_DESCRIPTION, note.getDescription());
                i.putExtra(AddEditActivity.EXTRA_PRIORITY, note.getTitle());
                i.putExtra(AddEditActivity.EXTRA_ID, note.getId());
                startActivityForResult(i, EDIT_NOTE_RREQUEST);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all_notes:
                noteViewModel.deleteAllNote();
                Toast.makeText(this, "All notes deleted", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_NOTE_RREQUEST && resultCode == RESULT_OK) {
            //then handle the request for The Add Note Activity if request_code id true
            String title = data.getStringExtra(AddEditActivity.EXTRA_TITLE);
            String desccription = data.getStringExtra(AddEditActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditActivity.EXTRA_PRIORITY, 1);

            Note note = new Note(title, desccription, priority);
            noteViewModel.insert(note);
            Toast.makeText(this, "Note saved", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_NOTE_RREQUEST && resultCode == RESULT_OK) {

            int id = data.getIntExtra(AddEditActivity.EXTRA_ID, -1);
            if (id == -1) {
                Toast.makeText(this, "Something wentm wrong, please retry", Toast.LENGTH_SHORT).show();
                return;
            }

            String title = data.getStringExtra(AddEditActivity.EXTRA_TITLE);
            String desccription = data.getStringExtra(AddEditActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditActivity.EXTRA_PRIORITY, 1);

            Note note = new Note(title, desccription, priority);
            note.setId(id);

            noteViewModel.update(note);
            Toast.makeText(this, "Note update successful", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "Note was not saved", Toast.LENGTH_SHORT).show();
        }


    }
}
