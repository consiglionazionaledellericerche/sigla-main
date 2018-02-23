<%@ page pageEncoding="UTF-8"  import = "it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*, it.cnr.jada.util.action.*, it.cnr.contab.missioni00.bp.*, it.cnr.contab.missioni00.docs.bulk.*, it.cnr.jada.bulk.*, it.cnr.contab.docamm00.bp.*" %>

<%  
	CRUDMissioneBP bp = (CRUDMissioneBP)BusinessProcess.getBusinessProcess(request);
	MissioneBulk missione = (MissioneBulk) bp.getModel();

	if(missione == null)
		missione = new MissioneBulk();
%>

<div class="Group" style="width:100%">
<table>
<tr></tr>
<tr></tr>

 <% if (bp.isSearching()) 
 	{ 
	  boolean isInSpesaMode = (bp instanceof IDocumentoAmministrativoSpesaBP && ((IDocumentoAmministrativoSpesaBP)bp).isSpesaBP()) ? true : false; %>
	  	<tr>
		<td><% bp.getController().writeFormLabel(out,"stato_pagamento_fondo_ecoForSearch");%></td>
	    <td><% bp.getController().writeFormInput(out,null,"stato_pagamento_fondo_ecoForSearch", isInSpesaMode, null,""); %></td>
	  	</tr>
	  	
  <% } else { %>
	  
	  	<tr>
		<td><% bp.getController().writeFormLabel(out,"stato_pagamento_fondo_eco");%></td>      	
		<td><% bp.getController().writeFormInput(out,null,"stato_pagamento_fondo_eco",false,null,"onChange=\"submitForm('doDefault')\"");%></td>
	  	</tr>
	  	
	  	<tr>
		<td><% bp.getController().writeFormLabel(out,"dt_pagamento_fondo_eco"); %></td>
		<td><% bp.getController().writeFormInput(out,"dt_pagamento_fondo_eco"); %></td>
	  	</tr>
  <% } %>
 
<tr></tr>
<tr></tr>   
</table>
</div>

<fieldset>
<legend ACCESSKEY=G TABINDEX=1 style="font-weight:bold; font-family:sans-serif; font-size:12px; color:blue">Scadenza Impegno</legend>
<div class="Panel" style="width:100%">
<table>
	<tr>	
	<td><% bp.getController().writeFormLabel( out, "esercizio_ori_obbligazione"); %></td>
	<td><% bp.getController().writeFormInput( out, "esercizio_ori_obbligazione"); %></td>
	<td><% bp.getController().writeFormLabel( out, "pg_obbligazione"); %></td>
	<td><% bp.getController().writeFormInput( out, "pg_obbligazione"); %></td>
	</tr>
</table>
<table width="100%">
	<tr>		
	<td><% bp.getController().writeFormLabel(out, "pg_obbligazione_scadenzario");%></td>
	<td><% bp.getController().writeFormInput(out, "pg_obbligazione_scadenzario");%></td>
	<td><% bp.getController().writeFormLabel(out, "ds_scadenza_obbligazione");%></td>
	<td><% bp.getController().writeFormInput(out, "ds_scadenza_obbligazione");%></td>
	<td><% bp.getController().writeFormLabel(out, "im_scadenza");%></td>
	<td><% bp.getController().writeFormInput(out, "im_scadenza");%></td>	
	</tr>

	<tr>		
	<td><% bp.getController().writeFormLabel(out, "dt_scadenza");%></td>
	<td><% bp.getController().writeFormInput(out, "dt_scadenza");%></td>
	<td><% bp.getController().writeFormLabel(out, "esercizio_obbligazione");%></td>	
	<td><% bp.getController().writeFormInput(out, "esercizio_obbligazione");%></td>
	<td><% bp.getController().writeFormLabel(out, "cd_cds_obbligazione");%></td>
	<td><% bp.getController().writeFormInput(out, "cd_cds_obbligazione");%></td>	
	</tr>
	
	<tr></tr>
	<tr></tr>
