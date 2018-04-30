package recipehaoling;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * author: D Koger
 */
public class Recipe {
    private int recipeID;
    private String category;
    private String instruction;
    private String name;
    //full Constructor
    Recipe(int id, String cat, String instruct, String n){
        recipeID = id;
        category = cat;
        instruction = instruct;
        name = n;
    }
    //constructor without id
    Recipe(String cat, String instruct, String n){
        category = cat;
        instruction = instruct;
        name = n;
    }
    //constructor with id
    Recipe(int id){
        recipeID = id;
    }
    //constructor with name
    Recipe(String n){
        name = n;
    }
    public int getID(){
        return recipeID;
    }
    public String getCategory(){
        return category;
    }
    public String getInstruction(){
        return instruction;
    }
    public String getName(){
        return name;
    }
    public void setID(int id){
        recipeID = id;
    }
    public void setCategory(String c){
        category = c;
    }
    public void setInstruction(String i){
        instruction = i;
    }
    public void setName(String n){
        name = n;
    }
}
