package com.bi.oranj.serializer;

import com.bi.oranj.model.bi.GoalResponse;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * Created by jaloliddinbakirov on 5/31/17.
 */

public class GoalResponseSerializer extends StdSerializer<GoalResponse>{

    public GoalResponseSerializer(){
        this(null);
    }

    public GoalResponseSerializer(Class<GoalResponse> t){
        super(t);
    }

    @Override
    public void serialize(GoalResponse value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        String user = "";
        if (!value.getUsers().isEmpty()){
            user = value.getUsers().iterator().next().getClass().getSimpleName() + "s";
        }
        jgen.writeStartObject();
        jgen.writeNumberField("totalGoals", value.getTotalGoals());
        jgen.writeNumberField("total" + user, value.getTotalUsers());
//        jgen.writeNumberField("totalFirms", value.getTotalFirms());
//        jgen.writeNumberField("totalAdvisors", value.getTotalAdvisors());
//        jgen.writeNumberField("totalClients", value.getTotalClients());
        jgen.writeBooleanField("last", value.isLast());
        jgen.writeNumberField("page", value.getPage());
        jgen.writeNumberField("count", value.getCount());
        jgen.writeObjectField(user.toLowerCase(), value.getUsers());
        jgen.writeEndObject();

    }
}
