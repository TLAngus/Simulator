/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.entities;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

/**
 *
 * @author gillesbraun
 */
public class EntityAdapter implements JsonSerializer<Entity>, JsonDeserializer<Entity> {

    @Override
    public JsonElement serialize(Entity t, Type type, JsonSerializationContext jsc) {
        JsonObject result = new JsonObject();
        result.add("type", new JsonPrimitive(t.getClass().getSimpleName()));
        result.add("properties", jsc.serialize(t, t.getClass()));
        return result;
    }

    @Override
    public Entity deserialize(JsonElement je, Type typeOfT, JsonDeserializationContext jdc) throws JsonParseException {
        JsonObject jsonObject = je.getAsJsonObject();
        String type = jsonObject.get("type").getAsString();
        JsonElement element = jsonObject.get("properties");
        
        try {
            return jdc.deserialize(element, Class.forName("model.entities."+type));
        } catch (ClassNotFoundException ex) {
            throw new JsonParseException("Unknown element type: "+type, ex);
        }
    }
    
}
