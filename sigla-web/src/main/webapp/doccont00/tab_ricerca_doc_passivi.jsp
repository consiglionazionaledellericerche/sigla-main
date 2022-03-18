<!--
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*, it.cnr.jada.util.action.*,it.cnr.contab.doccont00.bp.*"
%>


<%  
		CRUDMandatoBP bp = (CRUDMandatoBP)BusinessProcess.getBusinessProcess(request);
		it.cnr.contab.doccont00.core.bulk.MandatoIBulk mandato = (it.cnr.contab.doccont00.core.bulk.MandatoIBulk)bp.getModel();
%>
	<div class="Group card">		
	<table border="0" cellspacing="0" cellpadding="2">
		<tr>
			<td><% bp.getController().writeFormLabel( out, "find_cd_terzo"); %></td>
			<td><% bp.getController().writeFormInput( out, "find_cd_terzo"); %></td>
			<td><% bp.getController().writeFormLabel( out, "find_cd_precedente"); %></td>
			<td><% bp.getController().writeFormInput( out, "find_cd_precedente"); %></td>			
		</tr>
		<tr>
			<td><% bp.getController().writeFormLabel( out, "find_cognome"); %></td>
			<td><% bp.getController().writeFormInput( out, "find_cognome"); %></td>
			<td><% bp.getController().writeFormLabel( out, "find_nome"); %></td>
			<td><% bp.getController().writeFormInput( out, "find_nome"); %></td>			
		</tr>
		<tr>
			<td><% bp.getController().writeFormLabel( out, "find_partita_iva"); %></td>
			<td><% bp.getController().writeFormInput( out, "find_partita_iva"); %></td>
			<td><% bp.getController().writeFormLabel( out, "find_codice_fiscale"); %></td>
			<td><% bp.getController().writeFormInput( out, "find_codice_fiscale"); %></td>			
		</tr>
		<tr>
			<td><% bp.getController().writeFormLabel( out, "find_ragione_sociale"); %></td>
			<td colspan=3><% bp.getController().writeFormInput( out, "find_ragione_sociale"); %></td>			
		</tr>
		<tr>
			<td colspan=4 align="center"><% bp.getController().writeFormInput( out, "find_doc_passivi"); %></td>
		</tr>
	</table>
	</div>	
	<div class="Group card">		
	<table border="0" cellspacing="0" cellpadding="2">		
		<tr>
			<td><% bp.getController().writeFormLabel( out, "find_nr_fattura_fornitore"); %></td>
			<td><% bp.getController().writeFormInput( out, "find_nr_fattura_fornitore"); %></td>
			<td><% bp.getController().writeFormLabel( out, "find_pg_doc_passivo"); %></td>
			<td><% bp.getController().writeFormInput( out, "find_pg_doc_passivo"); %></td>
		</tr>
		<tr>
			<td><% bp.getController().writeFormLabel( out, "find_cd_tipo_documento_amm"); %></td>
			<td><% bp.getController().writeFormInput( out, "find_cd_tipo_documento_amm"); %></td>
			<td></td>
			<td></td>
		</tr>
		<tr>
			<td><% bp.getController().writeFormLabel( out, "find_pg_obbligazione"); %></td>
			<td><% bp.getController().writeFormInput( out, "find_pg_obbligazione"); %></td>
			<td><% bp.getController().writeFormLabel( out, "find_dt_scadenza"); %></td>
			<td><% bp.getController().writeFormInput( out, "find_dt_scadenza"); %></td>
		</tr>
		<tr>
			<td><% bp.getController().writeFormLabel( out, "find_im_scadenza"); %></td>
			<td><% bp.getController().writeFormInput( out, "find_im_scadenza"); %></td>
			<td><% bp.getController().writeFormLabel( out, "find_ti_pagamento"); %></td>
			<td><% bp.getController().writeFormInput( out, "find_ti_pagamento"); %></td>
		</tr>
	</table>
	</div>

	<table border="0" cellspacing="0" cellpadding="2" class="w-100">
		<tr>
			<td colspan=2 align="center">
			    <% JSPUtils.button(out,
                    bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-search" : "img/find24.gif",
                    bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-search" : "img/find24.gif",
                    bp.encodePath("Ricerca"),
                    "javascript:submitForm('doCercaDocPassivi')",
                    "btn-outline-primary btn-title",
                    bp.isEditable(),
                    bp.getParentRoot().isBootstrap()); %>
			</td>
		</tr>
	
		<tr>
			<td colspan=2>
			      <b class="h3 text-primary text-align-center d-block">Documenti passivi disponibili</b>
			      <% bp.getDocumentiPassivi().writeHTMLTable(pageContext,null,false,false,false,"100%","200px", true); %>
			</td>
		</tr>
		<tr>
			<td  colspan=2 align = "center">
			    <% JSPUtils.button(out,
			        bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-floppy-o" : "img/save24.gif",
                    bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-floppy-o" : "img/save24.gif",
                    bp.encodePath("Conferma"),
			        "javascript:submitForm('doAggiungiDocPassivi')",
			        "btn-outline-primary btn-title",
			        bp.isEditable(),
			        bp.getParentRoot().isBootstrap()); %>
			</td>
		</tr>
	</table>
