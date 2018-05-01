package recipehaoling;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;

public class DatabaseHandler {

    Connection conn = null;
    
    //makes connection to database
    private static Connection setupConnection(){
        //creating database information strings
        //will be used to log into the database
        String jdbcDriver = "oracle.jdbc.driver.OracleDriver";
        String jdbcURL = "jdbc:oracle:thin:@cswinserv.eku.edu:1521:cscdb";        
        String username = "koger5452018";
        String password = "3053";
        
        try {
            //load jdbc driver
            Class.forName(jdbcDriver);          
            //connect to db
            Connection conn = DriverManager.getConnection(jdbcURL, username, password);
            return conn;
        }
        catch(Exception e){
            //display what went wrong
            JOptionPane.showMessageDialog(null, e);
        }
    return null;

    }
    //closing connections
    private static void close(Connection conn) 
    {
        if(conn != null) 

        {
            try

            {
                conn.close();
            }
            catch(Throwable whatever)
            {}
        }
    }
    //closing prepared statements
    private static void close(OraclePreparedStatement st)
    {
        if(st != null)
        {
            try
            {
                st.close();
            }
            catch(Throwable whatever)
            {}
        }
    }
    //closing result sets
    private static void close(OracleResultSet rs)
    {
        if(rs != null)
        {
            try
            {
                rs.close();
            }
            catch(Throwable whatever)

            {}
        }
    }
    
    /*
    this method will insert a recipe into the database
    it will first call insertRecipe then make several insert statements into
    the RecipeIngredients table for each ingredient that is required in that
    recipe
    
    **This method will require a recipe object and an array of ingredients**
    */
    public static void addRecipe(Recipe r, Ingredient[] i) throws InterruptedException{
        //add recipe to the table
        insertRecipe(r);
        //need to get r's ID number from the database
        getRecipeID(r);
        //insert all ingredients
        //for(int j = 0; j<i.length;j++){
        //    insertIngredient(i[j]);
        //}
        //add all of the ingredients required to the RecipeIngredients table
        
        insertRecipeIngredient(r,i);
	
        
    }
    
    //this method will insert the recipe and corresponding ingredients into the recipeIngredients table
    private static void insertRecipeIngredient(Recipe r, Ingredient[]i){
        //creating connection to the database
        Connection conn = DatabaseHandler.setupConnection();
        //creating oracle objects
        OraclePreparedStatement pst = null;
        for(int j=0;j<i.length;j++){
            try
            {           
                //creating the sql statement to insert the recipe to the database
                /*
                TODO: either get rid of quantity from the database or add qty to
                the ingredient class
                */
                String sqlStatement = "insert into RecipeIngredients values (?, ?, 1)";            

                pst = (OraclePreparedStatement) conn.prepareStatement(sqlStatement);            
                //attaching the data to the statement           
                pst.setString(1, i[j].getName());
                pst.setInt(2, r.getID());               
                //inserting into the database
                pst.execute();                                    
            }
            catch (Exception e)

            {
                //displaying what went wrong
                JOptionPane.showMessageDialog(null, e);
            }

            finally

            {
                
            }
        }
                //closing the connections
                DatabaseHandler.close(pst);
                DatabaseHandler.close(conn);
    }
    
    //this method will insert a recipe into the database
    //this method is private because the gui should use addRecipe in order to ensure
    //that the user is providing the recipe along with the ingredients.
    private static void insertRecipe(Recipe r){
        //creating connection to the database
        Connection conn = DatabaseHandler.setupConnection();
        //creating oracle objects
        OraclePreparedStatement pst = null;

        try
        {           
            //creating the sql statement to insert the recipe to the database
            String sqlStatement = "insert into recipe values (seq_recipe.nextval, ? , ? , ?)";            

            pst = (OraclePreparedStatement) conn.prepareStatement(sqlStatement);            
            //attaching the data to the statement           
            pst.setString(1, r.getCategory());
            pst.setString(2, r.getInstruction());
            pst.setString(3, r.getName());
            //inserting into the database
            pst.execute();                                    
        }
        catch (Exception e)

        {
            //displaying what went wrong
            JOptionPane.showMessageDialog(null, e);
        }

        finally

        {
            //closing the connections
            DatabaseHandler.close(pst);
            DatabaseHandler.close(conn);
        }
    }
    
