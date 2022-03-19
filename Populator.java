import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

//https://github.com/simoc/csvjdbc
import org.relique.jdbc.csv.CsvDriver;

/**
 * Class which needs to be modified in order to complete some of the later milestones
 */
public class Populator {

    public static final boolean debug=false;

    /**
     * Constructor
     */
    public  Populator() {


    }

    /** 
     * @param args standard main args
     * @throws Exception it could all go horribly wrong
     */
    public static void main(String[] args) throws Exception {
        //dummyExample();
        // calls to methods which will complete the database setup and data population
        Populator p = new Populator();
        p.setup("solution.sql");
        p.milestone1("f.csv", "f");
        //p.milestone2("B.csv", "B", "output", false);
    }

    /**
     * A single text file containing all the INSERT..INTO statements required for
     * the csv file:
     * e.g.
     * INSERT INTO S(r,g,a) VALUES( 101, 'Eric Noel', '2001-012-25' );
     * NB the text field values are quoted in primes’.
     * 
     * @param csvFileName       name of the csv file containing the data
     * @param tableToInsertInto name of the table to insert data into
     * @return a string composed of the DML statements needed to insert the data.
     */
    public String milestone1(String csvFileName, String tableToInsertInto) {
        String result = "";

        result = insertCSVData(csvFileName, tableToInsertInto);

        return result; //return the insert statements
    }

    /*
     * @author David Bowes
     */
    static class Attribute implements Comparable<Attribute> {
        final String name;
        final String type;

        public Attribute(String name, String type) {
            this.name = name;
            this.type = type;
        }

        @Override
        public int compareTo(Populator.Attribute arg0) {
            return name.compareTo(arg0.name);
        }
    }

    /**
     * DO NOT MODIFY
     * 
     * @param csvFileName       name of the csv file containing the data
     * @param tableToInsertInto name of the table to insert data into
     * @param fileNameToSaveTo  name of file to save the DML statements to
     * @param append determine if the contents should be appended to the file if it already exists
     */
    public void milestone2(String csvFileName, String tableToInsertInto, String fileNameToSaveTo,boolean append) {
        String sql = milestone1(csvFileName, tableToInsertInto);
        saveStringToFile(fileNameToSaveTo, sql,append);
    }

    /**
     * 
     * @param csvFileName       name of the csv file containing the data
     * @param tableToInsertInto name of the table to insert data into
     * @throws Exception you need to do something with this!
     */
    public void milestone3(String csvFileName, String tableToInsertInto) throws Exception {
        exec(milestone1(csvFileName, tableToInsertInto));
    }

    /**
     * Complete the code which will read all csv files in the ENTITIES folder and
     * populate the appropriate ENTITIES.
     */
    public void milestone4() {
        // TODO
    }

    /**
     * Method to take a csv file where the first row is a head of field names and 
     * all other rows are data to be inserted into the named table
     * e.g.\\
     * DATA FILE LANGUAGE.CSV to be inserted into B:
     * 
     * l
     * 'English'
     * 'French'
     * 'Plutonian'
     * 
     * results in:
     * 
     * BEGIN TRANSACTION;
     * -- NB might already exist --
     * INSERT OR IGNORE INTO B(l) VALUES( 'English' );
     * INSERT OR IGNORE INTO B(l) VALUES( 'French' );
     * INSERT OR IGNORE INTO B(l) VALUES( 'Plutonian' );
     * COMMIT;
     * @param csvFileName name of the csvFile containing the data NB fileName does not have to match the entity name
     * @param primaryTableToInsertInto name of the table to insert into
     * @return a sql string containing appropriate insert into statements
     */
    public String insertCSVData(String csvFileName, String primaryTableToInsertInto) {
        String result = "";

        try{
            Connection csv = csvConn();
            Connection sqliteConnection = sqliteConn();

             /**
             * Getting table attributes.
             * @author David Bowes
             */
            DatabaseMetaData metaDataSql = sqliteConnection.getMetaData();
            TreeMap<String, Attribute> arrayList = new TreeMap<>();
            ResultSet resultSet = metaDataSql.getColumns(null, null, primaryTableToInsertInto, null);
            String name = null;
            String type = null;
            while (resultSet.next()){
                name = resultSet.getString("COLUMN_NAME");
                type = resultSet.getString("TYPE_NAME");
                arrayList.put(name, new Attribute(name, type));
            }

            // Create a Statement object to execute the query with.
            // A Statement is not thread-safe.
            Statement stmt = csv.createStatement();

            // Declare all the needed variables
            ResultSet results = stmt.executeQuery("SELECT * FROM " + primaryTableToInsertInto);
            ResultSetMetaData metaData = null;
            int countColumn = 0;
            String nameColumn = "";
            String value = "";
            String statementSQL = null;
            System.out.println("");

            //getting the headers and initialising them into the nameColumn variable
            while(results.next()){ //looping through all the rows
                if(metaData == null){ //if metadata is null, go into the if statement
                    metaData = results.getMetaData(); //getting metadata
				    countColumn = metaData.getColumnCount(); //getting our column count
                    //System.out.println(countColumn);

                    for (int i = 1; i <= countColumn; i++)
					{
                        nameColumn += metaData.getColumnName(i) + ","; //get all the column names
					}
                    nameColumn = nameColumn.substring(0, nameColumn.length() - 1); //trim the last comma
                    System.out.println("Column Names: " + nameColumn + "\n"); //print out so we can see them
                }
  
                for(int i = 1; i <= countColumn; i++){
                    if(type.equals("VARCHAR(64)")){ //if type equals VARCHAR(64)
                        value = value + results.getString(i) + ",";  //put the value in the string
                    } else {
                        value = value + results.getString(i) + ","; //put the value in the string but in an integer way
                        Integer integer = Integer.parseInt(value);
                        value = value + integer;
                    }
                }
                value = value.substring(0, value.lastIndexOf(",")); //trim the last comma
                statementSQL = "INSERT OT IGNORE INTO " + primaryTableToInsertInto + "(" + nameColumn + ") VALUES(" + value + ");\n"; //insert statements

                value = ""; //reset value
                result = result + statementSQL; //put all the statements in the return
            }
            System.out.println(result); //print the statements
        } catch (Exception e) {
            System.out.println(e);
        }
        return result; //return the insert statements
    }

