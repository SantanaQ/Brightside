package org.saveload;

import com.google.gson.*;
import java.lang.reflect.Type;

public class PolymorphicDeSerializer<T> implements JsonSerializer<T>, JsonDeserializer<T>
{
    private static final String CLASS = "CLASS";
    private static final String DATA = "DATA";

    @Override
    public JsonElement serialize(T object, Type type, JsonSerializationContext context)
    {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(CLASS, object.getClass().getName());
        jsonObject.add(DATA, context.serialize(object));
        return jsonObject;
    }

    @Override
    public T deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context)
    {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonPrimitive primitive = (JsonPrimitive) jsonObject.get(CLASS);
        String className = primitive.getAsString();

        try
        {
            Class encodedClass = Class.forName(className);
            return context.deserialize(jsonObject.get(DATA), encodedClass);
        }
        catch(ClassNotFoundException e)
        { throw new JsonParseException(e); }
    }
}
