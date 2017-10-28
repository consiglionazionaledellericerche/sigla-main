<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<%@ page 
	import="it.cnr.jada.util.jsp.*,
	        it.cnr.jada.action.*,
	        java.util.*,
	        it.cnr.jada.util.action.*,
	        it.cnr.contab.utenze00.bulk.*"
%>

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<% 
	BusinessProcess bp = BusinessProcess.getBusinessProcess(request);
	java.io.StringWriter sw = new java.io.StringWriter();
	Throwable e = (Throwable)request.getAttribute("uncaughtException");
	if (e == null)
		e = (Throwable)request.getAttribute("javax.servlet.jsp.jspException");
	if (e != null)
		e.printStackTrace(new java.io.PrintWriter(sw));
    if (e != null && e instanceof javax.servlet.ServletException && ((javax.servlet.ServletException)e).getRootCause() != null)
        ((javax.servlet.ServletException)e).getRootCause().printStackTrace(new java.io.PrintWriter(sw));
    if (e != null && e.getCause() != null)
        e.getCause().printStackTrace(new java.io.PrintWriter(sw));

	String stackTrace = sw.toString();
	CNRUserInfo userInfo = (CNRUserInfo)HttpActionContext.getUserInfo(request);
	bp.insertError(new HttpActionContext(this,request,response), userInfo.getUserid(),userInfo.getEsercizio(), (userInfo.getUnita_organizzativa() != null) ? userInfo.getUnita_organizzativa().getCd_unita_organizzativa() : null,stackTrace);
%>
<script language="JavaScript" archive="scripts.jar" src="scripts/util.js"></script>
<script>
function showStackTrace() {
	popup = window.open("", "stackTrace", 'toolbar=no,resizable,scrollbars,width=800,height=600') 
	popup.document.writeln("Session id: <%= request.getSession().getId()%><br>")
	popup.document.writeln("Thread corrente: <%= Thread.currentThread().toString()%><br>")
	popup.document.writeln("Clone Info:<ul>")
	<% 	java.net.InetAddress inet = java.net.InetAddress.getLocalHost();
		int hostHash = inet.hashCode();%>
	popup.document.write("<li>Host Address: <%= inet.getHostAddress() %></li>")
	popup.document.write("<li>Host Name: <%= inet.getHostName() %></li>")
	popup.document.write("</ul>")

	<% if (userInfo != null) { %>
	popup.document.writeln("Utente: <%= userInfo.getUserid()%><br>")
	popup.document.writeln("Esercizio: <%= userInfo.getEsercizio()%><br>")
	<%   if (userInfo.getUnita_organizzativa() != null) { %>
	popup.document.writeln("Unità organizzativa: <%= userInfo.getUnita_organizzativa().getCd_unita_organizzativa()%><br>")
	<%   }
	   } %>
	<% if (bp != null) {%>
	popup.document.writeln("BusinessProcess: <%= bp.getPath()%><br>")
	<% } %>
	popup.document.writeln("Stack trace:")
	popup.document.writeln("<pre><%= JSPUtils.encodeJavascriptString(stackTrace)%></pre>")
}
</script>
<title>Gestione errori interni</title>
</head>

<body>
<form name="mainForm" action="FormAction.do">
<% 	BusinessProcess.encode(bp,pageContext); %>
<input type="hidden" name="comando" value="doDefault">
<P align=center>
Non è possibile portare a termine l'operazione richiesta a causa di un errore interno del server.
<P align=center>
<button onclick="if (disableDblClick()) submitForm('doDefault')">Chiudi</button>
<button onclick="showStackTrace()">Mostra errore</button>
</form>

</body>
</html>