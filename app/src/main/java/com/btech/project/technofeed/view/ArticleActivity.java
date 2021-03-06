package com.btech.project.technofeed.view;

import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.btech.project.technofeed.R;
import com.btech.project.technofeed.model.Constants;
import com.btech.project.technofeed.util.UtilityMethods;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class ArticleActivity extends AppCompatActivity {
    private String URL;
    private Typeface montserrat_regular;
    private Typeface montserrat_semiBold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        createToolbar();
        floatingButton();
        assetManager();
        receiveFromDataAdapter(montserrat_regular, montserrat_semiBold);
        buttonLinktoFullArticle(montserrat_regular);
        if (!UtilityMethods.isNetworkAvailable()) {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.article_content), "No Internet Connection!", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    private void assetManager() {
        AssetManager assetManager = this.getApplicationContext().getAssets();
        montserrat_regular = Typeface.createFromAsset(assetManager, "fonts/Montserrat-Regular.ttf");
        montserrat_semiBold = Typeface.createFromAsset(assetManager, "fonts/Montserrat-SemiBold.ttf");
    }

    private void buttonLinktoFullArticle(Typeface montserrat_regular) {
        Button linkToFullArticle = findViewById(R.id.article_button);
        linkToFullArticle.setTypeface(montserrat_regular);
        linkToFullArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWebViewActivity();
            }
        });
    }

    private void openWebViewActivity() {
        Intent browserIntent = new Intent(ArticleActivity.this, WebViewActivity.class);
        browserIntent.putExtra(Constants.INTENT_URL, URL);
        startActivity(browserIntent);
    }

    private void receiveFromDataAdapter(Typeface montserrat_regular, Typeface montserrat_semiBold) {
        String headLine = getIntent().getStringExtra(Constants.INTENT_HEADLINE);
        String description = getIntent().getStringExtra(Constants.INTENT_DESCRIPTION);
        String date = getIntent().getStringExtra(Constants.INTENT_DATE);
        String imgURL = getIntent().getStringExtra(Constants.INTENT_IMG_URL);
        URL = getIntent().getStringExtra(Constants.INTENT_ARTICLE_URL);
        TextView content_Headline = findViewById(R.id.content_Headline);
        content_Headline.setText(headLine);
        content_Headline.setTypeface(montserrat_semiBold);
        TextView content_Description = findViewById(R.id.content_Description);
        content_Description.setText(description);
        content_Description.setTypeface(montserrat_regular);
        ImageView collapsingImage = findViewById(R.id.collapsingImage);
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .error(R.mipmap.ic_placeholder);
        Glide.with(this)
                .load(imgURL)
                .apply(options)
                .transition(withCrossFade())
                .into(collapsingImage);
    }

    private void createToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar_article);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void floatingButton() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this news! Sent from TechnoFeed App :\n\n" + Uri.parse(URL) + "\n\nDownload TechnoFeed App : http://v.ht/tfdwnld");
                startActivity(Intent.createChooser(shareIntent, "Share with"));
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}