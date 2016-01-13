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
 *
 * @author Vijay
 */
public class DataLoader {

    private final Gson gson;
    private final File outputFile;

    public DataLoader() {
        this.gson = new Gson();
        this.outputFile = new File("C:\\Users\\Vijay\\Desktop\\test.txt");
    }

    public void updateContent() {
        new Thread(() -> {
            String query = "?table_name=" + "ALL" + "&linker=" + "app_id" + "&link=" + 1;

            try {
                //get connection to server
                URL server = new URL("http://localhost/Numerical-Solver-Admin/DataToJSON.php" + query);
                URLConnection serveConnection = server.openConnection();

                //initialize the writer, if output is to be written.
                FileWriter writer = new FileWriter(outputFile);

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
        }).start();
    }

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

    private static String getJsonString(String table_name, String linker, Object link) {
        String response = "";

        try {
            String query = "?table_name=" + table_name + "&linker=" + linker + "&link=" + link;

            //TODO: replace this mechanism to fetch from a JSON file.
            URL server = new URL("http://localhost/Numerical-Solver-Admin/DataToJSON.php" + query);
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
}
