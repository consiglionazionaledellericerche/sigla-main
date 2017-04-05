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

<%  bp.getDettaglioSpesaAllegatiController().writeHTMLTable(pageContext,"default",true,false,true,"100%","150px"); %>  
<div class="Group">
  <table>
  	<% bp.getDettaglioSpesaAllegatiController().writeForm(out, "default");  %>
  </table>
</div> 
