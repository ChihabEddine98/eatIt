package com.chihab_eddine98.eatit.controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class Register extends AppCompatActivity {



    EditText edt_phone,edt_mdp,edt_nom,edt_prenom;
    Button btnRegister;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);



        edt_phone=(MaterialEditText)findViewById(R.id.edt_phone);
        edt_mdp=(MaterialEditText)findViewById(R.id.edt_mdp);
        edt_nom=(MaterialEditText)findViewById(R.id.edt_nom);
        edt_prenom=(MaterialEditText)findViewById(R.id.edt_prenom);


        btnRegister=(Button) findViewById(R.id.btnRegister);

        final FirebaseDatabase bdd=FirebaseDatabase.getInstance();
        final DatabaseReference table_user=bdd.getReference("User");


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Common.isConnectedToNet(getBaseContext()))
                {
                    table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if(!dataSnapshot.child(edt_phone.getText().toString()).exists())
                            {
                                User user=new User(edt_nom.getText().toString(),edt_prenom.getText().toString(),
                                        "12/09/1998",edt_mdp.getText().toString());

                                table_user.child(edt_phone.getText().toString()).setValue(user);
                                Toast.makeText(Register.this," Account Created Succesfully !",Toast.LENGTH_SHORT).show();


                            }
                            else
                            {
                                Toast.makeText(Register.this," Try to connect this account already exists.",Toast.LENGTH_SHORT).show();
                                return;
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
                else
                {
                    Toast.makeText(Register.this," Check your connection !",Toast.LENGTH_SHORT).show();

                }


            }
        });



    }
}
