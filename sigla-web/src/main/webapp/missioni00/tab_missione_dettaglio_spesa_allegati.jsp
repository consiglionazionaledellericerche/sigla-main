<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page import = "it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*, it.cnr.jada.util.action.*, it.cnr.contab.missioni00.bp.*, it.cnr.contab.missioni00.docs.bulk.*"%>

<%  
	CRUDMissioneBP bp = (CRUDMissioneBP)BusinessProcess.getBusinessProcess(request);
	Missione_dettaglioBulk spesa = (Missione_dettaglioBulk) bp.getSpesaController().getModel();

	if(spesa == null)
		spesa = new Missione_dettaglioBulk();	
%>

<script language="JavaScript">
function doVisualizzaGiustificativiCollegati() {
  doPrint('<%=JSPUtils.getAppRoot(request)%>genericdownload/GiustificativiCollegati/<%=bp.getNomeAllegato()!=null?bp.getNomeAllegato().replace("'", "_"):""%>?methodName=scaricaGiustificativiCollegati&it.cnr.jada.action.BusinessProcess=<%=bp.getPath()%>');
}
</script>

<%  bp.getDettaglioSpesaAllegatiController().writeHTMLTable(pageContext,"allegatiDettaglioSpese",true,false,true,"100%","150px"); %>  
<div class="Group">
  <table>
	<tr>
		<td><% bp.getDettaglioSpesaAllegatiController().writeFormLabel(out,"default","file"); %></td>
		<td colspan="2"><% bp.getDettaglioSpesaAllegatiController().writeFormInput(out,"default","file", false,"FormInput",null); %></td>
	</tr>
	<tr>
		<td><% bp.getDettaglioSpesaAllegatiController().writeFormLabel(out,"default","nome"); %></td>
		<td><% bp.getDettaglioSpesaAllegatiController().writeFormInput(out,"default","nome"); %></td>
		<td align="left"><% bp.getDettaglioSpesaAllegatiController().writeFormInput(out,"default","attivaFile");%></td>
	</tr>
	<tr>
		<td><% bp.getDettaglioSpesaAllegatiController().writeFormLabel(out,"default","descrizione"); %></td>
		<td colspan="2"><% bp.getDettaglioSpesaAllegatiController().writeFormInput(out,"default","descrizione", false,"FormInput",null); %></td>
	</tr>
  </table>
</div> 
