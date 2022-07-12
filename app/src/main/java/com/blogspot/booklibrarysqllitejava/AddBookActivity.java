package com.blogspot.booklibrarysqllitejava;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class AddBookActivity extends AppCompatActivity {

    private EditText bookName, bookAuthor, bookInt;
    private Button submite,delete;
    private int id=0;
    private  Boolean isUpdate= false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);
        bookName = findViewById(R.id.book_title);
        bookAuthor = findViewById(R.id.bookAuthor);
        bookInt = findViewById(R.id.bookPage);
        submite = findViewById(R.id.submite);
        delete = findViewById(R.id.delete);
        Intent in = getIntent();

        if (in != null) {
            String type = in.getStringExtra("type");
            if (type != null && type.equals("update")) {
                getSupportActionBar().setTitle("Update Book");
                submite.setText("Update");
                isUpdate=true;
                delete.setVisibility(View.VISIBLE);
            }
            String authorString = in.getStringExtra("Author");
            String titleString = in.getStringExtra("title");
            String pageString = in.getStringExtra("page");
            String idString = in.getStringExtra("id");
            Log.d("System", "onCreate:authorString "+authorString+" titleString "+ titleString+ " pageString "+pageString +" idString " +idString);
            bookName.setText(titleString);
            bookInt.setText(pageString);
            bookAuthor.setText(authorString);
            if(idString != null) {
                id = Integer.valueOf(idString);
            }
        }


        submite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String bn = bookName.getText().toString().trim();
                String ba = bookAuthor.getText().toString().trim();
                String bpString = bookInt.getText().toString().trim();


                if (!bn.equals("")) {
                    if (!ba.equals("")) {
                        if (!bpString.equals("")) {
                            int bp = Integer.valueOf(bpString);
                            MyDatabaseHelper db = new MyDatabaseHelper(AddBookActivity.this);
                            if (isUpdate) {
                                if (db.update(id, bn, ba, String.valueOf(bp))) {
                                    Toast.makeText(AddBookActivity.this, "Added Book Successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(AddBookActivity.this, "filed try again", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                if (db.saveBook(bn, ba, bp)) {

                                    restText();
                                    Toast.makeText(AddBookActivity.this, "Added Book Successfully", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(AddBookActivity.this, "filed try again", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } else {
                            bookName.setError("Enter Page count");

                        }
                    } else {
                        bookName.setError("Enter Author");
                    }

                } else {
                    bookName.setError("Enter Book");
                }

            }
        });

        delete.setOnClickListener(view -> {
            MyDatabaseHelper db = new MyDatabaseHelper(AddBookActivity.this);
            if(db.deleteById(id)){
                finish();
            }else {
                Toast.makeText(this, "Failed To Delete", Toast.LENGTH_SHORT).show();
            };
        });





    }

    private void restText() {
        bookName.setText("");
        bookAuthor.setText("");
        bookInt.setText("");
    }


}