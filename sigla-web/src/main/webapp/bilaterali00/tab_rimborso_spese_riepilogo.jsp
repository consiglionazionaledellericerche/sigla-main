<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
	        it.cnr.jada.util.jsp.*,
	        it.cnr.contab.anagraf00.tabrif.bulk.Rif_modalita_pagamentoBulk,
	        it.cnr.contab.bilaterali00.bulk.Blt_visiteBulk,
	        it.cnr.contab.bilaterali00.bp.CRUDBltVisiteBP"%>
<%
	CRUDBltVisiteBP bp = (CRUDBltVisiteBP) BusinessProcess
			.getBusinessProcess(request);
	Blt_visiteBulk model = (Blt_visiteBulk) bp.getModel();
%>
<fieldset class="fieldset">
<legend class="GroupLabel">Riepilogo Pagamenti</legend>
<table>
	<tr>
  		<td>&nbsp;</td>
	    <td align="center"><span class="FormLabel">Importo</span></td>
		<% if (!model.isConvenzioneAccettata()) {%>
			<td>&nbsp;</td>
		    <td align="center"><span class="FormLabel">Ritenute</span></td>
		<% } %>
		<td>&nbsp;</td>
	    <td align="center"><span class="FormLabel">Contributi</span></td>
		<td>&nbsp;</td>
	    <td align="center"><span class="FormLabel">Totale Pagamento</span></td>
	</tr>

<% if (model.isAnticipoPrevisto()) { %>
	<tr>         
    	<td><span class="FormLabel">Anticipo</span></td>
	    <td><% bp.getController().writeFormInput(out,"imRimbSpeseAntRO");%></td>
		<% if (!model.isConvenzioneAccettata()) {%>
			<td>+</td>
		    <td><% bp.getController().writeFormInput(out,"imRitenutaFiscaleAnticipo");%></td>
		<% } %>
		<td>+</td>
		<td><% bp.getController().writeFormInput(out,"imContributiAnticipo");%></td>
		<td>=</td>
		<td><% bp.getController().writeFormInput(out,"imRimborsoTotaleAnticipo");%></td>
	</tr>
<% } %>

	<tr>         
    	<td><span class="FormLabel">Saldo</span></td>
	    <td><% bp.getController().writeFormInput(out,"imRimbSpeseRO");%></td>
		<% if (!model.isConvenzioneAccettata()) {%>
			<td>+</td>
		    <td><% bp.getController().writeFormInput(out,"imRitenutaFiscaleSaldo");%></td>
		<% } %>
		<td>+</td>
		<td><% bp.getController().writeFormInput(out,"imContributiSaldo");%></td>
		<td>=</td>
		<td><% bp.getController().writeFormInput(out,"imRimborsoTotaleSaldo");%></td>
	</tr>

	<tr>         
    	<td><span class="FormLabel">Totale</span></td>
	    <td><% bp.getController().writeFormInput(out,"imRimborsoTotaleNetto");%></td>
		<% if (!model.isConvenzioneAccettata()) {%>
			<td>-</td>
		    <td><% bp.getController().writeFormInput(out,"imRitenutaFiscaleTotale");%></td>
		<% } %>
		<td>+</td>
		<td><% bp.getController().writeFormInput(out,"imContributiTotale");%></td>
		<td>=</td>
		<td><% bp.getController().writeFormInput(out,"imRimborsoTotale");%></td>
	</tr>
</table>
</fieldset>
