<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
			it.cnr.jada.action.*,
			it.cnr.jada.util.action.*,
			it.cnr.contab.pdg00.bulk.Stampa_situazione_analitica_x_GAEBulk"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>

<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Stampa Uscite in c/residui</title>
</head>
<body class="Form">

  <% BulkBP bp = (BulkBP)BusinessProcess.getBusinessProcess(request); 
		 bp.openFormWindow(pageContext);
		 Stampa_situazione_analitica_x_GAEBulk bulk=(Stampa_situazione_analitica_x_GAEBulk)bp.getModel(); %> 
	
	 
<div class="card p-3">
    <table class="w-100">
      <tr>
        <td><% bp.getController().writeFormLabel(out,"esercizio"); %></td>
        <td><% bp.getController().writeFormInput(out,"esercizio"); %></td>
      </tr>
      <tr>
      <td><% bp.getController().writeFormLabel(out,"findCdsForPrint"); %></td>
        <td><% bp.getController().writeFormInput(out,"findCdsForPrint"); %></td>
      </tr>
      <tr>
      <td><% bp.getController().writeFormLabel(out,"findUoForPrint"); %></td>
        <td><% bp.getController().writeFormInput(out,"findUoForPrint"); %></td>
      </tr>
      <tr>
        <td><% bp.getController().writeFormLabel(out,"findCdrForPrint"); %></td>
        <td><% bp.getController().writeFormInput(out,"findCdrForPrint"); %></td>
      </tr>
    </table>
</div>
<%	bp.closeFormWindow(pageContext); %>
</body>
</html>