package com.example.books;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;


import com.example.books.adapter.BooksAdapter;
import com.example.books.model.Book;
import com.example.books.networking.ApiUtil;

import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private ProgressBar mLoadingProgress;
    private RecyclerView mBooksList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLoadingProgress = findViewById(R.id.progressBar);
        mBooksList = findViewById(R.id.recyclerViewBooks);

        URL bookUrl = ApiUtil.buildURL("cooking");
        new BooksQueryTask().execute(bookUrl);

        LinearLayoutManager manager = new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false );
        mBooksList.setLayoutManager(manager);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        try {
            URL bookUrl = ApiUtil.buildURL(query);
            new BooksQueryTask().execute(bookUrl);

        } catch (Exception e) {
            e.printStackTrace();

        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public class BooksQueryTask extends AsyncTask<URL, Void, String> {

        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            return ApiUtil.getJSON(searchUrl);
        }

        @Override
        protected void onPostExecute(String result) {
            mLoadingProgress.setVisibility(View.INVISIBLE);

            ArrayList<Book> mBooks = ApiUtil.getBooksFromJson(result);
            String resultString = "";

            BooksAdapter adapter = new BooksAdapter(mBooks);
            mBooksList.setAdapter(adapter);

        }

        @Override
        protected void onPreExecute() {
            mLoadingProgress.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.book_list_menu, menu);
        final MenuItem mSearchItem = menu.findItem(R.id.action_search);
        final SearchView mSearchView = (SearchView) MenuItemCompat.getActionView(mSearchItem);
        mSearchView.setOnQueryTextListener(this);
        return true;
    }


}
