package logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import javax.xml.ws.Response;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;
import java.lang.reflect.Array;

public class Json {
    private static final String JSON_FILE = "src"+File.separator+"main"+File.separator+"game.json";

    // Encode game session to Json
    public Gson Encode(Session s) {
        Gson j = new Gson();

        JsonArray arr = new JsonArray();

        // create objects for later use
        JsonObject o  = new JsonObject();
        JsonObject o2 = new JsonObject();

        Player p;

        // Player 1
        p = s.getPlayer1();
        o.addProperty("name", p.getName());
        o.addProperty("color", p.getColor().toString());
        o.addProperty("score", p.getScore());
        o2.add("player1", o);
        arr.add(o2);

        // Player 2
        p = s.getPlayer2();
        o = new JsonObject();
        o2 = new JsonObject();
        o.addProperty("name", p.getName());
        o.addProperty("color", p.getColor().toString());
        o.addProperty("score", p.getScore());
        o2.add("player2", o);
        arr.add(o2);

        // current turn
        o = new JsonObject();
        o.addProperty("turn", s.getCurrentTurn().getName());
        arr.add(o);

        // board status
        o = new JsonObject();
        JsonArray board = new JsonArray();
        board.add("soon"); // TODO: write board status function, to deliver: board size and each player positions
        o.add("board", board);
        arr.add(o);

        System.out.println(j.toJson((arr)));

        return j;
    }

    private void save2File(Gson json) {
        // output json to file
        try (Writer writer = new FileWriter(JSON_FILE)) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(json, writer);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // convert json (from file) to array
    public void Decode() {
        try {
            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new FileReader(JSON_FILE));
            //Response data = gson.fromJson(reader);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}