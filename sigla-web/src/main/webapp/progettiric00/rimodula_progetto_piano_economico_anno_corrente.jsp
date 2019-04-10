<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
	    java.util.Optional,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.progettiric00.bp.*,
		it.cnr.contab.progettiric00.core.bulk.*"
%>

<%
	RimodulaProgettiRicercaBP bp = (RimodulaProgettiRicercaBP)BusinessProcess.getBusinessProcess(request);
	SimpleDetailCRUDController controller = bp.getCrudPianoEconomicoAnnoCorrente();
	SimpleDetailCRUDController controllerVoci = bp.getCrudPianoEconomicoVoceBilancioAnnoCorrente();
	boolean isKeyEditable = controller.getModel()!=null && controller.getModel().isNotNew();
%>

<%	controller.writeHTMLTable(pageContext,"piano_economico1_rimodulato",true,false,true,"100%","100px"); %>
<table class="Panel card p-2 mt-1">
  <tr>
  	<td><% controller.writeFormLabel(out,"voce_piano");%></td>
  	<td colspan="3"><% controller.writeFormInput(out,null,"voce_piano",isKeyEditable,null,null);%></td>
  </tr>
  <tr>
  	<% controller.writeFormField(out,"imSpesaFinanziatoRimodulato");%>
  	<% controller.writeFormField(out,"imSpesaCofinanziatoRimodulato");%>
  	<% controller.writeFormField(out,"imTotaleSpesaRimodulato");%>
  </tr>
</table>
</br>
<fieldset class="fieldset">
	<legend class="GroupLabel text-primary">Voci Bilancio Associate</legend>
<%	controllerVoci.writeHTMLTable(pageContext,"voce_bilancio_rimodulato",true,false,true,"100%","100px"); %>
	</br>
	<table class="Panel">
	  <TR>
	  	<% controllerVoci.writeFormField(out,"elemento_voce");%>
	  </TR>
	</table>	
</fieldset>