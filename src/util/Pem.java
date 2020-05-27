package util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.Key;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;

public class Pem {

	private PemObject pemObject;
	
	// Save the information of description and data of key in PEM ojbect.
	public Pem(Key key, String description) {
		this.pemObject = new PemObject(description, key.getEncoded());
	}
	
	// Saving a PEM file, Give specific file name.
	public void write(String filename) throws FileNotFoundException, IOException{
		PemWriter pemWriter = new PemWriter(new OutputStreamWriter(new FileOutputStream(filename)));
		try {
			pemWriter.writeObject(pemObject);
		} finally {
			pemWriter.close();
		}
	}
}
