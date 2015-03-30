package it.cnr.contab.docamm00.comp;

import org.apache.commons.codec.binary.Base64;

public class Encrypt {

	public static void main(String[] args) {
		String password = "changeme";
		byte[] temp;
	    String encodedPassword = null;
	    temp = Base64.encodeBase64(password.getBytes());
	    encodedPassword = new String(temp);
	    System.out.println(encodedPassword);
	}

}
