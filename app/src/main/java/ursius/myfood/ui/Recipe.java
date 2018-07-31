package ursius.myfood.ui;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Recipe implements Serializable
{
    private String id;
    private String uid;
    private String title;
    private String description;
    private Date lastEaten;
    private double points;
    private String remarks;
    private int suggestionValue;
    private List<String> ingredients;

    //needed for Firebase
    public Recipe() {
    }

    public Recipe(String id,String uid, String title, String description, Date lastEaten, double points, String remarks,
                  int suggestionValue, List<String> ingredients) {
        this.id = id;
        this.uid = uid;
        this.title = title;
        this.description = description;
        this.lastEaten = lastEaten;
        this.points = points;
        this.remarks = remarks;
        this.suggestionValue = suggestionValue;
        this.ingredients = ingredients;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getLastEaten() {
        return lastEaten;
    }

    public void setLastEaten(Date lastEaten) {
        this.lastEaten = lastEaten;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSuggestionValue() {
        return suggestionValue;
    }

    public void setSuggestionValue(int suggestionValue) {
        this.suggestionValue = suggestionValue;
    }

    public void calculateSuggestionValue(){
        Random random = new Random();
        int randomValue = random.nextInt(50) + 1;

        int ratingValue = (int) (getPoints() * 20);

        long differenceDays = getDifferenceDays(getLastEaten(),new Date());
        int dateValue = (int) (differenceDays * 0.33);

        setSuggestionValue(randomValue + ratingValue + dateValue);
    }

    public static long getDifferenceDays(Date d1, Date d2) {
        long diff = d2.getTime() - d1.getTime();
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }
}
