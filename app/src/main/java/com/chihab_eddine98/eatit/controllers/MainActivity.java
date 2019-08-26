package com.chihab_eddine98.eatit.controllers;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chihab_eddine98.eatit.R;

public class MainActivity extends AppCompatActivity {



    Button btnConnect,btnRegister;
    TextView txtWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnConnect=(Button)findViewById(R.id.btnConnect);
        btnRegister=(Button)findViewById(R.id.btnRegister);

        txtWelcome=findViewById(R.id.txtWelcome);
        Typeface typeface=Typeface.createFromAsset(getAssets(),"fonts/Caviar_Dreams/Caviar_Dreams_Bold.ttf");
        txtWelcome.setTypeface(typeface);

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent login=new Intent(MainActivity.this, Login.class);
                startActivity(login);

            }
        });



        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent register=new Intent(MainActivity.this, Register.class);
                startActivity(register);

            }
        });





    }
}
