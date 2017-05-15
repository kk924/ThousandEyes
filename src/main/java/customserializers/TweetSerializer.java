package customserializers;

import challenge.Tweet;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class TweetSerializer extends JsonSerializer<Tweet> {

    @Override
    public void serialize(Tweet value, JsonGenerator gen, SerializerProvider serializers) throws IOException, JsonProcessingException {
        gen.writeStartObject();
        gen.writeNumberField("id", value.getId());
        gen.writeNumberField("person_id", value.getPerson_id());
        gen.writeStringField("content", value.getContent());
        gen.writeEndObject();
    }
}
