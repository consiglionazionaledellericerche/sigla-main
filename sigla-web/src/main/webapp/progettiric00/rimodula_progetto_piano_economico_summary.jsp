<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.progettiric00.bp.*,
		it.cnr.contab.progettiric00.core.bulk.*"
%>
<%
	RimodulaProgettiRicercaBP bp = (RimodulaProgettiRicercaBP)BusinessProcess.getBusinessProcess(request);
	SimpleDetailCRUDController controllerVoce = bp.getPianoEconomicoSummaryVoce();
	SimpleDetailCRUDController controllerAnno = bp.getPianoEconomicoSummaryAnno();
%>
<div class="GroupLabel h3 text-primary">Totali per Voce Piano Economico</div>
<div class="Group card">
    <table class="Panel border-info p-2">
        <tr>
            <td><%controllerVoce.writeHTMLTable(pageContext,"piano_economico1_rimodulato",false,false,false,"100%","160px"); %></td>
        </tr>
    </table>
</div>
</br>
<div class="GroupLabel h3 text-primary">Totali per Esercizio</div>
<div class="Group card">
    <table class="Panel border-info p-2 w-100" style="width:100%">
        <tr>
            <td><%controllerAnno.writeHTMLTable(pageContext,"piano_economico2_rimodulato",false,false,false,"100%","160px"); %></td>
        </tr>
    </table>
</div>