import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Arrays;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSABench {

	private Cipher cipher;
	private BigInteger q, p, e;
	private KeyFactory factory;

	public RSABench(int primeSize, int moduloSize) {
		// Set the primes.
		this.q = new BigInteger(primeSize, 10, new Random());
		this.p = new BigInteger(primeSize, 10, new Random());
		this.e = new BigInteger(moduloSize, 10, new Random());

		// init cipher.
		try {
			this.cipher = Cipher.getInstance("RSA/ECB/NoPadding");
			this.factory = KeyFactory.getInstance("RSA");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e1) {
			e1.printStackTrace();
		}

	}

	public byte[] encrypt(byte[] m) {
		RSAPublicKeySpec spec = new RSAPublicKeySpec(p.multiply(q), e);
		try {
			java.security.interfaces.RSAPublicKey pub = (RSAPublicKey) factory.generatePublic(spec);
			cipher.init(Cipher.ENCRYPT_MODE, pub);
			return cipher.doFinal(m);
		} catch (InvalidKeySpecException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e1) {

			e1.printStackTrace();
		}
		return null;
	}

	public byte[] decrypt(byte[] secret) throws InvalidKeyException {
		RSAPrivateKeySpec spec = new RSAPrivateKeySpec(p.multiply(q),
				e.modInverse(p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE))));
		java.security.interfaces.RSAPrivateKey priv;
		try {
			priv = (java.security.interfaces.RSAPrivateKey) factory.generatePrivate(spec);
			cipher.init(Cipher.DECRYPT_MODE, priv);
			return cipher.doFinal(secret);
		} catch (InvalidKeySpecException | IllegalBlockSizeException | BadPaddingException e1) {

			e1.printStackTrace();
		}

		return null;
	}
	
	public static double getAvg(long[] data){
		double total = 0; 
		for(long dt : data) total += dt;
		return total / data.length; 
	}

	public static void main(String[] args) {
		String m = "MESSAGE";
		try {
			long[] t512 = new long[100]; 
			long[] t800 = new long[100];
			long[] t1300 = new long[100]; 

			// 64
			for (int i = 0; i < 100; i++) {
				long t = System.currentTimeMillis();
				RSABench r = new RSABench(512, 512);
				byte[] s = r.encrypt(m.getBytes("UTF-8"));
				r.decrypt(s);
				long T = System.currentTimeMillis();
				t512[i] = T - t; 
				
				
			}
			System.out.println("AVG TIME--> p = q = 512 e = 512: " +getAvg(t512));
			// 128
			for (int i = 0; i < 100; i++){
				long t = System.currentTimeMillis();
				RSABench r = new RSABench(800, 512);
				byte[] s = r.encrypt(m.getBytes("UTF-8"));
				r.decrypt(s);
				long T = System.currentTimeMillis();
				t800[i] = T - t; 
			}
			System.out.println("AVG TIME--> p = q = 800 e = 512: " +getAvg(t800));
			//512
			for(int i = 0; i < 100; i++){
				long t = System.currentTimeMillis();
				RSABench r = new RSABench(1300, 512);
				byte[] s = r.encrypt(m.getBytes("UTF-8"));
				r.decrypt(s);
				long T = System.currentTimeMillis();
				t1300[i] = T - t; 
			}
			System.out.println("AVG TIME--> p = q = 1300 e = 512: " +getAvg(t1300));
			

		} catch (InvalidKeyException | UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
