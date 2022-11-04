package com.example.wimm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wimm.fragment.ChangePasswordFragment;
import com.example.wimm.fragment.ChartFragment;
import com.example.wimm.fragment.HistoryFragment;
import com.example.wimm.fragment.HomeFragment;
import com.example.wimm.fragment.SpendingMoneyFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    Toolbar toolbar;
    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_SPENDING_MONEY = 1;
    private static final int FRAGMENT_CHART = 2;
    private static final int FRAGMENT_HISTORY = 3;
    private static  final int FRAGMENT_CHANGE_PASSWORD = 4;
    private int currentFragment = FRAGMENT_HOME;

    private NavigationView navigationView;

    private ImageView imgAVT;//vì circleimageview extend imageview
    private TextView textViewName, textViewEmail;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawerLayout);

        mAuth = FirebaseAuth.getInstance();

        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        initUI();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = findViewById(R.id.navigatonView);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
        replaceFragment(new HomeFragment());
        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);

        showUserInfor();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();
        if(user == null){
            startActivity(new Intent(MainActivity.this, Login.class));
        }

    }

    private void initUI(){
        navigationView = findViewById(R.id.navigatonView);
        imgAVT = navigationView.getHeaderView(0).findViewById(R.id.img_avata);
        textViewName = navigationView.getHeaderView(0).findViewById(R.id.tv_name);
        textViewEmail = navigationView.getHeaderView(0).findViewById(R.id.tv_email);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_home:
                if (currentFragment != FRAGMENT_HOME) {
                    replaceFragment(new HomeFragment());
                    currentFragment = FRAGMENT_HOME;
                }
                break;
            case R.id.nav_new_spending:
                if (currentFragment != FRAGMENT_SPENDING_MONEY) {
                    replaceFragment(new SpendingMoneyFragment());
                    currentFragment = FRAGMENT_SPENDING_MONEY;
                }
                break;
            case R.id.nav_chart:
                if (currentFragment != FRAGMENT_CHART) {
                    replaceFragment(new ChartFragment());
                    currentFragment = FRAGMENT_CHART;
                }
                break;
            case R.id.nav_history_spending:
                if (currentFragment != FRAGMENT_HISTORY) {
                    replaceFragment(new HistoryFragment());
                    currentFragment = FRAGMENT_HISTORY;
                }
                break;
            case R.id.nav_logout:
                mAuth.signOut();
                startActivity(new Intent(MainActivity.this, Login.class));
                break;
            case R.id.nav_change_password:
                if (currentFragment != FRAGMENT_CHANGE_PASSWORD) {
                    replaceFragment(new ChangePasswordFragment());
                    currentFragment = FRAGMENT_CHANGE_PASSWORD;
                }
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else super.onBackPressed();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.commit();
    }

    private void showUserInfor(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            return;
        }

        String name = user.getDisplayName();
        String email = user.getEmail();
        Uri photoUrl = user.getPhotoUrl();

        if(name == null){
            textViewName.setVisibility(View.GONE);
        } else {
            textViewName.setVisibility(View.VISIBLE);
            textViewName.setText(name);
        }
        textViewEmail.setText(email);
        Glide.with(this).load(photoUrl).error(R.drawable.avatar).into(imgAVT);
    }
}
