package soac.softarch.nsc.solver;

import soac.softarch.nsc.models.Unit;
import soac.softarch.nsc.models.Parameter;
import soac.softarch.nsc.models.Numerical;
import soac.softarch.nsc.models.Topic;
import soac.softarch.nsc.models.Formula;
import soac.softarch.nsc.models.Initiable;
import soac.softarch.nsc.models.Admin;
import soac.softarch.nsc.models.App;
import soac.softarch.nsc.models.Chapter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Instance of this class provide methods that can download latest content from
 * the server to a desired location, and then fetch data from it locally. All
 * the content downloaded is stored in form of a simple JSON file.<br>
 * There are two settings that need to be configured before using DataLoader,
 * they are 'servicePath', and 'contentFilePath'. Both of them can be set by
 * calling their respective setter methods. {@code servicePath} is the path to
 * the script that will serve the latest content, and {@code contentFilePath} is
 * the path to a local file in which the JSON formatted content will be written.
 *
 * @author Vijay
 */
public class DataLoader {

    private static DataLoader instance;

    private final Gson gson;

    private String servicePath;
    private String contentFilePath;

    private DataLoader(String servicePath, String contentFilePath) {
        this.gson = new Gson();
        this.servicePath = servicePath;
        this.contentFilePath = contentFilePath;
    }

    /**
     * Gets the only instance of this class. Note that DataLoader must have been
     * initialized before calling this method.<br>
     * DataLoader can be initialized by its {@code initDataLoader()} method.
     *
     * @return Returns the instance of this singleton class.
     */
    public static final DataLoader getInstance() {
        if (instance == null) {
            System.err.println("DataLoader has not yet been initialized.");
        }

        return instance;
    }

    /**
     * Initializes the DataLoader class instance.
     *
     * @param servicePath The path to the online service that will serve the
     * latest content for this App.
     * @param contentFilePath
     */
    public static void initDataLoader(String servicePath, String contentFilePath) {
        instance = new DataLoader(servicePath, contentFilePath);
    }

