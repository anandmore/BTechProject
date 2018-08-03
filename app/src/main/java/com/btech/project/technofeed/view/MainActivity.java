package com.btech.project.technofeed.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.btech.project.technofeed.TechnoFeedApplication;
import com.btech.project.technofeed.R;
import com.btech.project.technofeed.adapter.DataAdapter;
import com.btech.project.technofeed.model.ArticleStructure;
import com.btech.project.technofeed.model.Constants;
import com.btech.project.technofeed.model.NewsResponse;
import com.btech.project.technofeed.network.ApiClient;
import com.btech.project.technofeed.network.ApiInterface;
import com.btech.project.technofeed.network.interceptors.OfflineResponseCacheInterceptor;
import com.btech.project.technofeed.network.interceptors.ResponseCacheInterceptor;
import com.btech.project.technofeed.util.UtilityMethods;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private String[] SOURCE_ARRAY = {"engadget", "techcrunch", "techradar", "the-next-web", "the-verge"};
    private String SOURCE;
    public String URL;
    public Boolean TOP;

    private ArrayList<ArticleStructure> articleStructure = new ArrayList<>();
    private DataAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Drawer result;
    private AccountHeader accountHeader;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private Parcelable listState;
    private Typeface montserrat_regular;
    private TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AssetManager assetManager = this.getApplicationContext().getAssets();
        montserrat_regular = Typeface.createFromAsset(assetManager, "fonts/Montserrat-Regular.ttf");

        createToolbar();
        createRecyclerView();

        SOURCE = SOURCE_ARRAY[0];
        TOP=true;
        mTitle.setText(R.string.toolbar_default_text);
        onLoadingSwipeRefreshLayout();

        createDrawer(savedInstanceState, toolbar, montserrat_regular);
    }

    private void createToolbar() {
        toolbar = findViewById(R.id.toolbar_main_activity);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        mTitle = findViewById(R.id.toolbar_title);
        mTitle.setTypeface(montserrat_regular);
    }

    private void createRecyclerView() {
        recyclerView = findViewById(R.id.card_recycler_view);
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    }

    private void createDrawer(Bundle savedInstanceState, final Toolbar toolbar, Typeface montserrat_regular) {
        PrimaryDrawerItem item0 = new PrimaryDrawerItem().withIdentifier(0).withName("Top Headlines")
                .withIcon(R.drawable.ic_whatshot).withTypeface(montserrat_regular);
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName("NEWS SOURCE")
                .withTypeface(montserrat_regular).withSelectable(false).withTextColor(getColor(R.color.menuText));
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withIdentifier(2).withName("Engadget")
                .withIcon(R.mipmap.ic_engadget).withTypeface(montserrat_regular);
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withIdentifier(3).withName("TechCrunch")
                .withIcon(R.mipmap.ic_techcrunch).withTypeface(montserrat_regular);
        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withIdentifier(4).withName("TechRadar")
                .withIcon(R.mipmap.ic_techradar).withTypeface(montserrat_regular);
        PrimaryDrawerItem item5 = new PrimaryDrawerItem().withIdentifier(5).withName("The Next Web")
                .withIcon(R.mipmap.ic_thenextweb).withTypeface(montserrat_regular);
        PrimaryDrawerItem item6 = new PrimaryDrawerItem().withIdentifier(6).withName("The Verge")
                .withIcon(R.mipmap.ic_theverge).withTypeface(montserrat_regular);
        SecondaryDrawerItem item7 = new SecondaryDrawerItem().withIdentifier(7).withName("MORE INFO")
                .withTypeface(montserrat_regular).withSelectable(false).withTextColor(getColor(R.color.menuText));
        SecondaryDrawerItem item8 = new SecondaryDrawerItem().withIdentifier(8).withName("About the app")
                .withIcon(R.drawable.ic_info).withTypeface(montserrat_regular);
        SecondaryDrawerItem item9 = new SecondaryDrawerItem().withIdentifier(9).withName("Open Source")
                .withIcon(R.drawable.ic_code).withTypeface(montserrat_regular);
        SecondaryDrawerItem item10 = new SecondaryDrawerItem().withIdentifier(10).withName("Powered by newsapi.org")
                .withIcon(R.drawable.ic_power).withTypeface(montserrat_regular);

        accountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.raw.ic_background)
                .withSavedInstance(savedInstanceState)
                .build();

        result = new DrawerBuilder()
                .withAccountHeader(accountHeader)
                .withActivity(this)
                .withToolbar(toolbar)
                .withSelectedItem(0)
                .addDrawerItems(item0, item1, item2, item3, item4, item5, item6, item7, item8, item9,
                        item10)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        int selected = (int) (long) drawerItem.getIdentifier();
                        switch (selected) {
                            case 0:
                                SOURCE = SOURCE_ARRAY[0];
                                TOP=true;
                                onLoadingSwipeRefreshLayout();
                                mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case 2:
                                SOURCE = SOURCE_ARRAY[0];
                                TOP=false;
                                onLoadingSwipeRefreshLayout();
                                mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case 3:
                                SOURCE = SOURCE_ARRAY[1];
                                TOP=false;
                                onLoadingSwipeRefreshLayout();
                                mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case 4:
                                SOURCE = SOURCE_ARRAY[2];
                                TOP=false;
                                onLoadingSwipeRefreshLayout();
                                mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case 5:
                                SOURCE = SOURCE_ARRAY[3];
                                TOP=false;
                                onLoadingSwipeRefreshLayout();
                                mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case 6:
                                SOURCE = SOURCE_ARRAY[4];
                                TOP=false;
                                onLoadingSwipeRefreshLayout();
                                mTitle.setText(((Nameable) drawerItem).getName().getText(MainActivity.this));
                                break;
                            case 8:
                                openAboutActivity();
                                break;
                            case 9:
                                URL="https://github.com/anandmore/BTechProject";
                                openWebViewActivity();
                                break;
                            case 10:
                                URL="https://newsapi.org/";
                                openWebViewActivity();
                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();
    }

    private void loadTopJSON() {
        swipeRefreshLayout.setRefreshing(true);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addNetworkInterceptor(new ResponseCacheInterceptor());
        httpClient.addInterceptor(new OfflineResponseCacheInterceptor());
        httpClient.cache(new Cache(new File(TechnoFeedApplication.getTechnoFeedApplicationInstance().getCacheDir(), "ResponsesCache"), 10 * 1024 * 1024));
        httpClient.readTimeout(60, TimeUnit.SECONDS);
        httpClient.connectTimeout(60, TimeUnit.SECONDS);
        httpClient.addInterceptor(logging);

        ApiInterface request = ApiClient.getClient(httpClient).create(ApiInterface.class);

        String country="in";
        String category="technology";
        Call<NewsResponse> call = request.getTopHeadlines(country, category, Constants.API_KEY);
        call.enqueue(new Callback<NewsResponse>() {

            @Override
            public void onResponse(@NonNull Call<NewsResponse> call, @NonNull Response<NewsResponse> response) {

                if (response.isSuccessful() && response.body().getArticles() != null) {

                    if (!articleStructure.isEmpty()) {
                        articleStructure.clear();
                    }

                    articleStructure = response.body().getArticles();

                    adapter = new DataAdapter(MainActivity.this, articleStructure);
                    recyclerView.setAdapter(adapter);
                    swipeRefreshLayout.setRefreshing(false);
                }
            }


            @Override
            public void onFailure(@NonNull Call<NewsResponse> call, @NonNull Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void loadJSON() {
        swipeRefreshLayout.setRefreshing(true);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addNetworkInterceptor(new ResponseCacheInterceptor());
        httpClient.addInterceptor(new OfflineResponseCacheInterceptor());
        httpClient.cache(new Cache(new File(TechnoFeedApplication.getTechnoFeedApplicationInstance().getCacheDir(), "ResponsesCache"), 10 * 1024 * 1024));
        httpClient.readTimeout(60, TimeUnit.SECONDS);
        httpClient.connectTimeout(60, TimeUnit.SECONDS);
        httpClient.addInterceptor(logging);

        ApiInterface request = ApiClient.getClient(httpClient).create(ApiInterface.class);

        Call<NewsResponse> call = request.getHeadlines(SOURCE, Constants.API_KEY);
        call.enqueue(new Callback<NewsResponse>() {

            @Override
            public void onResponse(@NonNull Call<NewsResponse> call, @NonNull Response<NewsResponse> response) {

                if (response.isSuccessful() && response.body().getArticles() != null) {

                    if (!articleStructure.isEmpty()) {
                        articleStructure.clear();
                    }

                    articleStructure = response.body().getArticles();

                    adapter = new DataAdapter(MainActivity.this, articleStructure);
                    recyclerView.setAdapter(adapter);
                    swipeRefreshLayout.setRefreshing(false);
                }
            }


            @Override
            public void onFailure(@NonNull Call<NewsResponse> call, @NonNull Throwable t) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onRefresh() {
        if(TOP==true){
            loadTopJSON();
        }
        else {
            loadJSON();
        }
    }

    private void onLoadingSwipeRefreshLayout() {
        if (!UtilityMethods.isNetworkAvailable()) {
            Toast.makeText(MainActivity.this, "No internet connection!", Toast.LENGTH_SHORT).show();
        }
        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        if(TOP==true){
                            loadTopJSON();
                        }
                        else {
                            loadJSON();
                        }
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_search:
                openSearchActivity();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void openAboutActivity() {
        Intent aboutIntent = new Intent(this, AboutActivity.class);
        startActivity(aboutIntent);
        this.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    private void openSearchActivity() {
        Intent searchIntent = new Intent(this, SearchActivity.class);
        startActivity(searchIntent);
        this.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    private void sendEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto: anandmore1997.am@gmail.com"));
        startActivity(Intent.createChooser(emailIntent, "Send feedback"));
    }

    public void onBackPressed() {
        if (result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.MyAlertDialogStyle);
            builder.setTitle(R.string.app_name);
            builder.setIcon(R.mipmap.ic_launcher_round);
            builder.setMessage("Do you want to Exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            finish();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        bundle = result.saveInstanceState(bundle);
        bundle = accountHeader.saveInstanceState(bundle);

        super.onSaveInstanceState(bundle);
        listState = recyclerView.getLayoutManager().onSaveInstanceState();
        bundle.putParcelable(Constants.RECYCLER_STATE_KEY, listState);
        bundle.putString(Constants.SOURCE, SOURCE);
        bundle.putString(Constants.TITLE_STATE_KEY, mTitle.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            SOURCE = savedInstanceState.getString(Constants.SOURCE);
            createToolbar();
            mTitle.setText(savedInstanceState.getString(Constants.TITLE_STATE_KEY));
            listState = savedInstanceState.getParcelable(Constants.RECYCLER_STATE_KEY);
            createDrawer(savedInstanceState, toolbar, montserrat_regular);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (listState != null) {
            recyclerView.getLayoutManager().onRestoreInstanceState(listState);
        }
    }

    private void openWebViewActivity() {
        Intent browserIntent = new Intent(MainActivity.this, WebViewActivity.class);
        browserIntent.putExtra(Constants.INTENT_URL, URL);
        startActivity(browserIntent);
        this.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }
}