    /**
     * A single text file containing all the correct INSERT..INTO statements for
     * data which involves TWO entities.
     * e.g.\\
     * DATA FILE HERO.CSV to be inserted into S and R:
     * 
     * r,g,a,q,p
     * 101,'Eric Noel','2001-12-25','Eric Noel', 'Santa Clause'
     * 
     * results in:
     * 
     * PRAGMA foreign_keys=ON;
     * BEGIN TRANSACTION;
     * -- NB might already exist --
     * INSERT OR IGNORE INTO S(r,g,a) VALUES( 101, 'Eric Noel',
     * '2001-012-25' );
     * INSERT INTO R(r,q,p) VALUES (101,'Eric Noel','Santa Clause');
     * COMMIT;
     * 
     * @param csvFileName                name of the csv file containing the data
     * @param primaryTableToInsertInto   name of the table to insert data into
     * @param secondaryTableToInsertInto name of the table to insert data into
     *                                   which has a foreign key dependency on the
     *                                   primaryTableToInsertInto i.e. data can't be
     *                                   put into secondaryTableToInsertInto if
     *                                   there is no related item in the
     *                                   primaryTableToInsertInto
     * @return a string composed of the DML statements needed to insert the data.
     */
    public String milestone5(String csvFileName, String primaryTableToInsertInto, String secondaryTableToInsertInto) {
        String result = "";
        // TODO
        return result;
    }

    /**
     * DO NOT MODIFY
     * Insert data from a single csv file which spans two entities
     * This is not easy and is a stretch problem
     * @param csvFileName                name of the csv file containing the data
     * @param primaryTableToInsertInto   name of the table to insert data into
     * @param secondaryTableToInsertInto name of the table to insert data into
     *                                   which has a foreign key dependency on the
     *                                   primaryTableToInsertInto i.e. data can't be
     *                                   put into secondaryTableToInsertInto if
     *                                   there is no related item in the
     *                                   primaryTableToInsertInto
     * @param fileNameToSaveTo           name of file to save the DML statements to
     * @param append determine if the contents should be append to a file if it already exists
     */
    public void instoCSV(String csvFileName, String primaryTableToInsertInto, 
    String secondaryTableToInsertInto,
            String fileNameToSaveTo,boolean append) {
        String sql = milestone5(csvFileName, primaryTableToInsertInto, secondaryTableToInsertInto);
        saveStringToFile(fileNameToSaveTo, sql,append);
    }

    /**
     * A method to update the ages in S.j based on S.a which is a date of birth.
     * See the courswork assignment for an appropriate sql update statement.
     */
    public void updateAllAges(){
        // TODO

    }

    /**
     * A method to update the Plaent census data in D.e 
     * based on A and S which is the relationship(A) of who (S) lives on which planet(D).
     * NB, the census data is the count of people where their age (S.j) is LESS than 100 years on the date of the census.
     * ALSO ignore ages which are less than 0.
     * 
     */
    public void updateCensus(){
        // TODO

    }

