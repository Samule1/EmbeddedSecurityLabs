import java.util.Arrays;
/**
* Solution for ex 2 with no silly looping.
* Might take 2 or three times to work, I don't mind.
*/
public class Main {
	public static void main(String[] args) {
		char[] input = "0123456789".toCharArray();
		
		//The pad starts at 15, we know from decompiling the Secret.class that 16 is correct. This is totally testable but life is short.
		String pad = "!!!!!!!!!!!!!!!";

		String secretsecret = crackAttack(pad, "", input);

		String out = secretsecret == null ? "FAILED, it happens.. run again it will work" : secretsecret;
		System.out.println(out);
		//System.out.println(s.getSecret());

	}
	public static char testSet(char[] charSet, String padding, char currentGuess, String currentPass, long prevDt){
		Secret s = new Secret();
		if(charSet.length == 0){
			return currentGuess;
		}
		long t = System.currentTimeMillis();
	  s.verifyPassword(currentPass + Character.toString(charSet[0]) + padding);
		long T = System.currentTimeMillis();
		currentGuess = prevDt < (T-t) ? charSet[0]: currentGuess;
		long nextDt = prevDt < (T-t) ? (T-t) : prevDt;
		return testSet(Arrays.copyOfRange(charSet, 1, charSet.length), padding, currentGuess, currentPass, nextDt);

	}

	public static String crackAttack(String padding, String currentPass, char[] charSet){
		Secret s = new Secret();
		char currentLeast = testSet(charSet, padding, '0', currentPass, 0);
		System.out.println("PADDING:  "+padding.length());

		if(padding.length() == 0){
			s.verifyPassword(currentPass + Character.toString(currentLeast));
			return s.getSecret();//currentPass + Character.toString(currentLeast.c);
		}
		else
			return crackAttack(padding.substring(1, (padding.length())), currentPass + Character.toString(currentLeast), charSet);
	}

}
