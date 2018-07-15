package ursius.myfood;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import ursius.myfood.service.Controller;
import ursius.myfood.service.DataSourceInterface;
import ursius.myfood.ui.Recipe;
import ursius.myfood.ui.ViewInterface;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

@RunWith(MockitoJUnitRunner.class)
public class ControllerUnitTest {

    @Mock
    DataSourceInterface dataSource;

    @Mock
    ViewInterface view;

    Controller controller;

    private static final Recipe testRecipe =  new Recipe(
            "Ovenschotel met speltpasta, kalfsgehaktballetjes en broccoli",
            "Sandra Bekkari - p.133",
            new Date(System.currentTimeMillis()-10000),
            6.5,
            "Recept in boek is voor 4 porties",
            new ArrayList<String>(Arrays.asList("ingredient1, ingredient2, ingredient3"))
    );

    @Before
    public void setUpTest(){
        MockitoAnnotations.initMocks(this);
        controller = new Controller(view, dataSource);
    }

    @Test
    public void onGetListDataSuccessful(){
        //Set up any data we need for the test
        ArrayList<Recipe> listOfRecipes = new ArrayList<>();
        listOfRecipes.add(testRecipe);

        //Set up our Mocks to return the Data we want
        Mockito.when(dataSource.getListOfRecipes())
                .thenReturn(listOfRecipes);

        //Call the method(Unit) we are testing
        controller.getListFromDataSource();

        //Check how the tested Class responds to the data it receives
        //or test it's behaviour
        Mockito.verify(view).setUpAdapterAndView(listOfRecipes);
    }
}