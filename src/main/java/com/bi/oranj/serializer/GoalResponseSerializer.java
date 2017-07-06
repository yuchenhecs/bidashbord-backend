package com.bi.oranj.serializer;

import com.bi.oranj.model.bi.Goal;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.Collections;

public class GoalResponseSerializer extends StdSerializer<Goal>{

    public GoalResponseSerializer(){
        this(null);
    }

    public GoalResponseSerializer(Class<Goal> t){
        super(t);
    }

    @Override
    public void serialize(Goal value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        String user = value.getType() + "s";
        if (!value.getUsers().isEmpty()) {
            user = value.getUsers().iterator().next().getClass().getSimpleName() + "s";
        }
        jgen.writeStartObject();
        jgen.writeNumberField("totalGoals", value.getTotalGoals());
        jgen.writeNumberField("total" + user, value.getTotalUsers());
        jgen.writeBooleanField("last", value.isLast());
        jgen.writeNumberField("page", value.getPage());
        jgen.writeNumberField("count", value.getCount());
        jgen.writeObjectField(user, value.getUsers());
        jgen.writeEndObject();

    }
}