    //this method will insert an ingredent into the database
    //TODO: need to check to see what happens when a duplicate ingredient name is added
    public static void insertIngredient(Ingredient i){
        //create connection to database
        Connection conn = DatabaseHandler.setupConnection();
        //preparing statement object
        OraclePreparedStatement pst = null;

        try
        {           
            //sql statement to insert the ingredient
            String sqlStatement = "insert into ingredients values (?, ?, ?, ?, ?, ?, ?)";                                    
            pst = (OraclePreparedStatement) conn.prepareStatement(sqlStatement);            
            //adding values to the statement
            pst.setString(1, i.getName());
            pst.setInt(2, i.getCalories());
            pst.setInt(3, i.getFat());
            pst.setInt(4, i.getSodium());
            pst.setString(5, i.getGroup());
            pst.setInt(6, i.getProtien());
            pst.setInt(7, i.getSugar());
            //inserting the data
            pst.execute();                                    
        }
        catch (Exception e)
        {
            //display what went wrong
            JOptionPane.showMessageDialog(null, e);
        }
        finally
        {            
            //closing the connections
            DatabaseHandler.close(pst);
            DatabaseHandler.close(conn);
        } 
    }
    
    //will return a list of all recipies in the database as recipe objects
    public static Recipe[] getRecipes(){
        //creating connection to database
        Connection conn = DatabaseHandler.setupConnection();
        //preparing oracle objects
        OraclePreparedStatement pst = null;
        OracleResultSet rs = null;
        //preparing Recipe List to return
        List<Recipe> currentRecipes = new ArrayList<>();        
        try
        {
            //grabbing all rows from the recipe table
            String sqlStatement = "SELECT * FROM Recipe";            
            pst = (OraclePreparedStatement) conn.prepareStatement(sqlStatement);
            //will contain the rows from the query
            rs = (OracleResultSet) pst.executeQuery();
            //iterating through each row
            while(rs.next()){
                //create recipe object from the columns in the row.
                String category = rs.getString("Category");
                String instruction = rs.getString("Instruction");
                String name = rs.getString("Name");
                //make a recipe object from the columns
                Recipe row = new Recipe(category,instruction,name);
                //add the recipe to the list
                currentRecipes.add(row);
            }
        }
        catch (Exception e)
        {
            //display an error message of what went wrong
            JOptionPane.showMessageDialog(null, e);
        }
        finally
        {            
            //close the connections
            DatabaseHandler.close(rs);
            DatabaseHandler.close(pst);
            DatabaseHandler.close(conn);
        } 
        //turn the list into an array for the calling method
        //TODO: ask Brain if the calling method can use a list instead of an array.
        Recipe[] returnedRecipes = new Recipe[currentRecipes.size()];
        for(int i=0;i<currentRecipes.size();i++){
           returnedRecipes[i]=currentRecipes.get(i);
        }
        //return the list        
        return returnedRecipes;
    }
    
    //this method will return an array of ingredients from the database
    public static Ingredient[] getIngredients(){
        //creating connection to database
        Connection conn = DatabaseHandler.setupConnection();
        //preparing oracle objects
        OraclePreparedStatement pst = null;
        OracleResultSet rs = null;
        //preparing Ingredient List to return
        List<Ingredient> currentIngredients = new ArrayList<Ingredient>();        
        try
        {
            //grabbing all rows from the ingredient table
            String sqlStatement = "SELECT * FROM Ingredients";            
            pst = (OraclePreparedStatement) conn.prepareStatement(sqlStatement);
            //will contain the rows from the query
            rs = (OracleResultSet) pst.executeQuery();
            //iterating through each row
            while(rs.next()){
                //create ingredient object from the columns in the row.
                String name = rs.getString("Name");
                int calories = rs.getInt("Calories");
                int fat = rs.getInt("Fat");
                int sodium = rs.getInt("Sodium");
                String group = rs.getString("Fgroup");
                int proteins = rs.getInt("Proteins");
                int sugar = rs.getInt("Sugar");
                
                //make an Ingredient object from the columns
                Ingredient row = new Ingredient(name,calories,fat,sodium,group,proteins,sugar);
                //add the recipe to the list
                currentIngredients.add(row);
            }
        }
        catch (Exception e)
        {
            //display an error message of what went wrong
            JOptionPane.showMessageDialog(null, e);
        }
        finally
        {            
            //close the connections
            DatabaseHandler.close(rs);
            DatabaseHandler.close(pst);
            DatabaseHandler.close(conn);
        } 
        //turn the list into an array for the calling method
        //TODO: ask Brain if the calling method can use a list instead of an array.
        Ingredient[] returnedIngredients = new Ingredient[currentIngredients.size()];
        for(int i=0;i<currentIngredients.size();i++){
           returnedIngredients[i]=currentIngredients.get(i);
        }
        //return the list        
        return returnedIngredients;
    }
    
