<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.compensi00.bp.*,
		it.cnr.contab.compensi00.docs.bulk.*,
		it.cnr.contab.anagraf00.tabrif.bulk.*"
%>

<%	CRUDMinicarrieraBP bp = (CRUDMinicarrieraBP)BusinessProcess.getBusinessProcess(request);
	MinicarrieraBulk carriera = (MinicarrieraBulk)bp.getModel();
	it.cnr.contab.compensi00.docs.bulk.V_terzo_per_compensoBulk percipiente = carriera.getPercipiente();
%>

<div class="Group" style="width:100%">
<table width="100%">
	<tr>
		<td colspan=4>
			<% bp.getController().writeFormInput(
									out,
									null,
									(bp.isSearching()) ? "ti_anagraficoForSearch" : "ti_anagrafico",
									false,
									null,
									"onClick=\"submitForm('doOnTipoAnagraficoChange')\""); %>
		</td>
	</tr>
	<tr>
		<td><% bp.getController().writeFormLabel(out,"cd_terzo"); %></td>
		<td><% bp.getController().writeFormInput(out,"cd_terzo"); %>
			<% bp.getController().writeFormInput(out,"find_percipiente"); %></td>
	</tr>
	<tr>
		<% bp.getController().writeFormField(out,"cd_precedente"); %>
	</tr>
	<tr>
		<% bp.getController().writeFormField(out,"nome"); %>
		<% bp.getController().writeFormField(out,"cognome"); %>
	</tr>
	<tr>
		<td><% bp.getController().writeFormLabel(out,"ragione_sociale"); %></td>
		<td colspan=3><% bp.getController().writeFormInput(out,"ragione_sociale"); %></td>
	</tr>
	<tr>
		<td><% bp.getController().writeFormLabel(out,"indirizzoPercipiente");%></td>
		<td colspan=3><% bp.getController().writeFormInput(out,"indirizzoPercipiente");%></td>
	</tr>
	<tr>
		<% bp.getController().writeFormField(out,"ds_comune");%>
		<% bp.getController().writeFormField(out,"ds_provincia");%>
	</tr>
	<tr>
		<% bp.getController().writeFormField(out,"codice_fiscale"); %>
		<% bp.getController().writeFormField(out,"partita_iva"); %>
	</tr>
</table>
<table class="Panel" width="100%">
	<tr>
		<td>
			<%
				String [][] defaultPages = new String[][] {
							{ "tabMinicarrieraPercipientePagamenti","Pagamenti","/compensi00/tab_minicarriera_percipiente_pagamenti.jsp" },
							{ "tabMinicarrieraPercipienteTipologie","Tipologie","/compensi00/tab_minicarriera_percipiente_tipologie.jsp" }
						};

				JSPUtils.tabbed(
								pageContext,
								"subtab",
								defaultPages,
								bp.getTab("subtab"),
								"center",
								"100%",
								null );
			%>
		</td>
	</tr>
</table>
</div>