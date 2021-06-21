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
                    .filter(IDocumentoAmministrativoBulk.class::isInstance)
                    .map(IDocumentoAmministrativoBulk.class::cast)
                    .map(IDocumentoAmministrativoBulk::getScrittura_partita_doppia)
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
<table class="Panel card p-2 mb-2 card-shadow">
    <tr><% scrittura.writeFormField(out, "dt_contabilizzazione", FormController.VIEW, bp.getFieldValidationMap(), bp.getParentRoot().isBootstrap()); %></tr>
    <tr><% scrittura.writeFormField(out, "ds_scrittura", FormController.VIEW, bp.getFieldValidationMap(), bp.getParentRoot().isBootstrap()); %></tr>
    <tr><% scrittura.writeFormField(out, "attiva", FormController.VIEW, bp.getFieldValidationMap(), bp.getParentRoot().isBootstrap()); %></tr>
    <tr><% scrittura.writeFormField(out, "pg_scrittura_annullata", FormController.VIEW, bp.getFieldValidationMap(), bp.getParentRoot().isBootstrap()); %></tr>
</table>
<% bp.getMovimentiDare().writeHTMLTable(pageContext, "scrittura", false, false, false,"100%","100px", true); %>
<% if (!bp.getMovimentiDare().isCollapsed()) { %>
    <table class="Panel mt-1 p-2 card card-shadow">
        <tr><% bp.getMovimentiDare().writeFormField(out, "find_voce_ep_searchtool"); %></tr>
        <tr>
            <td><% bp.getMovimentiDare().writeFormLabel(out, "ti_istituz_commerc");%></td>
            <td><% bp.getMovimentiDare().writeFormInput(out, "ti_istituz_commerc");%></td>
        </tr>
        <tr>
            <td><% bp.getMovimentiDare().writeFormLabel(out, "dt_da_competenza_coge"); %></td>
            <td><% bp.getMovimentiDare().writeFormInput(out, "dt_da_competenza_coge");
                   bp.getMovimentiDare().writeFormLabel(out, "dt_a_competenza_coge");
                   bp.getMovimentiDare().writeFormInput(out, "dt_a_competenza_coge");%></td>
        </tr>
        <tr>
            <td><% bp.getMovimentiDare().writeFormLabel(out, "im_movimento"); %></td>
            <td><% bp.getMovimentiDare().writeFormInput(out, "im_movimento");%></td>
        </tr>
    </table>
<% } %>
<% bp.getMovimentiDare().closeHTMLTable(pageContext);%>
<div class="mt-1">
    <% bp.getMovimentiAvere().writeHTMLTable(pageContext,"scrittura", false, false, false,"100%","100px", true); %>
    <% if (!bp.getMovimentiAvere().isCollapsed()) { %>
    <table class="Panel mt-1 p-2 card card-shadow">
        <tr><% bp.getMovimentiAvere().writeFormField(out, "find_voce_ep_searchtool"); %></tr>
        <tr>
            <td><% bp.getMovimentiAvere().writeFormLabel(out, "ti_istituz_commerc");%></td>
            <td><% bp.getMovimentiAvere().writeFormInput(out, "ti_istituz_commerc");%></td>
        </tr>
        <tr>
            <td><% bp.getMovimentiAvere().writeFormLabel(out, "dt_da_competenza_coge"); %></td>
            <td><% bp.getMovimentiAvere().writeFormInput(out, "dt_da_competenza_coge");
                   bp.getMovimentiAvere().writeFormLabel(out, "dt_a_competenza_coge");
                   bp.getMovimentiAvere().writeFormInput(out, "dt_a_competenza_coge");%></td>
        </tr>
        <tr>
            <td><% bp.getMovimentiAvere().writeFormLabel(out, "im_movimento"); %></td>
            <td><% bp.getMovimentiAvere().writeFormInput(out, "im_movimento");%></td>
        </tr>
    </table>
    <% } %>
    <% bp.getMovimentiAvere().closeHTMLTable(pageContext);%>
</div>