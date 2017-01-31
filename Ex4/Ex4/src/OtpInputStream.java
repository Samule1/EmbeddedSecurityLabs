import java.io.IOException;
import java.io.InputStream;

public class OtpInputStream extends InputStream{

	private String plainText;
	private String encrypted;
	private String key;
	private char letter;
	
	public OtpInputStream(String k, String text){
		plainText = text;
		key  = k;
	}
	
	public char getChar() {
		return this.letter;
	}
	
	public void setChar(char val) {
		letter = val;
	}
	
	@Override
	public int read() throws IOException {
		int nr = (int)letter;
		return nr;
	}
	
	
	public String GetEncryptedMsg(){
		return encrypted;
	}
	
	public void SetEncryptedMsg(String encMsg){
		encrypted = encMsg;
	}
	
	
	public void OtpEncryption() throws IOException{
		String encryptedMsg = "";
		int textInt[] = new int[plainText.length()]; 
		int keyInt[] = new int[plainText.length()];
		for(int i=0; i<plainText.length(); i++){
			if(Character.isLowerCase(plainText.charAt(i))){
				System.out.println("Not only Uppercase");
			}
			else{
				setChar(plainText.charAt(i));
				textInt[i] = read();
				setChar(key.charAt(i));
				keyInt[i] = read();
			}
		}
		for(int i=0; i<textInt.length; i++){
			encryptedMsg += (char)(((textInt[i] + keyInt[i]) % 26)+65);
		}
		SetEncryptedMsg(encryptedMsg);
	}

	
	public String OtpDecryption() throws IOException{
		String decryptedMsg = "";
		int textInt[] = new int[plainText.length()];
		int keyInt[] = new int[plainText.length()];
		for(int i=0; i<encrypted.length(); i++){
			setChar(encrypted.charAt(i));
			textInt[i] = read();
			setChar(key.charAt(i));
			keyInt[i] = read();
		}
		for(int i=0; i<textInt.length; i++){
			decryptedMsg += (char)((Math.floorMod((textInt[i] - (keyInt[i])), 26))+65);
		}
		return decryptedMsg;
	}
	
	
	public static void main(String[] args) throws IOException{
		
		OtpInputStream test = new OtpInputStream("KEYAIS", "MARCUS");
		test.OtpEncryption();
		System.out.println("Encrypted Msg : " + test.GetEncryptedMsg());
		System.out.println("Decrypted Msg : " + test.OtpDecryption());
		
		OtpInputStream test2 = new OtpInputStream("UTSLRJ", "HAMPUS");
		test2.OtpEncryption();
		System.out.println("Encrypted Msg : " + test2.GetEncryptedMsg());
		System.out.println("Decrypted Msg : " + test2.OtpDecryption());
		

	}


}
