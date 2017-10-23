<%@ page 
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
%>
<fieldset class="card fieldset">
    <legend class="GroupLabel card-header text-primary">Righe di fattura</legend>
    <table width="100%">
        <tr>
            <td>
                <% bp.getDettaglio().writeHTMLTable(pageContext,"righiSet",false,false,false,"100%","200px"); %>
            </td>
        </tr>
    </table>
</fieldset>
<fieldset class="card fieldset">
    <legend class="GroupLabel card-header text-primary">Righe di consegna</legend>
    <table width="100%">
        <tr>
            <td>
                <% bp.getFattureRigaOrdiniController().writeHTMLTable(pageContext,"default",true,false,true,"100%","100%"); %>
            </td>
        </tr>
    </table>
    <fieldset class="card fieldset ">
        <legend class="GroupLabel card-header text-primary">Rettifiche</legend>
        <table cellpadding="5px">
            <tr>
                <% bp.getFattureRigaOrdiniController().writeFormField(out, "voceIva"); %>
                <% bp.getFattureRigaOrdiniController().writeFormField(out, "prezzoUnitarioRett"); %>
            </tr>
        </table>
        <table cellpadding="5px">
            <tr>
                <% bp.getFattureRigaOrdiniController().writeFormField(out, "sconto1Rett"); %>
                <% bp.getFattureRigaOrdiniController().writeFormField(out, "sconto2Rett"); %>
                <% bp.getFattureRigaOrdiniController().writeFormField(out, "sconto3Rett"); %>
            </tr>
        </table>
    </fieldset>
</fieldset>