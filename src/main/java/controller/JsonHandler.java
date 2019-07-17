package controller;

import com.google.gson.*;
import model.Pawn;

import java.awt.*;
import java.io.*;

public class JsonHandler {
    private static final String JSON_FILE_PATH = "src" + File.separator + "main" + File.separator + "game.json";
    private Gson gson = new Gson();

    // Encode game session to Json
    public void saveSessionData(Session s) throws IOException {
        Gson js = new Gson();
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
        player1Object.add("pawns", new JsonArray());
        layout.add("player1", player1Object);

        // Player 2
        player2Object.addProperty("name", p2.getName());
        player2Object.addProperty("color", p2.toString());
        player2Object.addProperty("score", p2.getScore());
        player2Object.add("pawns", new JsonArray());
        layout.add("player2", player2Object);

        // board status
        int size = Board.CELLS_IN_ROW; // board size

        int i, j;
        Board board = s.getBoard();
        Pawn pawn;

        String pawns[][] = new String[size][size];

        for (i = 0; i < size; i++) {
            for (j = 0; j < size; j++) {
                pawn = board.getPawnAtPosition(i, j);
                if (pawn != null) {
                    JsonObject pawnJson = new JsonObject();
                    pawnJson.addProperty("row", pawn.getPosition().getRow());
                    pawnJson.addProperty("column", pawn.getPosition().getCol());
                    pawnJson.addProperty("queen", pawn.isQueen());
                    if (pawn.getPlayer().getColor().equals(Color.RED)) {
                        player1Object.get("pawns").getAsJsonArray().add(pawnJson);
                    } else {
                        player2Object.get("pawns").getAsJsonArray().add(pawnJson);
                    }
                }
            }
        }

        // Extra info
        extraInfo.addProperty("size", size);
        extraInfo.addProperty("turn", s.getCurrentTurn().toString());
        layout.add("board", extraInfo);

        save2File(layout);
    }

    private void save2File(JsonObject json) throws IOException {
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
        gson = new Gson();
        BufferedReader br = new BufferedReader(new FileReader(JSON_FILE_PATH));
        JsonObject fullString = gson.fromJson(br, JsonObject.class);
        JsonObject fullObject = fullString.getAsJsonObject();
        return fullObject.get("totalScore").getAsJsonObject();
    }

    public void loadSessionData(File fileOpened, Board board, Player player1, Player player2) throws FileNotFoundException {
        JsonObject jsonObject = gson.fromJson(new FileReader(fileOpened), JsonObject.class);
        board.setBoard(inflateBoard(jsonObject, player1, player2));
    }

    private Pawn[][] inflateBoard(JsonObject fromJson, Player player1, Player player2) {
        Pawn[][] result = new Pawn[Board.CELLS_IN_ROW][Board.CELLS_IN_ROW];
        JsonArray player1pawns = fromJson.getAsJsonObject("player1").get("pawns").getAsJsonArray();
        JsonArray player2pawns = fromJson.getAsJsonObject("player2").get("pawns").getAsJsonArray();
        inflatePlayerPawns(player1, result, player1pawns);
        inflatePlayerPawns(player2, result, player2pawns);
        return result;
    }

    private void inflatePlayerPawns(Player player1, Pawn[][] result, JsonArray player1pawns) {
        for (JsonElement pawn : player1pawns) {
            JsonObject pawnJsonObject = (JsonObject) pawn;
            int row = pawnJsonObject.get("row").getAsInt();
            int column = pawnJsonObject.get("column").getAsInt();
            Pawn newPawn = new Pawn(player1, row, column);
            result[row][column] = newPawn;
            newPawn.setQueen(pawnJsonObject.get("queen").getAsBoolean());
        }
    }
}