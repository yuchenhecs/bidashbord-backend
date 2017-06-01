package com.bi.oranj.serializer;

import com.bi.oranj.wrapper.User;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * Created by jaloliddinbakirov on 5/31/17.
 */
public class UserSerializer extends StdSerializer<User>{

    public UserSerializer(){
        this(null);
    }

    public UserSerializer(Class<User> t){
        super(t);
    }

    @Override
    public void serialize (User value, JsonGenerator jgen, SerializerProvider provider) throws IOException, JsonProcessingException{
        String user = "";
        if (value != null){
            user = value.getClass().getSimpleName().toLowerCase();
        }

        jgen.writeStartObject();
        jgen.writeNumberField(user + "Id", value.getUserId()); //or firmName
        if (value.getName() != null)
            jgen.writeStringField("name", value.getName());
        if (value.getFirstName() != null)
            jgen.writeStringField("firstName", value.getFirstName());
        if (value.getLastName() != null)
            jgen.writeStringField("lastName", value.getLastName());
        jgen.writeNumberField("total", value.getTotal());
        jgen.writeObjectField("goals", value.getGoals());
        jgen.writeEndObject();
    }
}
