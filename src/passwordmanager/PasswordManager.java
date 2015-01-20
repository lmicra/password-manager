package passwordmanager;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PasswordManager {

	private static class CharLimit {

		private Integer min;
		private Integer max;

		public Integer getMin() {
			return min;
		}

		public Integer getMax() {
			return max;
		}

		public void setMin(Integer min) {
			this.min = min;
		}

		public void setMax(Integer max) {
			this.max = max;
		}

	}

	private static class PasswordGroupConfig {

		private CharLimit validation = new CharLimit();
		private String chars;

		public CharLimit getValidation() {
			return validation;
		}

		public String getChars() {
			return chars;
		}

		public void setValidation(CharLimit validation) {
			this.validation = validation;
		}

		public void setChars(String chars) {
			this.chars = chars;
		}
	}

	private static class PasswordConfig {

		private CharLimit validation = new CharLimit();
		private CharLimit generation = new CharLimit();
		private PasswordGroupConfig[] groups;
		@JsonIgnore
		private String allowedChars;

		public CharLimit getValidation() {
			return validation;
		}

		public CharLimit getGeneration() {
			return generation;
		}

		public PasswordGroupConfig[] getGroups() {
			return groups;
		}

		public void setValidation(CharLimit validation) {
			this.validation = validation;
		}

		public void setGeneration(CharLimit generation) {
			this.generation = generation;
		}

		public void setGroups(PasswordGroupConfig[] groups) {
			this.groups = groups;
			StringBuilder sb = new StringBuilder();
			if (this.groups.length > 0) {
				for (PasswordGroupConfig g : this.groups) {
					if (g.getChars() != null && !g.getChars().isEmpty()) {
						sb.append(g.getChars());
					}
				}
			}
			this.allowedChars = sb.toString();
		}

		public String getAllowedChars() {
			return allowedChars;
		}
	}

	private PasswordConfig passwordConfig;
	private boolean initialized = false;

	private static final ObjectMapper _OBJECT_MAPPER = new ObjectMapper();

	public boolean initialize(final JsonNode configData) {
		try {
			this.passwordConfig = _OBJECT_MAPPER.readValue(configData.toString(), PasswordConfig.class);
			this.initialized = true;
			return true;
		} catch (IOException ex) {
			Logger.getLogger(PasswordManager.class.getName()).log(Level.SEVERE, null, ex);
			this.initialized = false;
		}
		return this.initialized;
	}

	public boolean isValid(final String password) {
		if (password == null || password.isEmpty()) {
			return false;
		}
		if (this.passwordConfig.getValidation().getMin() != null && password.length() < this.passwordConfig.getValidation().getMin()) {
			return false;
		}
		if (this.passwordConfig.getValidation().getMax() != null && password.length() > this.passwordConfig.getValidation().getMax()) {
			return false;
		}
		for (PasswordGroupConfig passwordGroupConfig : this.passwordConfig.getGroups()) {
			if (passwordGroupConfig.getValidation().getMin() != null || passwordGroupConfig.getValidation().getMax() != null) {
				int c = countMatches(password, passwordGroupConfig.getChars());
				if (passwordGroupConfig.getValidation().getMin() != null && c < passwordGroupConfig.getValidation().getMin()) {
					return false;
				}
				if (passwordGroupConfig.getValidation().getMax() != null && c > passwordGroupConfig.getValidation().getMax()) {
					return false;
				}
			}
		}
		return true;
	}

	private static char getRandomChar(final String dictionary) {
		return dictionary.charAt((int) (Math.random() * dictionary.length()));
	}

	private static int countMatches(final String str, final String dict) {
		if (str == null || str.isEmpty() || dict == null || dict.isEmpty()) {
			return 0;
		}
		int count = 0;
		int idx = 0;

		for (int i = dict.length() - 1; i >= 0; --i) {
			while ((idx = str.indexOf(dict.charAt(i), idx)) != -1) {
				count++;
				idx += 1;
			}
		}
		return count;
	}

	public static <T> T coalesce(T... args) {
		for (T s : args) {
			if (s != null) {
				return s;
			}
		}
		return null;
	}

	public String generate() {
		int min = coalesce(this.passwordConfig.getGeneration().getMin(), this.passwordConfig.getValidation().getMin(), 8);
		int max = coalesce(this.passwordConfig.getGeneration().getMax(), this.passwordConfig.getValidation().getMax(), 20);
		int passwordNumberOfChars = (int) (Math.random() * (max - min) + min);

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < passwordNumberOfChars; i++) {
			sb.append(getRandomChar(this.passwordConfig.getAllowedChars()));
		}

		while (!this.isValid(sb.toString())) {
			sb.setCharAt((int) Math.floor(Math.random() * passwordNumberOfChars), getRandomChar(this.passwordConfig.getAllowedChars()));
		}

		return sb.toString();
	}
}