    /**
     * Inserts data from a csv file into the set of entities (and related relationships)
     * A super stretch question
     * You will have to work out which entities contain which attributes 
     * and then work out the order in which they should be inserted
     * where relations are involved, you may have needed to 
     * add extra attributes to tables to code 1:M relationships which will have their own names not in the er diagram!
     * This class will therefore have to 'know' about the extra attibutes
     * @param csvFile the path of the csv file. The file will ALWAYS have a header row at the start which indicates the attribute names.
     * @param entities the array of entities which may be needed
     * @param relations the array of relationships which may be needed
     * @return return true if the process was successfull
     */
    public boolean insertCSVData(String csvFile,String[] entities,String[] relations){
        boolean result=true;
        // TODO
        return result;
    }


    /**
     * Method to find all of the super heroes in the database which have a power containing the the value of txt
     * @param txt the text which is contained in a super heroes power
     * @return a list of the hidden identies of the super heroes with a power
     */
    public List<String> listSuperHeroesContainingPower(String txt){
        List<String> result=new ArrayList<>();
        // TODO
        return result;
    }

    public TreeSet<String> entityAttributes(String entityName){
        TreeSet<String> result=new TreeSet<>();
        // TODO
        return result;
    }

    /**
     * Method to create appropriate sql statements to insert data from a csv file
     * @param csvFile path to the csv file.  The file will ALWAYS have a header row at the start which indicates the attribute names.
     * @param entities the array of entities which may be needed
     * @param relations the array of relationships which may be needed
     * @return DML sql string which will insert data into the entities from the csv file
     */
    public String createInsertSQLForCsvFile(String csvFile,String[] entities,String[] relations){
        StringBuilder builder=new StringBuilder();
        // TODO
        return builder.toString();
    }

