<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.docamm00.tabrif.bulk.*,
		it.cnr.contab.docamm00.docs.bulk.*,
		it.cnr.contab.doccont00.core.bulk.*,
		it.cnr.contab.docamm00.bp.*"
%>

<%	CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP)BusinessProcess.getBusinessProcess(request);
	Fattura_passivaBulk fatturaPassiva = (Fattura_passivaBulk)bp.getModel();
	String collapseIconClass = bp.getFatturaOrdiniController().isRettificheCollapse() ? "fa-chevron-circle-down" : "fa-chevron-circle-up";
%>

<% bp.getCrudDocEleAcquistoColl().writeHTMLTable(pageContext,"default",false,false,false,"100%","200px"); %>
<% bp.getCrudDocEleAcquistoColl().closeHTMLTable(pageContext);%>
<div class="card">
    <fieldset class="fieldset mt-1 mb-1">
        <legend class="GroupLabel card-header text-primary p-0 pl-2">Righe di consegna selezionate</legend>
        <table width="100%">
            <tr>
                <td>
                    <% bp.getFatturaOrdiniController().writeHTMLTable(pageContext,"default",false,false,true,"100%","400px"); %>
                </td>
            </tr>
        </table>
    </fieldset>
</div>
<div class="card border-primary">
    <div class="card-header">
        <h5 class="mb-0">
            <a onclick="submitForm('doToggle(ordiniRettifiche)')" class="text-primary"><i aria-hidden="true" class="fa <%=collapseIconClass%>"></i> Rettifiche</a>
        </h5>
    </div>
    <div class="card-block">
        <% if (!bp.getFatturaOrdiniController().isRettificheCollapse()) { %>
            <table cellpadding="5px">
                <tr>
                    <% bp.getFatturaOrdiniController().writeFormField(out, "voceIva"); %>
                    <% bp.getFatturaOrdiniController().writeFormField(out, "prezzoUnitarioRett"); %>
                </tr>
            </table>
            <table cellpadding="5">
                <tr>
                    <% bp.getFatturaOrdiniController().writeFormField(out, "sconto1Rett"); %>
                    <% bp.getFatturaOrdiniController().writeFormField(out, "sconto2Rett"); %>
                    <% bp.getFatturaOrdiniController().writeFormField(out, "sconto3Rett"); %>
                </tr>
            </table>
            <table cellpadding="5px">
                <tr>
                    <% bp.getFatturaOrdiniController().writeFormField(out, "imponibileErrato"); %>
                </tr>
            </table>
        <% } %>
    </div>
</div>