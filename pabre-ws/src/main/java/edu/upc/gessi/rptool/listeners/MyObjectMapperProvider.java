package edu.upc.gessi.rptool.listeners;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

@Provider
public class MyObjectMapperProvider implements ContextResolver<ObjectMapper> {

    // Solution from:
    // https://stackoverflow.com/questions/18872931/custom-objectmapper-with-jersey-2-2-and-jackson-2-1

    final ObjectMapper defaultObjectMapper;

    public MyObjectMapperProvider() {
	defaultObjectMapper = createDefaultMapper();
    }

    private ObjectMapper createDefaultMapper() {
	final ObjectMapper jackson = new ObjectMapper();
	jackson.enable(SerializationFeature.INDENT_OUTPUT);
	jackson.enable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
	return jackson;
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
	return defaultObjectMapper;
    }

}
