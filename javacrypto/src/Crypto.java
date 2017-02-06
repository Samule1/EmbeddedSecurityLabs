import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMDecryptorProvider;
import org.bouncycastle.openssl.PEMEncryptedKeyPair;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcePEMDecryptorProviderBuilder;

public class Crypto {

	public static void main(String[] args) throws NoSuchAlgorithmException {

		Security.addProvider(new BouncyCastleProvider());
		messageDisgest();
		readRSAKeys1();
		readRSAKeys2();
		signAndVerify();
		aesCipher();
		
	}

	public static void aesCipher() {
		try {
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			SecureRandom rnd = SecureRandom.getInstance("SHA1PRNG");

			byte[] salt = new byte[8];
			rnd.nextBytes(salt); // Store !!!

			KeySpec spec = new PBEKeySpec("pass".toCharArray(), salt, 65536, 128);
			SecretKey tmp = factory.generateSecret(spec);
			SecretKey key = new SecretKeySpec(tmp.getEncoded(), "AES");
			/*
			 * Alternative: byte[] keyData = new byte[16];
			 * rnd.nextBytes(keyData); SecretKey key = new
			 * SecretKeySpec(keyData, "AES");
			 */
			Cipher c = Cipher.getInstance("AES/CBC/PKCS7Padding");
			c.init(Cipher.ENCRYPT_MODE, key);
			byte[] iv = c.getIV(); // Store!!!
			String secret = "Secret";
			byte[] ciphertext = c.doFinal(secret.getBytes());
			System.out.println("AES cipher text: " + formatBytes(ciphertext, ":"));

			c.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
			byte[] plaintext = c.doFinal(ciphertext);
			System.out.println("AES Decrypted message: " + new String(plaintext));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void readRSAKeys1() {
		try {

			/* DER key C:\Skola\Embedded security\EmbeddedSecurityLabs\javacrypto\keys */ 
			File derKeyFile = new File("C:/Skola/Embedded security/EmbeddedSecurityLabs/javacrypto/keys/rsaprivate.der");
			FileInputStream in = new FileInputStream(derKeyFile);
			ByteArrayOutputStream bo = new ByteArrayOutputStream();
			for (int c = in.read(); c != -1; c = in.read())
				bo.write(c);
			in.close();
			bo.close();
			PKCS8EncodedKeySpec keyspec = new PKCS8EncodedKeySpec(bo.toByteArray());
			PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(keyspec);
			System.out.println("Decoded DER key class: " + privateKey.getClass().getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void readRSAKeys2() {
		try {
			/* PEM key */ 
			File pemKeyFile = new File("C:/Skola/Embedded security/EmbeddedSecurityLabs/javacrypto/keys/rsaprivate.pem");
			PEMParser pem = new PEMParser(new InputStreamReader(new FileInputStream(pemKeyFile)));
			Object obj = pem.readObject();
			pem.close();
			PEMKeyPair kpPem = null;
			if (obj instanceof PEMEncryptedKeyPair) {
				PEMDecryptorProvider decryptorProv = new JcePEMDecryptorProviderBuilder()
						.build("pass".toCharArray());
				kpPem = ((PEMEncryptedKeyPair) obj).decryptKeyPair(decryptorProv);
			} else if (obj instanceof PEMKeyPair) {
				kpPem = (PEMKeyPair) obj;
			}
			PrivateKey privateKey = null;
			if (kpPem != null) {
				privateKey = new JcaPEMKeyConverter().getPrivateKey(kpPem.getPrivateKeyInfo());
				System.out.println("Decoded PEM key class: " + privateKey.getClass().getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void signAndVerify() {
		try {
			KeyPairGenerator kpgen = KeyPairGenerator.getInstance("RSA");
			kpgen.initialize(2048, SecureRandom.getInstance("SHA1PRNG"));
			KeyPair kp = kpgen.generateKeyPair();
			System.out.println(kp.getPrivate().getClass().getName());

			String data = "Data to be signed";
			Signature sig = Signature.getInstance("SHA256withRSA");

			/* Sign */
			sig.initSign(kp.getPrivate());
			sig.update(data.getBytes());
			byte[] signature = sig.sign();
			System.out.println(formatBytes(signature, ""));

			/* Verify */
			sig.initVerify(kp.getPublic());
			sig.update(data.getBytes());
			System.out.println("Signature verification result: " + sig.verify(signature));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void messageDisgest() {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA1");
			byte[] sha = md.digest("Message".getBytes());
			System.out.println(formatBytes(sha, ":"));
			System.out.println();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	public static void listProviders() {
		for (Provider p : Security.getProviders()) {
			System.out.println(p.getName());
		}
	}

	public static String formatBytes(byte[] bytes, String sep) {
		if (sep == null)
			sep = "";
		String r = "";
		for (byte b : bytes)
			r += String.format("%02X", b) + sep;
		if (sep.length() > 0)
			r = r.substring(0, r.length() - sep.length());
		return r;
	}
}
