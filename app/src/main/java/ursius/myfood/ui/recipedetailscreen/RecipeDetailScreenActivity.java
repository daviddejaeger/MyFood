package ursius.myfood.ui.recipedetailscreen;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import ursius.myfood.R;
import ursius.myfood.service.pickers.DatePickerFragment;

public class RecipeDetailScreenActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail_screen);
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        //do some stuff for example write on log and update TextField on activity
        ((EditText) findViewById(R.id.editTextRecipeLastEaten)).setText(dayOfMonth + "/" + (month+1) + "/" +year);
    }
}
