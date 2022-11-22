package com.gabys.sscproject.ui;


import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.gabys.sscproject.R;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private FragmentManager fragmentManager;

    private CodeFragment codeFragment;
    private BlocksFragment blocksFragment;

    private final int codeFragmentIndex = R.id.nav_second_fragment;
    private final int blocksFragmentIndex = R.id.nav_first_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        nvDrawer = (NavigationView) findViewById(R.id.nvView);

        setupDrawerContent(nvDrawer);

        fragmentManager = getSupportFragmentManager();
        try {
            codeFragment = (CodeFragment) CodeFragment.class.newInstance();
            blocksFragment = (BlocksFragment) BlocksFragment.class.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        fragmentManager.beginTransaction().add(R.id.flContent, codeFragment, "code").commit();
        fragmentManager.beginTransaction().add(R.id.flContent, blocksFragment, "blocks").commit();

        blocksFragment.addFragmentManager(fragmentManager);
        blocksFragment.addCodeFragment(codeFragment);

        fragmentManager.beginTransaction().hide(blocksFragment).commit();
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                menuItem -> {
                    selectDrawerItem(menuItem);
                    return true;
                });
    }

    public void selectDrawerItem(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case blocksFragmentIndex:
                if (fragmentManager.findFragmentByTag("blocks") == null)
                    fragmentManager.beginTransaction().add(R.id.flContent, blocksFragment, "blocks").commit();
                fragmentManager.beginTransaction().show(blocksFragment).commit();
                fragmentManager.beginTransaction().hide(codeFragment).commit();
                setTitle("Blocks");
                break;
            case codeFragmentIndex:
                fragmentManager.beginTransaction().show(codeFragment).commit();
                fragmentManager.beginTransaction().hide(blocksFragment).commit();
                setTitle("Code Section");
                break;
            default:
        }
        menuItem.setChecked(true);
        mDrawer.closeDrawers();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mDrawer.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}