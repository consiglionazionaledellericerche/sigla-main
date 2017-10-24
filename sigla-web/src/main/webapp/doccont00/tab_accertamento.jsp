<%@page import="it.cnr.contab.doccont00.core.bulk.AccertamentoResiduoBulk"%>
<%@ page 
	import="it.cnr.jada.util.jsp.*,
	it.cnr.jada.action.*,
	java.util.*,
	it.cnr.jada.util.action.*,
    it.cnr.contab.doccont00.bp.*"
%>

<%  
	CRUDAccertamentoBP bp = (CRUDAccertamentoBP)BusinessProcess.getBusinessProcess(request);
	it.cnr.contab.doccont00.core.bulk.AccertamentoBulk accertamento = (it.cnr.contab.doccont00.core.bulk.AccertamentoBulk)bp.getModel();
%>

	<table border="0" cellspacing="0" cellpadding="2">
	
	<tr>
<!--
	<td>	<% bp.getController().writeFormLabel( out, "esercizio_competenza"); %></td>
	<td>	<% bp.getController().writeFormInput(out,"default","esercizio_competenza", bp.isFromDocAmm(),null,null); %>
-->	
	<td><% bp.getController().writeFormLabel( out, "esercizio_originale"); %></td>
	<td><% bp.getController().writeFormInput( out,"default","esercizio_originale",false,null,null); %>
	  		<% if ( !accertamento.isResiduo()) 
	  			{ 
			 		bp.getController().writeFormLabel( out, "dt_cancellazione"); 
	 		   	bp.getController().writeFormInput( out, "dt_cancellazione"); 
	 		   }	%>
	</td>		
	<td align="right">
		<%if (bp instanceof CRUDAccertamentoResiduoBP && ((CRUDAccertamentoResiduoBP)bp).isFlagAutomaticoEnabledOnView()) { %>
	        <% bp.getController().writeFormInput(out,"default","fl_calcolo_automatico_eov",false,null,"onclick=\"submitForm('doCambiaFl_calcolo_automatico')\""); %>
  	        <% bp.getController().writeFormLabel( out, "fl_calcolo_automatico_eov"); %></td>
		<% } else { %>
		<td align="right">
	        <% bp.getController().writeFormInput(out,"default","fl_calcolo_automatico",false,null,"onclick=\"submitForm('doCambiaFl_calcolo_automatico')\""); %>
  	        <% bp.getController().writeFormLabel( out, "fl_calcolo_automatico"); %></td>
		<% } %>
	</tr>
	
	<tr>
	<td>	<% bp.getController().writeFormLabel( out, "cd_unita_organizzativa"); %></td>
	<td colspan=2>	
	        <% bp.getController().writeFormInput( out, "cd_unita_organizzativa"); %>
			<% bp.getController().writeFormInput( out, "ds_unita_organizzativa"); %></td>				 
	</tr>
	
	<tr>
<%  if( bp instanceof CRUDAccertamentoResiduoBP && bp.isSearching() ) { %>
		<td>			<% bp.getController().writeFormLabel( out, "pg_accertamento_search"); %></td>	
		<td colspan=2>  <% bp.getController().writeFormInput( out, "pg_accertamento_search"); %>
<%  } else { %>
		<td>	<% bp.getController().writeFormLabel( out, "pg_accertamento"); %></td>	
		<td colspan=2>  <% bp.getController().writeFormInput( out, "pg_accertamento"); %>
<%  } %>

				<% bp.getController().writeFormLabel( out, "dt_registrazione"); %>
		  		<% bp.getController().writeFormInput(out,null,"dt_registrazione",false,null,"onChange=\"javascript:submitForm('doValidaDataRegistrazione')\"");%>

