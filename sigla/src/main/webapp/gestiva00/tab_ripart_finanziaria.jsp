<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.gestiva00.bp.*"
%>

<% LiquidazioneDefinitivaIvaBP bp = (LiquidazioneDefinitivaIvaBP)BusinessProcess.getBusinessProcess(request);
   SimpleDetailCRUDController controller = bp.getRipartizione_finanziaria();
   boolean isEnable = bp.isTabRipartizioneFinanziariaEnable();
   controller.writeHTMLTable(pageContext,"default",isEnable,false,isEnable,"100%","200px"); %>
	<div class="Group" style="width:100%">
	<table class="Panel">
  	  <TR><TD>
	  	<% controller.writeFormLabel(out,"esercizio_variazione");%>
	  	</TD><TD colspan="3">
	    <% controller.writeFormInput(out,"default","esercizio_variazione",!isEnable,null,null); %>
	  </TD></TR>
  	  <TR><TD>
	  	<% controller.writeFormLabel(out,"linea_di_attivita");%>
	  	</TD><TD colspan="3">
	    <% controller.writeFormInput(out,"default","linea_di_attivita",!isEnable,null,null); %>
	  </TD></TR>
  	  <TR><TD>
	  	<% controller.writeFormLabel(out,"im_variazione");%>
	  	</TD><TD colspan="3">
	    <% controller.writeFormInput(out,"default","im_variazione",!isEnable,null,null); %>
	  </TD></TR>
	</table>
	</div>