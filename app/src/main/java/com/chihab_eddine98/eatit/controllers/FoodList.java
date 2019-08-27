package com.chihab_eddine98.eatit.controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.chihab_eddine98.eatit.R;
import com.chihab_eddine98.eatit.common.Common;
import com.chihab_eddine98.eatit.database.Database;
import com.chihab_eddine98.eatit.interfaces.ItemClickListener;
import com.chihab_eddine98.eatit.model.Food;
import com.chihab_eddine98.eatit.viewHolder.FoodVH;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mancj.materialsearchbar.SimpleOnSearchActionListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FoodList extends AppCompatActivity {

    RecyclerView recycler_food;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Food, FoodVH> adapter;

    // Databases
    // Firebase
    FirebaseDatabase bdd;
    DatabaseReference table_food;
    // Local Db
    Database localDb;

    String categoryId="";


    // Search food by name
    MaterialSearchBar searchBar;
    FirebaseRecyclerAdapter<Food, FoodVH> searchAdapter;
    List<String> suggList=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);


        recycler_food=findViewById(R.id.recycler_food);
        recycler_food.setHasFixedSize(true);

        layoutManager=new LinearLayoutManager(this);

        recycler_food.setLayoutManager(layoutManager);




        bdd=FirebaseDatabase.getInstance();
        table_food=bdd.getReference("Food");
        localDb=new Database(this);




        if(getIntent()!=null)
        {
            categoryId=getIntent().getStringExtra("categoryId");

        }
        if(!categoryId.isEmpty() && categoryId!=null)
        {
            if (Common.isConnectedToNet(getBaseContext()))
            {
                loadListFood(categoryId);
            }
            else
            {
                Toast.makeText(FoodList.this," Check your connection !",Toast.LENGTH_SHORT).show();
                return;
            }
        }

        //Search

        searchBar=findViewById(R.id.searchBar);
        searchBar.setHint(" Entrez votre plat !");

        loadSuggest();
        searchBar.setLastSuggestions(suggList);
        searchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                List<String> suggest=new ArrayList<>();

                for (String search:suggList)
                {
                    if (search.toLowerCase().contains(searchBar.getText().toLowerCase()))
                       suggest.add(search);
                }

                searchBar.setLastSuggestions(suggest);


            }

            @Override
            public void afterTextChanged(Editable editable) {


            }
        });
        searchBar.setOnSearchActionListener(new SimpleOnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {
                if (!enabled)
                    recycler_food.setAdapter(adapter);

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text);
            }

            @Override
            public void onButtonClicked(int buttonCode) {
                super.onButtonClicked(buttonCode);
            }
        });




    }

    // Search Méthodes
    private void startSearch(CharSequence text) {


        searchAdapter=new FirebaseRecyclerAdapter<Food, FoodVH>(
                Food.class,
                R.layout.food_item,
                FoodVH.class,
                table_food.orderByChild("nom").equalTo(text.toString())
        ) {
            @Override
            protected void populateViewHolder(FoodVH foodVH, Food food, int i) {


                foodVH.food_nom.setText(food.getNom());
                Picasso.with(getBaseContext()).load(food.getImgUrl())
                        .into(foodVH.food_img);


                final Food local=food;

                foodVH.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        Intent foodDetailActivity=new Intent(FoodList.this,FoodDetail.class);
                        foodDetailActivity.putExtra("foodId",searchAdapter.getRef(position).getKey());

                        startActivity(foodDetailActivity);
                    }
                });

            }
        };


        recycler_food.setAdapter(searchAdapter);
    }

    private void loadSuggest() {

        table_food.orderByChild("categoryId").equalTo(categoryId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot postData:dataSnapshot.getChildren())
                {
                    Food itemSugg=postData.getValue(Food.class);
                    suggList.add(itemSugg.getNom());
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        });
    }

    private void loadListFood(String categoryId) {

        adapter=new FirebaseRecyclerAdapter<Food, FoodVH>(Food.class,R.layout.food_item,FoodVH.class,
                table_food.orderByChild("categoryId").equalTo(categoryId)) {
            @Override
            protected void populateViewHolder(final FoodVH foodVH, Food food,final int position) {

                foodVH.food_nom.setText(food.getNom());
                Picasso.with(getBaseContext()).load(food.getImgUrl())
                        .into(foodVH.food_img);

                // Favoris

                if(localDb.isFavoris(adapter.getRef(position).getKey()))
                {
                    foodVH.fav_img.setImageResource(R.drawable.ic_favorite_black_24dp);
                }

                // Changer l'icon après le clique

                foodVH.fav_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!localDb.isFavoris(adapter.getRef(position).getKey()))
                        {
                          localDb.addToFavoris(adapter.getRef(position).getKey());
                          foodVH.fav_img.setImageResource(R.drawable.ic_favorite_black_24dp);
                          Toast.makeText(FoodList.this, "Ajouté aux favoris !", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                          localDb.removeFromFavoris(adapter.getRef(position).getKey());
                          foodVH.fav_img.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                          Toast.makeText(FoodList.this, "Supprimé des favoris !", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


                final Food local=food;

                foodVH.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {

                        Intent foodDetailActivity=new Intent(FoodList.this,FoodDetail.class);
                        foodDetailActivity.putExtra("foodId",adapter.getRef(position).getKey());

                        startActivity(foodDetailActivity);
                        finish();
                    }
                });



            }
        };


        recycler_food.setAdapter(adapter);



    }
}
