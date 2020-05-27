package core;

import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import util.EC;

public class BlockChainStarter {
	

	public static void main(String[] args) throws Exception {
		// Set using Bouncy Castle cipher library.
		Security.addProvider(new BouncyCastleProvider());
		
		// Save the public and private key in separate files from creating Elliptic Curve object.
		EC ec = new EC();
		ec.generate("private1.pem", "public1.pem");
		ec.generate("private2.pem", "public2.pem");
		
		// Load the pair of keys from file.
		PrivateKey privateKey1 = ec.readPrivateKeyFromPemFile("private1.pem");
		PublicKey publicKey1 = ec.readPublicKeyFromPemFile("public1.pem");
		PrivateKey privateKey2 = ec.readPrivateKeyFromPemFile("private2.pem");
		PublicKey publicKey2 = ec.readPublicKeyFromPemFile("public2.pem");
		

	    Signature ecdsa;
	    ecdsa = Signature.getInstance("SHA1withECDSA");
	    // Encrypt by using privateKey1.
	    ecdsa.initSign(privateKey1);
	    
	    String text = "plain text";
	    System.out.println("Information of plain text: " + text);
	    byte[] baText = text.getBytes("UTF-8");
	    
	    // print the signature data from encoding plain data.
	    ecdsa.update(baText);
	    byte[] baSignature = ecdsa.sign();
	    System.out.println("Signature number: 0x" + (new BigInteger(1, baSignature).toString(16)).toUpperCase());


	    Signature signature;
	    signature = Signature.getInstance("SHA1withECDSA");
	    
	    // Verify from decoding publicKey2.
	    signature.initVerify(publicKey2);
	    signature.update(baText);
	    boolean result = signature.verify(baSignature);
	    
	    // Fail to match with privateKey1.
	    System.out.println("Verify identification: " + result);
	}

}
