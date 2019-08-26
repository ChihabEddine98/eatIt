package com.chihab_eddine98.eatit.controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chihab_eddine98.eatit.R;
import com.chihab_eddine98.eatit.common.Common;
import com.chihab_eddine98.eatit.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class Login extends AppCompatActivity {


    EditText edt_phone,edt_mdp;
    Button btnConnect;
    CheckBox rememberMe;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edt_phone=(MaterialEditText)findViewById(R.id.edt_phone);
        edt_mdp=(MaterialEditText)findViewById(R.id.edt_mdp);
        rememberMe=findViewById(R.id.rememberMe);

        btnConnect=findViewById(R.id.btnConnect);


        FirebaseDatabase bdd=FirebaseDatabase.getInstance();
        final DatabaseReference table_user=bdd.getReference("User");

        Paper.init(this);

        btnConnect.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View view) {

                    if(Common.isConnectedToNet(getBaseContext()))
                    {
                        if (rememberMe.isChecked())
                        {
                            Paper.book().write(Common.USR_KEY,edt_phone.getText().toString());
                            Paper.book().write(Common.USR_Pass,edt_mdp.getText().toString());
                        }

                        table_user.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if (dataSnapshot.child(edt_phone.getText().toString()).exists())
                                {
                                    User user=dataSnapshot.child(edt_phone.getText().toString()).getValue(User.class);
                                    user.setPhone(edt_phone.getText().toString());

                                    if (user.getMdp().equals(edt_mdp.getText().toString()))
                                    {
                                        Intent homeActivity=new Intent(Login.this,Home.class);
                                        Common.currentUser=user;
                                        startActivity(homeActivity);
                                        finish();

                                    }
                                    else
                                    {
                                        Toast.makeText(Login.this," Failed !",Toast.LENGTH_SHORT).show();

                                    }
                                }
                                else
                                {
                                    Toast.makeText(Login.this," Register First !",Toast.LENGTH_SHORT).show();
                                }




                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(Login.this," Check your connection !",Toast.LENGTH_SHORT).show();
                        return;
                    }

                }





        });





    }
}
