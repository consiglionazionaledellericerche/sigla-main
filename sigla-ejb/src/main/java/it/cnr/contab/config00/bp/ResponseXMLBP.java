package it.cnr.contab.config00.bp;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.jsp.PageContext;

public interface ResponseXMLBP {
	public abstract void generaXML(PageContext pagecontext) throws IOException, ServletException;
}