    //this method will set query the database for a recipe and then set the objects
    //id to the RecipeID from the table
    public static Recipe getRecipeID(Recipe r){
        //creating connection to database
        Connection conn = DatabaseHandler.setupConnection();
        //preparing oracle objects
        OraclePreparedStatement pst = null;
        OracleResultSet rs = null;
        try
        {
            //grabbing all rows from the recipe table
            String sqlStatement = "SELECT * FROM Recipe where Name=?";            
            pst = (OraclePreparedStatement) conn.prepareStatement(sqlStatement);
            //adding data to sql statement
            
            pst.setString(1,r.getName());
            //will contain the rows from the query
            rs = (OracleResultSet) pst.executeQuery();
            //iterating through each row
            while(rs.next()){
                //add recipe id to the recipe object
                r.setID(rs.getInt("RecipeID"));
                
            }
        }
        catch (Exception e)
        {
            //display an error message of what went wrong
            JOptionPane.showMessageDialog(null, e);
        }
        finally
        {            
            //close the connections
            DatabaseHandler.close(rs);
            DatabaseHandler.close(pst);
            DatabaseHandler.close(conn);
        } 
        return r;
    }
    
    //this method will query the database for recipes that are similar to user input
    public static Recipe[] searchRecipes(String search){
        //creating connection to database
        Connection conn = DatabaseHandler.setupConnection();
        //preparing oracle objects
        OraclePreparedStatement pst = null;
        OracleResultSet rs = null;
        //preparing Recipe List to return
        List<Recipe> currentRecipes = new ArrayList<Recipe>();        
        try
        {
            //grabbing all rows from the recipe table
            String sqlStatement = "SELECT * FROM Recipe WHERE upper(category) Like upper(?)";            
            pst = (OraclePreparedStatement) conn.prepareStatement(sqlStatement);
            //adding data to prepared statement
            pst.setString(1, "%"+search+"%");
            //will contain the rows from the query
            rs = (OracleResultSet) pst.executeQuery();
            //iterating through each row
            while(rs.next()){
                //create recipe object from the columns in the row.
                int id = rs.getInt("RecipeID");
                String category = rs.getString("Category");
                String instruction = rs.getString("Instruction");
                String name = rs.getString("Name");
                //make a recipe object from the columns
                Recipe row = new Recipe(id,category,instruction,name);
                //add the recipe to the list
                currentRecipes.add(row);
            }
        }
        catch (Exception e)
        {
            //display an error message of what went wrong
            JOptionPane.showMessageDialog(null, e);
        }
        finally
        {            
            //close the connections
            DatabaseHandler.close(rs);
            DatabaseHandler.close(pst);
            DatabaseHandler.close(conn);
        } 
        //turn the list into an array for the calling method
        //TODO: ask Brain if the calling method can use a list instead of an array.
        Recipe[] returnedRecipes = new Recipe[currentRecipes.size()];
        for(int i=0;i<currentRecipes.size();i++){
           returnedRecipes[i]=currentRecipes.get(i);
        }
        //return the list        
        return returnedRecipes;
    }
    
