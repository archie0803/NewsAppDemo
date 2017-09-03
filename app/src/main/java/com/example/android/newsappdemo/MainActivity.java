package com.example.android.newsappdemo;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.android.newsappdemo.Constants.BASE_URL;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    MainFragment mainFragment;
    DrawerLayout drawer;
    ListView mDrawerList;

    ArrayList<Source.Res> sourceArrayList;
    ArrayList<String> sourceNameList;
    ArrayAdapter<String> mSourceAdapter;
    String category;

    RetrofitHelper retrofitHelper;
    ApiInterface apiInterface;
    Call<Source> sourceResponseCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Home");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        retrofitHelper = new RetrofitHelper(BASE_URL);
        apiInterface = retrofitHelper.getAPI();
        mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.main_fragment);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        NavigationView rightNavigationView = (NavigationView) findViewById(R.id.nav_right_view);

        sourceArrayList = new ArrayList<>();
        sourceNameList = new ArrayList<>();
        mSourceAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, sourceNameList);
        mDrawerList = (ListView) findViewById(R.id.right_drawer);
        mDrawerList.setAdapter(mSourceAdapter);
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                setTitle(sourceArrayList.get(pos).getName());
                mainFragment.changeData(sourceArrayList.get(pos).getId());
                drawer.closeDrawer(GravityCompat.END);
            }
        });
        category = "";
        loadSources(category);


        rightNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                // Handle Right navigation view item clicks here.
                drawer.closeDrawer(GravityCompat.END); /*Important Line*/
                return true;
            }
        });
    }

    public void loadSources(String category) {
        sourceResponseCall = apiInterface.getSourceByCategory(category);
        sourceResponseCall.enqueue(new Callback<Source>() {
            @Override
            public void onResponse(Call<Source> call, Response<Source> response) {
                if (!response.isSuccessful()) {
                    sourceResponseCall = call.clone();
                    sourceResponseCall.enqueue(this);
                    return;
                }

                if (response.body() == null) return;
                if (sourceNameList.size() != 0) {
                    sourceNameList.clear();
                    sourceArrayList.clear();
                    mSourceAdapter.notifyDataSetChanged();
                }
                for (Source.Res source : response.body().getSources()) {
                    if (source != null && source.getId() != null && source.getName() != null && source.getCategory() != null) {
                        sourceNameList.add(source.getName());
                        sourceArrayList.add(source);
                    }
                    mSourceAdapter.notifyDataSetChanged();
                    mDrawerList.setAdapter(mSourceAdapter);
                }
            }

            @Override
            public void onFailure(Call<Source> call, Throwable t) {
                Log.i("TAG", t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.none) {
            loadSources("");
        } else if (id == R.id.business) {
            loadSources("business");
        } else if (id == R.id.entertainment) {
            loadSources("entertainment");
        } else if (id == R.id.gaming) {
            loadSources("gaming");
        } else if (id == R.id.general) {
            loadSources("general");
        } else if (id == R.id.music) {
            loadSources("music");
        } else if (id == R.id.politics) {
            loadSources("politics");
        } else if (id == R.id.science) {
            loadSources("science-and-nature");
        } else if (id == R.id.sport) {
            loadSources("sport");
        } else if (id == R.id.technology) {
            loadSources("technology");
        }

        if (id == R.id.action_openRight) {
            drawer.openDrawer(GravityCompat.END); /*Opens the Right Drawer*/
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
            setTitle("Home");
            mainFragment.changeData("bbc-news");

        } else if (id == R.id.nav_settings) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
