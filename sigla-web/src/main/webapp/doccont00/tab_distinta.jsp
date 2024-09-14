<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*, it.cnr.jada.util.action.*,it.cnr.contab.doccont00.bp.*"
%>
<% 
it.cnr.contab.doccont00.bp.CRUDDistintaCassiereBP bp = (it.cnr.contab.doccont00.bp.CRUDDistintaCassiereBP)BusinessProcess.getBusinessProcess(request);
%>
<script language="JavaScript">
function doScarica() {	
	 doPrint('<%=JSPUtils.getAppRoot(request)%>genericdownload/<%=bp.getDocumento()%>?methodName=scaricaDistinta&it.cnr.jada.action.BusinessProcess=<%=bp.getPath()%>');
}
function doScaricaFirmato() {	
	 doPrint('<%=JSPUtils.getAppRoot(request)%>genericdownload/<%=bp.getDocumentoFirmato()%>?methodName=scaricaDistintaFirmata&it.cnr.jada.action.BusinessProcess=<%=bp.getPath()%>');
}
</script>

  <div class="Group">		
	<table class="Panel w-100 card p-2">
		<tr colspan=4>
			<td><% bp.getController().writeFormLabel( out, "esercizio"); %></td>
			<td colspan=3>
				<% bp.getController().writeFormInput( out, "esercizio"); %></td>
		</tr>
		<tr>
		    <td><% bp.getController().writeFormLabel( out, "cds"); %></td>
            <td colspan=3>
                <% bp.getController().writeFormInput( out, "default", "cds", !bp.isUoEnte(), null, null); %>
            </tr>
		<tr>
		    <td><% bp.getController().writeFormLabel( out, "unita_organizzativa"); %></td>
            <td colspan=3>
                <% bp.getController().writeFormInput( out, "default", "unita_organizzativa", !bp.isUoEnte(), null, null); %>
            </tr>
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
   <% if (bp.isAttivoSiopeplus()) { %>
   <div class="Group">
   	<b class="text-info h3 d-flex justify-content-center mt-2">SIOPE+</b>
	<table class="Panel card border-info p-2 mt-2">
		<tr>
			<% bp.getController().writeFormField( out, "progFlusso"); %>
			<% bp.getController().writeFormField( out, "identificativoFlussoBT"); %>
			<% bp.getController().writeFormField( out, "fl_invia_pec"); %>
		</tr>
		<tr>
			<td><% bp.getController().writeFormLabel( out, "statoLabel"); %></td>
			<td colspan="5"><% bp.getController().writeFormInput( out, (bp.isSearching() ? "stato":"statoLabel")); %></td>
		</tr>
   </table>
   </div>
   <% } %>
   <% if (!bp.isSearching()) {%>
       <div class="Group">
        <b class="text-primary h3 d-flex justify-content-center mt-2">Totali distinta</b>
        <table class="Panel card p-2 mt-2">
            <tr>
                <td><% bp.getController().writeFormLabel( out, "totMandati"); %></td>
                <td><% bp.getController().writeFormInput( out, "totMandati"); %></td>
                <td><% bp.getController().writeFormLabel( out, "totReversali"); %></td>
                <td><% bp.getController().writeFormInput( out, "totReversali"); %></td>
                <td rowspan=3 align=center>
                    <% JSPUtils.button(out,
                            bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-search-plus" : bp.encodePath("img/zoom24.gif"),
                            bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-search-plus" : bp.encodePath("img/zoom24.gif"),
                            "Visualizza dettagli totali",
                            "javascript:submitForm('doVisualizzaDettagliTotali')",
                            "btn-outline-primary btn-title",
                            bp.isVisualizzaDettagliTotaliButtonEnabled(),
                            bp.getParentRoot().isBootstrap()); %>
                </td>
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
        <b class="text-primary h3 d-flex justify-content-center mt-2">Totali trasmessi</b>
        <table class="Panel card p-2 mt-2">
            <tr>
                <td><% bp.getController().writeFormLabel( out, "totStoricoMandatiTrasmessi"); %></td>
                <td><% bp.getController().writeFormInput( out, "totStoricoMandatiTrasmessi"); %></td>
                <td><% bp.getController().writeFormLabel( out, "totStoricoReversaliTrasmesse"); %></td>
                <td><% bp.getController().writeFormInput( out, "totStoricoReversaliTrasmesse"); %></td>
                <td rowspan=2 align=center>
                    <% JSPUtils.button(out,
                         bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-search-plus" : bp.encodePath("img/zoom24.gif"),
                         bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-search-plus" : bp.encodePath("img/zoom24.gif"),
                         "Visualizza dettagli totali trasmessi",
                         "javascript:submitForm('doVisualizzaDettagliTotaliTrasmessi')",
                         "btn-outline-primary btn-title",
                         bp.isVisualizzaDettagliTotaliTrasmessiButtonEnabled(),
                         bp.getParentRoot().isBootstrap()); %>
                </td>
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
   <% } %>