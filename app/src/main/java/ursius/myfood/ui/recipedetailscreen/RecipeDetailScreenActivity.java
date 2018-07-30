package ursius.myfood.ui.recipedetailscreen;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ursius.myfood.R;
import ursius.myfood.service.pickers.DatePickerFragment;
import ursius.myfood.ui.Recipe;

public class RecipeDetailScreenActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private EditText mTitle;
    private EditText mDescription;
    private EditText mLastEaten;
    private Date dateLastEaten;
    private EditText mRemarks;
    private RatingBar mRating;
    private Recipe recipe;
    private boolean isNew;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail_screen);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mDescription = findViewById(R.id.editTextRecipeDescription);
        mRemarks = findViewById(R.id.editTextRecipeRemarks);
        mTitle = findViewById(R.id.editTextRecipeTitle);
        mLastEaten = findViewById(R.id.editTextRecipeLastEaten);
        mRating = findViewById(R.id.ratingBar);

        mDescription.setImeOptions(EditorInfo.IME_ACTION_DONE);
        mDescription.setRawInputType(InputType.TYPE_CLASS_TEXT);
        mRemarks.setImeOptions(EditorInfo.IME_ACTION_DONE);
        mRemarks.setRawInputType(InputType.TYPE_CLASS_TEXT);

        // Using getSerializableExtra(String key) method
        recipe = (Recipe) getIntent().getSerializableExtra("RECIPE");
        //Nieuw recept
        if (recipe == null) {
            recipe = new Recipe();
            isNew = true;
        }
        //Bestaand recipt dus alle goed zetten
        else{
            mDescription.setText(recipe.getDescription());
            mRemarks.setText(recipe.getRemarks());
            mTitle.setText(recipe.getTitle());

            SimpleDateFormat sm = new SimpleDateFormat("dd/MM/yyyy");
            String strDate = "";
            if (recipe.getLastEaten() != null)
                strDate = sm.format(recipe.getLastEaten());
            mLastEaten.setText(strDate);

            mRating.setRating((float) recipe.getPoints());
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        //do some stuff for example write on log and update TextField on activity
        ((EditText) findViewById(R.id.editTextRecipeLastEaten)).setText(dayOfMonth + "/" + (month+1) + "/" +year);

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);

        dateLastEaten = calendar.getTime();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    public void onSaveButtonClicked(View view){
        //Recipe recipe =  new Recipe();
        recipe.setTitle(mTitle.getText().toString());
        recipe.setDescription(mDescription.getText().toString());
        recipe.setLastEaten(dateLastEaten);
        recipe.setRemarks(mRemarks.getText().toString());
        recipe.setPoints(mRating.getRating());

        if (isNew)
        {
            FirebaseUser user = mAuth.getCurrentUser();
            recipe.setUid(user.getUid());

            db.collection("recipes")
                    .add(recipe)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            //startActivity(new Intent(RecipeDetailScreenActivity.this,RecipeMainScreenActivity.class));
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Log.w(TAG, "Error adding document", e);
                            showSnackbar(R.string.ErrorSave);
                        }
                    });
        }
        else {
            db.collection("recipes").document(recipe.getId())
                    .set(recipe)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //Log.d(TAG, "DocumentSnapshot successfully written!");
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //Log.w(TAG, "Error writing document", e);
                            showSnackbar(R.string.ErrorSave);
                        }
                    });

        }
    }

    private void showSnackbar(int message){
        Snackbar.make(findViewById(R.id.myCoordinatorLayout), message, Snackbar.LENGTH_SHORT)
                .show();
    }
}
