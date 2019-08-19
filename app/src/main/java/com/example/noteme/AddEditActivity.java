package com.example.noteme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

public class AddEditActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String EXTRA_ID = "com.example.noteme.EXTRA_ID";
    public static final String EXTRA_TITLE = "com.example.noteme.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.example.noteme.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY = "com.example.noteme.EXTRA_PRIORITY";


    private EditText editTitle;
    private EditText editDesc;
    private NumberPicker priorityPicker;

    private String mTitle;
    private String mDesc;
    private String mPriority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        editTitle = findViewById(R.id.edit_title);
        editDesc = findViewById(R.id.edit_desc);
        priorityPicker = findViewById(R.id.priority_picker);
        priorityPicker.setMinValue(1);
        priorityPicker.setMaxValue(10);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            setTitle("Edit Note");
            editTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editDesc.setText(intent.getStringExtra(EXTRA_DESCRIPTION));
            priorityPicker.setValue(intent.getIntExtra(EXTRA_PRIORITY, 1));
        } else {
            setTitle("Add New Note");
        }
    }

    private void saveNote() {
        String title = editTitle.getText().toString();
        String description = editDesc.getText().toString();
        int priority = priorityPicker.getValue();

        if (title.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(this, "All fields reauired, Fill now!", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, title);
        data.putExtra(EXTRA_DESCRIPTION, description);
        data.putExtra(EXTRA_PRIORITY, priority);

        //check for value of the ID
        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if(id != -1){
            data.putExtra(EXTRA_ID, id);
        }
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save_note) {
            saveNote();
            return true;
        } else return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

    }
}
