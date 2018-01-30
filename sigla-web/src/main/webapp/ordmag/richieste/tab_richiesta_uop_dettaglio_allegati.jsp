<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@page import="it.cnr.contab.ordmag.richieste.bulk.RichiestaUopRigaBulk"%>
<%@ page import = "it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*, it.cnr.jada.util.action.*, it.cnr.contab.ordmag.richieste.bp.CRUDRichiestaUopBP, it.cnr.contab.ordmag.richieste.bulk.AllegatoRichiestaDettaglioBulk, it.cnr.contab.ordmag.anag00.*"%>

<%  
	CRUDRichiestaUopBP bp = (CRUDRichiestaUopBP)BusinessProcess.getBusinessProcess(request);
	RichiestaUopRigaBulk riga = (RichiestaUopRigaBulk) bp.getRighe().getModel();

	if(riga == null)
		riga = new RichiestaUopRigaBulk();	
%>

<script language="JavaScript">
function doVisualizzaDocumentiCollegati() {
  doPrint('<%=JSPUtils.getAppRoot(request)%>genericdownload/DocumentiCollegati/<%=bp.getNomeAllegato()!=null?bp.getNomeAllegato().replace("'", "_"):""%>?methodName=scaricaDocumentoDettaglioCollegato&it.cnr.jada.action.BusinessProcess=<%=bp.getPath()%>');
}
</script>

<%  bp.getDettaglioAllegatiController().writeHTMLTable(pageContext,"allegatiRichiestaRiga",true,false,true,"100%","150px"); %>  
<div class="Group">
  <table>
	<tr>
		<td><% bp.getDettaglioAllegatiController().writeFormLabel(out,"default","file"); %></td>
		<td colspan="2"><% bp.getDettaglioAllegatiController().writeFormInput(out,"default","file", false,"FormInput",null); %></td>
	</tr>
	<tr>
		<td><% bp.getDettaglioAllegatiController().writeFormLabel(out,"default","nome"); %></td>
		<td><% bp.getDettaglioAllegatiController().writeFormInput(out,"default","nome"); %></td>
		<td align="left"><% bp.getDettaglioAllegatiController().writeFormInput(out,"default","attivaFile");%></td>
	</tr>
	<tr>
		<td><% bp.getDettaglioAllegatiController().writeFormLabel(out,"default","descrizione"); %></td>
		<td colspan="2"><% bp.getDettaglioAllegatiController().writeFormInput(out,"default","descrizione", false,"FormInput",null); %></td>
	</tr>
  </table>
</div> 
