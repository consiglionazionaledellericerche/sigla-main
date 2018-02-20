<%@ page 
	import="it.cnr.jada.action.*,
			it.cnr.jada.util.action.*,
			it.cnr.contab.utenze00.bp.GestioneUtenteBP"
%>
<html>
<head>
<% it.cnr.jada.util.jsp.JSPUtils.printBaseUrl(pageContext); %>
<script language="javascript" src="scripts/css.js"></script>
<script language="JavaScript" src="scripts/util.js"></script>
<title>Logo</title>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
</head>
<body class="Workspace">
<script>
restoreWorkspace();
</script>
<%
	BusinessProcess bp = BusinessProcess.getBusinessProcess(request);
%>
<% if (!bp.getParentRoot().isBootstrap()) {%>
	<table cellspacing="0" cellpadding="0" style="font-family : sans-serif; font-size : 12px; width: 100%">
		<tr valign=bottom>
			<td align=center><img id="AttendereImg" width="174" height="14" src="img/spacer.gif"></td>
		</tr>
		<tr valign=bottom>	
			<td align=center id="AttendereMessage" style="width:100%;color:white;font-size: 20px">&nbsp;</td>
		</tr>
	</table>
<%} %>	
<%
	if (bp instanceof GestioneUtenteBP) {
		GestioneUtenteBP gestioneUtenteBP = (GestioneUtenteBP)bp;
		gestioneUtenteBP.openForm(pageContext);
		if (!bp.getParentRoot().isBootstrap())
			gestioneUtenteBP.writePreferiti(out, HttpActionContext.getUserContext(session));
		gestioneUtenteBP.closeForm(pageContext);
	}
%>
</body>
<script>
	window.top.AttendereText = getElementById(document,"AttendereMessage");
	window.top.AttendereImg = getElementById(document,"AttendereImg");
</script>
</html>