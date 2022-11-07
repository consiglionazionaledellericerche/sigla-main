<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*, it.cnr.jada.util.action.*, it.cnr.contab.doccont00.bp.*,it.cnr.contab.doccont00.action.*, it.cnr.contab.doccont00.core.bulk.*,
			it.cnr.contab.doccont00.core.bulk.*"
%>
<%  
	CRUDAccertamentoBP bp = (CRUDAccertamentoBP)BusinessProcess.getBusinessProcess(request);
	AccertamentoResiduoBulk accertamento = (AccertamentoResiduoBulk)bp.getModel();
%>

<table class="Panel">
	<tr>
		<% bp.getController().writeFormField( out, "im_quota_inesigibile_ripartita"); %>
		<% 
		   if (accertamento.isStatoParzialmenteInesigibile() || accertamento.isStatoInesigibile()) {
			  bp.getController().writeFormField( out, "im_quota_inesigibile_da_ripartire");
		   } else if (accertamento.isStatoDubbio() || accertamento.isStatoGiudizialmenteControverso()) {
			  bp.getController().writeFormField( out, "importoNonIncassato");
		   }
		%>
	</tr>
</table>
<%
   	JSPUtils.tabbed(
				pageContext,
				"tabVincoli",
				new String[][] {
						{ "tabVincoliRisorseCopertura","Risorse a Copertura","/doccont00/tab_accertamento_vincoli_risorse_copertura.jsp" },
						{ "tabVincoliRisorsePerenti","Risorse Perenti","/doccont00/tab_accertamento_vincoli_risorse_perenti.jsp" } },
				bp.getTab("tabVincoli"),
				"center",
				"100%",
				null);
%>
