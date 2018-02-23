<!-- 
 ?ResourceName ""
 ?ResourceTimestamp ""
 ?ResourceEdition ""
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.anagraf00.bp.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.anagraf00.core.bulk.*"
%>


<%
  CRUDAnagraficaBP bp = (CRUDAnagraficaBP)BusinessProcess.getBusinessProcess(request);  
  AnagraficoBulk anagrafico = (AnagraficoBulk)bp.getModel();
%>
	
<%if( !((AnagraficoBulk)bp.getModel()).isStrutturaCNR() ) { %>
	<%	bp.getCrudCarichi_familiari_anag().writeHTMLTable(pageContext,null,(!anagrafico.isDipendente()),false,(!anagrafico.isDipendente()),"100%","150px"); %>
	
	<table>
		<tr>
			<% bp.getCrudCarichi_familiari_anag().writeFormField(out,"ti_persona");%>
			<% bp.getCrudCarichi_familiari_anag().writeFormField(out,"pg_carico_anag");%>
		</tr>
		<tr>
			<% bp.getCrudCarichi_familiari_anag().writeFormField(out,"codice_fiscale");%>
		</tr>
		<tr>
			<% bp.getCrudCarichi_familiari_anag().writeFormField(out,"dt_ini_validita");%>
			<% bp.getCrudCarichi_familiari_anag().writeFormField(out,"dt_fin_validita");%>
		</tr>
		<tr>
			<td><% bp.getCrudCarichi_familiari_anag().writeFormLabel(out,"prc_carico"); %></td>
			<!-- <td><% bp.getCrudCarichi_familiari_anag().writeFormInput(out,null,"prc_carico",bp.getCrudCarichi_familiari_anag().getModel() != null && ((Carico_familiare_anagBulk)bp.getCrudCarichi_familiari_anag().getModel()).isConiuge(),null,null);%></td> --> 
			<td><% bp.getCrudCarichi_familiari_anag().writeFormInput(out,"prc_carico");%></td>
		</tr>
<% if(bp.getCrudCarichi_familiari_anag().getModel() != null &&
	((Carico_familiare_anagBulk)bp.getCrudCarichi_familiari_anag().getModel()).isFiglio() ) {%>
		<tr>
			<% bp.getCrudCarichi_familiari_anag().writeFormField(out,"dt_nascita_figlio");%>
		</tr>
		<tr>
			<% bp.getCrudCarichi_familiari_anag().writeFormField(out,"dt_fine_figlio_ha_treanni");%>
		</tr>
		<tr>
			<% bp.getCrudCarichi_familiari_anag().writeFormField(out,"fl_handicap");%>
			<% bp.getCrudCarichi_familiari_anag().writeFormField(out,"fl_primo_figlio");%>
			<td><% bp.getCrudCarichi_familiari_anag().writeFormLabel(out,"fl_primo_figlio_manca_con"); %>
				<% bp.getCrudCarichi_familiari_anag().writeFormInput(out,null,"fl_primo_figlio_manca_con",false,null,"onClick=\"submitForm('doClickFlagFigliosenza')\"");%></td>
		</tr>
		<tr>
			<% bp.getCrudCarichi_familiari_anag().writeFormField(out,"codice_fiscale_altro_gen");%>
		</tr>
<%}%>
	</table>
<%}%>