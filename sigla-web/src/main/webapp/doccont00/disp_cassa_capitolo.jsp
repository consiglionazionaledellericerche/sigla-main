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
		it.cnr.contab.doccont00.bp.*,
		it.cnr.contab.doccont00.core.bulk.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
	<head>
		<script language="JavaScript" src="scripts/util.js"></script>
		<% JSPUtils.printBaseUrl(pageContext);%>
	</head>
	<script language="javascript" src="scripts/css.js"></script>
	<title>Disponibilità sul Capitolo</title>
	<body class="Form">
	<% DispCassaCapitoloBP bp = (DispCassaCapitoloBP)BusinessProcess.getBusinessProcess(request);
	 bp.openFormWindow(pageContext); 
	 MandatoBulk mandato = ((DispCassaCapitoloBulk)bp.getModel()).getMandato();%>

<table class="w-100">
	<tr>
	<% if ( mandato.TIPO_COMPETENZA.equals( mandato.getTi_competenza_residuo() )) { %>
		<td>
			<% bp.getDispCassa().setEnabled(false);
			   bp.getDispCassa().writeHTMLTable(pageContext,"mandato",false,false,false,"100%","200px");	 %>
		</td>
	<% } else { %>
		<td>
			<% bp.getDispCassaRes().setEnabled(false);
			   bp.getDispCassaRes().writeHTMLTable(pageContext,"mandato",false,false,false,"100%","200px");	 %>
		</td>
	<% } %>		
		
	</tr>
</table>
	
	<%	bp.closeFormWindow(pageContext); %>
</body>