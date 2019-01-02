<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*,it.cnr.jada.util.action.*"
%>

<% 
	JSPUtils.printBaseUrl(pageContext); 
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
    
    <table class="Panel">	   
	<tr>
		<td>   
   			 <%bp.getDistintaCassDet().writeHTMLTable(pageContext,setCol,bp.isAddDocContabiliButtonEnabled(),false,bp.isRemoveDocContabiliButtonEnabled(),"100%","60vh", true); %>
		</td>
	</tr>
	</table>
	<table>
	 <tr>
		<td><big class="text-primary mr-2">Totale Mandati</big></td>
		<td><% bp.getController().writeFormInput( out, "totMandati"); %></td>
	
		<td><big class="text-primary mr-2">Totale Reversali</big></td>
		<td><% bp.getController().writeFormInput( out, "totReversali"); %></td>
	</tr>
	</table>