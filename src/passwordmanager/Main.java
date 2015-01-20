package passwordmanager;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.logging.Logger;

public class Main {

	private static ObjectMapper _OBJECT_MAPPER = new ObjectMapper();
	private static final Logger _LOGGER = Logger.getLogger(Main.class.getName());

	public static void main(String[] args) throws IOException {
		String passwordManagerJson01 = "{\n"
				+ "  \"validation\": {\n"
				+ "    \"min\": 8,\n"
				+ "    \"max\": 20\n"
				+ "  },\n"
				+ "  \"generation\": {\n"
				+ "    \"min\": 10,\n"
				+ "    \"max\": 10\n"
				+ "  },\n"
				+ "  \"groups\": [\n"
				+ "    {\n"
				+ "      \"validation\": {\n"
				+ "        \"min\": 1\n"
				+ "      },\n"
				+ "      \"chars\": \"0123456789\"\n"
				+ "    },\n"
				+ "    {\n"
				+ "      \"validation\": {\n"
				+ "        \"min\": 1\n"
				+ "      },\n"
				+ "      \"chars\": \"abcdefghijklmnopqrstuvwxyz\"\n"
				+ "    },\n"
				+ "    {\n"
				+ "      \"validation\": {\n"
				+ "        \"min\": 1\n"
				+ "      },\n"
				+ "      \"chars\": \"ABCDEFGHIJKLMNOPQRSTUVWXYZ\"\n"
				+ "    },\n"
				+ "    {\n"
				+ "      \"chars\": \"_-.:,;=)(/&%$#\\\"!{[]}?'<>*).\"\n"
				+ "    }\n"
				+ "  ]\n"
				+ "}";

		PasswordManager passwordManager01 = new PasswordManager();
		JsonNode passwordConfigJson = _OBJECT_MAPPER.readTree(passwordManagerJson01);
		passwordManager01.initialize(passwordConfigJson);

		String newPassword;
		newPassword = "abcdefG0";
		if (passwordManager01.isValid(newPassword)) {
			_LOGGER.info("password is valid");
		}
		newPassword = passwordManager01.generate();
		_LOGGER.info(newPassword);
	}
}
