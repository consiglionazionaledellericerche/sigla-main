<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.cori00.docs.bulk.*,
		it.cnr.contab.cori00.bp.*"
%>

<% CRUDLiquidazioneCORIBP bp = (CRUDLiquidazioneCORIBP)BusinessProcess.getBusinessProcess(request);	
	 Liquid_coriBulk liquid_cori = (Liquid_coriBulk)bp.getModel(); %>

	<div class="Group">
	<table class="Panel">
		<tr>
		  <td>
			<% bp.getController().writeFormLabel(out,"cd_cds");%>
		  </td>
		  <td>
			<% bp.getController().writeFormInput(out,"cd_cds");%>
			<% bp.getController().writeFormInput(out,"ds_cds");%>			
		  </td>
		</tr>
		<tr>
		  <td>
			<% bp.getController().writeFormLabel(out,"cd_unita_organizzativa");%>
		  </td>
		  <td>
			<% bp.getController().writeFormInput(out,"cd_unita_organizzativa");%>
			<% bp.getController().writeFormInput(out,"ds_unita_organizzativa");%>			
		  </td>
		</tr>
		<tr>
		  <td>
			<% bp.getController().writeFormLabel(out,"esercizio");%>
		  </td>
		  <td>
			<% bp.getController().writeFormInput(out,"esercizio");%>
		  </td>
		</tr>
	</table>
	</div>
	<div class="Group">
	<table class="Panel">
		<tr>
		  <td colspan=2>
			<% bp.getController().writeFormInput(out,null,"da_esercizio_precedente",bp.isCalcolato(),null,null);%>
			<% bp.getController().writeFormLabel(out,"da_esercizio_precedente");%>
		  </td>
		</tr>
		<tr>
		  <td>
			<% bp.getController().writeFormLabel(out,"pg_liquidazione");%>
		  </td>
		  <td>
			<% bp.getController().writeFormInput(out,"pg_liquidazione");%>
		  </td>
		</tr>
		<tr>
		  <td>
			<% bp.getController().writeFormLabel(out,"dacr");%>
		  </td>
		  <td>
			<% bp.getController().writeFormInput(out,"dacr");%>
		  </td>
		</tr>
		<tr>
		  <td>
			<% bp.getController().writeFormLabel(out,"stato");%>
		  </td>
		  <td>
			<% bp.getController().writeFormInput(out,"stato");%>
		  </td>
		</tr>
	</table>
	<table>
		<tr>
		  <td>
			<% bp.getController().writeFormLabel(out,"dt_da");%>		  
			<% bp.getController().writeFormInput(out,null,"dt_da",bp.isCalcolato(),null,null);%>
		  </td>
		  <td>
			<% bp.getController().writeFormLabel(out,"dt_a");%>		  
			<% bp.getController().writeFormInput(out,null,"dt_a",bp.isCalcolato(),null,null);%>
		  </td>
		</tr>			
	</table>
	<% if (bp.isInserting()){ %>
		<table>
			<tr>	
				<td>
					<% JSPUtils.button(out,null,null,"Calcola liquidazione","javascript:submitForm('doCalcolaLiquidazione')", !bp.isCalcolato(),bp.getParentRoot().isBootstrap()); %>
				</td>
				<% if (bp.isAbilitatoF24()){ %>
				<td>
					<% JSPUtils.button(out,null,null,"Seleziona Gruppi F24","javascript:submitForm('doSelezionaF24')",bp.isCalcolato(),bp.getParentRoot().isBootstrap()); %>
				</td>
				<td>
					<% JSPUtils.button(out,null,null,"Seleziona Gruppi F24 Previd.","javascript:submitForm('doSelezionaF24Prev')",bp.isCalcolato(), bp.getParentRoot().isBootstrap()); %>
				</td>
				<% } %>
		</tr>
		</table>
	<% } %>
	</div>
	<% if ((bp.isCalcolato() || bp.isEditing())|| bp.isViewing()){ %> 
		 <table class="Group" style="width:100%">
	       <tr>
	    	<td>
			  <span class="FormLabel" style="color:blue">Gruppi:</span>
			</td>
	       </tr>
	       <tr>
	         <td>
	   		<% bp.getGruppi().writeHTMLTable(pageContext,"v_liquid_gruppo",false,false,false,"100%","200px"); %>	
	   		</td>
	       </tr>
	       
		<% if (bp.isInserting()){ %>
		       <tr>	
				<td>
					<% JSPUtils.button(out,null,null,"Liquida","javascript:submitForm('doLiquidazione')", !bp.isLiquidato(),bp.getParentRoot().isBootstrap()); %>
				</td>
			   </tr>
		<% } %>
	    </table>
	    <table class="Group" style="width:100%">
	       <tr>
	    	<td>
			  <span class="FormLabel" style="color:blue">Dettagli:</span>
			</td>
	       </tr>
	       <tr>
	         <td>
		   		<% bp.getGruppiDet().writeHTMLTable(pageContext,"v_liquid_gruppo_det",false,false,false,"100%","200px"); %>	
	   		</td>
	       </tr>
	    </table>
	<% } %>