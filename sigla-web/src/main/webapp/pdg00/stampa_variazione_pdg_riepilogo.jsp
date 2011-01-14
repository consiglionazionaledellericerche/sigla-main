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
<%	StampaVariazioniPdgRiepBP bp = (StampaVariazioniPdgRiepBP)BusinessProcess.getBusinessProcess(request);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title><%=bp.getBulkInfo().getShortDescription()%></title>
</head>
<body class="Form">

<%	bp.openFormWindow(pageContext); %>

<table width=100%>
 <tr>
  <td height="123">
	<div class="Group">
	<table>
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"esercizio"); %></td>
		<td><% bp.getController().writeFormInput(out,"esercizio"); %></td>
	  </tr>	
	  <tr>	
		<td><% bp.getController().writeFormLabel(out,"findCDSForPrint"); %></td>
		<td><% bp.getController().writeFormInput(out,"findCDSForPrint"); %></td>
	  </tr>  
	  <tr>	
		<td><% bp.getController().writeFormLabel(out,"findUOForPrint"); %></td>
		<td><% bp.getController().writeFormInput(out,"findUOForPrint"); %></td>
	  </tr>  
	  <tr>	
		<td><% bp.getController().writeFormLabel(out,"stato"); %></td>
		<td><% bp.getController().writeFormInput(out,"stato"); %></td>
	  </tr> 
	  </table>
	  </div>
	  
	  <div class="Group">
	  <table>
	  <tr> 
		<td><% bp.getController().writeFormLabel(out,"DataApprovazione_da"); %></td>
		<td><% bp.getController().writeFormInput(out,"DataApprovazione_da"); %></td>
		<td><% bp.getController().writeFormLabel(out,"DataApprovazione_a"); %></td>
		<td><% bp.getController().writeFormInput(out,"DataApprovazione_a"); %></td>
	  </tr> 
	</table>
	</div>
	
	<div class="Group">
 	  <% bp.getCrudRiepilogoVariazioni().writeHTMLTable(pageContext,"RiepilogoVariazioni",true,false,true,"95%","300px"); %>
	</div>
	
   </td>
  </tr>
</table>


<% bp.closeFormWindow(pageContext); %>

</body>
</html>