<%  if( bp instanceof CRUDAccertamentoResiduoBP ) { %>
	  		<% bp.getController().writeFormLabel( out, "pg_accertamento_ori_riporto"); %>
	  		<% bp.getController().writeFormInput( out, "pg_accertamento_ori_riporto"); %>
	  		
<%  } %>

		</td>
	</tr>
	<%  if( bp instanceof CRUDAccertamentoResiduoBP  && ((CRUDAccertamentoResiduoBP)bp).isStatoVisibile()) { %>
		<tr>
			<td><% bp.getController().writeFormLabel( out, ((CRUDAccertamentoResiduoBP)bp).isROStato() ?"statoAccertamento_ro":"statoAccertamento"); %></td>
			<td colspan=2>
			<% bp.getController().writeFormInput( out, ((CRUDAccertamentoResiduoBP)bp).isROStato() ?"statoAccertamento_ro":"statoAccertamento"); %>
			<% if (((AccertamentoResiduoBulk)accertamento).isParzialmenteInesigibile() || ((AccertamentoResiduoBulk)accertamento).isInesigibile()) {%>
				<% bp.getController().writeFormLabel( out, ((CRUDAccertamentoResiduoBP)bp).isROStato() || ((AccertamentoResiduoBulk)accertamento).isInesigibile()?"im_quota_inesigibile_ro":"im_quota_inesigibile"); %>
				<% bp.getController().writeFormInput( out, ((CRUDAccertamentoResiduoBP)bp).isROStato() || ((AccertamentoResiduoBulk)accertamento).isInesigibile()?"im_quota_inesigibile_ro":"im_quota_inesigibile"); %>
			<% } %>
			</td>
		</tr>		  		
	<%  } %>
	<tr>
	<td>	<% bp.getController().writeFormLabel( out, "ds_accertamento"); %></td>
	<td colspan=2> <% bp.getController().writeFormInput( out, "ds_accertamento"); %></td>
	</tr>
	</table>
	<div class="GroupLabel">Repertorio Contratti</div>  
	<div class="Group">
	<table>
	  <tr>
		<%if (bp instanceof CRUDAccertamentoResiduoBP && ((CRUDAccertamentoResiduoBP)bp).isContrattoEnabledOnView()) { %>
	        <td><% bp.getController().writeFormLabel( out, "find_contratto_eov"); %></td>
		    <td colspan=2>
		        <% bp.getController().writeFormInput( out, "esercizio_contratto_eov"); %>
   			    <% bp.getController().writeFormInput( out, "pg_contratto_eov"); %>
 			    <% bp.getController().writeFormInput( out, "oggetto_contratto"); %>
			    <% bp.getController().writeFormInput( out, "find_contratto_eov"); %>
			    <% bp.getController().writeFormInput( out, "crea_contratto_eov"); %>
		    </td>
		<% } else { %>
	        <td><% bp.getController().writeFormLabel( out, "find_contratto"); %></td>
		    <td colspan=2>
		        <% bp.getController().writeFormInput( out, "esercizio_contratto"); %>
   			    <% bp.getController().writeFormInput( out, "pg_contratto"); %>
 			    <% bp.getController().writeFormInput( out, "oggetto_contratto"); %>
			    <% bp.getController().writeFormInput( out, "find_contratto"); %>
			    <% bp.getController().writeFormInput( out, "crea_contratto"); %>
		    </td>
		<% } %>
	  </tr>
	</table>	
    </div>	  	
	<div class="Group">
	<table>
	<tr>
	<td>	<% bp.getController().writeFormLabel( out, "cd_terzo"); %></td>
	<td>	<% bp.getController().writeFormInput( out, "cd_terzo"); %>
			<% bp.getController().writeFormInput( out, "ds_debitore"); %>
			<% bp.getController().writeFormInput( out, "find_debitore"); %>
			<% bp.getController().writeFormInput( out, "crea_debitore" ); %></td>
	</tr>
	<tr>
	<td>	<% bp.getController().writeFormLabel( out, "cd_terzo_precedente"); %></td>
	<td>	<% bp.getController().writeFormInput( out, "cd_terzo_precedente"); %></td>
	</tr>

	<tr>
	<td>	<% bp.getController().writeFormLabel( out, "codice_fiscale"); %></td>
	<td>	<% bp.getController().writeFormInput( out, "codice_fiscale"); %>
	        <% bp.getController().writeFormLabel( out, "partita_iva"); %>
	        <% bp.getController().writeFormInput( out, "partita_iva"); %>	        
	</td>
	</tr>
	</table>
    </div>
	<table>
	<tr>
  	<% if (bp instanceof CRUDAccertamentoResiduoBP){ %>
  		<% if (((CRUDAccertamentoResiduoBP)bp).isROImporto()){ %>
			<td><% bp.getController().writeFormLabel( out, "im_accertamento_ro" ); %></td>
			<td><% bp.getController().writeFormInput( out, "im_accertamento_ro" ); %></td>
	  	<%} else {%>
			<td><% bp.getController().writeFormLabel( out, "im_accertamento" ); %></td>
			<td><% bp.getController().writeFormInput( out, "im_accertamento" ); %></td>
	  	<%}%>
  	<%} else {%>
		<td><% bp.getController().writeFormLabel( out, "im_accertamento"); %></td>
		<td><% bp.getController().writeFormInput( out, "im_accertamento"); %></td>
  	<%}%>
	</tr>
	
	<tr>
	<td>	<% bp.getController().writeFormLabel( out, "cd_voce"); %></td>
	<td>	<% bp.getController().writeFormInput( out, "default","cd_voce", bp.isROCapitolo(),null,null); %>
			<% bp.getController().writeFormInput( out, "default","ds_voce", bp.isROCapitolo(),null,null); %>
			<% bp.getController().writeFormInput( out, "default","find_capitolo", bp.isROFindCapitolo(),null,null); %></td>				 
	</tr>
 	
 	<% if (bp.isElementoVoceNewVisible()){ %>
	<tr>
		<td colspan="3">
			<div class="Group" style="border-color:red">
			<table>
			<tr>
				<td><% bp.getController().writeFormLabel( out, "cd_elemento_voce_next"); %><label> <%=Integer.valueOf(accertamento.getEsercizio()+1).toString()%></label></td>
				<td colspan=2>
				    <% bp.getController().writeFormInput(out,"default","cd_elemento_voce_next"); %>
				    <% bp.getController().writeFormInput(out,"default","ds_elemento_voce_next"); %>
				    <% bp.getController().writeFormInput(out,"default","find_elemento_voce_next"); %>
				</td>				 
			</tr>
			</table>
			</div>
		</td>
	</tr>
    <%}%>
	<tr>
	
	
	<tr>
	<td>	<% bp.getController().writeFormLabel( out, "cd_riferimento_contratto"); %></td>
	<td>	<% bp.getController().writeFormInput( out, "cd_riferimento_contratto"); %>	
			<% bp.getController().writeFormLabel( out, "dt_scadenza_contratto"); %>
			<% bp.getController().writeFormInput( out, "dt_scadenza_contratto"); %></td>
	</tr>

  </table>