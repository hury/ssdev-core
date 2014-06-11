package ctd.test.dsa;



import java.math.BigInteger;
import java.security.Key;

import java.security.KeyPair;
import java.security.KeyPairGenerator;


import java.security.SecureRandom;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.RSAPublicKeySpec;



import javax.crypto.Cipher;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import ctd.util.io.Bytes;


public class DSATester {
	private final static int KEYSIZE = 1024; 
	private final static BouncyCastleProvider provider = new BouncyCastleProvider();
	/**
	 * @param args
	 */
	
	public static byte[] encrypt(byte[] message, Key key) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA",provider);
		cipher.init(Cipher.ENCRYPT_MODE, key);
		return cipher.doFinal(message);
	}
	
	public static byte[] decrypt(byte[] message, Key key) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA",provider);
		cipher.init(Cipher.DECRYPT_MODE, key);
		return cipher.doFinal(message);
	}


	public static void main(String[] args) {
		 try {
			KeyPairGenerator pairgen= KeyPairGenerator.getInstance("RSA",provider);
			SecureRandom random=new SecureRandom();   
			pairgen.initialize(KEYSIZE, random); 
			KeyPair keyPair=pairgen.generateKeyPair(); 
			
			RSAPublicKey publicKey = (RSAPublicKey)keyPair.getPublic();
			RSAPrivateKey privateKey = (RSAPrivateKey)keyPair.getPrivate();
			
			//System.out.println(publicKey.);
			
			System.out.println("privateExponent:" + privateKey.getPrivateExponent().toString(16));
			System.out.println("modulus:" + privateKey.getModulus().toString(16));
			
			System.out.println("publicKeyLen:" + publicKey.getEncoded().length);
			//System.out.println("publicKey:" + Bytes.bytes2hex(publicKey.getModulus().toByteArray()));
			//System.out.println("publicKey-codec:"+Hex.encodeHexString(publicKey.getModulus().toByteArray()));
			System.out.println("modulus:" + publicKey.getModulus().toString(16));
			System.out.println("publicExponent:" + publicKey.getPublicExponent().toString(16));
			
			String moduluesHex =  publicKey.getPublicExponent().toString(16);//Bytes.bytes2hex(publicKey.getModulus().toByteArray());
			
//			
//			byte[] modulus = Bytes.hex2bytes("935e4eb47d1ec2404117220c272538c32439d8e40828bc81ee364a5f60d6674945f570fd9e3e77e50a9691a5589732e0f4903569d23f42ad644afc6a7e274b01");
//			
			byte[] modulus =  Bytes.hex2bytes(moduluesHex);//publicKey.getModulus().toByteArray();
			byte[] publicExponent = publicKey.getPublicExponent().toByteArray();
			RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(new BigInteger(modulus), new BigInteger(publicExponent));
			//publicKey = (RSAPublicKey) KeyFactory.getInstance("RSA",provider).generatePublic(pubKeySpec);
			
			String content = "hello world";
			System.out.println("len="+ content.getBytes().length);
			
			
			byte[] result =  encrypt(content.getBytes(),publicKey);
			String strResult = Bytes.bytes2hex(result);
			System.out.println("context=" + strResult);
			//System.out.println("context=" + Hex.encodeHexString(result));
			
			result = Bytes.hex2bytes(strResult);
			
			byte[] result2 = decrypt(result, privateKey);
			System.out.println(new String(result2));
			
		 } 
		 catch (Exception e) {
			e.printStackTrace();
		} 

	}

}
