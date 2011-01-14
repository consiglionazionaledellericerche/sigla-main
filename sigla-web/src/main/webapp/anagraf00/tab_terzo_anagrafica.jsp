<%@ page 
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.anagraf00.bp.*,
		it.cnr.contab.anagraf00.core.bulk.*"
%>

<%
	CRUDTerzoBP bp = (CRUDTerzoBP)BusinessProcess.getBusinessProcess(request);
%>

<table>
<% if(bp.getAnagrafico() == null || !bp.getAnagrafico().isStrutturaCNR() ) { %>
<tr>
  <td><%bp.writeFormLabel(out,"ti_terzo");%></td>
  <td><%bp.writeFormInput(out,"ti_terzo");%></td>
</tr>
<%}%>
<tr>
  <td><% bp.writeFormLabel(out,"default","denominazione_sede"); %></td>
  <td><% bp.writeFormInput(out,"default","denominazione_sede"); %></td>
</tr>
<% if(bp.getAnagrafico() != null && bp.getAnagrafico().isStrutturaCNR() ) { %>
<tr>
	<td><% bp.writeFormLabel(out,"ds_unita_organizzativa");%></td>
	<td><% bp.writeFormInput(out,null,"cd_unita_org",!bp.isInserting(),null,null);%>
		<% bp.writeFormInput(out,"ds_unita_organizzativa");%>
		<% bp.writeFormInput(out,null,"find_unita_organizzativa",!bp.isInserting(),null,null);%></td>
</tr>
<% }%>
<tr>
  <td><% bp.writeFormLabel(out,"default","cd_precedente"); %></td>
  <td><% bp.writeFormInput(out,"default","cd_precedente"); %></td>
</tr>
<% if(bp.getAnagrafico() == null || !bp.getAnagrafico().isStrutturaCNR() ) { %>
<tr>
	<td>
		<% bp.writeFormLabel(out,"nome_rapp_legale");%>
	</td>
	<td>
		<% bp.writeFormInput(out,"cognome_rapp_legale");%>
		<% bp.writeFormInput(out,"nome_rapp_legale");%>
		<% bp.writeFormInput(out,"find_rapp_legale");%>
	</td>
</tr>
<% }%>
<tr>
	<% bp.writeFormField(out,"note");%>
</tr>
<tr>
<%	if(bp.getAnagrafico() != null && bp.getAnagrafico().isStrutturaCNR() )
		bp.writeFormField(out,"dt_fine_validita");
	else
		bp.writeFormField(out,"dt_fine_rapporto");
%>
</tr>
</table>

<fieldset>
<legend class="GroupLabel">Indirizzo sede</legend>
<table>
<tr>
	<td><% bp.writeFormLabel(out,"comune_sede");%></td>
	<td><% bp.writeFormInput(out,"comune_sede");%>
		<% bp.writeFormInput(out,"find_comune_sede");%></td>
	<% bp.writeFormField(out,"cap_comune_sede");%>
</tr>
<tr>
	<% bp.writeFormField(out,"frazione_sede");%>
	<% bp.writeFormField(out,"ds_provincia_sede");%>
</tr>
<tr>
	<% bp.writeFormField(out,"via_sede");%>
	<% bp.writeFormField(out,"numero_civico_sede");%>
</tr>
<tr>
	<% bp.writeFormField(out,"ds_nazione_sede");%>
	<% bp.writeFormField(out,"codice_iso_sede");%>
</tr>
</table>
</fieldset>