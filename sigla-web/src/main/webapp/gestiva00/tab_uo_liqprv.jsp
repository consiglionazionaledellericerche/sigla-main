<%@ page 
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.gestiva00.bp.*"
%>
<% LiquidazioneMassaIvaBP bp = (LiquidazioneMassaIvaBP)BusinessProcess.getBusinessProcess(request);%>

	<div class="Group" style="width:100%">
		<table width="100%">
			<tr>
				<td colspan="2" align="left">
						<% JSPUtils.button(out,
								bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-floppy-o text-primary" : "img/save24.gif",
								bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-floppy-o text-primary" : "img/save24.gif",
								"Liquidazione Massiva Provvisoria",
								"javascript:submitForm('doLiquidazioneMassivaProvvisoria')",
								"btn-secondary btn-title",
								true,
								bp.getParentRoot().isBootstrap()); %>
				</td>
			</tr>
		</table>
	</div>
<% SimpleDetailCRUDController controller = bp.getUoLiquidazioniProvvisorie();
   controller.writeHTMLTable(pageContext,"liquidazione_massiva",false,false,false,"100%","200px"); 
%>
