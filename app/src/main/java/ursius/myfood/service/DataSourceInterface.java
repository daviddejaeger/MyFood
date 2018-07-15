package ursius.myfood.service;

import java.util.List;

import ursius.myfood.ui.Recipe;

public interface DataSourceInterface
{
    List<Recipe> getListOfRecipes();
}
