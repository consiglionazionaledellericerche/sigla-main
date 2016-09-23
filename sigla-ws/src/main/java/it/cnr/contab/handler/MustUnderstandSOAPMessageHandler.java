package it.cnr.contab.handler;
import java.util.HashSet;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

public class MustUnderstandSOAPMessageHandler implements SOAPHandler<SOAPMessageContext> {
    @Override
    public Set<QName> getHeaders() {
        final QName securityHeader = new QName(
            "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd",
            "Security",
            "wsse");

        final HashSet<QName> headers = new HashSet<QName>();
        headers.add(securityHeader);

        // notify the runtime that this is handled
        return headers;
    }

	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		return false;
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		return false;
	}

	@Override
	public void close(MessageContext context) {
		
	}
}