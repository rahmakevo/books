package com.example.books.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.books.BookDetailActivity;
import com.example.books.R;
import com.example.books.model.Book;

import java.util.ArrayList;

public class BooksAdapter extends RecyclerView.Adapter<BooksAdapter.BookViewHolder> {
    ArrayList<Book> books;

    public BooksAdapter(ArrayList<Book> books) { this.books = books; }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.book_list_item, parent, false);
        return new BookViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = books.get(position);
        holder.bind(book);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public class BookViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mTitle;
        TextView mAuthors;
        TextView mPublisher;
        TextView mPublishedDate;
        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.textViewTitle);
            mAuthors = itemView.findViewById(R.id.textViewAuthor);
            mPublisher = itemView.findViewById(R.id.textViewPublisher);
            mPublishedDate = itemView.findViewById(R.id.textViewPublisherDate);

            itemView.setOnClickListener(this);
        }
        public void bind(Book book) {
            mTitle.setText(book.title);
            mAuthors.setText(book.author);
            mPublishedDate.setText(book.publishedDate);
            mPublisher.setText(book.publisher);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Book selectedBook = books.get(position);

            Intent mIntent = new Intent(itemView.getContext(), BookDetailActivity.class);
            mIntent.putExtra("Book", selectedBook);
            itemView.getContext().startActivity(mIntent);
        }
    }
}
