<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.docamm00.tabrif.bulk.*,
		it.cnr.contab.docamm00.intrastat.bulk.*,
		it.cnr.contab.docamm00.docs.bulk.*,
		it.cnr.contab.doccont00.core.bulk.*,
		it.cnr.contab.coepcoan00.core.bulk.*,
		it.cnr.contab.docamm00.bp.*"
%>
<%
    IDocAmmEconomicaBP bp = (IDocAmmEconomicaBP)BusinessProcess.getBusinessProcess(request);
    Scrittura_partita_doppiaBulk scrittura =
                Optional.ofNullable(bp.getModel())
                    .filter(IDocumentoCogeBulk.class::isInstance)
                    .map(IDocumentoCogeBulk.class::cast)
                    .map(IDocumentoCogeBulk::getScrittura_partita_doppia)
                    .orElse(new Scrittura_partita_doppiaBulk());
%>
<div class="btn-toolbar" role="toolbar" aria-label="Toolbar with button groups">
    <div class="btn-group mr-2" role="group">
		<% JSPUtils.button(out,
				bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-2x fa-bolt" : "img/bringback24.gif",
				bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-2x fa-bolt" : "img/bringback24.gif",
				"Genera scrittura",
				"javascript:submitForm('doGeneraScritturaEconomica')",
				"btn-outline-primary btn-title",
				true,
				bp.getParentRoot().isBootstrap()); %>
    </div>
</div>
<table class="Panel card p-2 mb-2 card-shadow" cellpadding="2">
    <tr>
        <% scrittura.writeFormField(out, "dt_contabilizzazione", FormController.VIEW, bp.getFieldValidationMap(), bp.getParentRoot().isBootstrap()); %>
        <% scrittura.writeFormField(out, "ti_istituz_commerc", FormController.VIEW, bp.getFieldValidationMap(), true); %>
        <% scrittura.writeFormField(out, "ds_scrittura", FormController.VIEW, bp.getFieldValidationMap(), bp.getParentRoot().isBootstrap()); %>
    </tr>
    <% if (Optional.ofNullable(scrittura.getPg_scrittura_annullata()).isPresent()) { %>
        <tr><% scrittura.writeFormField(out, "pg_scrittura_annullata", FormController.VIEW, bp.getFieldValidationMap(), bp.getParentRoot().isBootstrap()); %></tr>
    <% } %>
</table>
<% bp.getMovimentiDare().writeHTMLTable(pageContext, "scrittura", false, false, false,"100%","100px", true); %>
<% if (!bp.getMovimentiDare().isCollapsed() && Optional.ofNullable(bp.getMovimentiDare().getModel()).isPresent()) { %>
    <table class="Panel mt-1 p-2 card card-shadow" cellpadding="2">
        <tr>
            <td><% bp.getMovimentiDare().writeFormLabel(out, "find_voce_ep_searchtool"); %></td>
            <td colspan="7" class="w-100"><% bp.getMovimentiDare().writeFormInput(out, "find_voce_ep_searchtool"); %></td>
        </tr>
        <tr>
            <% bp.getMovimentiDare().writeFormField(out, "ti_istituz_commerc");%>
            <% bp.getMovimentiDare().writeFormField(out, "im_movimento");%>
            <td colspan="4">
                <% bp.getMovimentiDare().writeFormLabel(out, "dt_da_competenza_coge"); %>
                <% bp.getMovimentiDare().writeFormInput(out, "dt_da_competenza_coge");
                   bp.getMovimentiDare().writeFormLabel(out, "dt_a_competenza_coge");
                   bp.getMovimentiDare().writeFormInput(out, "dt_a_competenza_coge");%>
            </td>
        </tr>
        <tr>
            <% bp.getMovimentiDare().writeFormField(out, "ti_riga");%>
            <% bp.getMovimentiDare().writeFormField(out, "partita");%>
            <% bp.getMovimentiDare().writeFormField(out, "terzo_movimento");%>
            <% bp.getMovimentiDare().writeFormField(out, "cd_contributo_ritenuta");%>
        </tr>
    </table>
<% } %>
<% bp.getMovimentiDare().closeHTMLTable(pageContext);%>
<div class="mt-1">
    <% bp.getMovimentiAvere().writeHTMLTable(pageContext,"scrittura", false, false, false,"100%","100px", true); %>
    <% if (!bp.getMovimentiAvere().isCollapsed() && Optional.ofNullable(bp.getMovimentiAvere().getModel()).isPresent()) { %>
    <table class="Panel mt-1 p-2 card card-shadow" cellpadding="2">
        <tr>
            <td><% bp.getMovimentiAvere().writeFormLabel(out, "find_voce_ep_searchtool"); %></td>
            <td colspan="7" class="w-100"><% bp.getMovimentiAvere().writeFormInput(out, "find_voce_ep_searchtool"); %></td>
        </tr>
        <tr>
            <% bp.getMovimentiAvere().writeFormField(out, "ti_istituz_commerc");%>
            <% bp.getMovimentiAvere().writeFormField(out, "im_movimento");%>
            <td colspan="4">
                <% bp.getMovimentiAvere().writeFormLabel(out, "dt_da_competenza_coge"); %>
                <% bp.getMovimentiAvere().writeFormInput(out, "dt_da_competenza_coge");
                   bp.getMovimentiAvere().writeFormLabel(out, "dt_a_competenza_coge");
                   bp.getMovimentiAvere().writeFormInput(out, "dt_a_competenza_coge");%>
            </td>
        </tr>
        <tr>
            <% bp.getMovimentiAvere().writeFormField(out, "ti_riga");%>
            <% bp.getMovimentiAvere().writeFormField(out, "partita");%>
            <% bp.getMovimentiAvere().writeFormField(out, "terzo_movimento");%>
            <% bp.getMovimentiAvere().writeFormField(out, "cd_contributo_ritenuta");%>
        </tr>
    </table>
    <% } %>
    <% bp.getMovimentiAvere().closeHTMLTable(pageContext);%>
</div>