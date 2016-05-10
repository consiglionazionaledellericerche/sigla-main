package it.cnr.si.undertow.wsse.security;


import static io.undertow.UndertowMessages.MESSAGES;
import static io.undertow.util.StatusCodes.UNAUTHORIZED;
import io.undertow.security.api.AuthenticationMechanism;
import io.undertow.security.api.AuthenticationMechanismFactory;
import io.undertow.security.api.SecurityContext;
import io.undertow.security.idm.Account;
import io.undertow.security.idm.IdentityManager;
import io.undertow.security.idm.PasswordCredential;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.form.FormParserFactory;
import io.undertow.servlet.handlers.ServletRequestContext;
import io.undertow.servlet.spec.HttpServletRequestImpl;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

/**
 * The authentication handler responsible for WSSE authentication
 *
 * @author <a href="mailto:marco.spasiano@cnr.it">Marco Spasiano</a>
 */
public class WSSEAuthenticationMechanism implements AuthenticationMechanism {

    
    private final String name;

    /** The Constant USERNAME_TOKEN_STRING. */
    private static final String USERNAME_TOKEN_STRING = "UsernameToken";

    /** The Constant USERNAME_STRING. */
    private static final String USERNAME_STRING = "Username";

    /** The Constant PASSWORD_STRING. */
    private static final String PASSWORD_STRING = "Password";


    private final IdentityManager identityManager;

    public WSSEAuthenticationMechanism(final String name, final IdentityManager identityManager) {
    	this.name = name;
        this.identityManager = identityManager;
    }

    @SuppressWarnings("deprecation")
    private IdentityManager getIdentityManager(SecurityContext securityContext) {
        return identityManager != null ? identityManager : securityContext.getIdentityManager();
    }

    @SuppressWarnings("unchecked")
	private SOAPElement extractUserNameInfo(Iterator<SOAPElement> childElems) {
        SOAPElement child = null;
        Name sName;
        // iterate through child elements
        while (childElems.hasNext()) {
            Object elem = childElems.next();

            if (elem instanceof SOAPElement) {

                // Get child element and its name
                child = (SOAPElement) elem;
                sName = child.getElementName();

                // Check whether there is a UserNameToken element
                if (!USERNAME_TOKEN_STRING.equalsIgnoreCase(sName.getLocalName())) {

                    if (child.getChildElements().hasNext()) {
                        return extractUserNameInfo(child.getChildElements());
                    }
                }
            }
        }
        return child;
    }

    private static class AuthenticationRequestWrapper extends HttpServletRequestWrapper {  
	    private final String xmlPayload;	     
	    public AuthenticationRequestWrapper (HttpServletRequest request){	         
	        super(request);         
	        // read the original payload into the xmlPayload variable
	        StringBuilder stringBuilder = new StringBuilder();
	        BufferedReader bufferedReader = null;
	        try {
	            // read the payload into the StringBuilder
	            InputStream inputStream = request.getInputStream();
	            if (inputStream != null) {
	                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
	                char[] charBuffer = new char[128];
	                int bytesRead = -1;
	                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
	                    stringBuilder.append(charBuffer, 0, bytesRead);
	                }
	            } else {
	                // make an empty string since there is no payload
	                stringBuilder.append("");
	            }
	        } catch (IOException ex) {
	
	        } finally {
	            if (bufferedReader != null) {
	                try {
	                    bufferedReader.close();
	                } catch (IOException iox) {
	                    // ignore
	                }
	            }
	        }
	        xmlPayload = stringBuilder.toString();
	    }
  
	    public boolean isRequestPresent() {
	    	return xmlPayload.length() != 0;
	    }
	    
