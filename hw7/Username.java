import java.util.Random;

public class Username {

    // Potentially useless note: (int) '0' == 48, (int) 'a' == 97

    // Instance Variables (remember, they should be private!)
    private String username = "";

    public Username() {
		Random rand = new Random();
		boolean result = rand.nextBoolean();
		if (result) {
			String randomUserName = "";
			Integer[] order = new Integer[3];
			order[0] = rand.nextInt(4);
			order[1] = rand.nextInt(4);
			order[2] = rand.nextInt(4);
			
			for (int o : order) {
				if (o == 1) {
					randomUserName += Integer.toString(randomInt());
				} else if (o == 2) {
					randomUserName += (char) randomLowerAZ();
				} else {
					randomUserName += (char) randomUpperAZ();
				}
			}
			username = randomUserName;			
		} else {
			String randomUserName = "";
			Integer[] order = new Integer[2];
			order[0] = rand.nextInt(4);
			order[1] = rand.nextInt(4);
			
			for (int o : order) {
				if (o == 1) {
					randomUserName += Integer.toString(randomInt());
				} else if (o == 2) {
					randomUserName += (char) randomLowerAZ();
				} else {
					randomUserName += (char) randomUpperAZ();
				}
			}
			username = randomUserName;	
		}
    }
	
	private int randomInt() {
		Random rand = new Random();
		return rand.nextInt(10);
	}
	
	private int randomLowerAZ() {
		Random rand = new Random();
		return rand.nextInt((122-97) + 1) + 97;
	}
	
	private int randomUpperAZ() {
		Random rand = new Random();
		return rand.nextInt((90-65) + 1) + 65;
	}

    public Username(String reqName) {
		if (reqName == null) {
			throw new NullPointerException("Requested username is null");
		} else if (reqName.length() < 2 || reqName.length() > 3) {
        	throw new IllegalArgumentException("Username must be 2-3 alphanumeric characters");
        } else {
        	int length = reqName.length();
			for (int i = 0; i < length; i = i + 1) {
				if (!Character.isLetterOrDigit(reqName.codePointAt(i))) {
					throw new IllegalArgumentException("Username contains invalid characters");
				}
			}
			username = reqName;
		}		
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Username) {
        	Username otherUser = (Username) o;
			return username.equalsIgnoreCase(otherUser.username);
		}
		return false;	
    }

    @Override
    public int hashCode() { 
        String code = "";
		int length = username.length();
		for (int i = 0; i < length; i ++) {
			char c = username.charAt(i);
			if (Character.isDigit(c)) {
				code += c;
			} else if (Character.isLetter(c)) {
				code += Integer.toString(Character.getNumericValue(c));
			}
		}
		return Integer.parseInt(code);
    }

    public static void main(String[] args) {
		Username u1 = new Username();
		System.out.println(u1.username);
		System.out.println(u1.hashCode());
		Username u2 = new Username("bcc");
		Username u3 = new Username("ba");
		System.out.println(u2.equals(u3));
		System.out.println(u2.hashCode());
		System.out.println(u3.hashCode());
		Username u4 = new Username("BCC");
		System.out.println(u2.equals(u4));
		System.out.println(u2.username);
		System.out.println(u4.hashCode());
		System.out.println(u2.hashCode());
		
    }
}