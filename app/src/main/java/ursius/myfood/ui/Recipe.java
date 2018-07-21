package ursius.myfood.ui;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Recipe implements Serializable
{
    private String uid;
    private String title;
    private String description;
    private Date lastEaten;
    private double points;
    private String remarks;
    private List<String> ingredients;

    //needed for Firebase
    public Recipe() {
    }

    public Recipe(String uid, String title, String description, Date lastEaten, double points, String remarks, List<String> ingredients) {
        this.uid = uid;
        this.title = title;
        this.description = description;
        this.lastEaten = lastEaten;
        this.points = points;
        this.remarks = remarks;
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
}
