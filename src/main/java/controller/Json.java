package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import model.Pawn;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Writer;

public class Json {
    private static final String JSON_FILE = "src"+File.separator+"main"+File.separator+"game.json";

    // Encode game session to Json
    public void getAllSessionData(Session s) {
        Gson js = new Gson();
        JsonArray arr = new JsonArray();

        // create objects for later use
        JsonObject o  = new JsonObject();
        JsonObject o2 = new JsonObject();

        Player p1, p2;
        p1 = s.getPlayer1();
        p2 = s.getPlayer2();

        // Player 1
        o.addProperty("name", p1.getName());
        o.addProperty("color", p1.toString());
        o.addProperty("score", p1.getScore());
        o2.add("player1", o);
        arr.add(o2);

        // Player 2
        o = new JsonObject();
        o2 = new JsonObject();
        o.addProperty("name", p2.getName());
        o.addProperty("color", p2.toString());
        o.addProperty("score", p2.getScore());
        o2.add("player2", o);
        arr.add(o2);

        // current turn
        o = new JsonObject();

        arr.add(o);

        // board status
        int size = Board.CELLS_IN_ROW; // board size

        int i, j;
        Board board = s.getBoard();
        Pawn pawn;

        String pawns[][] = new String[size][size];

        String color;
        for(i = 0; i < size; i++) {
            for (j = 0; j < size; j++) {
                pawn = board.getPawnAtPosition(i, j);
                if(pawn == null)
                    color = "";
                else
                    color = pawn.toString();

                pawns[i][j] = color;
            }
        }

        o = new JsonObject();
        o2 = new JsonObject();
        o2.addProperty("size", size);
        o2.addProperty("pawns", js.toJson(pawns));
        o2.addProperty("turn", s.getCurrentTurn().toString());
        o.add("board", o2);
        arr.add(o);

        // TODO: delete before release
        System.out.println(js.toJson((arr)));

        //return js;
        save2File(js);
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