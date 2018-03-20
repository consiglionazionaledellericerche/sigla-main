<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.inventario00.tabrif.bulk.*,
		it.cnr.contab.inventario00.bp.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<!-- 
 ?ResourceName "risultato_ricerca.jsp"
 ?ResourceTimestamp "13/12/00 19.48.42"
 ?ResourceEdition "1.0"
-->

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<title>Risultato Ricerca</title>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
</head>

<body class="Form">

<% SelezionatoreListaAlberoBP bp = (SelezionatoreListaAlberoBP)BusinessProcess.getBusinessProcess(request);
	 bp.openFormWindow(pageContext); %>

	<%	for (Enumeration e = bp.getHistory().elements();e.hasMoreElements();) {
			Ubicazione_beneBulk ubi = (Ubicazione_beneBulk)e.nextElement();
			if (ubi==null)  { %>
				<font size="2" face="Arial"><a href="javascript:submitForm('doGoToLevel(0)')"> Livello 0</a> >></font>
			<%	} else { %>
				<font size="2" face="Arial"><a href="javascript:submitForm('doGoToLevel(<%=ubi.getLivello().intValue()+1%>)')"> Livello <%=ubi.getLivello().intValue()+1%></a> >></font>
			<%	} %>
	<%	} %>
	<font size="2" face="Arial"> Livello <%=bp.getHistory().size()%></font>
	
	<table class="Panel">
		<tr>
		  <td><% bp.writeHTMLTable(pageContext,"100%",null); %></td>
		</tr>
		<tr>
		  <td><% bp.writeHTMLNavigator(out); %></td>
		</tr>	
	</table>

<%	bp.closeFormWindow(pageContext); %>
</body>

</html>