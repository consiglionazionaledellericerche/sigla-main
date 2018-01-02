<%@ page 
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.pdg00.cdip.bulk.*,
		it.cnr.contab.pdg00.bp.*"
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<%	BulkBP bp = (BulkBP)BusinessProcess.getBusinessProcess(request);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Stampa Variazione allo Stanziamento Residuo</title>
</head>
<body class="Form">

<%	bp.openFormWindow(pageContext); %>

<div class="Group card p-2">
<table class="w-100">
  <tr>
	<% bp.getController().writeFormField(out,"esercizio"); %>
  </tr>	
  <tr>	
	<% bp.getController().writeFormField(out,"findvariazioneForPrint"); %>
  </tr>  
</table>
</div>

<% bp.closeFormWindow(pageContext); %>

</body>
</html>