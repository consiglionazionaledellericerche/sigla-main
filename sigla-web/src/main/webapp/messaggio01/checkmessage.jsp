<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*,it.cnr.jada.util.action.*"
	session="false"
%>

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="javascript" src="scripts/css.js"></script>
</head>

<body style="background: buttonface;">
	<table cellspacing="0" cellpadding="0" style="font-family : sans-serif;font-size : 12px; height:100%; width: 100%">
		<tr>
			<td class="MessageBar">
				<% if (request.getParameter("user") != null) { %>
				<%   Integer priorita = (Integer)request.getAttribute("it.cnr.contab.messaggio00.servlet.MessageCheckServlet.priorita");
				     if (priorita != null) { %>
				     <form name=co action="javascript:window.parent.frames['desktop'].frames['workspace'].apriListaMessaggi('GestioneUtente.do?comando=doApriListaMessaggi');"></form>     
				     <script language="JavaScript">
				       if(confirm('Ci sono nuovi Messaggi, vuoi leggerli?')){       
				           document.co.submit();
				       }  
				     </script>
				
								<a href="javascript:window.parent.frames['desktop'].frames['workspace'].apriListaMessaggi('GestioneUtente.do?comando=doApriListaMessaggi');"><img src="img/msgpriority<%= priorita%>.gif" ></a>
				<%   } else { %>
				                <a href="javascript:window.parent.frames['desktop'].frames['workspace'].apriListaMessaggi('GestioneUtente.do?comando=doApriListaMessaggi');"><img src="img/sendmail16.gif"></a>
				<%   } %>
				<% } else { %>
					&nbsp;
				<% } %>
			</td>
		</tr>
	</table>	
</body>
<script>
window.top.MessageCheckFrame = window;
</script>
</html>