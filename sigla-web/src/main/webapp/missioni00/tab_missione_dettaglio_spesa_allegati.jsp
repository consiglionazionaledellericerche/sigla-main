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
  doPrint('genericdownload/GiustificativiCollegati/<%=bp.getNomeAllegatoDettaglio()%>?methodName=scaricaGiustificativiCollegati&it.cnr.jada.action.BusinessProcess=<%=bp.getPath()%>');
}
</script>

<%  bp.getDettaglioSpesaAllegatiController().writeHTMLTable(pageContext,"allegatiDettaglioSpese",true,false,true,"100%","150px"); %>  
<div class="Group">
  <table>
	<tr>
		<td><% controller.writeFormLabel(out,"default","file"); %></td>
		<td colspan="2"><% controller.writeFormInput(out,"default","file", true,"FormInput",null); %></td>
	</tr>
	<tr>
		<td><% controller.writeFormLabel(out,"default","nome"); %></td>
		<td><% controller.writeFormInput(out,"default","nome"); %></td>
		<% if (bulk != null && bulk.isContentStreamPresent()){%>
			 <td align="left"><% controller.writeFormInput(out,"default","attivaFile");%></td>
		<%}%>
	</tr>
	<tr>
		<td><% controller.writeFormLabel(out,"default","descrizione"); %></td>
		<td colspan="2"><% controller.writeFormInput(out,"default","descrizione", true,"FormInput",null); %></td>
	</tr>
  </table>
</div> 
