package com.example.books.networking;

import android.net.Uri;

import com.example.books.model.Book;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class ApiUtil {
    public ApiUtil() { }

    private static final String QUERY_PARAMETER_KEY = "q";
    private static final String BASE_URL = "https://www.googleapis.com/books/v1/volumes";
    private static final String KEY = "key";
    private static final String apiKey = "AIzaSyCK9Z5fuQBLM0RXV58u1Wmkt9zznb0269c";

    private static final String TITLE = "intitle:";
    private static final String AUTHOR = "inauthor:";
    private static final String PUBLISHER = "inpublisher:";
    private static  final String ISBN = "isbn:";

    public static URL buildURL(String title) {
        URL url = null;
        try {
            Uri uri = Uri.parse(BASE_URL).buildUpon()
                    .appendQueryParameter(QUERY_PARAMETER_KEY, title)
                    .appendQueryParameter(KEY, apiKey)
                    .build();
            url = new URL(uri.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildURL(String title, String author, String publisher, String isbn) {
        URL url = null;
        StringBuilder sb = new StringBuilder();

        if (!title.isEmpty()) sb.append(TITLE).append(title).append("+");
        if (!author.isEmpty()) sb.append(AUTHOR).append(author).append("+");
        if (!publisher.isEmpty()) sb.append(PUBLISHER).append(publisher).append("+");
        if (!isbn.isEmpty()) sb.append(ISBN).append(isbn).append("+");

        sb.setLength(sb.length() -1);

        String query = sb.toString();
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAMETER_KEY, query)
                .appendQueryParameter(KEY, apiKey)
                .build();

        try {
            url = new URL(uri.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

    public  static  String getJSON(URL url) {
        // establish a connection
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
             InputStream mStream = urlConnection.getInputStream();
            Scanner scanner = new Scanner(mStream);
            scanner.useDelimiter("\\A");

            // check if we have data
            boolean hasData = scanner.hasNext();
            if (hasData) {
                return scanner.next();
            }

            urlConnection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
            return e.getLocalizedMessage();
        }
        return null;
    }

    public static ArrayList<Book> getBooksFromJson(String json) {
        final String ID = "id";
        final String TITLE = "title";
        final String SUBTITLE = "subtitle";
        final String AUTHORS = "authors";
        final String PUBLISHER = "publisher";
        final String PUBLISHED_DATE="publishedDate";
        final String ITEMS = "items";
        final String VOLUME_INFO = "volumeInfo";
        final String DESCRIPTION = "description";
        final String IMAGE_LINKS = "imageLinks";
        final String THUMBNAIL = "thumbnail";

        ArrayList<Book> books = new ArrayList<>();
        try {
            JSONObject jsonBooks = new JSONObject(json);
            JSONArray arrayBooks = jsonBooks.getJSONArray(ITEMS);
            int numberOfBooks = arrayBooks.length();

            for (int i =0; i<numberOfBooks;i++){
                JSONObject bookJSON = arrayBooks.getJSONObject(i);
                JSONObject volumeInfoJSON =
                        bookJSON.getJSONObject(VOLUME_INFO);
                JSONObject imageLinkJson = volumeInfoJSON.getJSONObject(IMAGE_LINKS);
                int authorNum = volumeInfoJSON.getJSONArray(AUTHORS).length();
                String[] authors = new String[authorNum];
                for (int j=0; j<authorNum;j++) {
                    authors[j] = volumeInfoJSON.getJSONArray(AUTHORS).get(j).toString();
                }
                Book book = new Book(
                        bookJSON.getString(ID),
                        volumeInfoJSON.getString(TITLE),
                        (volumeInfoJSON.isNull(SUBTITLE)?"":volumeInfoJSON.getString(SUBTITLE)),
                        authors,
                        (volumeInfoJSON.isNull(PUBLISHER)?"":volumeInfoJSON.getString(PUBLISHER)),
                        (volumeInfoJSON.isNull(PUBLISHED_DATE)?"":volumeInfoJSON.getString(PUBLISHED_DATE)),
                        (volumeInfoJSON.isNull(DESCRIPTION)?"":volumeInfoJSON.getString(DESCRIPTION)),
                        (volumeInfoJSON.isNull(THUMBNAIL)?"":imageLinkJson.getString(THUMBNAIL)));
                books.add(book);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return books;
    }

}
