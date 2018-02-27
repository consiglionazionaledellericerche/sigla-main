<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
  	    it.cnr.jada.UserContext,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.config00.bp.*,
		it.cnr.contab.config00.bulk.*"
%>

<%
	LockObjectBP bp = (LockObjectBP)BusinessProcess.getBusinessProcess(request);
%>

<html>
<head>
	<% JSPUtils.printBaseUrl(pageContext); %>
	<script language="javascript" src="scripts/css.js"></script>
	<script language="JavaScript" src="scripts/util.js"></script>
	<title>Sessioni in lock su Tabelle</title>
</head>
<body class="Form">
<%
	bp.openFormWindow(pageContext);
%>
	<table class="Panel">
		<TR>
			<TD><% bp.getController().writeFormLabel(out,"cds");%></TD>
			<TD><% bp.getController().writeFormInput(out,"cds");%></TD>
		</TR>
		<tr>
			<td colspan=2>
				  <b ALIGN="CENTER"><font size=2>Utenti</font></b>
			      <% bp.getUtenti().writeHTMLTable(pageContext,null,false,false,false,"100%","100px", true); %>
			</td>
		</tr>
		<tr>
			<td colspan=2>
				  <b ALIGN="CENTER"><font size=2>Oggetti</font></b>
			      <% bp.getOggetti().writeHTMLTable(pageContext,"oggetti",false,false,false,"100%","300px", true); %>
			</td>
		</tr>
	</table>
<%	
	bp.closeFormWindow(pageContext); 
%>
</body>