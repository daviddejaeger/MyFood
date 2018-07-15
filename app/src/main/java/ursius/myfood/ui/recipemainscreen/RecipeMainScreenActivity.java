package ursius.myfood.ui.recipemainscreen;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ursius.myfood.R;
import ursius.myfood.service.Controller;
import ursius.myfood.service.FakeDataSource;
import ursius.myfood.ui.Recipe;
import ursius.myfood.ui.ViewInterface;

public class RecipeMainScreenActivity extends AppCompatActivity implements ViewInterface {

    private List<Recipe> listOfRecipes;
    private LayoutInflater layoutInflater;
    private Controller controller;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_main_screen);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        layoutInflater = getLayoutInflater();

        controller = new Controller(this, new FakeDataSource());

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        //mAdapter = new MyAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void startDetailActivity(Recipe recipe) {

    }

    @Override
    public void setUpAdapterAndView(List<Recipe> listOfRecipes) {

    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }

        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private TextView mTextViewTitle;
            private TextView mTextViewDescription;
            private TextView mEditTextView;
            private TextView mDeleteTextView;

            public MyViewHolder(View itemView) {
                super(itemView);

                this.mTextViewTitle = itemView.findViewById(R.id.textView_recipe_title);
                this.mTextViewDescription = itemView.findViewById(R.id.textView_recipe_description);
                this.mEditTextView = itemView.findViewById(R.id.btnEdit);
                this.mDeleteTextView = itemView.findViewById(R.id.btnDelete);

                mEditTextView.setOnClickListener(this);
                mDeleteTextView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                Recipe recipe = listOfRecipes.get(this.getAdapterPosition());
                controller.get
            }
        }
    }
}
