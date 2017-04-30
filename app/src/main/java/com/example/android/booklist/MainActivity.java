package com.example.android.booklist;


import android.app.LoaderManager.LoaderCallbacks;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderCallbacks<List<Book>> {

    private static final String GOOGLE_REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?q=";

    private static final int BOOK_LOADER_ID = 1;

    private BookAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView emptyView = (TextView) findViewById(R.id.state_text_view);
        final ListView booksListView = (ListView) findViewById(R.id.list_view);
        emptyView.setText(getString(R.string.state_empty_view));

        booksListView.setEmptyView(emptyView);

        final ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        ImageView search = (ImageView) findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                final boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();

                TextView state = (TextView) findViewById(R.id.state_text_view);
                final EditText bookName = (EditText) findViewById(R.id.book_name);

                if (isConnected) {
                    state.setVisibility(View.GONE);

                    mAdapter = new BookAdapter(MainActivity.this, new ArrayList<Book>());

                    booksListView.setAdapter(mAdapter);

                    if (!TextUtils.isEmpty(bookName.getText().toString().trim())) {
                        updateInfo();
                    } else {
                        Toast.makeText(MainActivity.this, getString(R.string.toast), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    state.setText(getString(R.string.state_no_internet));
                    state.setVisibility(View.VISIBLE);
                }

            }
        });


    }

    private void updateInfo() {
        android.app.LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(BOOK_LOADER_ID, null, MainActivity.this);
    }

    public static class BookLoader extends AsyncTaskLoader<List<Book>> {

        /**
         * Query URL
         */
        private String mUrl;

        /**
         * Constructs a new {@link BookLoader}.
         *
         * @param context of the activity
         * @param url     to load data from
         */
        public BookLoader(Context context, String url) {
            super(context);
            mUrl = url;
        }

        @Override
        protected void onStartLoading() {
            forceLoad();
        }

        /**
         * This is on a background thread.
         */
        @Override
        public List<Book> loadInBackground() {
            if (mUrl == null) {
                return null;
            }

            List<Book> books = Utils.fetchBookData(mUrl);
            return books;
        }
    }

    @Override
    public android.content.Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        EditText bookName = (EditText) findViewById(R.id.book_name);
        String title = bookName.getText().toString();
        title = title.replace(" ", "+");
        String uriString = GOOGLE_REQUEST_URL + title;

        return new BookLoader(this, uriString);
    }

    @Override
    public void onLoadFinished(android.content.Loader<List<Book>> loader, List<Book> books) {
        mAdapter.clear();
        if (books != null && !books.isEmpty()) {
            mAdapter.addAll(books);
        }
    }

    @Override
    public void onLoaderReset(android.content.Loader<List<Book>> loader) {
        mAdapter.clear();
    }


}