    /**
     * Downloads local content, by fetching the latest one from the server.
     */
    public void updateContent() {
        String query = "?table_name=" + "ALL" + "&linker=" + "app_id" + "&link=" + 1;

        try {
            //get connection to server
            URL server = new URL(servicePath + query);
            URLConnection serveConnection = server.openConnection();

            //initialize the writer, if output is to be written.
            File contentFile = new File(this.contentFilePath);
            FileWriter writer = new FileWriter(contentFile);

            //Append to writer
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(serveConnection.getInputStream()))) {
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    writer.append(inputLine);
                }
                writer.flush();
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(DataLoader.class
                    .getName()).log(Level.SEVERE, null, ex);

        } catch (IOException ex) {
            Logger.getLogger(DataLoader.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Gets a single object of specified class and a unique id.
     *
     * @param <T> The type of object you wish to retrieve. <b>T</b> can be one
     * of the model class from the package "soac.softarch.nsc.models".
     * @param classofT The class of T i.e. the object you wish to retrieve.
     * @param id The unique id of the object to retrieve
     * @return Returns an object from the server of specified type and unique id
     */
    public <T> T getT(Class<T> classofT, int id) {
        String jsonString;

        if (classofT.equals(Admin.class)) {
            jsonString = getJsonString("admins", "id", id);

        } else if (classofT.equals(App.class)) {
            jsonString = getJsonString("apps", "id", id);

        } else if (classofT.equals(Chapter.class)) {
            jsonString = getJsonString("chapters", "id", id);

        } else if (classofT.equals(Formula.class)) {
            jsonString = getJsonString("formulas", "id", id);

        } else if (classofT.equals(Numerical.class)) {
            jsonString = getJsonString("numericals", "id", id);

        } else if (classofT.equals(Parameter.class)) {
            jsonString = getJsonString("parameters", "id", id);

        } else if (classofT.equals(Topic.class)) {
            jsonString = getJsonString("topics", "id", id);

        } else if (classofT.equals(Unit.class)) {
            jsonString = getJsonString("units", "id", id);
        } else {
            throw new IllegalArgumentException("Cannot get list of class " + classofT);
        }

        JsonElement jsonElement = new JsonParser().parse(jsonString);
        if (jsonElement.isJsonArray()) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            if (jsonArray.size() == 1) {
                T t = gson.fromJson(jsonArray.get(0), classofT);
                if (t instanceof Initiable) {
                    ((Initiable) t).init();
                }
                return t;
            } else {
                System.err.println("You have some problem in Database. Possible"
                        + "duplicate values with same id! (impossible)");
                return null;
            }
        } else {
            System.err.println("There is some problem in database service. Not serving arrays.");
            return null;
        }
    }

    /**
     * Gets a list of objects of specified class and filter.<br>
     * Filter is used to reference the foreign relation of the object you are
     * fetching. For eg. If you are retrieving list of topics of a particular
     * chapter, then {@code classofT} will be {@code Topic.class} and filter
     * should be an instance of {@code Chapter} whose topics you want to get.
     *
     * @param <T> The type of object you wish to retrieve in a list. <b>T</b>
     * can be one of the model class from the package
     * "soac.softarch.nsc.models".
     * @param classofT The class of <b>T</b> i.e. the object you wish to
     * retrieve in a list.
     * @param filter An object which has a foreign relation with the list of
     * objects you wish to retrieve.
     * @return Returns a list of objects of type <b>T</b> constrained to the
     * filter.
     */
    public <T> List<T> getListOfT(Class<T> classofT, Object filter) {
        List<T> list = new ArrayList<>();
        String jsonString;

        if (classofT.equals(Admin.class)) {
            String email = (filter).toString();
            jsonString = getJsonString("admins", "email", email);

        } else if (classofT.equals(App.class)) {
            int id = ((Admin) filter).getId();
            jsonString = getJsonString("apps", "admin", id);

        } else if (classofT.equals(Chapter.class)) {
            int app_id = ((App) filter).getId();
            jsonString = getJsonString("chapters", "app_id", app_id);

        } else if (classofT.equals(Formula.class)) {
            int numerical_id = ((Numerical) filter).getId();
            jsonString = getJsonString("formulas", "numerical_id", numerical_id);

        } else if (classofT.equals(Numerical.class)) {
            int topic_id = ((Topic) filter).getId();
            jsonString = getJsonString("numericals", "topic_id", topic_id);

        } else if (classofT.equals(Parameter.class)) {
            int chapter_id = ((Chapter) filter).getId();
            jsonString = getJsonString("parameters", "chapter_id", chapter_id);

        } else if (classofT.equals(Topic.class)) {
            int chapter_id = ((Chapter) filter).getId();
            jsonString = getJsonString("topics", "chapter_id", chapter_id);

        } else if (classofT.equals(Unit.class)) {
            int chapter_id = ((Chapter) filter).getId();
            jsonString = getJsonString("units", "chapter_id", chapter_id);
        } else {
            throw new IllegalArgumentException("Cannot get list of class " + classofT);
        }

        JsonElement jsonElement = new JsonParser().parse(jsonString);

        if (jsonElement.isJsonArray()) {
            for (JsonElement jsonArrayItem : jsonElement.getAsJsonArray()) {
                T t = gson.fromJson(jsonArrayItem, classofT);
                if (t instanceof Initiable) {
                    ((Initiable) t).init();
                }
                list.add(t);
            }
        } else {
            System.err.println("There is some problem in database service. Not serving arrays.");
            return null;
        }

        return list;
    }

    private String getJsonString(String table_name, String linker, Object link) {
        String response = "";

        try {
            String query = "?table_name=" + table_name + "&linker=" + linker + "&link=" + link;

            //TODO: replace this mechanism to fetch from a JSON file.
            URL server = new URL(servicePath + query);
            URLConnection conn = server.openConnection();
            try (BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()))) {
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response += inputLine;

                }
            }
        } catch (MalformedURLException ex) {
            Logger.getLogger(DataLoader.class
                    .getName()).log(Level.SEVERE, null, ex);
            response = "A MalformedURLException occured.";

        } catch (IOException ex) {
            Logger.getLogger(DataLoader.class
                    .getName()).log(Level.SEVERE, null, ex);
            response = "An IOException occured.";
        }

        return response;
    }

    /**
     * Gets the path to the service that returns the app content.
     *
     * @return Returns the service path.
     */
    public String getServicePath() {
        return servicePath;
    }

    /**
     * Sets the path to the service that will return the app content. The
     * content returned by the service must be a well formatted JSON string.
     *
     * @param servicePath The path to service.
     */
    public void setServicePath(String servicePath) {
        this.servicePath = servicePath;
    }

    /**
     * Gets the path to the file that stores local content.
     *
     * @return Returns the path to file that has the local content.
     */
    public String getContentFilePath() {
        return contentFilePath;
    }

    /**
     * Sets the path to the file that will have the local content.
     *
     * @param contentFilePath The path to file.
     */
    public void setContentFilePath(String contentFilePath) {
        this.contentFilePath = contentFilePath;
    }

}
