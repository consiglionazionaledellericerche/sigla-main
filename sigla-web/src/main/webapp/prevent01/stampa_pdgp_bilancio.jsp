<%@ page 
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.prevent01.bulk.*,
		it.cnr.contab.prevent01.bp.*"
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<%	BulkBP bp = (BulkBP)BusinessProcess.getBusinessProcess(request);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Stampa Bilancio Previsione</title>
</head>
<body class="Form">

<%	bp.openFormWindow(pageContext); %>

<table width=100%>
 <tr>
  <td>
	<div class="Group">
	<table>
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"esercizio"); %></td>
		<td><% bp.getController().writeFormInput(out,"esercizio"); %></td>
	  </tr>	
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"ti_gestione"); %></td>
        <td><% bp.getController().writeFormInput(out,null,"ti_gestione",false,null,"onClick=\"submitForm('doOnTipoChange')\""); %></td>
	  </tr>  
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"ti_fonte"); %></td>
        <td><% bp.getController().writeFormInput(out,null,"ti_fonte",false,null,"onClick=\"submitForm('doOnFonteChange')\""); %></td>
	  </tr>  
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"ti_livello"); %></td>
		<td><% bp.getController().writeFormInput(out,"ti_livello"); %></td>
	  </tr>	
	</table>
	</div>
   </td>
  </tr>
</table>


<% bp.closeFormWindow(pageContext); %>

</body>
</html>