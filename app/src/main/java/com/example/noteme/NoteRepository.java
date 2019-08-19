package com.example.noteme;

import android.app.Application;
import android.os.AsyncTask;

import java.util.List;

import androidx.lifecycle.LiveData;

public class NoteRepository {
    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;

    public NoteRepository(Application application){
        NoteDatabase noteDatabase = NoteDatabase.getInstance(application);
        noteDao = noteDatabase.noteDao();
        allNotes = noteDao.selectAllNotes();
    }

    //APIs which viewmodel will reach for data from db
    public void insert(Note note){
        new InsertNoteAsycnTask(noteDao).execute(note);
    }
    public void update(Note note){
        new UpdateNoteAsycnTask(noteDao).execute(note);
    }
    public void delete(Note note){
        new DeleteNoteAsycnTask(noteDao).execute(note);
    }

    public void deleteAllNotes(){
        new DeleteAllNoteAsycnTask(noteDao).execute();
    }

    public LiveData<List<Note>> selectAllNotes(){
        //Room will auto run the code needed to generate Livedata on background thread, hence, we need not bother
        return allNotes;
    }

    private static class InsertNoteAsycnTask extends AsyncTask<Note, Void, Void>{
        NoteDao noteDao;

        public InsertNoteAsycnTask(NoteDao noteDao){
            this.noteDao = noteDao;
        }
        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.insert(notes[0]);
            return null;
        }
    }
    private static class UpdateNoteAsycnTask extends AsyncTask<Note, Void, Void>{
        NoteDao noteDao;

        public UpdateNoteAsycnTask(NoteDao noteDao){
            this.noteDao = noteDao;
        }
        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.update(notes[0]);
            return null;
        }
    }
    private static class DeleteNoteAsycnTask extends AsyncTask<Note, Void, Void>{
        NoteDao noteDao;

        public DeleteNoteAsycnTask(NoteDao noteDao){
            this.noteDao = noteDao;
        }
        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.delete(notes[0]);
            return null;
        }
    }
    private static class DeleteAllNoteAsycnTask extends AsyncTask<Void, Void, Void>{
        NoteDao noteDao;

        public DeleteAllNoteAsycnTask(NoteDao noteDao){
            this.noteDao = noteDao;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.deleteAllNotes();
            return null;
        }
    }

}
