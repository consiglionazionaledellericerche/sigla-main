package it.cnr.contab.util.rest;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.StringTokenizer;

import javax.ejb.EJBException;
import javax.xml.bind.DatatypeConverter;

import org.jboss.resteasy.util.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.cnr.contab.utente00.nav.ejb.GestioneLoginComponentSession;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.AdminUserContext;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;

public class BasicAuthentication {
	private static final Logger logger = LoggerFactory.getLogger(BasicAuthentication.class);
	private static final String AUTHENTICATION_SCHEME = "Basic";
	public static UtenteBulk authenticate(String authorization) throws ComponentException{
        boolean authorized = false;
		UtenteBulk utente = new UtenteBulk();
        // authenticate as specified by HTTP Basic Authentication
        if (authorization != null && authorization.length() != 0)
        {
            logger.info("Authorisation: " + authorization);
            String[] authorizationParts = authorization.split(" ");
            if (!authorizationParts[0].equalsIgnoreCase("basic"))
            {
                throw new ApplicationException("Authorization '" + authorizationParts[0] + "' not supported.");
            }
            String decodedAuthorisation = new String(DatatypeConverter.parseBase64Binary(authorizationParts[1]));
            logger.info("Decoded Authorisation: " + decodedAuthorisation);
            String[] parts = decodedAuthorisation.split(":");
            
            if (parts.length == 2)
            {
                // assume username and password passed as the parts
                String username = parts[0];
                String password = parts[1];
                
                logger.info("Username: " + username + ". Password: "+password);
                
				utente = new UtenteBulk();
				utente.setCd_utente(username.toUpperCase());
				utente.setLdap_password(password);
				utente.setPasswordInChiaro(password.toUpperCase());
				try {
					utente = loginComponentSession().validaUtente(AdminUserContext.getInstance(), utente);
					if (utente != null)
						authorized = true;
				} catch (RemoteException e) {
					throw new ApplicationException(e.getMessage());
				} catch (EJBException e) {
					throw new ApplicationException(e.getMessage());				
				}
            }
        }
        
        // request credentials if not authorized
        if (!authorized)
        {
           	logger.info("Requesting authorization credentials");
            return null;
        }
        return utente;
	}
	
	private static UtenteBulk authenticate (String username, String password)throws ComponentException{
		UtenteBulk utente = new UtenteBulk();
		utente.setCd_utente(username.toUpperCase());
		utente.setLdap_password(password);
		utente.setPasswordInChiaro(password.toUpperCase());
		try {
			return loginComponentSession().validaUtente(AdminUserContext.getInstance(), utente);
		} catch (RemoteException e) {
			throw new ApplicationException(e.getMessage());
		} catch (EJBException e) {
			throw new ApplicationException(e.getMessage());				
		}
	}
	public static UtenteBulk authenticate(List<String> authorization) throws IOException, ComponentException{
		// If no authorization information present; block access
		if (authorization == null || authorization.isEmpty()) {
			return null;
		}

		// Get encoded username and password
		final String encodedUserPassword = authorization.get(0)
				.replaceFirst(AUTHENTICATION_SCHEME + " ", "");

		// Decode username and password
		String usernameAndPassword = null;
		usernameAndPassword = new String(Base64.decode(encodedUserPassword));

		// Split username and password tokens
		final StringTokenizer tokenizer = new StringTokenizer(
				usernameAndPassword, ":");
		final String username = tokenizer.nextToken();
		final String password = tokenizer.nextToken();

		// Verifying Username and password
		logger.info(username);
		logger.info(password);
		return authenticate(username, password);
	}
	public static List getRuoli(UserContext userContext, UtenteBulk utente) throws IOException, ComponentException{
		return loginComponentSession().getRuoli(userContext, utente);
	}
	public static GestioneLoginComponentSession loginComponentSession() throws javax.ejb.EJBException, java.rmi.RemoteException {
		return (GestioneLoginComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRUTENZE00_NAV_EJB_GestioneLoginComponentSession");
	}
}
