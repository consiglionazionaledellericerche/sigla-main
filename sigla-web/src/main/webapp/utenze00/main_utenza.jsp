<!-- 
 ?ResourceName ""
 ?ResourceTimestamp ""
 ?ResourceEdition ""
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.utenze00.bp.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<script language="JavaScript" src="scripts/util.js"></script>
<% JSPUtils.printBaseUrl(pageContext); %>
</head>
<script language="javascript" src="scripts/css.js"></script>
<title>Gestione utenza</title>
<body class="Form">

<% CRUDBP bp = (CRUDBP)BusinessProcess.getBusinessProcess(request);
	 bp.openFormWindow(pageContext); %>

	<div class="Panel">
		<%	JSPUtils.tabbed(
					pageContext,
					"tab",
					new String[][] {
							{ "tabUtenza","Utenza","/utenze00/tab_utenza.jsp" },
							{ "tabAccessi","Accessi","/utenze00/tab_accessi.jsp" },
							{ "tabRuoli","Ruoli","/utenze00/tab_ruoli.jsp" },
                            { "tabMail","Indirizzi E-Mail","/utenze00/tab_mail.jsp" }, 
                            { "tabAbilOrdMag","Abilitazione Ordini","/utenze00/tab_abil_ordini.jsp" }},
					
					bp.getTab("tab"),
					"center",
					"100%",
                   "100%" );
		%>
	</div>

<%	bp.closeFormWindow(pageContext); %>
</body>
</html>