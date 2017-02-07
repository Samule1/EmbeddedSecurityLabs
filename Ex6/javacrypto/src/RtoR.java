import java.awt.peer.CheckboxMenuItemPeer;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;

import org.bouncycastle.asn1.pkcs.RSAPrivateKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class RtoR {
	
	public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeySpecException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException, NoSuchProviderException, UnsupportedEncodingException{
		
		//p, q and public exponent.
		BigInteger q = new BigInteger(1024, 10, new Random());
		BigInteger p = new BigInteger(1024, 10, new Random()); 
		BigInteger e = new BigInteger(2048, 10, new Random());
		
		//Get simple RSA 
		RSA rsa = new RSA(p.toString(), q.toString(), e.toString()); 
		
		//h is the message, 104 in BigInteger
		byte[] input = "h".getBytes();
		BigInteger c = rsa.encrypt(new BigInteger(input));
		
		
		System.out.println("Enc: "+c.toString());
		
		System.out.println("Dec:" +rsa.decrypt(c));	
		
		//A cipher, YAAY
		Cipher cipher = Cipher.getInstance("RSA/ECB/NoPadding");
		
		KeyFactory facrory = KeyFactory.getInstance("RSA");
		
		//For publickeyspec use RSAPublicKeySpec(n, e) e is private exponent. For private: RSAPrivateKeySpec(n, privateExponent)
		RSAPrivateKeySpec spec = new RSAPrivateKeySpec(p.multiply(q), e.modInverse(new BigInteger(p.toString()).subtract(BigInteger.ONE).multiply(new BigInteger(q.toString()).subtract(BigInteger.ONE))));
		
		//Get private key from specc.
		java.security.interfaces.RSAPrivateKey priv = (java.security.interfaces.RSAPrivateKey) facrory.generatePrivate(spec);
			
		
		cipher.init(Cipher.DECRYPT_MODE, priv);
		
		//Decrypted output
		byte[] dec = cipher.doFinal(c.toByteArray());
		
		
		System.out.println("Decoded using big boy API: " + new BigInteger(dec));
		
		
		
	}

}
