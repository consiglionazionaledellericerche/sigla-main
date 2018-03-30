<%@ page pageEncoding="UTF-8"
    import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*,it.cnr.jada.util.action.*,it.cnr.contab.config00.bp.*"
	language="java"
	session="false"
	contentType="text/xml; charset=UTF-8"%><%((ResponseXMLBP)BusinessProcess.getBusinessProcess(request)).generaXML(pageContext);%>