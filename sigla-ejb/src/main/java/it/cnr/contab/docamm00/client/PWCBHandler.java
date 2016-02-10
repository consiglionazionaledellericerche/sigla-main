package it.cnr.contab.docamm00.client;

import org.apache.ws.security.WSPasswordCallback;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import java.io.IOException;

public class PWCBHandler implements CallbackHandler {
	private static String siglaWSClientPassword;
	
	public static String getWSSiglaClientPassword() {
		return siglaWSClientPassword;
	}

	public static void setSiglaClientWSPassword(String siglaClientWSPassword) {
		PWCBHandler.siglaWSClientPassword = siglaClientWSPassword;
	}

	public void handle(Callback[] callbacks) throws IOException,
		UnsupportedCallbackException {
		for (int i = 0; i < callbacks.length; i++) {
			WSPasswordCallback pwcb = (WSPasswordCallback)callbacks[i];
			String id = pwcb.getIdentifer();
			if("sigla".equals(id)) {
				pwcb.setPassword(getWSSiglaClientPassword());
			}
		}
	}
}