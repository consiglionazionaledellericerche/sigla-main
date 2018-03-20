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
		<td><% bp.getController().writeFormLabel(out,"ti_etr_spe"); %></td>
        <td><% bp.getController().writeFormInput(out,null,"ti_etr_spe",false,null,"onClick=\"submitForm('doOnTipoChange')\""); %></td>
	  </tr>  
	  <tr>	
		<td><% bp.getController().writeFormLabel(out,"findDipartimentoForPrint"); %></td>
		<td><% bp.getController().writeFormInput(out,"findDipartimentoForPrint"); %></td>
	  </tr>
	  <tr>	
		<td><% bp.getController().writeFormLabel(out,"findProgettoForPrint"); %></td>
		<td><% bp.getController().writeFormInput(out,"findProgettoForPrint"); %></td>
	  </tr>	  
	  <tr>	
		<td><% bp.getController().writeFormLabel(out,"findCDSForPrint"); %></td>
		<td><% bp.getController().writeFormInput(out,"findCDSForPrint"); %></td>
	  </tr>
	  <!--
       <tr>
		<td><% bp.getController().writeFormLabel(out,"findElemento_voceForPrint"); %></td>
		<td><% bp.getController().writeFormInput(out,"findElemento_voceForPrint"); %></td>
	  </tr>	  
	   -->
	</table>
	</div>
   </td>
  </tr>
</table>
<table>  
  <tr>
   <td>	
	<div class="Group">
	<table>    	  
	  <tr>
		<td class="GroupLabel">Raggruppamenti</td>
		<td><% bp.getController().writeFormInput(out,"seleziona"); %></td>
	  </tr>	
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"ragrr_dipartimento"); %></td>
		<td><% bp.getController().writeFormInput(out,"ragrr_dipartimento"); %></td>
	  </tr>
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"ragrr_progetto"); %></td>
		<td><% bp.getController().writeFormInput(out,"ragrr_progetto"); %></td>
	  </tr>
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"ragrr_tipo_progetto"); %></td>
		<td><% bp.getController().writeFormInput(out,"ragrr_tipo_progetto"); %></td>
	  </tr>
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"ragrr_istituto"); %></td>
		<td><% bp.getController().writeFormInput(out,"ragrr_istituto"); %></td>
	  </tr>
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"ragrr_area"); %></td>
		<td><% bp.getController().writeFormInput(out,"ragrr_area"); %></td>
	  </tr>
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"ragrr_commessa"); %></td>
		<td><% bp.getController().writeFormInput(out,"ragrr_commessa"); %></td>
	  </tr>
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"ragrr_modulo"); %></td>
		<td><% bp.getController().writeFormInput(out,"ragrr_modulo"); %></td>
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
		<td><% bp.getController().writeFormLabel(out,"ragrr_natura"); %></td>
		<td><% bp.getController().writeFormInput(out,"ragrr_natura"); %></td>
	  </tr>
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"ragrr_elemento_voce"); %></td>
		<td><% bp.getController().writeFormInput(out,"ragrr_elemento_voce"); %></td>
	  </tr>	  
	 </table>
	 </div>
	</td>
   </tr>	    
</table>

<% bp.closeFormWindow(pageContext); %>

</body>
</html>