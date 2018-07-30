package ursius.myfood.ui.recipemainscreen;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.firebase.ui.firestore.SnapshotParser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.List;

import ursius.myfood.R;
import ursius.myfood.ui.Recipe;
import ursius.myfood.ui.ViewInterface;
import ursius.myfood.ui.recipedetailscreen.RecipeDetailScreenActivity;

public class RecipeMainScreenActivity extends AppCompatActivity implements ViewInterface {

    private RecyclerView mRecyclerView;
    private FirestoreRecyclerAdapter<Recipe, RecipeHolder> adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private final FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_main_screen);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecipeMainScreenActivity.this, RecipeDetailScreenActivity.class);
                startActivity(intent);
            }
        });

        mRecyclerView = findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        Query query = FirebaseFirestore.getInstance()
                .collection("recipes")
                .whereEqualTo("uid",currentUser.getUid());

        // Configure recycler adapter options:
        //  * query is the Query object defined above.
        //  * Recipe.class instructs the adapter to convert each DocumentSnapshot to a Recipe object
        FirestoreRecyclerOptions<Recipe> options = new FirestoreRecyclerOptions.Builder<Recipe>()
                //.setQuery(query, Recipe.class)
                .setQuery(query, new SnapshotParser<Recipe>() {
                    @NonNull
                    @Override
                    public Recipe parseSnapshot(@NonNull DocumentSnapshot snapshot) {
                        return snapshot.toObject(Recipe.class);
                    }
                })
                .build();

        adapter = new FirestoreRecyclerAdapter<Recipe, RecipeHolder>(options) {
            @Override
            public void onBindViewHolder(RecipeHolder holder, int position, Recipe model) {
                // Bind the Recipe object to the RecipeHolder
                // ...
                final Recipe recipe = model;
                DocumentSnapshot snapshot =  getSnapshots().getSnapshot(holder.getAdapterPosition());
                recipe.setId(snapshot.getId());

                holder.setTitle(model.getTitle());
                holder.setDescription(model.getDescription());
                holder.mEditTextView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Intent intent = new Intent(RecipeMainScreenActivity.this, RecipeDetailScreenActivity.class);
                        intent.putExtra("RECIPE", recipe);
                        startActivity(intent);
                    }
                });
                holder.mDeleteTextView.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        db.collection("recipes").document(recipe.getId())
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        //Log.d(TAG, "DocumentSnapshot successfully deleted!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        showSnackbar(R.string.ErrorSave);
                                    }
                                });
                    }
                });
            }

            @Override
            public RecipeHolder onCreateViewHolder(ViewGroup group, int i) {
                // Create a new instance of the ViewHolder, in this case we are using a custom
                // layout called R.layout.message for each item
                View view = LayoutInflater.from(group.getContext())
                        .inflate(R.layout.item_recipe, group, false);

                return new RecipeHolder(view);
            }

            @Override
            public void onDataChanged() {
                // Called each time there is a new query snapshot. You may want to use this method
                // to hide a loading spinner or check for the "no documents" state and update your UI.
                // ...
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                // Called when there is an error getting a query snapshot. You may want to update
                // your UI to display an error message to the user.
                // ...
            }
        };

        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void startDetailActivity(Recipe recipe) {

    }

    @Override
    public void setUpAdapterAndView(List<Recipe> listOfRecipes) {

    }

    private class RecipeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTextViewTitle;
        private TextView mTextViewDescription;
        private TextView mEditTextView;
        private TextView mDeleteTextView;

        public RecipeHolder(View itemView) {
            super(itemView);

            this.mEditTextView = itemView.findViewById(R.id.btnEdit);
            this.mDeleteTextView = itemView.findViewById(R.id.btnDelete);
        }

        @Override
        public void onClick(View view) {

        }

        public void setTitle(String title) {
            this.mTextViewTitle = itemView.findViewById(R.id.textView_recipe_title);
            mTextViewTitle.setText(title);
        }


        public void setDescription(String description) {
            this.mTextViewDescription = itemView.findViewById(R.id.textView_recipe_description);
            mTextViewDescription.setText(description);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    private void showSnackbar(int message){
        Snackbar.make(findViewById(R.id.myCoordinatorLayout), message, Snackbar.LENGTH_SHORT)
                .show();
    }
}
