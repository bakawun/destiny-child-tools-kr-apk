package com.arsylk.dcwallpaper.activities;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import com.arsylk.dcwallpaper.Adapters.DCWikiPagesAdapter;
import com.arsylk.dcwallpaper.R;

public class DCWikiActivity extends AppCompatActivity {
    private Context context = DCWikiActivity.this;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private EditText searchInput;
    private ViewGroup searchStars;
    private ListView listView;
    private DCWikiPagesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dcwiki);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        initViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.wiki_region_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    private void initViews() {
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white);
        }

        listView = findViewById(R.id.wiki_pages_list);
        drawerLayout = findViewById(R.id.wiki_drawer_layout);
        navigationView = findViewById(R.id.wiki_navigation_view);
        searchInput = navigationView.getHeaderView(0).findViewById(R.id.search_input);
        searchStars = navigationView.getHeaderView(0).findViewById(R.id.search_stars_layout);


        adapter = new DCWikiPagesAdapter(context);
        adapter.cacheBitmaps();
        listView.setAdapter(adapter);

        drawerLayout.setScrimColor(Color.TRANSPARENT);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(!item.isChecked());
                item.getIcon().setAlpha(item.isChecked() ? 127 : 255);
                if(adapter != null)
                    adapter.toggleParameter(item.getItemId());
                return false;
            }
        });

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(adapter != null) {
                    adapter.getFilter().filter(searchInput.getText());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        for(int i = 0; i < searchStars.getChildCount(); i++) {
            searchStars.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(adapter != null) {
                        int stars = Integer.parseInt(view.getTag().toString());
                        markUpStars(stars);
                        adapter.toggleStars(stars);
                    }
                }
            });
        }
    }

    private void markUpStars(int stars) {
        for(int i = 1; i < searchStars.getChildCount(); i++) {
            searchStars.getChildAt(i).setAlpha((i <= stars) ? 1.0f : 0.5f);
        }
    }
}
