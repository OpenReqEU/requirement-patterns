package edu.upc.gessi.rptool.rest.utilities;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtilities {

	private JsonUtilities() {
		//utility class
	}

    /**
     * This method is used to know if a String in JSON format constains a given
     * field
     * 
     * @param json
     *            String in JSON format
     * @param field
     *            Field to search
     * @return True if the json contains that field, otherwise returns False
     * @throws JsonProcessingException
     * @throws IOException
     */
    public static boolean jsonHasField(String json, String field) throws JsonProcessingException, IOException {
	ObjectMapper jsonMapper = new ObjectMapper();
	JsonNode node;
	node = jsonMapper.readTree(json);
	return node.has(field);

    }
}
