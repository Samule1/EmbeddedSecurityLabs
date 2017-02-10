import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class AES {
	public static void main(String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException{
		//Security.addProvider(new BouncyCastleProvider());
		//Take the key from c code and paste. 
		byte[] key = "0011223344556677".getBytes();
		
		//Generate secret key from byte array.
		SecretKey secretKey = new SecretKeySpec(key, "AES"); 
		
		//IV
		byte[] iv = "0123456789abcdef".getBytes();
		
		//Cipher, YAY 
		Cipher c = Cipher.getInstance("AES/CBC/NoPadding");
		c.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
		
		byte[] ciphertext = c.doFinal(DatatypeConverter.parseHexBinary("9e4816cc13810b8424d788fbcd4b006b31bf45f5f9191072820ae0a545500c966cf22afda1002466a78b7e4ddf02587f"));
		
		System.out.println("AES Decrypted message: " + new String(ciphertext));
		
		
		
	}
}
