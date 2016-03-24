import java.util.Map;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;

public class UsernameBank {

    // Instance variables (remember, they should be private!)
    private Map<String, String> nameToEmail;
	private Map<String, String> emailToName;
	private Map<String, Integer> badNames;
	private Map<String, Integer> badEmails;

    public UsernameBank() {
		nameToEmail = new HashMap<String, String>();
		emailToName = new HashMap<String, String>();
		badNames = new HashMap<String, Integer>();
		badEmails = new HashMap<String, Integer>();
    }
	
	private boolean invalidUsername(String username) {
		if (username.length() < 2 || username.length() > 3) {
			return true;
		} else {
        	int length = username.length();
			for (int i = 0; i < length; i = i + 1) {
				if (!Character.isLetterOrDigit(username.codePointAt(i))) {
					return true;
				}
			}
			return false;
		}
	}

    public void generateUsername(String username, String email) {
		if (username == null || email == null) {
			throw new NullPointerException("Username, email, or both null");
		} else if (!invalidUsername(username)) {
			if (!nameToEmail.containsKey(username)) {
				nameToEmail.put(username, email);
				emailToName.put(email, username);
			} else {
				throw new IllegalArgumentException("Username already exists");
			}
		} else {
			if (username.length() < 2 || username.length() > 3) {
				throw new IllegalArgumentException("Username must be 2-3 alphanumeric characters");
			} else {
				int length = username.length();
				for (int i = 0; i < length; i = i + 1) {
					if (!Character.isLetterOrDigit(username.codePointAt(i))) {
						throw new IllegalArgumentException("Username contains invalid characters");
					}
				}
			}
		}
   }

    public String getEmail(String username) {
        if (username == null) {
        	throw new NullPointerException("Username is null");
        } else if (invalidUsername(username)) {
			if (badNames.containsValue(username)) {
				int count = badNames.get(username);
				count = count + 1;
				badNames.put(username, count);
			} else {
				badNames.put(username, 1);
			}
			return null;
        } else {
        	if (!nameToEmail.containsKey(username)) {
				if (badNames.containsValue(username)) {
					int count = badNames.get(username);
					count = count + 1;
					badNames.put(username, count);
					return null;
				} else {
					badNames.put(username, 1);
					return null;
				}
        	} else {
        		return nameToEmail.get(username);
        	}
        }
    }

    public String getUsername(String userEmail)  {
		if (userEmail == null) {
			throw new NullPointerException("Email is null");
		} else if (!emailToName.containsValue(userEmail)) {
			if (badEmails.containsKey(userEmail)) {
				int count = badEmails.get(userEmail);
				count = count + 1;
				badEmails.put(userEmail, count);
				return null;
			} else {
				badEmails.put(userEmail, 1);
				return null;
			} 
		} else {
			return emailToName.get(userEmail);
		}
    }

    public Map<String, Integer> getBadEmails() {
        return badEmails;
    }

    public Map<String, Integer> getBadUsernames() {
        return badNames;
    }

    public String suggestUsername() {
        //HashSet<String> allUserNames = nameToEmail.keySet();
        return null;
    }

    // The answer is somewhere in between 3 and 1000.
    public static final int followUp() {
        // YOUR CODE HERE
        return 0;
    }

    // Optional, suggested method. Use or delete as you prefer.
    private void recordBadUsername(String username) {
        // YOUR CODE HERE
    }

    // Optional, suggested method. Use or delete as you prefer.
    private void recordBadEmail(String email) {
        // YOUR CODE HERE
    }
}