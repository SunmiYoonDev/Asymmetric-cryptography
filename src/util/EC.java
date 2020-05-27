package util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.bouncycastle.util.encoders.Base64;

public class EC {
	// Use sect193k1 algorithm.
	private final String ALGORITHM = "sect163k1";
	
	public void generate(String privateKeyName, String publicKeyName) throws Exception{
		// Define the key object to create random public key and private key.
		// Use ECDSA of Bouncy Castle.
		KeyPairGenerator generator = KeyPairGenerator.getInstance("ECDSA","BC");
		
		// Generate Elliptic Curve digital signature algorithm object.
		// Use sect163k1 for Elliptic Curve algorithm.
		ECGenParameterSpec ecsp;
		// Define detail of algorithm spec.
		ecsp = new ECGenParameterSpec(ALGORITHM);
		// Create random temporary key.
		generator.initialize(ecsp, new SecureRandom());

		// Create pair of public key and private key.
		KeyPair kp = generator.generateKeyPair();
		System.out.println("Create pair of public key and private key.");
		PrivateKey privKey = kp.getPrivate();
		PublicKey pubKey = kp.getPublic();
		
		// Saving the pair of public key and private key, Give specific file name.
		writePemFile(privKey, "EC PRIVATE KEY", privateKeyName);
		writePemFile(pubKey, "EC PUBLIC KEY", publicKeyName);
		
	}
	
	// The function to save the file contained pair keys by Pem class
	private void writePemFile(Key key, String description, String filename) 
			throws FileNotFoundException, IOException{
		Pem pemFile = new Pem(key, description);
		pemFile.write(filename);
		System.out.println(String.format("Send EC encryte key %s to %s file.", description, filename));
	}
	
	// The function to extract the private key from pem file of string form.
	public PrivateKey readPrivateKeyFromPemFile(String privateKeyName) 
			throws FileNotFoundException, IOException, NoSuchAlgorithmException, InvalidKeySpecException{
		String data = readString(privateKeyName);
		System.out.println("Load EC private key from "+ privateKeyName);
		System.out.println(data);
		
		// Eliminate unnecessary description.
		data = data.replaceAll("-----BEGIN EC PRIVATE KEY-----", "");
	    data = data.replaceAll("-----END EC PRIVATE KEY-----", "");
	    
	    // Decoding PEM file by BASE64.
	    byte[] decoded = Base64.decode(data);
	    PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
	    KeyFactory factory = KeyFactory.getInstance("ECDSA");
	    PrivateKey privateKey = factory.generatePrivate(spec);
		
	    return privateKey;
	}

	// The function to extract the public key from pem file of string form.
	public PublicKey readPublicKeyFromPemFile(String publicKeyName) 
			throws FileNotFoundException, IOException, NoSuchAlgorithmException, InvalidKeySpecException{
		String data = readString(publicKeyName);
		System.out.println("Load EC public key from "+ publicKeyName);
		System.out.println(data);
		
		// Eliminate unnecessary description.
		data = data.replaceAll("-----BEGIN EC PUBLIC KEY-----", "");
	    data = data.replaceAll("-----END EC PUBLIC KEY-----", "");
	    
	    // Decoding PEM file by BASE64.
	    byte[] decoded = Base64.decode(data);
	    X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
	    KeyFactory factory = KeyFactory.getInstance("ECDSA");
	    PublicKey publicKey = factory.generatePublic(spec);
	    
	    return publicKey;
	}
	
	// The function read the text from specific file.
	private String readString(String fileName) throws FileNotFoundException, IOException{
		String pem = "";
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String line;
		while((line = br.readLine()) != null) {
			pem += line + "\n";
		}
		br.close();
		return pem;
	}
}
