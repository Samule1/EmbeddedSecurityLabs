import java.io.IOException;
import java.io.InputStream;

public class XORInputStream extends InputStream {

	private String plainText;
	private String encrypted;
	private String key;
	private char letter;

	public XORInputStream(String k, String text){
		plainText = text;
		key  = k;
	}

	@Override
	public int read() throws IOException {
		int nr = (int)letter;
		return nr;
	}

	public char getChar() {
		return this.letter;
	}

	public void setChar(char val) {
		letter = val;
	}


	public String GetEncryptedMsg(){
		return encrypted;
	}

	public void SetEncryptedMsg(String encMsg){
		encrypted = encMsg;
	}

	public void XOREncryption() throws IOException{
		String encryptedMsg = "";
		int textInt[] = new int[plainText.length()];
		int keyInt[] = new int[plainText.length()];
		for(int i=0; i<plainText.length(); i++){
				setChar(plainText.charAt(i));
				textInt[i] = read();
				setChar(key.charAt(i));
				keyInt[i] = read();

		}
		for(int i=0; i<textInt.length; i++){
			encryptedMsg += (char)(textInt[i]^ keyInt[i]);
		}
		SetEncryptedMsg(encryptedMsg);
	}

	public String XORDecryption() throws IOException{
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
			decryptedMsg += (char)(textInt[i]^keyInt[i]);
		}
		return decryptedMsg;
	}


	public static void main(String args[]) throws IOException{
		XORInputStream test = new XORInputStream("dsiMMs", "MARCUS");

		test.XOREncryption();
		System.out.println("Encrypted msg : " + test.GetEncryptedMsg());
		System.out.println("Decrypted msg : " + test.XORDecryption());

		XORInputStream test2 = new XORInputStream("DGSDTS", "HAMPUS");
		test2.XOREncryption();
		System.out.println("Encrypted msg : " + test2.GetEncryptedMsg());
		System.out.println("Decrypted msg : " +test2.XORDecryption());

		XORInputStream test3 = new XORInputStream("KG12cb", "Marc11");
		test3.XOREncryption();
		System.out.println("Encrypted msg : " + test3.GetEncryptedMsg());
		System.out.println("Decrypted msg : " + test3.XORDecryption());
	}


}
