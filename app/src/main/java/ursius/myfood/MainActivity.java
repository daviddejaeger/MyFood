package ursius.myfood;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import java.util.Arrays;
import java.util.List;

import ursius.myfood.ui.Recipe;
import ursius.myfood.ui.recipemainscreen.RecipeMainScreenActivity;

public class MainActivity extends AppCompatActivity {

    // Choose an arbitrary request code value
    private static final int RC_SIGN_IN = 123;
    private static final String TAG = "ERROR";
    private FirebaseAuth mAuth;
    private RecyclerView mRecyclerView;
    private FirestoreRecyclerAdapter<Recipe, MainActivity.RecipeHolder> adapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            // already signed in
            FirebaseUser user = mAuth.getCurrentUser();
            //ToDO: Show user in toolbar and rest of MainActivity

            mRecyclerView = findViewById(R.id.my_recycler_view);

            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            mRecyclerView.setHasFixedSize(true);

            // use a linear layout manager
            mLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLayoutManager);

            Query query = FirebaseFirestore.getInstance()
                    .collection("recipes")
                    .whereEqualTo("uid",user.getUid());

            // Configure recycler adapter options:
            //  * query is the Query object defined above.
            //  * Recipe.class instructs the adapter to convert each DocumentSnapshot to a Recipe object
            FirestoreRecyclerOptions<Recipe> options = new FirestoreRecyclerOptions.Builder<Recipe>()
                    .setQuery(query, Recipe.class)
                    .build();

            adapter = new FirestoreRecyclerAdapter<Recipe, MainActivity.RecipeHolder>(options) {
                @Override
                public void onBindViewHolder(MainActivity.RecipeHolder holder, int position, Recipe model) {
                    // Bind the Recipe object to the RecipeHolder
                    // ...
                    holder.setTitle(model.getTitle());
                    holder.setDescription(model.getDescription());
                }

                @Override
                public MainActivity.RecipeHolder onCreateViewHolder(ViewGroup group, int i) {
                    // Create a new instance of the ViewHolder, in this case we are using a custom
                    // layout called R.layout.message for each item
                    View view = LayoutInflater.from(group.getContext())
                            .inflate(R.layout.item_recipe, group, false);

                    return new MainActivity.RecipeHolder(view);
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
            mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
        } else {
            // not signed in
            startActivityForResult(
                    // Get an instance of AuthUI based on the default app
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setIsSmartLockEnabled(!BuildConfig.DEBUG /* credentials */, true /* hints */)
                            .setAvailableProviders(Arrays.asList(
                                    new AuthUI.IdpConfig.EmailBuilder().build(),
                                    new AuthUI.IdpConfig.FacebookBuilder().build(),
                                    new AuthUI.IdpConfig.GoogleBuilder().build()))
                            .build(),
                    RC_SIGN_IN);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // RC_SIGN_IN is the request code you passed into startActivityForResult(...) when starting the sign in flow.
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {
                startActivity(new Intent(MainActivity.this, MainActivity.class));
                finish();
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    showSnackbar(R.string.sign_in_cancelled);
                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    showSnackbar(R.string.no_internet_connection);
                    return;
                }

                showSnackbar(R.string.unknown_error);
                Log.e(TAG, "Sign-in error: ", response.getError());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       MenuInflater inflater = getMenuInflater();
       inflater.inflate(R.menu.menu, menu);
       return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.recipes:
                startActivity(new Intent(this,RecipeMainScreenActivity.class));
                return true;
            case R.id.signOut:
                signOut();
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    private void showSnackbar(int message){
        Snackbar.make(findViewById(R.id.myCoordinatorLayout), message, Snackbar.LENGTH_SHORT)
                .show();
    }

    private void signOut(){
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(MainActivity.this, MainActivity.class));
                        finish();
                    }
                });
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

            mEditTextView.setOnClickListener(this);
            mDeleteTextView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            /*Recipe recipe = listOfRecipes.get(this.getAdapterPosition());
            Recipe item = mDataset.get(this.getAdapterPosition());
            if (view.getId() == mEditTextView.getId()){
                mRecyclerViewClickListener.onEditClick(item,view);
            } else if(view.getId() == mDeleteTextView.getId()){
                mRecyclerViewClickListener.onDeleteClick(item,view);
            }*/
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
}
