<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
	        it.cnr.jada.action.*,
	        java.util.*,
	        it.cnr.jada.util.action.*,
	        it.cnr.contab.coepcoan00.bp.*,
	        it.cnr.contab.coepcoan00.core.bulk.*,
	        it.cnr.contab.docamm00.bp.*"
%>
<%IDocAmmEconomicaBP bp = (IDocAmmEconomicaBP)BusinessProcess.getBusinessProcess(request);%>

<table class="Panel">
    <tr>
        <td><% bp.getMovimentiDare().writeHTMLTable(pageContext, "scrittura", false, false, false,"100%","100px", true); %></td>
    </tr>
</table>
<table class="Panel p-2 card card-shadow">
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