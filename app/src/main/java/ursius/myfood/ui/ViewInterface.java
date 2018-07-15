package ursius.myfood.ui;

import java.util.Date;
import java.util.List;

public interface ViewInterface
{
    void startDetailActivity(Recipe recipe);
    void setUpAdapterAndView(List<Recipe> listOfRecipes);
}
