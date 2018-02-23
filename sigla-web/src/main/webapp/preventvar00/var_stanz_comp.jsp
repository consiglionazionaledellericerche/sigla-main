<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.action.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.preventvar00.consultazioni.bp.*" %>


<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Variazioni allo Stanziamento di Competenza</title>
</head>
<body class="Form">

<% 
	BulkBP bp = (BulkBP)BusinessProcess.getBusinessProcess(request);
	ConsVarStanzCompetenzaBP bp1 = (ConsVarStanzCompetenzaBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext);
	boolean isFieldReadOnly = true;
%>

	<div class="Group" style="width:100%">
		<table width="100%">
			  <tr>
				<td> <% bp.getController().writeFormLabel(out,"dt_approvazione_da");%></td>
				<td> <% bp.getController().writeFormInput(out,null,"dt_approvazione_da",false,null,"");%></td>
				<td> <% bp.getController().writeFormLabel(out,"dt_approvazione_a");%></td>
				<td> <% bp.getController().writeFormInput(out,null,"dt_approvazione_a",false,null,"");%></td>
			</tr> 
			
 			
 			<tr>
				<td> <% bp.getController().writeFormLabel(out,"abs_tot_variazione_da"); %></td>
    			<td> <% bp.getController().writeFormInput(out,null,"abs_tot_variazione_da",false,null,"");%></td>
    			<td> <% bp.getController().writeFormLabel(out,"abs_tot_variazione_a"); %></td>
    			<td> <% bp.getController().writeFormInput(out,null,"abs_tot_variazione_a",false,null,"");%></td>
 			</tr>

			<tr>
				<td><% bp.getController().writeFormLabel(out,"ti_gestione"); %></td>
    			<td><% bp.getController().writeFormInput(out,null,"ti_gestione",false,null,"onclick=\"submitForm('doCambiaGestione')\""); %></td>
 			</tr>   
 			
			<tr>
			
		    	<td><% bp.getController().writeFormLabel( out, "find_classificazione_voci"); %></td>		
		   		<td><% bp.getController().writeFormInput( out, "find_classificazione_voci");%></td>
		   		
			</tr>
 			
		</table>
		
		<table>  
  <tr>
   <td>	
	<div class="Group">
	<table>    	  
	  <tr>
		<td class="GroupLabel">Tipologia Variazioni</td>
		<td><% bp.getController().writeFormInput(out,"seleziona"); %></td>
	  </tr>	
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"ragr_STO_S_CDS"); %></td>
		<td><% bp.getController().writeFormInput(out,"ragr_STO_S_CDS"); %></td>
	  </tr>
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"ragr_STO_E_CDS"); %></td>
		<td><% bp.getController().writeFormInput(out,"ragr_STO_E_CDS"); %></td>
	  </tr> 
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"ragr_STO_S_AREA"); %></td>
		<td><% bp.getController().writeFormInput(out,"ragr_STO_S_AREA"); %></td>
	  </tr>
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"ragr_STO_E_AREA"); %></td>
		<td><% bp.getController().writeFormInput(out,"ragr_STO_E_AREA"); %></td>
	  </tr>
	  
	    <tr>
		<td><% bp.getController().writeFormLabel(out,"ragr_VAR_MENO_FON"); %></td>
		<td><% bp.getController().writeFormInput(out,"ragr_VAR_MENO_FON"); %></td>
	  </tr>
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"ragr_VAR_PIU_FON"); %></td>
		<td><% bp.getController().writeFormInput(out,"ragr_VAR_PIU_FON"); %></td>
	  </tr> 
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"ragr_STO_S_TOT"); %></td>
		<td><% bp.getController().writeFormInput(out,"ragr_STO_S_TOT"); %></td>
	  </tr>
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"ragr_STO_E_TOT"); %></td>
		<td><% bp.getController().writeFormInput(out,"ragr_STO_E_TOT"); %></td>
	  </tr>
	  
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"ragr_VAR_PIU_CDS"); %></td>
		<td><% bp.getController().writeFormInput(out,"ragr_VAR_PIU_CDS"); %></td>
	  </tr>
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"ragr_VAR_MENO_CDS"); %></td>
		<td><% bp.getController().writeFormInput(out,"ragr_VAR_MENO_CDS"); %></td>
	  </tr>
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"ragr_VAR_PIU_TOT"); %></td>
		<td><% bp.getController().writeFormInput(out,"ragr_VAR_PIU_TOT"); %></td>
	  </tr>
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"ragr_VAR_MENO_TOT"); %></td>
		<td><% bp.getController().writeFormInput(out,"ragr_VAR_MENO_TOT"); %></td>
	  </tr>
	   <tr>
		<td><% bp.getController().writeFormLabel(out,"ragr_PREL_FON"); %></td>
		<td><% bp.getController().writeFormInput(out,"ragr_PREL_FON"); %></td>
	  </tr>
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"ragr_REST_FOND"); %></td>
		<td><% bp.getController().writeFormInput(out,"ragr_REST_FOND"); %></td>
	  </tr>
	  <tr>
		<td><% bp.getController().writeFormLabel(out,"ragr_NO_TIPO"); %></td>
		<td><% bp.getController().writeFormInput(out,"ragr_NO_TIPO"); %></td>
	  </tr>
	  </table>
	 </div>
	</td>
   </tr>	    
</table> 
	</div>
<%	bp.closeFormWindow(pageContext); %>
</body>
</html> 