    //this method will query the database for recipesIDs from the MealPlanRecipe table
    public static Recipe[] searchRecipeFromMealPlanRecipe(MealPlan m){
        //creating connection to database
        Connection conn = DatabaseHandler.setupConnection();
        //preparing oracle objects
        OraclePreparedStatement pst = null;
        OracleResultSet rs = null;
        //preparing Recipe List to return
        List<Recipe> currentRecipesIDs = new ArrayList<>();        
        try
        {
            //grabbing all rows from the recipe table
            String sqlStatement = "SELECT * FROM MealPlanRecipe where MealID =?";            
            pst = (OraclePreparedStatement) conn.prepareStatement(sqlStatement);
            //attach data to prepared statement
            pst.setInt(1, m.getID());
            
            //will contain the rows from the query
            rs = (OracleResultSet) pst.executeQuery();
            //iterating through each row
            while(rs.next()){
                //create recipe object from the RecipeID.
                int id = rs.getInt("RecipeID");
                
                //make a recipe object from the id
                Recipe row = new Recipe(id);
                //add the recipe to the list
                currentRecipesIDs.add(row);
            }
        }
        catch (Exception e)
        {
            //display an error message of what went wrong
            JOptionPane.showMessageDialog(null, e);
        }
        finally
        {            
            //close the connections
            DatabaseHandler.close(rs);
            DatabaseHandler.close(pst);
            DatabaseHandler.close(conn);
        } 
        //turn the list into an array for the calling method
        //TODO: ask Brain if the calling method can use a list instead of an array.
        Recipe[] returnedRecipes = new Recipe[currentRecipesIDs.size()];
        for(int i=0;i<currentRecipesIDs.size();i++){
           returnedRecipes[i]=currentRecipesIDs.get(i);
        }
        //return the list        
        return returnedRecipes;
    }
    
    //this method will query the database for recipes based on their ids and get their details
    public static Recipe queryRecipeDetails(Recipe r){
        //creating connection to database
        Connection conn = DatabaseHandler.setupConnection();
        //preparing oracle objects
        OraclePreparedStatement pst = null;
        OracleResultSet rs = null;
        //preparing Recipe List to return
        //List<Recipe> currentRecipes = new ArrayList<>();        
        try
        {
            //grabbing all rows from the recipe table
            String sqlStatement = "SELECT * FROM Recipe where RecipeID =?";            
            pst = (OraclePreparedStatement) conn.prepareStatement(sqlStatement);
            //add data to pst
            pst.setInt(1, r.getID());
            //will contain the rows from the query
            rs = (OracleResultSet) pst.executeQuery();
            //iterating through each row
            while(rs.next()){
                //create recipe object from the columns in the row.
                String category = rs.getString("Category");
                String instruction = rs.getString("Instruction");
                String name = rs.getString("Name");
                //make a recipe object from the columns
                r.setCategory(category);
                r.setInstruction(instruction);
                r.setName(name);
                //add the recipe to the list
                //currentRecipes.add(row);
            }
        }
        catch (Exception e)
        {
            //display an error message of what went wrong
            JOptionPane.showMessageDialog(null, e);
        }
        finally
        {            
            //close the connections
            DatabaseHandler.close(rs);
            DatabaseHandler.close(pst);
            DatabaseHandler.close(conn);
        }                         
        return r;
    }
    //this method will return the mealplan id
    private static MealPlan getMealPlanID(MealPlan m){
        //creating connection to database
        Connection conn = DatabaseHandler.setupConnection();
        //preparing oracle objects
        OraclePreparedStatement pst = null;
        OracleResultSet rs = null;                     
        try
        {
            //grabbing all rows from the recipe table
            String sqlStatement = "SELECT * FROM MealPlan WHERE DayOfWeek=? and weekID=? and Meal=?";            
            pst = (OraclePreparedStatement) conn.prepareStatement(sqlStatement);
            //adding data to prepared statement
            pst.setString(1, m.getDay());
            pst.setString(2, m.getWeekID());
            pst.setString(3, m.getMeal());
            //will contain the rows from the query
            rs = (OracleResultSet) pst.executeQuery();
            //iterating through each row
            while(rs.next()){
                //set the mealplan's id
                m.setID(rs.getInt("MealID"));
            }
        }
        catch (Exception e)
        {
            //display an error message of what went wrong
            JOptionPane.showMessageDialog(null, e);
        }
        finally
        {            
            //close the connections
            DatabaseHandler.close(rs);
            DatabaseHandler.close(pst);
            DatabaseHandler.close(conn);
        } 
        //return the mealplan
        return m;
    }
    //this method will query the database for mealplans that match a certian day and meal
    public static Recipe[] searchMealPlans(MealPlan m){
        //need to get mealplan id
        getMealPlanID(m);
        //get array of recipes
        Recipe[] foundRecipes = searchRecipeFromMealPlanRecipe(m);
        //add in the recipe details to the recipe objects
        for(int i = 0; i<foundRecipes.length;i++){
            queryRecipeDetails(foundRecipes[i]);
        }
        return foundRecipes;
    }
    
