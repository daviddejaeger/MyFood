package ursius.myfood.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ursius.myfood.ui.Recipe;

public class FakeDataSource implements DataSourceInterface
{


    @Override
    public List<Recipe> getListOfRecipes() {
        ArrayList<Recipe> listOfRecipes = new ArrayList<>();

        ArrayList<String> ingRecipe1 = new ArrayList<>();
        ingRecipe1.add("250g gehakt");
        ingRecipe1.add("250g americain");
        listOfRecipes.add(new Recipe(
                "uid1",
                "Spaghetti Bolognaise",
                "Snelle manier met frito",
                new Date(System.currentTimeMillis()-1000),
                8.5,
                "Remarks",
                ingRecipe1
                ));

        ArrayList<String> ingRecipe2 = new ArrayList<>();
        ingRecipe2.add("2 blikjes kokosmelk");
        ingRecipe2.add("2 el gele curry");
        listOfRecipes.add(new Recipe(
                "uid2",
                "Kip curry",
                "Met echte curry",
                new Date(System.currentTimeMillis()-10000),
                8.5,
                "Remarks",
                ingRecipe2
        ));

        ArrayList<String> ingRecipe3 = new ArrayList<>();
        ingRecipe3.add("1 broccoli");
        ingRecipe3.add("250g kalfsgehakt");
        listOfRecipes.add(new Recipe(
                "uid3",
                "Ovenschotel met speltpasta, kalfsgehaktballetjes en broccoli",
                "Sandra Bekkari - p.133",
                new Date(System.currentTimeMillis()-10000),
                6.5,
                "Recept in boek is voor 4 porties",
                ingRecipe3
        ));

        return listOfRecipes;
    }
}
