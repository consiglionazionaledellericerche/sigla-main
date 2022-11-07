/*
 * Copyright (C) 2019  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.contab.web.rest.config;

import it.cnr.contab.utente00.nav.comp.GestioneLoginComponent;
import it.cnr.contab.utente00.nav.ejb.GestioneLoginComponentSession;
import it.cnr.contab.utenze00.bp.CNRUserContext;
import it.cnr.contab.utenze00.bulk.UtenteBulk;
import it.cnr.contab.utenze00.bulk.Utente_unita_ruoloBulk;
import it.cnr.contab.util.servlet.JSONRESTRequest;
import it.cnr.contab.web.rest.exception.UnauthorizedException;
import it.cnr.jada.UserContext;
import it.cnr.jada.action.AdminUserContext;
import it.cnr.jada.comp.ApplicationException;
import it.cnr.jada.comp.ComponentException;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.*;

import javax.ejb.EJBException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BasicAuthentication {
	private static final Logger logger = LoggerFactory.getLogger(BasicAuthentication.class);
	private static final String AUTHENTICATION_SCHEME = "Basic";

	public static UtenteBulk findUtenteBulk(String username) throws ComponentException {
		UtenteBulk utente = new UtenteBulk();
		try {
			utente.setCd_utente(username);
			utente = loginComponentSession().validaUtente(
					AdminUserContext.getInstance(),
					utente,
					GestioneLoginComponent.VALIDA_FASE_INIZIALE);
			if (utente != null)
				return utente;
		} catch (RemoteException e) {
			throw new ApplicationException(e.getMessage());
		} catch (EJBException e) {
			throw new ApplicationException(e.getMessage());
		}
		return null;
	}

	public static UtenteBulk authenticateUtenteMultiplo(String authorization, String utenteMultiplo) throws ComponentException{
        boolean authorized = false;
		UtenteBulk utente = new UtenteBulk();
        // authenticate as specified by HTTP Basic Authentication
        if (authorization != null && authorization.length() != 0)
        {
            String[] parts = getUsernameAndPassword(authorization);
            
            if (parts.length == 2)
            {
                // assume username and password passed as the parts
                String username = parts[0];
                String password = parts[1];
                
                logger.debug("Username: {} Password: {}", username, password);
                
				utente = new UtenteBulk();
				utente.setCd_utente(username.toUpperCase());
				utente.setLdap_password(password);
				utente.setPasswordInChiaro(password.toUpperCase());
				utente.setUtente_multiplo(utenteMultiplo);
				try {
					utente = loginComponentSession().validaUtente(AdminUserContext.getInstance(), utente,
							Optional.ofNullable(utenteMultiplo)
								.map(s -> GestioneLoginComponent.VALIDA_FASE_INIZIALE_UTENTE_MULTIPLO)
								.orElseGet(() -> GestioneLoginComponent.VALIDA_FASE_INIZIALE)
							);
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

	public static String getUsername(String authorization) throws IOException, ComponentException{
		// If no authorization information present; block access
		String[] parts = getUsernameAndPassword(authorization);
		String username = parts[0];
		return username;
	}

	private static String[] getUsernameAndPassword(String authorization) throws ApplicationException {
		logger.debug("Authorization: " + authorization);
		String[] authorizationParts = authorization.split(" ");
		if (!authorizationParts[0].equalsIgnoreCase("basic"))
		{
		    throw new ApplicationException("Authorization '" + authorizationParts[0] + "' not supported.");
		}
		String decodedAuthorisation = new String(DatatypeConverter.parseBase64Binary(authorizationParts[1]));
		logger.debug("Decoded Authorization: " + decodedAuthorisation);
		String[] parts = decodedAuthorisation.split(":");
		return parts;
	}
	
	private static UtenteBulk authenticate (HttpServletRequest httpServletRequest, String username, String password)throws ComponentException{
		UtenteBulk utente = new UtenteBulk();
		utente.setCd_utente(username.toUpperCase());
		utente.setLdap_password(password);
		utente.setPasswordInChiaro(password.toUpperCase());
		try {
			try {
				if (!Optional.ofNullable(httpServletRequest.getUserPrincipal()).isPresent())
					httpServletRequest.login(username, password);
			} catch (ServletException e) {
				if (e.getMessage().contains("Login failed")) {
					throw new UnauthorizedException("", e);
				}
				throw new RuntimeException(e);
			}
			return loginComponentSession().validaUtente(AdminUserContext.getInstance(), utente);
		} catch (RemoteException e) {
			throw new ApplicationException(e.getMessage());
		} catch (EJBException e) {
			throw new ApplicationException(e.getMessage());				
		}
	}

	public static UtenteBulk authenticate(HttpServletRequest httpServletRequest,List<String> authorization) throws IOException, ComponentException{
		return Optional.ofNullable(getUsernameAndPassword(authorization))
				.map(stringTokenizer -> {
					try {
						final String username = stringTokenizer.nextToken();
						final String password = stringTokenizer.nextToken();
						// Verifying Username and password
						logger.debug("UserName: {} Password: {}", username, password);
						return authenticate(httpServletRequest, username, password);
					} catch (ComponentException| NoSuchElementException e) {
						return null;
					}
				}).orElse(null);
	}

	public static String getUsername(List<String> authorization) throws IOException, ComponentException{
		// If no authorization information present; block access
		final StringTokenizer tokenizer = getUsernameAndPassword(authorization);
		final String username = tokenizer.nextToken();
		return username;
	}

	private static StringTokenizer getUsernameAndPassword(List<String> authorization) throws IOException {
		if (authorization == null || authorization.isEmpty()) {
			return null;
		}

		// Get encoded username and password
		final String encodedUserPassword = authorization.get(0)
				.replaceFirst(AUTHENTICATION_SCHEME + " ", "");

		// Decode username and password
		String usernameAndPassword = null;
		usernameAndPassword = new String(Base64.getDecoder().decode(encodedUserPassword));

		// Split username and password tokens
		final StringTokenizer tokenizer = new StringTokenizer(
				usernameAndPassword, ":");
		return tokenizer;
	}
	
	public static CNRUserContext getContextFromRequest(JSONRESTRequest jsonRequest, String user, String sessionId) throws IOException, ApplicationException {
 		if (jsonRequest != null && jsonRequest.getContext() != null) {
 			if (jsonRequest.getContext().getEsercizio() == null)
 				throw new ApplicationException("Esercizio non puo essere vuoto");
	
 			if (jsonRequest.getContext().getCd_cds() == null)
 				throw new ApplicationException("Il codice del CdS non puo essere vuoto");
 			if (jsonRequest.getContext().getCd_unita_organizzativa() == null)
 				throw new ApplicationException("Il codice della UO non puo essere vuoto");
 			if (jsonRequest.getContext().getCd_cdr() == null)
 				throw new ApplicationException("Il codice del CdR non puo essere vuoto");
 
 			return new CNRUserContext(user, sessionId, jsonRequest.getContext().getEsercizio(), 
 					jsonRequest.getContext().getCd_unita_organizzativa(), 
 					jsonRequest.getContext().getCd_cds(), 
 					jsonRequest.getContext().getCd_cdr());			
 		} else {
 			throw new ApplicationException("E' necessario valorizzare il contesto utente.");
 		}
 	}    
	
	@SuppressWarnings("unchecked")
	public static List<Utente_unita_ruoloBulk> getRuoli(UserContext userContext, UtenteBulk utente) throws IOException, ComponentException{
		return loginComponentSession().getRuoli(userContext, utente);
	}
	public static GestioneLoginComponentSession loginComponentSession() throws javax.ejb.EJBException, java.rmi.RemoteException {
		return (GestioneLoginComponentSession)it.cnr.jada.util.ejb.EJBCommonServices.createEJB("CNRUTENZE00_NAV_EJB_GestioneLoginComponentSession");
	}
}