    //this method will add a mealplan and its recipes to mealplanRecipe table
    public static void addMealPlan(MealPlan m, Recipe r){
        //insert the mealplan into the table
        insertMealPlan(m);
        //add the mealplans id to the object
        getMealPlanID(m);
        //add the recipes id to the object
        getRecipeID(r);
        //insert into table
        insertMealPlanRecipe(m,r);
    }
    
    //this method will insert into the MealPlanRecipeTable
    private static void insertMealPlanRecipe(MealPlan m, Recipe r){
        //creating connection to the database
        Connection conn = DatabaseHandler.setupConnection();
        //creating oracle objects
        OraclePreparedStatement pst = null;

        try
        {           
            //creating the sql statement to insert the mealplanrecipe to the database
            String sqlStatement = "insert into MealPlanRecipe values ( ? , ?)";            

            pst = (OraclePreparedStatement) conn.prepareStatement(sqlStatement);            
            //attaching the data to the statement           
            pst.setInt(1, r.getID());
            pst.setInt(2, m.getID());
            
            //inserting into the database
            pst.execute();                                    
        }
        catch (Exception e)

        {
            //displaying what went wrong
            JOptionPane.showMessageDialog(null, e);
        }

        finally

        {
            //closing the connections
            DatabaseHandler.close(pst);
            DatabaseHandler.close(conn);
        }
    }
    //this method will insert a mealplan object into the database
    private static void insertMealPlan(MealPlan m){
        //creating connection to the database
        Connection conn = DatabaseHandler.setupConnection();
        //creating oracle objects
        OraclePreparedStatement pst = null;

        try
        {           
            //creating the sql statement to insert the mealplan to the database
            String sqlStatement = "insert into MealPlan values (seq_mealplan.nextval, ?, ?, ?)";            

            pst = (OraclePreparedStatement) conn.prepareStatement(sqlStatement);            
            //attaching the data to the statement           
            pst.setString(1, m.getDay());
            pst.setString(2,m.getWeekID());
            pst.setString(3, m.getMeal());
            
            //inserting into the database
            pst.execute();                                    
        }
        catch (Exception e)

        {
            //displaying what went wrong
            JOptionPane.showMessageDialog(null, e);
        }

        finally

        {
            //closing the connections
            DatabaseHandler.close(pst);
            DatabaseHandler.close(conn);
        }
    }
    
    //this method will query the database for all of the ingredients needed for
    //a specific meal plan. The gui can use this to figure out which ingredients
    //the user needs.
    
