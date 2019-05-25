package logic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.lang.reflect.Array;

public class Json {
    //private json;

    // convert sring to json
    public Gson Encode(Session s) {
        Gson j = new Gson();
        String jsonOut;



        JsonArray arr = new JsonArray();

        JsonObject obj = new JsonObject();

        Player p;

        // Player 1
        p = s.getPlayer1();
        obj.addProperty("name", p.getName());
        obj.addProperty("color", p.getColor().toString());
        obj.addProperty("score", p.getScore());

        arr.add(obj);

        // Player 2
        p = s.getPlayer2();
        obj = new JsonObject();
        obj.addProperty("name", p.getName());
        obj.addProperty("color", p.getColor().toString());
        obj.addProperty("score", p.getScore());

        arr.add(obj);


        System.out.println(j.toJson((arr)));

        return j;
    }

    // convert json to array
    /*public function Decode(String json) {

    }*/
}