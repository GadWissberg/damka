package controller;

import com.google.gson.*;
import model.Pawn;

import java.io.*;

public class JsonHandler {
    private static final String JSON_FILE_PATH = "src" + File.separator + "main" + File.separator + "game.json";

    // Encode game session to Json
    public void saveSessionData(Session s) throws IOException{
        Gson js = new Gson();
        JsonArray arr = new JsonArray();

        // create objects for later use
        JsonObject layout = new JsonObject();
        JsonObject scoresObject = new JsonObject();
        JsonObject player1Object = new JsonObject();
        JsonObject player2Object = new JsonObject();
        JsonObject extraInfo = new JsonObject();


        Player p1, p2;
        p1 = s.getPlayer1();
        p2 = s.getPlayer2();

        // Total scores
        scoresObject.addProperty("player1", 0);
        scoresObject.addProperty("player2", 0);
        layout.add("totalScore", scoresObject);

        // Player 1
        player1Object.addProperty("name", p1.getName());
        player1Object.addProperty("color", p1.toString());
        player1Object.addProperty("score", p1.getScore());
        layout.add("player1", player1Object);

        // Player 2
        player2Object.addProperty("name", p2.getName());
        player2Object.addProperty("color", p2.toString());
        player2Object.addProperty("score", p2.getScore());
        layout.add("player2", player2Object);
        arr.add(layout);

        // board status
        int size = Board.CELLS_IN_ROW; // board size

        int i, j;
        Board board = s.getBoard();
        Pawn pawn;

        String pawns[][] = new String[size][size];

        String color;
        for (i = 0; i < size; i++) {
            for (j = 0; j < size; j++) {
                pawn = board.getPawnAtPosition(i, j);
                if (pawn == null)
                    color = "";
                else
                    color = pawn.toString();

                pawns[i][j] = color;
            }
        }

        // Extra info
        extraInfo.addProperty("size", size);
        extraInfo.addProperty("pawns", js.toJson(pawns));
        extraInfo.addProperty("turn", s.getCurrentTurn().toString());
        layout.add("board", extraInfo);
        arr.add(layout);

        save2File(arr);
    }

    private void save2File(JsonArray json) throws IOException {
        // output json to file
        try (Writer writer = new FileWriter(JSON_FILE_PATH)) {
            Gson gson = new GsonBuilder().create();
            gson.toJson(json, writer);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }
    }

    public int getScoreOne() throws FileNotFoundException {
        JsonObject totalScoresObject = getTotalScoresJson();
        return totalScoresObject.get("player1").getAsInt();
    }

    public int getScoreTwo() throws FileNotFoundException {
        JsonObject totalScoresObject = getTotalScoresJson();
        return totalScoresObject.get("player2").getAsInt();
    }

    private JsonObject getTotalScoresJson() throws FileNotFoundException {
        Gson gson = new Gson();
        BufferedReader br = new BufferedReader(new FileReader(JSON_FILE_PATH));
        JsonArray fullString = gson.fromJson(br, JsonArray.class);
        JsonObject fullObject = fullString.get(0).getAsJsonObject();
        return fullObject.get("totalScore").getAsJsonObject();
    }
}