    public static Ingredient[] getShoppingNeededIngredients(String weekID){
        //get array of mealplanIDs with weekID
        MealPlan[] selectedMealPlans = attachMealPlanID(weekID);
        //get recipe ids from mealplan recipe table
        Recipe[] selectedRecipes = attachRecipeID(selectedMealPlans);
        //get ingredients from recipeIngredient table
        Ingredient[] selectedIngredients = getIngredientArray(selectedRecipes);
                
        //return the array
        return selectedIngredients;
        //WITH THE ARRAY WILL NEED TO CHECK AGAINST THE FRIDGE LIST TO KNOW WHAT TO
        //DISPLAY
    }
    //this method works with getShoppingNeededIngredients to attach the mealplan id
    private static MealPlan[] attachMealPlanID(String weekID){
        //creating connection to database
        Connection conn = DatabaseHandler.setupConnection();
        //preparing oracle objects
        OraclePreparedStatement pst = null;
        OracleResultSet rs = null;        
        //preparing mealplan list
        List<MealPlan> currentMealPlans = new ArrayList<MealPlan>();
        try
        {
            //grabbing all rows from the recipe table
            String sqlStatement = "SELECT * FROM MealPlan WHERE  weekID=?";            
            pst = (OraclePreparedStatement) conn.prepareStatement(sqlStatement);
            //adding data to prepared statement
            pst.setString(1, weekID);
            
            //will contain the rows from the query
            rs = (OracleResultSet) pst.executeQuery();
            //iterating through each row
            while(rs.next()){
                //set the mealplan's id
                int id = rs.getInt("MealID");
                MealPlan m = new MealPlan(id,weekID);
                currentMealPlans.add(m);
            }
        }
        catch (Exception e)
        {
            //display an error message of what went wrong
            JOptionPane.showMessageDialog(null, e);
        }
        finally
        {            
            //close the connections
            DatabaseHandler.close(rs);
            DatabaseHandler.close(pst);
            DatabaseHandler.close(conn);
        } 
        MealPlan[] returnedMealPlans = new MealPlan[currentMealPlans.size()];
        for(int j=0; j<currentMealPlans.size();j++){
            returnedMealPlans[j] = currentMealPlans.get(j);
        }
        //return the mealplan array
        return returnedMealPlans;
    }
    //this method works with getShoppingNeededIngredients to attach the recipeIDs
    private static Recipe[] attachRecipeID(MealPlan[] m){
        //creating connection to database
        Connection conn = DatabaseHandler.setupConnection();
        //preparing oracle objects
        OraclePreparedStatement pst = null;
        OracleResultSet rs = null;        
        //preparing mealplan list
        List<Recipe> currentRecipes = new ArrayList<Recipe>();
        for(int j=0; j<m.length;j++){
            try
            {
                //grabbing all rows from the recipe table
                String sqlStatement = "SELECT * FROM MealPlanRecipe WHERE  MealID=?";            
                pst = (OraclePreparedStatement) conn.prepareStatement(sqlStatement);
                //adding data to prepared statement
                pst.setInt(1, m[j].getID());

                //will contain the rows from the query
                rs = (OracleResultSet) pst.executeQuery();
                //iterating through each row
                while(rs.next()){
                    //set the mealplan's id
                    int id = rs.getInt("RecipeID");
                    Recipe r = new Recipe (id);
                    currentRecipes.add(r);
                }
            }
            catch (Exception e)
            {
                //display an error message of what went wrong
                JOptionPane.showMessageDialog(null, e);
            }
            finally
            {            

            }
        }
        //close the connections
        DatabaseHandler.close(rs);
        DatabaseHandler.close(pst);
        DatabaseHandler.close(conn);
        
        Recipe[] returnedRecipes = new Recipe[currentRecipes.size()];
        for(int k=0; k<currentRecipes.size();k++){
            returnedRecipes[k]=currentRecipes.get(k);
        }
        return returnedRecipes;
    }
    //this method works with getShoppingNeededIngredients to create the ingredient array
    private static Ingredient[] getIngredientArray(Recipe[] r){
        //creating connection to database
        Connection conn = DatabaseHandler.setupConnection();
        //preparing oracle objects
        OraclePreparedStatement pst = null;
        OracleResultSet rs = null;        
        //preparing mealplan list
        List<Ingredient> currentIngredients = new ArrayList<Ingredient>();
        for(int j=0; j<r.length;j++){
            try
            {
                //grabbing all rows from the recipe table
                String sqlStatement = "SELECT * FROM RecipeIngredients WHERE  RecipeID=?";            
                pst = (OraclePreparedStatement) conn.prepareStatement(sqlStatement);
                //adding data to prepared statement
                pst.setInt(1, r[j].getID());

                //will contain the rows from the query
                rs = (OracleResultSet) pst.executeQuery();
                //iterating through each row
                while(rs.next()){
                    //set the mealplan's id
                    String name = rs.getString("IngredientsName");
                    Ingredient i = new Ingredient (name);
                    currentIngredients.add(i);
                }
            }
            catch (Exception e)
            {
                //display an error message of what went wrong
                JOptionPane.showMessageDialog(null, e);
            }
            finally
            {            
            }
        }
        //close the connections
        DatabaseHandler.close(rs);
        DatabaseHandler.close(pst);
        DatabaseHandler.close(conn);
        
        Ingredient[] returnedIngredients = new Ingredient[currentIngredients.size()];
        for(int k=0; k<currentIngredients.size();k++){
            returnedIngredients[k]=currentIngredients.get(k);
        }
        return returnedIngredients;
    }
}