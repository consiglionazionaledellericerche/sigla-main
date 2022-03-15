<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.gestiva00.bp.*,
		it.cnr.contab.gestiva00.core.bulk.*"
%>
<% LiquidazioneMassaIvaBP bp = (LiquidazioneMassaIvaBP)BusinessProcess.getBusinessProcess(request);
   Liquidazione_definitiva_ivaVBulk liquidazione = (Liquidazione_definitiva_ivaVBulk)bp.getModel();
%>

	<div class="Group" style="width:100%">
		<table width="100%">
			<tr>
				<td colspan="2" align="left">
						<% JSPUtils.button(out,
								bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-floppy-o text-primary" : "img/save24.gif",
								bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-floppy-o text-primary" : "img/save24.gif",
								"Liquidazione Massiva Provvisoria",
								"javascript:submitForm('doLiquidazioneMassivaProvvisoria')",
								"btn-outline-secondary btn-title",
								true,
								bp.getParentRoot().isBootstrap()); %>
				</td>
				<% if (liquidazione!=null && liquidazione.getDataAggiornamentoLastLiquidazioneProvvisoria()!=null) { %>
					<td colspan="2">
						<% bp.getController().writeFormLabel(out,"dataAggiornamentoLastLiquidazioneProvvisoria"); %> 
						<% bp.getController().writeFormInput(out,"dataAggiornamentoLastLiquidazioneProvvisoria"); %> 
					</td>
				 <% } %>
			</tr>
		</table>
	</div>
<% SimpleDetailCRUDController controller = bp.getUoLiquidazioniProvvisorie();
   controller.writeHTMLTable(pageContext,"liquidazione_massiva",false,false,false,"100%","200px"); 
%>
