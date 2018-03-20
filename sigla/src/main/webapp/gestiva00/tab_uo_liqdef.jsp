<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.gestiva00.bp.*"
%>

<% LiquidazioneMassaIvaBP bp = (LiquidazioneMassaIvaBP)BusinessProcess.getBusinessProcess(request);
   SimpleDetailCRUDController controller = bp.getUoLiquidazioniDefinitive();
   controller.writeHTMLTable(pageContext,"liquidazione_massiva",false,false,false,"100%","200px"); 
%>
