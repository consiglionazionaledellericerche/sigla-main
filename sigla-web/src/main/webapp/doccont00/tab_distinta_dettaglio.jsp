<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*,it.cnr.jada.util.action.*"
%>

<% 
it.cnr.contab.doccont00.bp.CRUDDistintaCassiereBP bp = (it.cnr.contab.doccont00.bp.CRUDDistintaCassiereBP)BusinessProcess.getBusinessProcess(request);
%>
<script language="JavaScript">
function doVisualizzaSingoloDocumento(esercizio, cds , uo, numero_documento, tipo) {
	doPrint('<%=JSPUtils.getAppRoot(request)%>genericdownload/Documento contabile '+esercizio+'-'+cds+'-'+numero_documento+'.pdf?esercizio='+esercizio+'&cds='+cds+'&uo='+uo+'&numero_documento='+numero_documento+'&tipo='+tipo+'&methodName=scaricaDocumento&it.cnr.jada.action.BusinessProcess=<%=bp.getPath()%>');
}
</script>

<% 	
	String setCol = null;
	if(bp.getParametriCnr()!=null &&bp.getParametriCnr().getFl_tesoreria_unica().booleanValue())
		setCol = "elencoConUoFirmati";
	else
	if (bp.isElencoConUo())
		setCol = "elencoConUo";
%>
    <%bp.getDistintaCassDet().writeHTMLTable(pageContext,setCol,bp.isAddDocContabiliButtonEnabled(),false,bp.isRemoveDocContabiliButtonEnabled(),"100%","auto;max-height:60vh", true); %>
    <table class="card p-2 mt-2 mb-2">
     <tr>
        <td><big class="text-primary mr-2">Totale Mandati</big></td>
        <td><% bp.getController().writeFormInput( out, "totMandati"); %></td>

        <td><big class="text-primary mr-2">Totale Reversali</big></td>
        <td><% bp.getController().writeFormInput( out, "totReversali"); %></td>
    </tr>
    </table>