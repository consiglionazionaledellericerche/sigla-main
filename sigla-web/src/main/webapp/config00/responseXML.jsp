<%@ page import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*,it.cnr.jada.util.action.*,
	        it.cnr.contab.config00.bp.*"
	language="java" 
	contentType="text/xml; charset=ISO-8859-1" 
	pageEncoding="ISO-8859-1"%><%((ResponseXMLBP)BusinessProcess.getBusinessProcess(request)).generaXML(pageContext);%>