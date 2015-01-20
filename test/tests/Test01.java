package tests;

import passwordmanager.PasswordManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author lalbino
 */
public class Test01 {

	private static ObjectMapper _OBJECT_MAPPER;
	private static final Logger _LOGGER = Logger.getLogger(Test01.class.getName());

	public Test01() {
	}

	@BeforeClass
	public static void setUpClass() {
	}

	@AfterClass
	public static void tearDownClass() {
	}

	@Before
	public void setUp() {
		_OBJECT_MAPPER = new ObjectMapper();
	}

	@After
	public void tearDown() {
	}

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

	String passwordManagerJson02 = "{\n"
			+ "  \"validation\": {\n"
			+ "    \"min\": 8,\n"
			+ "    \"max\": 12\n"
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
			+ "      \"validation\": {\n"
			+ "        \"min\": 1\n"
			+ "      },\n"
			+ "      \"chars\": \"_-.:,;=)(/&%$#\\\"!{[]}?'<>*).\"\n"
			+ "    }\n"
			+ "  ]\n"
			+ "}";

	String passwordManagerJson03 = "{\n"
			+ "  \"validation\": {\n"
			+ "    \"min\": 8,\n"
			+ "    \"max\": 12\n"
			+ "  },\n"
			+ "  \"generation\": {\n"
			+ "    \"min\": 8,\n"
			+ "    \"max\": 8\n"
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
			+ "    }\n"
			+ "  ]\n"
			+ "}";

	@Test
	public void t01() throws IOException {
		boolean result;
		JsonNode passwordConfigJson;

		PasswordManager passwordManager = new PasswordManager();
		passwordConfigJson = _OBJECT_MAPPER.readTree(passwordManagerJson01);
		passwordManager.initialize(passwordConfigJson);

		result = passwordManager.isValid("");
		assert (result == false);
		result = passwordManager.isValid("abcdefghijK0");
		assert (result == true);
		result = passwordManager.isValid("abcdefghijk0");
		assert (result == false);
		result = passwordManager.isValid("abcdefghijkK");
		assert (result == false);
		result = passwordManager.isValid("abcdefghikK=");
		assert (result == false);
		result = passwordManager.isValid("ahi0kK=");
		assert (result == false);
		result = passwordManager.isValid("aehfehwefjhwefjhwefjhwefjhqwoihi0kK=");
		assert (result == false);
	}

	@Test
	public void t02() throws IOException {
		JsonNode passwordConfigJson;

		PasswordManager passwordManager01 = new PasswordManager();
		passwordConfigJson = _OBJECT_MAPPER.readTree(passwordManagerJson01);
		passwordManager01.initialize(passwordConfigJson);

		PasswordManager passwordManager02 = new PasswordManager();
		passwordConfigJson = _OBJECT_MAPPER.readTree(passwordManagerJson02);
		passwordManager02.initialize(passwordConfigJson);

		PasswordManager passwordManager03 = new PasswordManager();
		passwordConfigJson = _OBJECT_MAPPER.readTree(passwordManagerJson03);
		passwordManager03.initialize(passwordConfigJson);

		String newPassword;

		newPassword = passwordManager01.generate();
		_LOGGER.info(newPassword);
		assert (passwordManager01.isValid(newPassword) == true);

		newPassword = passwordManager02.generate();
		_LOGGER.info(newPassword);
		assert (passwordManager02.isValid(newPassword) == true);

		newPassword = passwordManager03.generate();
		_LOGGER.info(newPassword);
		assert (passwordManager03.isValid(newPassword) == true);

		newPassword = passwordManager01.generate();
		_LOGGER.info(newPassword);
		assert (passwordManager01.isValid(newPassword) == true);

		newPassword = passwordManager02.generate();
		_LOGGER.info(newPassword);
		assert (passwordManager02.isValid(newPassword) == true);

		newPassword = passwordManager03.generate();
		_LOGGER.info(newPassword);
		assert (passwordManager03.isValid(newPassword) == true);

		newPassword = passwordManager01.generate();
		_LOGGER.info(newPassword);
		assert (passwordManager01.isValid(newPassword) == true);

		newPassword = passwordManager02.generate();
		_LOGGER.info(newPassword);
		assert (passwordManager02.isValid(newPassword) == true);

		newPassword = passwordManager03.generate();
		_LOGGER.info(newPassword);
		assert (passwordManager03.isValid(newPassword) == true);
	}
}
