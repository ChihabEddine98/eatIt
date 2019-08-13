package com.chihab_eddine98.eatit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chihab_eddine98.eatit.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class LoginActivity extends AppCompatActivity {


    EditText edt_phone,edt_mdp;
    Button btnConnect;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edt_phone=(MaterialEditText)findViewById(R.id.edt_phone);
        edt_mdp=(MaterialEditText)findViewById(R.id.edt_mdp);

        btnConnect=(Button) findViewById(R.id.btnConnect);


        FirebaseDatabase bdd=FirebaseDatabase.getInstance();
        final DatabaseReference table_user=bdd.getReference("User");


        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.child(edt_phone.getText().toString()).exists())
                        {
                            User user=dataSnapshot.child(edt_phone.getText().toString()).getValue(User.class);


                            if (user.getMdp().equals(edt_mdp.getText().toString()))
                            {
                                Toast.makeText(LoginActivity.this," Success !",Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(LoginActivity.this," Failed !",Toast.LENGTH_SHORT).show();

                            }
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this," Register First !",Toast.LENGTH_SHORT).show();
                        }




                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



            }
        });





    }
}
