<%@ page pageEncoding="UTF-8"
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
<%	StampaVariazioniPdgRiepBP bp = (StampaVariazioniPdgRiepBP)BusinessProcess.getBusinessProcess(request);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title><%=bp.getBulkInfo().getShortDescription()%></title>
</head>
<body class="Form">

<%	bp.openFormWindow(pageContext); %>

<div class="Group card p-2">
<table class="w-100">
  <tr>
	<% bp.getController().writeFormField(out,"esercizio"); %>
  </tr>	
  <tr>	
	<% bp.getController().writeFormField(out,"findCDSForPrint"); %>
  </tr>  
  <tr>	
	<% bp.getController().writeFormField(out,"findUOForPrint"); %>
  </tr>  
  <tr>	
	<% bp.getController().writeFormField(out,"stato"); %>
  </tr> 
</table>
</div>
	  
<div class="Group card p-2">
<table>
  <tr> 
	<% bp.getController().writeFormField(out,"DataApprovazione_da"); %>
	<% bp.getController().writeFormField(out,"DataApprovazione_a"); %>
  </tr> 
</table>
</div>
	
<div class="Group card">
  <% bp.getCrudRiepilogoVariazioni().writeHTMLTable(pageContext,"RiepilogoVariazioni",true,false,true,"100%","300px"); %>
</div>

<% bp.closeFormWindow(pageContext); %>

</body>
</html>