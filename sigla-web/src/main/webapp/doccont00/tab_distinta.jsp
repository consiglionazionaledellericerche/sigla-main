<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page 
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*, it.cnr.jada.util.action.*,it.cnr.contab.doccont00.bp.*"
%>
<% 
	JSPUtils.printBaseUrl(pageContext); 
it.cnr.contab.doccont00.bp.CRUDDistintaCassiereBP bp = (it.cnr.contab.doccont00.bp.CRUDDistintaCassiereBP)BusinessProcess.getBusinessProcess(request);
%>
<script language="JavaScript">
function doScarica() {	
	 doPrint('genericdownload/<%=bp.getDocumento()%>?methodName=scaricaDistinta&it.cnr.jada.action.BusinessProcess=<%=bp.getPath()%>');
}
function doScaricaFirmato() {	
	 doPrint('genericdownload/<%=bp.getDocumentoFirmato()%>?methodName=scaricaDistintaFirmata&it.cnr.jada.action.BusinessProcess=<%=bp.getPath()%>');
}
</script>

  <div class="Group">		
	<table class="Panel">
	<tr colspan=4>
	<td><% bp.getController().writeFormLabel( out, "esercizio"); %></td>
	<td colspan=3>
		<% bp.getController().writeFormInput( out, "esercizio"); %></td>
	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "cd_cds"); %></td>
	<td colspan=3>
		<% bp.getController().writeFormInput( out,"cd_cds" ); %>
	    <% bp.getController().writeFormInput( out,"ds_cds" ); %></td>		
	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "cd_unita_organizzativa"); %></td>
	<td colspan=3><% bp.getController().writeFormInput( out, "cd_unita_organizzativa"); %>
				  <% bp.getController().writeFormInput( out, "ds_unita_organizzativa"); %></td>				 
	</tr>
	<tr>
		<td><% bp.getController().writeFormLabel( out, "pg_distinta"); %></td>	
		<td><% bp.getController().writeFormInput( out, "pg_distinta"); %></td>
		<td><% bp.getController().writeFormLabel( out, "pg_distinta_def"); %></td>
		<td><% bp.getController().writeFormInput( out, "pg_distinta_def"); %></td>
	</tr>
	<tr>
		<td><% bp.getController().writeFormLabel( out, "dt_emissione"); %></td>	
		<td><% bp.getController().writeFormInput( out, "dt_emissione"); %></td>
		<td><% bp.getController().writeFormLabel( out, "dt_invio"); %></td>
		<td><% bp.getController().writeFormInput( out, "dt_invio"); %></td>
	</tr>
	</table>	
   </div>
   <div class="Group">		
	<table class="Panel">
	<tr><td colspan=4 align=center><b><big>Totali distinta</big></b></td></tr>	
	<tr>	
	<td><% bp.getController().writeFormLabel( out, "totMandati"); %></td>
	<td><% bp.getController().writeFormInput( out, "totMandati"); %></td>
	<td><% bp.getController().writeFormLabel( out, "totReversali"); %></td>
	<td><% bp.getController().writeFormInput( out, "totReversali"); %></td>
	<td rowspan=3 align=center><% Button.write(out,bp.encodePath("img/zoom24.gif"),bp.encodePath("img/zoom24.gif"),null,"javascript:submitForm('doVisualizzaDettagliTotali')", null, "Visualizza dettagli totali", bp.isVisualizzaDettagliTotaliButtonEnabled()  ); %></td>
	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "totMandatiAnnullati"); %></td>
	<td><% bp.getController().writeFormInput( out, "totMandatiAnnullati"); %></td>
	<td><% bp.getController().writeFormLabel( out, "totReversaliAnnullate"); %></td>
	<td><% bp.getController().writeFormInput( out, "totReversaliAnnullate"); %></td>
	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "totMandatiAnnullatiRitrasmessi"); %></td>
	<td><% bp.getController().writeFormInput( out, "totMandatiAnnullatiRitrasmessi"); %></td>
	<td><% bp.getController().writeFormLabel( out, "totReversaliAnnullateRitrasmesse"); %></td>
	<td><% bp.getController().writeFormInput( out, "totReversaliAnnullateRitrasmesse"); %></td>
	</tr>
   </table>	
   </div>
   <div class="Group">		
	<table class="Panel">
	<tr><td colspan=4 align=center><b><big>Totali trasmessi</big></b></td></tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "totStoricoMandatiTrasmessi"); %></td>
	<td><% bp.getController().writeFormInput( out, "totStoricoMandatiTrasmessi"); %></td>
	<td><% bp.getController().writeFormLabel( out, "totStoricoReversaliTrasmesse"); %></td>
	<td><% bp.getController().writeFormInput( out, "totStoricoReversaliTrasmesse"); %></td>
	<td rowspan=2 align=center><% Button.write(out,bp.encodePath("img/zoom24.gif"),bp.encodePath("img/zoom24.gif"),null,"javascript:submitForm('doVisualizzaDettagliTotaliTrasmessi')", null, "Visualizza dettagli totali trasmessi", bp.isVisualizzaDettagliTotaliTrasmessiButtonEnabled()  ); %></td>	
	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "totStoricoMandatiDaRitrasmettere"); %></td>
	<td><% bp.getController().writeFormInput( out, "totStoricoMandatiDaRitrasmettere"); %></td>
	<td><% bp.getController().writeFormLabel( out, "totStoricoReversaliDaRitrasmettere"); %></td>
	<td><% bp.getController().writeFormInput( out, "totStoricoReversaliDaRitrasmettere"); %></td>
	<td></td>	
	</tr>
   </table>	
   </div>