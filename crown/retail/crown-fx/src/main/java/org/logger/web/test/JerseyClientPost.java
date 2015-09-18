package org.logger.web.test;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.techlords.crown.business.model.CrownUserBO;
import com.techlords.crown.business.model.WarehouseBO;

public class JerseyClientPost {

	private static final ObjectMapper MAPPER = new ObjectMapper();

	public static <T extends Object> T unmarshal(String json, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {
		return MAPPER.readValue(json, clazz);
	}

	public static <T extends Object> List<T> unmarshalCollection(String json, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {
		return MAPPER.readValue(json,
				TypeFactory.defaultInstance().constructCollectionType(List.class, clazz));
	}

	public static String marshal(Object object) throws JsonParseException, JsonMappingException,
			IOException {
		return MAPPER.writeValueAsString(object);
	}

	public static void main(String[] args) {

		try {

			Client client = Client.create();

			WebResource webResource = client
					.resource("http://localhost:8080/crown-retail/rest/loginservice/items");

			// String json =
			// "{  \"properties\": {    \"Location\": \"Mumbai\",    \"Phone\": \"Android\",    \"Provider\": \"BSNL\"  },  \"message\": \"Sample Error Message\",  \"level\": \"ERROR\",  \"authCode\": \"BCKAUTH\"}";
			String json = "{  \"username\": \"sysadmin\",    \"password\": \"crownadmin\",    \"currentShop\": 2}";
			MultivaluedMap formData = new MultivaluedMapImpl();
			formData.add("username", "sysadmin");
			formData.add("password", "crownadmin");
			formData.add("currentShop", "2");
			// ClientResponse response = webResource.post(ClientResponse.class, formData);
			ClientResponse response = webResource.get(ClientResponse.class);

			// if (response.getStatus() != 201) {
			// throw new RuntimeException("Failed : HTTP error code : " + response.getStatus());
			// }

			System.out.println("Output from Server .... \n");
			List<?> output = unmarshal(response.getEntity(String.class), List.class);
			// System.out.println(unmarshal(response.getEntity(String.class), CrownUserBO.class));
			System.out.println(output);
			System.out.println("**********************");

		} catch (Exception e) {

			e.printStackTrace();

		}

	}
}