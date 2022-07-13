package com.blogspot.booklibrarysqllitejava;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FloatingActionButton addBook;
    private MyDatabaseHelper db;

    CustomAdapter customAdapter;
    private ArrayList<String> book_id, book_title, book_author, book_page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler);
        addBook = findViewById(R.id.addBook);
        db = new MyDatabaseHelper(MainActivity.this);
        book_id = new ArrayList<>();
        book_title = new ArrayList<>();
        book_author = new ArrayList<>();
        book_page = new ArrayList<>();
        customAdapter = new CustomAdapter(MainActivity.this, this, book_id, book_title, book_page, book_author);
        setAdapter();


        addBook.setOnClickListener(view -> {
            Intent in = new Intent(MainActivity.this, AddBookActivity.class);
            startActivityForResult(in,2);
        });
        displayData();
    }

    void displayData() {
        Cursor c = db.getAllBooks();
        if (c != null) {
            if (c.getCount() > 0) {
                while (c.moveToNext()) {
                    book_id.add(c.getString(0));
                    book_title.add(c.getString(1));
                    book_author.add(c.getString(2));
                    book_page.add(c.getString(3));
                    Log.d("saveBook", "displayData:book_author " + book_author.toString());
                    Log.d("saveBook", "displayData:book_title " + book_title.toString());
                }
            }
            setAdapter();
        }
    }

    void displayDataById(String id) {
        Cursor c = db.getAllBooksById(id);
        if (c != null) {
            if (c.getCount() > 0) {
                book_id.clear();
                book_title.clear();
                book_author.clear();
                book_page.clear();
                while (c.moveToNext()) {
                    book_id.add(c.getString(0));
                    book_title.add(c.getString(1));
                    book_author.add(c.getString(2));
                    book_page.add(c.getString(3));
                    Log.d("saveBook", "displayData:book_author " + book_author.toString());
                    Log.d("saveBook", "displayData:book_title " + book_title.toString());
                }
            } else {
                Snackbar.make(findViewById(android.R.id.content), "no results found", Snackbar.LENGTH_SHORT).show();
            }
            setAdapter();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("System", "onActivityResult: ");
        if (requestCode == 2) {
            recreate();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.delete_all, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d("System", "onOptionsItemSelected: " + item.getItemId());
        if (item.getItemId() == R.id.deleteAll) {
            conformToDelete();
        }
        if (item.getItemId() == R.id.search_icon) {
            showSearchBook();

        }

        return super.onOptionsItemSelected(item);

    }

    private void showSearchBook() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(this);
        alert.setTitle("Search Book");
        alert.setMessage("Enter Book Id:");

        alert.setView(edittext);

        alert.setPositiveButton("Search", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String etBookId = edittext.getText().toString().trim();
                Log.d("system", "onClick: " + etBookId.trim());

                if (etBookId.equals("")) {
                    edittext.setError("Enter Book Id");
                } else {
                    displayDataById(etBookId.trim());
                    dialog.cancel();
                }

            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
        alert.setCancelable(false);
        alert.show();
    }

    private void conformToDelete(){
        View view = findViewById(android.R.id.content);
        Snackbar snackbar = Snackbar
                .make(view, "Confirm delete?", Snackbar.LENGTH_LONG)
                .setAction("YES", view1 -> deleteAllBooks());
        snackbar.show();
    }
    private void deleteAllBooks(){
        MyDatabaseHelper db = new MyDatabaseHelper(MainActivity.this);
        db.deleteAll();
        recreate();
    }

    private void setAdapter(){
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    }




}