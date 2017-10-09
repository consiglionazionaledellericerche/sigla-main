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
    bp.getDettaglio().writeHTMLTable(pageContext,"righiSet",true,false,true,"100%","200px");
%>
<div class="Group card">
    <table width="100%">
        <tr>
            <td>
                <% bp.getFattureRigaOrdiniController().writeHTMLTable(pageContext,"default",true,false,true,"100%","200px"); %>
            </td>
        </tr>
    </table>
</div>