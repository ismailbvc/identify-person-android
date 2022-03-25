package com.elhardoum.identifyperson;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    static class Person {
        protected String name;
        protected Integer img;

        /**
          * A person object with their name and photo
          *
          * @param name name of person
          * @param img resource id for their image file
          */
        Person(String name, Integer img)
        {
            this.name = name;
            this.img = img;
        }
    }

    protected ArrayList<Person> persons = new ArrayList<>(Arrays.asList(
        new Person("Malala Yousafzai", R.drawable.malala_yousafzai),
        new Person("Brad Pitt", R.drawable.brad_pitt),
        new Person("Larry Page", R.drawable.larry_page),
        new Person("Nelson Mandela", R.drawable.nelson_mandela),
        new Person("LeBron James", R.drawable.lebron_james),
        new Person("Jeff Bezos", R.drawable.jeff_bezos),
        new Person("Steve Jobs", R.drawable.steve_jobs),
        new Person("Angela Merkel", R.drawable.angela_merkel)
    ));

    protected int index = 0;

    protected ImageView img;
    protected AppCompatButton opt1;
    protected AppCompatButton opt2;
    protected AppCompatButton opt3;
    protected AppCompatButton opt4;
    protected AppCompatButton next;

    protected int score = 0;

    /**
      * on activity creation
      *
      */
    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img = findViewById(R.id.person_avatar);
        opt1 = findViewById(R.id.button_option_1);
        opt2 = findViewById(R.id.button_option_2);
        opt3 = findViewById(R.id.button_option_3);
        opt4 = findViewById(R.id.button_option_4);
        AppCompatButton[] options = { opt1, opt2, opt3, opt4 };
        next = findViewById(R.id.button_continue);

        View.OnClickListener optClick = ref -> {
            AppCompatButton btn = (AppCompatButton) ref;
            boolean correct = btn.getText() == persons.get(index).name;

            // highlight wrong in red
            if ( ! correct ) {
                btn.setBackgroundTintList(getResources().getColorStateList(R.color.red, getTheme()));
                btn.setTextColor(getResources().getColor(R.color.white, getTheme()));
            } else {
                score++; // increment user score
            }

            for ( int i=0; i<4; i++ ) {
                // disable buttons
                options[i].setEnabled(false);

                // highlight correct option in green
                if (options[i].getText() == persons.get(index).name) {
                    options[i].setBackgroundTintList(getResources().getColorStateList(R.color.green, getTheme()));
                }
            }

            // enable continue
            next.setEnabled(true);
        };

        opt1.setOnClickListener(optClick);
        opt2.setOnClickListener(optClick);
        opt3.setOnClickListener(optClick);
        opt4.setOnClickListener(optClick);

        next.setOnClickListener(ref -> {
            if ( index+1 < persons.size() ) { // load next person
                for ( AppCompatButton btn: options ) {
                    btn.setBackgroundTintList(getResources().getColorStateList(R.color.light_grey, getTheme()));
                    btn.setTextColor(getResources().getColor(R.color.grey, getTheme()));
                    btn.setEnabled(true); // enable option
                }

                // disable continue
                ref.setEnabled(false);

                index++;
                reloadQuizUi();
            } else { // load scores
                 Intent intent = new Intent(MainActivity.this, ScoreActivity.class);
                 intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                 intent.putExtra("score_message", String.format("Your score: %d/%d", score, persons.size()));
                 MainActivity.this.startActivity(intent);
                 finish();
            }
        });

        Collections.shuffle(persons);
        reloadQuizUi();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.action_about) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("About App");
            builder.setMessage(R.string.about_text);
            builder.setPositiveButton("OK", null);
            builder.create().show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
      * refresh the quiz UI to show active person image and options
      *
      */
    @SuppressLint("DefaultLocale")
    protected void reloadQuizUi()
    {
        Person person = persons.get(index);
        img.setImageResource(person.img);

        ArrayList<String> others = new ArrayList<>();

        for ( int i=0; i<persons.size(); i++ ) {
            if (!persons.get(i).name.equals(person.name)) {
                others.add(persons.get(i).name);
            }
        }

        Collections.shuffle(others);

        int position = new Random().nextInt(4);

        opt1.setText( 0 == position ? person.name : others.get(0) );
        opt2.setText( 1 == position ? person.name : others.get(1) );
        opt3.setText( 2 == position ? person.name : others.get(2) );
        opt4.setText( 3 == position ? person.name : others.get(3) );

        Objects.requireNonNull(getSupportActionBar()).setTitle(String.format("Identify Person %d/%d", index+1, persons.size()));
    }
}