    /**
     * 
     * @param fileNameToSaveTo file name to save to
     * @param text the text to be saved into the file
     * @param append if true and the file already exists the text will be added to the end of file
     */
    private void saveStringToFile(String fileNameToSaveTo, String text,boolean append) {
        //NB APPEND contents to file
        try (FileWriter fw = new FileWriter(fileNameToSaveTo,append)) {
            fw.append(text);
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Inserts the result of parsing the csv file into the database
     * 
     * @param csvFileName                name of the csv file containing the data
     * @param primaryTableToInsertInto   name of the table to insert data into
     * @param secondaryTableToInsertInto name of the table to insert data into
     *                                   which has a foreign key dependency on the
     *                                   primaryTableToInsertInto i.e. data can't be
     *                                   put into secondaryTableToInsertInto if
     *                                   there is no related item in the
     *                                   primaryTableToInsertInto
     * @throws Exception  it could all go horribly wrong
     */
    public void milestone6(String csvFileName, String primaryTableToInsertInto, String secondaryTableToInsertInto)
            throws Exception {
                //NB assumes valid sql inserts were generated bile milestoneF1
                //do not rely on this in production code (HINT)
                //Can change the rest if you want
        exec(milestone5(csvFileName, primaryTableToInsertInto, secondaryTableToInsertInto));
    }

    /**
     * Here just as an example piece of code
     * 
     * @throws Exception it could all go horribly wrong
     */
    public static void dummyExample() throws Exception {
        Populator populator=new Populator();
        Connection csv = populator.csvConn();
        Connection sqlite = populator.sqliteConn();

        // get a list of tables and views
        System.out.println("List of table names based on CSV files");
        DatabaseMetaData md = csv.getMetaData();
        ResultSet rs = md.getTables(null, null, "%", null);
        while (rs.next()) {
            System.out.println(rs.getString(3));
        }
        // Create a Statement object to execute the query with.
        // A Statement is not thread-safe.
        Statement stmt = csv.createStatement();

        // Select the ID and NAME columns from sample.csv
        ResultSet results = stmt.executeQuery("SELECT r,g FROM S order by r ASC");
        // Dump out the results to a CSV file with the same format
        // using CsvJdbc helper function
        boolean append = true;
        System.out.println("\nData from planets.csv");
        CsvDriver.writeToCsv(results, System.out, append);
        System.out.println("\nData from human table");
        CsvDriver.writeToCsv(sqlite.createStatement().executeQuery("SELECT r,g FROM S order by r ASC"),
                System.out, append);
    }

    /**
     * Create a string containing the csv version of the resultset
     * @param r ResultSet to be used in building the csv contents
     * @return a String representing the attributes and data of the ResultSet
     */
    public String csvIt(ResultSet r) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            final String utf8 = StandardCharsets.UTF_8.name();
            PrintStream ps = new PrintStream(baos, true, utf8);
            CsvDriver.writeToCsv(r, ps, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String(baos.toByteArray());
    }

    //DO NOT CHANGE
    static Connection csv = null;

    /**
     * ON PAIN of Zylon, do not modify this method
     * 
     * @return a connection to the csv files in this directory
     */
    public Connection csvConn() {
        try {
            if (csv == null || csv.isClosed()) {
                String folder = "./ENTITIES";
                String url = "jdbc:relique:csv:" + folder + "?" +
                        "separator=," + "&" + "fileExtension=.csv";
                csv = DriverManager.getConnection(url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return csv;
    }

    //DO NOT CHANGE
    static Connection sqlite = null;

    /**
     * ON PAIN of Zylon, do not modify this method
     * 
     * @return a connection to the sqlite database created from your DDL sql script
     * @throws SQLException it could all go horribly wrong
     */
    public  Connection sqliteConn() throws SQLException {
        if (sqlite == null || sqlite.isClosed()) {
            try {
                Class.forName("org.sqlite.JDBC");
                sqlite = DriverManager.getConnection("jdbc:sqlite:LSHs.db");
                //System.out.println("Opened database successfully");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sqlite;
    }

    

    /**
     * Reads the contents of the solution.sql and executes the SQL commands
     * @throws IOException
     * @throws SQLException
     * @throws Exception it could all go horribly wrong
     */
    public void setup(String fileName) throws SQLException, IOException  {
        String code=readFile(fileName);
        exec(code);
    }



    /**
     * executes one or more sql commands. 
     * The commands are seperated by ;
     * For this assignment, we confirm that no data will contain the ; charachter
     * @param batch the string of commands seperated by ;
     * @throws Exception it could all go horribly wrong
     */
    public void exec(String batch) throws SQLException {
        String[] lines = batch.split(";");
        Connection conn = sqliteConn();
        conn.setAutoCommit(false);
        for (String sql : lines) {
            debug(sql);
            sql=sql.trim();
            try{
            conn.createStatement().execute(sql);
            } catch (Exception e){
                //don't use Log4j!!!!!
                if (debug) {e.printStackTrace();}
            }
        }
        try{
        conn.commit();//this could really throw an exception if the data violates constraints
        } catch (Exception e){
            try{
            conn.rollback();
            } catch (Exception r){
                debug(r.getLocalizedMessage());
            }
            debug(e.getLocalizedMessage());
        }
        try {
        conn.setAutoCommit(true);
        } catch (Exception f){
            if (!conn.isClosed()){
                conn.close();
            }
        }
    }

    private void debug(String txt) {
        if (debug){
            System.out.println(txt);
        }
    }


    /**
     * Utility function to read a file 
     * @param f name of the file to read
     * @return a String containing the contents of the file
     * @throws IOException it could all go horribly wrong
     */
    public static String readFile(String f) throws IOException {
        return new String(Files.readAllBytes(Paths.get(f)));
    }

    /**
     * pre-condition: You have create a DDL file called solution.sql
     * you have run a command in this directory:
     * sqlite3 LSH.db &lt; solution.sql
     * 
     * OR you have used the setup method above
     */
    public void milestoneA() {
    }

    /**
     * Runs a sql command against the sqlite database
     * @param sql command to be executed
     * @return The csv version of the results returned
     * @throws SQLException it could all go horribly wrong
     */
    public String runSqliteQuery(String sql) throws SQLException {
        return csvIt(sqliteConn().createStatement().executeQuery(sql));
    }

    /**
     * Runs a sql command against the CSV database
     * @param sql command to be executed
     * @return The csv version of the results returned
     * @throws SQLException anything could go wrong
     */
    public String runCSVQuery(String sql) throws SQLException {
        return csvIt(csvConn().createStatement().executeQuery(sql));
    }

    /**
     * Method to find the names of attributes in  a resultset
     * @param rs a result set
     * @return a list of names of attributes using in the resultset
     */
    public List<String> attributeNamesofResultSet(ResultSet rs){
        List<String> result = new ArrayList<>();
        
        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            int n=rsmd.getColumnCount();
            for (int i=1;i<=n;i++){
                result.add(rsmd.getColumnName(i));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * method to change the home world of a person S  NB if the planet does not exist
     * @param i is the r of the person entity (S)
     * @param planetName is the name of the planet that the person moves to (attribute d in entity D)
     * @throws UnknownPlanetException if planet does not exists
     * @throws UnknownPersonException if the person does not exist
     */
    public void changeHomePlanet(int i, String planetName) throws UnknownPlanetException,UnknownPersonException{
        // TODO
    }

    /**
     * find the population of a planet after the last census update
     * @param planetName
     * @return the last census population size
     */
    public long populationOf(String planetName) throws UnknownPlanetException{
        long result=-1;
        // TODO
        return result;
    }

    public static class UnknownPlanetException extends Exception {    }
    public static class UnknownPersonException extends Exception {    }
}
