package com.example.noteme;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

/*INSTANCE OF ROOM DATABASE...CREATE DAO CONNECTION HERE*/
@Database(entities = {Note.class}, version = 1)
public abstract class NoteDatabase extends RoomDatabase {
    private static NoteDatabase instance;

    public abstract NoteDao noteDao(); //use this method to access our DAO interface operation

    //call instance of our DB anywhere and avoid calling more than once on multiple threads(i.e synchronized)
    public static synchronized NoteDatabase getInstance(Context context){
        if(instance == null){ //instantiate DB only if there's none existence
            //because this class is abstract, we cannot call new, hence, we use the builder
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    NoteDatabase.class, "note_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    //populate db on creating the database
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDatabase(instance).execute();

        }
    };

    private static class PopulateDatabase extends AsyncTask<Void, Void, Void>{
        NoteDao noteDao;
        public PopulateDatabase(NoteDatabase db){
            this.noteDao = db.noteDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.insert(new Note("Title 1", "Description 1", 100));
            noteDao.insert(new Note("Title 2", "Description 2", 100));
            noteDao.insert(new Note("Title 3", "Description 3", 100));
            return null;
        }
    }
}
