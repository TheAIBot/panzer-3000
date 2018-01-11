package engine;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


public final class Crypto {
	
    private static final String ALGORITHM = "RSA";
    private static final String PROVIDER = "SUN";
    private static final String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
	
	private Crypto()  {
		// Empty
	}
	
	public static KeyPair getPair() throws NoSuchAlgorithmException, NoSuchProviderException {
		// Initialize a generator - DSA is algorithm, SUN is a provider
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM);
		
		// Initialize a random - SHA1PRNG is algorithm, SUN is a provider
		SecureRandom random = SecureRandom.getInstance("SHA1PRNG", PROVIDER);
		keyGen.initialize(1024, random);
		
		// Generate keys
		return keyGen.generateKeyPair();
	}
	
	public static byte[] encrypt(String data, PublicKey publicKey) throws Exception {
		
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);

		byte[] encryptedBytes = cipher.doFinal(data.getBytes());

		return encryptedBytes;
	}
	
	public static String decrypt(byte[] data, PrivateKey privateKey) throws IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
		
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, privateKey);

		byte[] decryptedBytes = cipher.doFinal(data);

		return new String(decryptedBytes);
		
	} 
	
	public static String getSaltString() {
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;

    }
	
	public static PublicKey unencode(byte[] publicKey) throws InvalidKeySpecException, NoSuchAlgorithmException {
		return KeyFactory.getInstance(ALGORITHM).generatePublic(new X509EncodedKeySpec(publicKey));
	}
	
}