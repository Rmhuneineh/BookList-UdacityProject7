package com.example.android.booklist;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String GOOGLE_REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?q=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        final boolean  isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if(isConnected){
            TextView state = (TextView) findViewById(R.id.internet_text_view);
            state.setVisibility(View.GONE);
            final BookAsyncTask task = new BookAsyncTask();

            ImageView search = (ImageView) findViewById(R.id.search);
            search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText bookName = (EditText) findViewById(R.id.book_name);
                    String url = GOOGLE_REQUEST_URL + bookName.getText().toString();
                    Log.v("MainActivity", "URL: " + url);
                    task.execute(url);
                }
            });
        } else {
            TextView state = (TextView) findViewById(R.id.internet_text_view);
            state.setVisibility(View.VISIBLE);
        }
    }

    private void updateUi(ArrayList<Book> books){
        ListView listView = (ListView) findViewById(R.id.list_view);

        BookAdapter mAdapter = new BookAdapter(this, books);

        listView.setAdapter(mAdapter);
    }

    public class BookAsyncTask extends AsyncTask<String, Book, ArrayList<Book>>{

        @Override
        protected ArrayList<Book> doInBackground(String... urls) {
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }


            ArrayList<Book> result = Utils.fetchBookData(urls[0]);
            return result;
        }

        protected void onPostExecute(ArrayList<Book> result) {
            // If there is no result, do nothing.
            if (result == null) {
                return;
            }

            updateUi(result);
        }
    }
}
