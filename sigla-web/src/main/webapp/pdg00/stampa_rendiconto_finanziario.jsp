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
<%	BulkBP bp = (BulkBP)BusinessProcess.getBusinessProcess(request);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title><%=bp.getBulkInfo().getShortDescription()%></title>
</head>
<body class="Form">

<%	bp.openFormWindow(pageContext); %>
<div class="card p-2 mb-2">
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
		<td></td>
		<td><% bp.getController().writeFormInput(out,"ti_etr_spe"); %></td>
	  </tr>  
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"tipo_stampa"); %></td>
		<td><% bp.getController().writeFormInput(out,"tipo_stampa"); %></td>
	  </tr>  
	  <tr>	
		<td><% bp.getController().writeFormLabel(out,"findDipartimento"); %></td>
		<td><% bp.getController().writeFormInput(out,"findDipartimento"); %></td>
	  </tr>
	  <tr>	
		<td><% bp.getController().writeFormLabel(out,"findCDSForPrint"); %></td>
		<td><% bp.getController().writeFormInput(out,"findCDSForPrint"); %></td>
	  </tr>	  
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"ufficiale"); %></td>
		<td><% bp.getController().writeFormInput(out,"ufficiale"); %></td>
	  </tr>  
	</table>
	</div>
   </td>
  </tr>
</table>
</div>
<div class="card p-2">
<table>  
  <tr>
   <td>	
	<div class="Group">
	<table>    	  
	  <tr>
		<td class="GroupLabel text-primary">Raggruppamenti</td>
		<td><% bp.getController().writeFormInput(out,"seleziona"); %></td>
	  </tr>	
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"ragrr_dipartimento"); %></td>
		<td><% bp.getController().writeFormInput(out,"ragrr_dipartimento"); %></td>
	  </tr>
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"ragrr_cds"); %></td>
		<td><% bp.getController().writeFormInput(out,"ragrr_cds"); %></td>
	  </tr>
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"ragrr_titolo"); %></td>
		<td><% bp.getController().writeFormInput(out,"ragrr_titolo"); %></td>
	  </tr>
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"ragrr_categoria"); %></td>
		<td><% bp.getController().writeFormInput(out,"ragrr_categoria"); %></td>
	  </tr>
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"ragrr_capitolo"); %></td>
		<td><% bp.getController().writeFormInput(out,"ragrr_capitolo"); %></td>
	  </tr>
	 </table>
	 </div>
	</td>
   </tr>	    
</table>
</div>
<% bp.closeFormWindow(pageContext); %>

</body>
</html>