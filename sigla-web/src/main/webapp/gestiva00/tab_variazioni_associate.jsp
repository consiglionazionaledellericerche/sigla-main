<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.gestiva00.bp.*"
%>

<% LiquidazioneDefinitivaIvaBP bp = (LiquidazioneDefinitivaIvaBP)BusinessProcess.getBusinessProcess(request);
   SimpleDetailCRUDController controller = bp.getVariazioni_associate();
   controller.writeHTMLTable(pageContext,"default",false,false,false,"100%","200px"); 
%>
