package ursius.myfood.service;

import ursius.myfood.ui.Recipe;
import ursius.myfood.ui.ViewInterface;

public class Controller
{
    private ViewInterface view;
    private DataSourceInterface dataSource;

    public Controller(ViewInterface view, DataSourceInterface dataSource) {
        this.view = view;
        this.dataSource = dataSource;

        getListFromDataSource();
    }

    public void getListFromDataSource()
    {
        view.setUpAdapterAndView(
                dataSource.getListOfRecipes()
        );
    }

    public void onRecipeClick(Recipe testRecipe){
        view.startDetailActivity(testRecipe);
    };
}
