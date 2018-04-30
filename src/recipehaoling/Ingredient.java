package recipehaoling;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * author: D. Koger
 */
public class Ingredient {
    private int id;
    private String name;
    private int calories;
    private int fat;
    private int sodium;
    private String group;
    private int protein;
    private int sugar;
    //full constructor
    Ingredient(int id, String n, int cal, int f, int so, String g, int p, int s){
        this.id = id;
        name = n;
        calories = cal;
        fat = f;
        sodium = so;
        group = g;
        protein = p;
        sugar = s;
    }
    //instructor without id
    Ingredient(String n, int cal, int f, int so, String g, int p, int s){
        name = n;
        calories = cal;
        fat = f;
        sodium = so;
        group = g;
        protein = p;
        sugar = s;
    }
    //null constructor
    Ingredient(){
        
    }
    public int getID(){
        return id;       
    }
    public String getName(){
        return name;
    }
    public int getCalories(){
        return calories;
    }
    public int getFat(){
        return fat;
    }
    public int getSodium(){
        return sodium;
    }
    public String getGroup(){
        return group;
    }
    public int getProtien(){
        return protein;
    }
    public int getSugar(){
        return sugar;
    }
    public void setID(int id){
        this.id = id;
    }
    public void setName(String n){
        name = n;
    }
    public void setCalories(int c){
        calories = c;
    }
    public void setFat(int f){
        fat = f;
    }
    public void setSodium(int s){
        sodium = s;
    }
    public void setGroup(String g){
        group = g;
    }
    public void setProtein(int p){
        protein = p;
    }
    public void setSugar(int s){
        sugar = s;
    }
}
