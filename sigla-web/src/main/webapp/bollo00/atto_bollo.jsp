<%@page import="it.cnr.contab.doccont00.tabrif.bulk.Tipo_bolloBulk"%>
<%@ page 
	import="it.cnr.jada.action.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.bollo00.bulk.*,
		it.cnr.contab.bollo00.tabrif.bulk.*"
%>

<%
	SimpleCRUDBP bp = (SimpleCRUDBP)BusinessProcess.getBusinessProcess(request);
	Atto_bolloBulk model = (Atto_bolloBulk)bp.getModel(); 
%>

<html>

<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="javascript" src="scripts/css.js"></script>
<script language="JavaScript" src="scripts/util.js"></script>
<title>Documenti da assoggettare a Bollo Virtuale</title>
</head>
<body class="Form">
<%
	bp.openFormWindow(pageContext);
%>
<div class="card p-2">
	<table class="Panel w-100">
		<TR><% bp.getController().writeFormField(out,"esercizio");%></TR>
		<TR><% bp.getController().writeFormField(out,"findUnitaOrganizzativa");%></TR>
		<TR><% bp.getController().writeFormField(out,"descrizioneAtto");%></TR>
		<tr><% bp.getController().writeFormField(out,"findTipoAttoBollo"); %></tr>
		<% if (model!=null && model.getTipoAttoBollo()!=null) {
			if (model.getTipoAttoBollo().isCalcoloPerFoglio()) { %>  
				<tr><% bp.getController().writeFormField(out,"numFogli"); %></tr>
				<% if ((model.getNumRighe()!=null && model.getNumRighe()>0) || 
					   (model.getNumDettagli()==null || model.getNumDettagli()==0)) { %>	
				<tr><% bp.getController().writeFormField(out,"numRighe"); %></tr>
				<% } %>
			<% } else if (model.getTipoAttoBollo().isCalcoloPerEsemplare()) { %>  
				<tr><% bp.getController().writeFormField(out,"numEsemplari"); %></tr>
			<% } %>
		<% } %>		
	</table>
</div>
<%	bp.closeFormWindow(pageContext); %>
</body>