</table>
<table width="100%">	
	<tr>
	<td ALIGN="CENTER">
		<% JSPUtils.button(out,bp.encodePath("img/new24.gif"),bp.encodePath("img/new24.gif"),"Crea/Ricerca<br>impegno","javascript:submitForm('doRicercaScadenzaObbligazione')",bp.areBottoniObbligazioneAbilitati(),bp.getParentRoot().isBootstrap()); %>
		<% JSPUtils.button(out,bp.encodePath("img/remove24.gif"),bp.encodePath("img/remove24.gif"),"Elimina<br>impegno","javascript:submitForm('doEliminaScadenzaObbligazione')",bp.areBottoniObbligazioneAbilitati(), bp.getParentRoot().isBootstrap()); %>
		<% JSPUtils.button(out,bp.encodePath("img/redo24.gif"),bp.encodePath("img/redo24.gif"), "Aggiorna in<br>manuale", "javascript:submitForm('doOpenObbligazioniWindow')", bp.isBottoneObbligazioneAggiornaManualeAbilitato(), bp.getParentRoot().isBootstrap()); %>
		<% JSPUtils.button(out,bp.encodePath("img/refresh24.gif"),bp.encodePath("img/refresh24.gif"), "Aggiorna in<br>automatico", "javascript:submitForm('doModificaScadenzaInAutomatico')", bp.areBottoniObbligazioneAbilitati(), bp.getParentRoot().isBootstrap()); %>
	</td>
	</tr>
</table>
</div>
</fieldset>

<fieldset>
<legend ACCESSKEY=G TABINDEX=1 style="font-weight:bold; font-family:sans-serif; font-size:12px; color:blue">Compenso</legend>
<div class="Panel" style="width:100%">
<table width="100%">
	<tr>	
	<td><% bp.getController().writeFormLabel( out, "pg_compenso"); %></td>
	<td><% bp.getController().writeFormInput( out, "pg_compenso"); %></td>	
	<td><% bp.getController().writeFormLabel( out, "esercizio_compenso"); %></td>
	<td><% bp.getController().writeFormInput( out, "esercizio_compenso"); %></td>
	</tr>

	<tr>
	<td><% bp.getController().writeFormLabel( out, "cd_uo_compenso"); %></td>
	<td><% bp.getController().writeFormInput( out, "cd_uo_compenso"); %></td>			
	<td><% bp.getController().writeFormLabel( out, "cd_cds_compenso"); %></td>
	<td><% bp.getController().writeFormInput( out, "cd_cds_compenso"); %></td>
	</tr>
	
	<tr></tr>
	<tr></tr>
</table>	
<table width="100%">		
	<tr>
	<td ALIGN="CENTER">
		<% JSPUtils.button(out, null, null, "Visualizza compenso", "javascript:submitForm('doVisualizzaCompenso')", true, bp.getParentRoot().isBootstrap()); %>
	</td>
	</tr>
</table>
</div>
</fieldset>

<fieldset>
<legend ACCESSKEY=G TABINDEX=1 style="font-weight:bold; font-family:sans-serif; font-size:12px; color:blue">Anticipo</legend>
<div class="Panel" style="width:100%">
<table>
	<tr>	
	<td><% bp.getController().writeFormLabel( out, "pg_anticipo"); %></td>
	<td>
  		<% bp.getController().writeFormInput(out,null,"pg_anticipo", bp.areCampiAnticipoReadonly(),null,""); %>
  		<% bp.getController().writeFormInput(out,null,"find_anticipo", bp.areCampiAnticipoReadonly(),null,""); %>  		  		
  	</td>

  	<td>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;		
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		&nbsp;&nbsp;&nbsp;&nbsp;		
		<% bp.getController().writeFormLabel(out, "im_anticipo");%>
		&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
		<% bp.getController().writeFormInput(out, "im_anticipo");%>  	
  	</td>  	
	</tr>
</table>
<table width="100%">
	<tr>		
	<td>
		<% bp.getController().writeFormLabel(out, "esercizio_anticipo");%>
		&nbsp;		
		<% bp.getController().writeFormInput(out, "esercizio_anticipo");%>
	</td>
	<td>
		<% bp.getController().writeFormLabel(out, "cd_cds_anticipo");%>
		<% bp.getController().writeFormInput(out, "cd_cds_anticipo");%>
	</td>	
	<td>
		<% bp.getController().writeFormLabel(out, "cd_uo_anticipo");%>
		<% bp.getController().writeFormInput(out, "cd_uo_anticipo");%>
	</td>	
	</tr>

	<tr></tr>
	<tr></tr>
</table>
<table width="100%">		
	<tr>
	<td ALIGN="CENTER">
		<% JSPUtils.button(out, null, null, "Visualizza anticipo", "javascript:submitForm('doVisualizzaAnticipo')", true, bp.getParentRoot().isBootstrap()); %>
	</td>
	</tr>
</table>
</div>
</fieldset>