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
<table class="Panel card p-2 mb-2 card-shadow">
    <tr><% scrittura.writeFormField(out, "dt_contabilizzazione", FormController.VIEW, bp.getFieldValidationMap(), bp.getParentRoot().isBootstrap()); %></tr>
    <tr><% scrittura.writeFormField(out, "ds_scrittura", FormController.VIEW, bp.getFieldValidationMap(), bp.getParentRoot().isBootstrap()); %></tr>
    <tr><% scrittura.writeFormField(out, "attiva", FormController.VIEW, bp.getFieldValidationMap(), bp.getParentRoot().isBootstrap()); %></tr>
    <tr><% scrittura.writeFormField(out, "pg_scrittura_annullata", FormController.VIEW, bp.getFieldValidationMap(), bp.getParentRoot().isBootstrap()); %></tr>
</table>
<%
    JSPUtils.tabbed(
        pageContext,
        "tabEconomica",
        new String[][] {
                { "tabDare","Dare","/coepcoan00/tab_docamm_dare.jsp" },
                { "tabAvere","Avere","/coepcoan00/tab_docamm_avere.jsp" }
        },
        bp.getTab("tabEconomica"),
        "center",
        "100%",
        null
    );
%>