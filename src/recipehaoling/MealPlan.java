/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package recipehaoling;

/**
 *
 * @author ekustudent
 */
public class MealPlan {
    private int id;
    private String meal;
    private String day;
    private String weekID;
    
    MealPlan(String wID,String m, String d){
        weekID = wID;
        meal = m;
        day = d;
    }
    MealPlan(int i, String wID){
        id = i;
        weekID = wID;
    }
    public String getMeal(){
        return meal;
    }
    public String getDay(){
        return day;
    }
    public String getWeekID(){
        return weekID;
    }
    public void setWeekID(String i){
        weekID = i;
    }
    public int getID(){
        return id;
    }
    public void setID(int i){
        id = i;
    }
}
