package edu.upc.gessi.rptool.rest.utilities;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Deserializer {

	private Deserializer() {
		//utility class
	}

    private static ObjectMapper mapper;

    private static void initMapper() {
	mapper = new ObjectMapper();
	mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
    }

    public static <T> T deserialize(String json, Class<T> unmarshall)
	    throws IOException {
	if (mapper == null) {
	    initMapper();
	}
	return mapper.readValue(json, unmarshall);
    }

    public static <T> T convertObject(Object obj, Class<T> unmarshall) {
	if (mapper == null) {
	    initMapper();
	}
	return mapper.convertValue(obj, unmarshall);
    }

}
