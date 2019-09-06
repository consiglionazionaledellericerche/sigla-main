<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.progettiric00.bp.*,
		it.cnr.contab.progettiric00.core.bulk.*"
%>
<%  
	RimodulaProgettiRicercaBP bp = (RimodulaProgettiRicercaBP)BusinessProcess.getBusinessProcess(request);
	SimpleDetailCRUDController controller = bp.getCrudVariazioniAssociate();
   	controller.writeHTMLTable(pageContext,"default",true,false,true,"100%","200px"); %>

<div class="Group" style="width:100%;padding:0px">
	<table class="Panel">
  	  	<tr>
  	  		<td><% controller.writeFormLabel(out,"findVariazioneResidua");%></td>
		  	<td colspan="3"><% controller.writeFormInput(out,"findVariazioneResidua"); %></td>
		</tr>
  	  	<tr>
  	  		<td><% controller.writeFormLabel(out,"findVariazioneCompetenza");%></td>
		  	<td colspan="3"><% controller.writeFormInput(out,"findVariazioneCompetenza"); %></td>
		</tr>
	</table>
</div>