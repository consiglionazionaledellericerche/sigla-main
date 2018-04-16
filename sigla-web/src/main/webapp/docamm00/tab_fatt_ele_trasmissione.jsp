<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.docamm00.tabrif.bulk.*,
		it.cnr.jada.*,
		it.cnr.contab.docamm00.docs.bulk.*,
		it.cnr.contab.docamm00.bp.*,
		it.cnr.contab.docamm00.fatturapa.bulk.*"
%>
<%	
	CRUDFatturaPassivaElettronicaBP bp = (CRUDFatturaPassivaElettronicaBP)BusinessProcess.getBusinessProcess(request);
%>
<table class="Panel">
	<tr>
		<% bp.writeFormField(out,"dataRicezioneSDI");%>
		<% bp.writeFormFieldTrasmissione(out,"soggettoEmittente");%>
	</tr>
	<tr>
		<td><% bp.writeFormLabelTrasmissione(out,"regimefiscale");%></td>
		<td colspan="3"><% bp.writeFormInputTrasmissione(out,"regimefiscale");%></td>				
	</tr>
</table>
<div class="card mt-2">
    <fieldset>
    <legend class="GroupLabel card-header text-primary">Dati del cedente / prestatore</legend>
    <table class="Panel card-block">
        <tr><% bp.writeFormFieldTrasmissione(out,"prestatoreCodice");%></tr>
        <tr><% bp.writeFormFieldTrasmissione(out,"prestatoreCodicefiscale");%></tr>
        <tr><% bp.writeFormFieldTrasmissione(out,"prestatoreDenominazione");%></tr>
        <tr><% bp.writeFormFieldTrasmissione(out,"prestatoreNome");%></tr>
        <tr><% bp.writeFormFieldTrasmissione(out,"prestatoreCognome");%></tr>
        <tr>
            <td><% bp.writeFormLabelTrasmissione(out,"prestatoreIndirizzo");%></td>
            <td>
            <% bp.writeFormInputTrasmissione(out,"prestatoreIndirizzo");%>
            <% bp.writeFormInputTrasmissione(out,"prestatoreNumerocivico");%>
            <% bp.writeFormLabelTrasmissione(out,"prestatoreCap");%>
            <% bp.writeFormInputTrasmissione(out,"prestatoreCap");%>
            </td>
        </tr>
        <tr>
            <td><% bp.writeFormLabelTrasmissione(out,"prestatoreComune");%></td>
            <td>
            <% bp.writeFormInputTrasmissione(out,"prestatoreComune");%>
            <% bp.writeFormInputTrasmissione(out,"prestatoreProvincia");%>
            <% bp.writeFormLabelTrasmissione(out,"prestatoreNazione");%>
            <% bp.writeFormInputTrasmissione(out,"prestatoreNazione");%>
            </td>
        </tr>
        <tr><% bp.writeFormField(out,"prestatore");%></tr>
        <tr><% bp.writeFormField(out,"prestatoreAnag");%></tr>
    </table>
    </fieldset>
</div>
<div class="card mt-2">
    <fieldset>
    <legend class="GroupLabel card-header text-primary">Dati del cessionario / committente</legend>
    <table class="Panel card-block">
        <tr><% bp.writeFormFieldTrasmissione(out,"committenteCodice");%></tr>
        <tr><% bp.writeFormFieldTrasmissione(out,"committenteCodicefiscale");%></tr>
        <tr><% bp.writeFormFieldTrasmissione(out,"committenteDenominazione");%></tr>
        <tr>
            <td><% bp.writeFormLabelTrasmissione(out,"committenteIndirizzo");%></td>
            <td>
            <% bp.writeFormInputTrasmissione(out,"committenteIndirizzo");%>
            <% bp.writeFormInputTrasmissione(out,"committenteNumerocivico");%>
            <% bp.writeFormLabelTrasmissione(out,"committenteCap");%>
            <% bp.writeFormInputTrasmissione(out,"committenteCap");%>
            </td>
        </tr>
        <tr>
            <td><% bp.writeFormLabelTrasmissione(out,"committenteComune");%></td>
            <td>
            <% bp.writeFormInputTrasmissione(out,"committenteComune");%>
            <% bp.writeFormInputTrasmissione(out,"committenteProvincia");%>
            <% bp.writeFormLabelTrasmissione(out,"committenteNazione");%>
            <% bp.writeFormInputTrasmissione(out,"committenteNazione");%>
            </td>
        </tr>
    </table>
    </fieldset>
</div>
<% if (bp.isSearching() || ((DocumentoEleTestataBulk)bp.getModel()).getDocumentoEleTrasmissione().getIntermediarioCodice() != null) {%>
<div class="card mt-2">
    <fieldset>
    <legend class="GroupLabel card-header text-primary">Dati del terzo intermediario soggetto emittente</legend>
    <table class="Panel card-block">
        <tr><% bp.writeFormFieldTrasmissione(out,"intermediarioCodice");%></tr>
        <tr><% bp.writeFormFieldTrasmissione(out,"intermediarioCodicefiscale");%></tr>
        <tr><% bp.writeFormFieldTrasmissione(out,"intermediarioDenominazione");%></tr>
        <tr><% bp.writeFormFieldTrasmissione(out,"intermediarioNome");%></tr>
        <tr><% bp.writeFormFieldTrasmissione(out,"intermediarioCognome");%></tr>
        <tr><% bp.writeFormField(out,"intermediario");%></tr>
        <tr><% bp.writeFormField(out,"intermediarioAnag");%></tr>
    </table>
    </fieldset>
</div>
<% } %>
<% if (bp.isSearching() || ((DocumentoEleTestataBulk)bp.getModel()).getDocumentoEleTrasmissione().getAnomalieRicezione() != null) {%>
<table>
	<tr><% bp.writeFormFieldTrasmissione(out,"anomalieRicezione");%></tr>
</table>
<% } %>