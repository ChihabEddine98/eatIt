package com.chihab_eddine98.eatit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {



    Button btnConnect,btnRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnConnect=(Button)findViewById(R.id.btnConnect);
        btnRegister=(Button)findViewById(R.id.btnRegister);


        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent login=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(login);

            }
        });



        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent register=new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(register);

            }
        });





    }
}