	    /**
	     * Override of the getInputStream() method which returns an InputStream that reads from the
	     * stored XML payload string instead of from the request's actual InputStream.
	     */
	    @Override
	    public ServletInputStream getInputStream () throws IOException {
	         
	        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(xmlPayload.getBytes());
	        ServletInputStream inputStream = new ServletInputStream() {
	            public int read () 
	                throws IOException {
	                return byteArrayInputStream.read();
	            }
	        };
	        return inputStream;
	    }
     
    }
    /**
     * @see io.undertow.server.HttpHandler#handleRequest(io.undertow.server.HttpServerExchange)
     */
    @SuppressWarnings("unchecked")
	@Override
    public AuthenticationMechanismOutcome authenticate(HttpServerExchange exchange, SecurityContext securityContext) {
    	try {
            final ServletRequestContext servletRequestContext = exchange.getAttachment(ServletRequestContext.ATTACHMENT_KEY);
            HttpServletRequestImpl req = servletRequestContext.getOriginalRequest();
            AuthenticationRequestWrapper authenticationRequestWrapper = new AuthenticationRequestWrapper(req);
            servletRequestContext.setServletRequest(authenticationRequestWrapper);
            if (!authenticationRequestWrapper.isRequestPresent())
                return AuthenticationMechanismOutcome.AUTHENTICATED;            	
			SOAPMessage message = MessageFactory.newInstance().createMessage(null, authenticationRequestWrapper.getInputStream());
			SOAPPart sp = message.getSOAPPart();
            SOAPEnvelope envelope = sp.getEnvelope();
            SOAPHeader header = envelope.getHeader();
            if (header != null) {
                Name sName;
                // variable for user name and password
                String userName = null;
                String password = null;                
                // look for authentication header element inside the HEADER block
                Iterator<SOAPElement> childElems = header.getChildElements();
                SOAPElement child = extractUserNameInfo(childElems);
                // get an iterator on child elements of SOAP element
                Iterator<SOAPElement> childElemsUserNameToken = child.getChildElements();
                
                // loop through child elements
                while (childElemsUserNameToken.hasNext()) {
                    // get next child element
                    Object elem = childElemsUserNameToken.next();
                    if (elem instanceof SOAPElement) {
                        child = (SOAPElement) elem;
                        // get the name of SOAP element
                        sName = child.getElementName();
                        // get the value of username element
                        if (USERNAME_STRING.equalsIgnoreCase(sName.getLocalName())) {
                            userName = child.getValue();
                        } else if (PASSWORD_STRING.equalsIgnoreCase(sName.getLocalName())) {
                            password = child.getValue();
                        }
                    }
                }
                final AuthenticationMechanismOutcome result;
                IdentityManager idm = getIdentityManager(securityContext);
                if (password == null)
                    return AuthenticationMechanismOutcome.NOT_ATTEMPTED;
                PasswordCredential credential = new PasswordCredential(password.toCharArray());
                Account account = idm.verify(userName, credential);   
                if (account != null) {
                	securityContext.authenticationComplete(account, name, false);
                	result = AuthenticationMechanismOutcome.AUTHENTICATED;
                } else {
                	securityContext.authenticationFailed(MESSAGES.authenticationFailed(userName), name);
                	result = AuthenticationMechanismOutcome.NOT_AUTHENTICATED;
                }
                return result;
            }			
    	} catch (IOException | SOAPException e1) {
            return AuthenticationMechanismOutcome.NOT_AUTHENTICATED;
		}
        return AuthenticationMechanismOutcome.NOT_ATTEMPTED;
    }

    @Override
    public ChallengeResult sendChallenge(HttpServerExchange exchange, SecurityContext securityContext) {
        return new ChallengeResult(true, UNAUTHORIZED);
    }

    public static class Factory implements AuthenticationMechanismFactory {

        private final IdentityManager identityManager;

        public Factory(IdentityManager identityManager) {
            this.identityManager = identityManager;
        }

        @Override
        public AuthenticationMechanism create(String mechanismName, FormParserFactory formParserFactory, Map<String, String> properties) {
            return new WSSEAuthenticationMechanism(mechanismName, identityManager);
        }
    }
}