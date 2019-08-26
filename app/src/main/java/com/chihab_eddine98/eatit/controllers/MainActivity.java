package com.chihab_eddine98.eatit.controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chihab_eddine98.eatit.R;
import com.chihab_eddine98.eatit.common.Common;
import com.chihab_eddine98.eatit.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {



    Button btnConnect,btnRegister;
    TextView txtWelcome;

    // FireBase
    FirebaseDatabase bdd;
    DatabaseReference table_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        btnConnect=(Button)findViewById(R.id.btnConnect);
        btnRegister=(Button)findViewById(R.id.btnRegister);

        txtWelcome=findViewById(R.id.txtWelcome);
        Typeface typeface=Typeface.createFromAsset(getAssets(),"fonts/Caviar_Dreams/Caviar_Dreams_Bold.ttf");
        txtWelcome.setTypeface(typeface);


        Paper.init(this);

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

        String phone=Paper.book().read(Common.USR_KEY);
        String mdp=Paper.book().read(Common.USR_Pass);

        if (phone!=null && mdp!=null)
        {
            if (!phone.isEmpty() && !mdp.isEmpty())
            {
                login(phone,mdp);
            }
        }




    }

    private void login(final String phone, final String mdp)
    {

        // Init Firebase
        bdd=FirebaseDatabase.getInstance();
        table_user=bdd.getReference("User");


        if(Common.isConnectedToNet(getBaseContext()))
        {

            table_user.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.child(phone).exists())
                    {
                        User user=dataSnapshot.child(phone).getValue(User.class);
                        user.setPhone(phone);

                        if (user.getMdp().equals(mdp))
                        {
                            Intent homeActivity=new Intent(MainActivity.this,Home.class);
                            Common.currentUser=user;
                            startActivity(homeActivity);
                            finish();

                        }
                        else
                        {
                            Toast.makeText(MainActivity.this," Failed !",Toast.LENGTH_SHORT).show();

                        }
                    }
                    else
                    {
                        Toast.makeText(MainActivity.this," Register First !",Toast.LENGTH_SHORT).show();
                    }




                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else
        {
            Toast.makeText(MainActivity.this," Check your connection !",Toast.LENGTH_SHORT).show();
            return;
        }
    }


}



