package com.example.books;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.books.networking.ApiUtil;

import java.net.URL;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        final EditText mTitle = findViewById(R.id.editTextTitle);
        final EditText mAuthor = findViewById(R.id.editTextAuthor);
        final EditText mPublisher = findViewById(R.id.editTextPublisher);
        final EditText mIsbn = findViewById(R.id.editTextIsbn);
        final Button btnSearch = findViewById(R.id.buttonSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = mTitle.getText().toString().trim();
                String author = mAuthor.getText().toString().trim();
                String publisher = mPublisher.getText().toString().trim();
                String isbn = mIsbn.getText().toString().trim();

                if (title.isEmpty() && author.isEmpty() && publisher.isEmpty() && isbn.isEmpty()) {
                    Toast.makeText(SearchActivity.this, "Please enter valid search data", Toast.LENGTH_SHORT).show();
                } else {
                    URL queryUrl = ApiUtil.buildURL(title, author, publisher, isbn);
                    Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                    intent.putExtra("query", queryUrl);
                    startActivity(intent);
                }
            }
        });
    }
}
