package com.example.mynotes;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;

import com.example.mynotes.ui.home.EditNoteFragment;
import com.example.mynotes.ui.home.NoteEntity;
import com.example.mynotes.ui.home.NoteListFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mynotes.databinding.ActivityMainBinding;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity implements NoteListFragment.Contract, EditNoteFragment.Contract {
    // to do

    private static final String NOTES_LIST_FRAGMENT_TAG = "NOTES_LIST_FRAGMENT_TAG";
    private boolean isTwoPaneMode = false;
    //
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.content_main);
        isTwoPaneMode = findViewById(R.id.optional_fragment_container) != null;
        showNoteList();
//

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment_content_main);
        assert navHostFragment != null;
        NavController navCo = navHostFragment.getNavController();
        NavigationUI.setupActionBarWithNavController(this, navCo, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navCo);


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    private void showNoteList() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.nav_host_fragment_content_main, new NoteListFragment(), NOTES_LIST_FRAGMENT_TAG)
                .commit();

    }

    private void showEditNote() {

        showEditNote(null);
    }

    private void showEditNote(@Nullable NoteEntity note) {
        if (!isTwoPaneMode) {
            setTitle(note == null ? R.string.create_note_title : R.string.edit_note_title);
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (!isTwoPaneMode) {
            transaction.addToBackStack(null);
        }
        transaction
                .add(isTwoPaneMode ? R.id.optional_fragment_container : R.id.nav_host_fragment_content_main, EditNoteFragment.newInstance(note))
                .commit();
    }

    @Override
    public void createNewNote() {

        showEditNote();
    }

    @Override
    public void editNote(NoteEntity note) {

        showEditNote(note);
    }

    @Override
    public void saveNote(NoteEntity note) {
        setTitle(R.string.app_name);
        getSupportFragmentManager().popBackStack();
        NoteListFragment noteListFragment = (NoteListFragment) getSupportFragmentManager().findFragmentByTag(NOTES_LIST_FRAGMENT_TAG);
        noteListFragment.addNote(note);
    }

// new
   
}