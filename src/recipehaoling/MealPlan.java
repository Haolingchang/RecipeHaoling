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
    
    MealPlan(String m, String d){
        meal = m;
        day = d;
    }
    public String getMeal(){
        return meal;
    }
    public String getDay(){
        return day;
    }
    public int getID(){
        return id;
    }
    public void setID(int i){
        id = i;
    }
}
