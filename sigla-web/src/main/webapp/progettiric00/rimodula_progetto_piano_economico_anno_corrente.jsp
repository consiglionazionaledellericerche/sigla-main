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
	Progetto_rimodulazioneBulk model = (Progetto_rimodulazioneBulk)bp.getModel();
	String viewCurrent = Optional.ofNullable(model).filter(Progetto_rimodulazioneBulk::getFlViewCurrent).map(el->"_current").orElse("");
	Progetto_piano_economicoBulk modelPpe = (Progetto_piano_economicoBulk)controller.getModel();
%>

<%	controller.writeHTMLTable(pageContext,"piano_economico1_rimodulato"+viewCurrent,true,false,true,"100%","auto"); %>
<table class="Panel card p-2 mt-1">
  <tr>
  	<td><% controller.writeFormLabel(out,"voce_piano");%></td>
  	<td colspan="3"><% controller.writeFormInput(out,null,"voce_piano",isKeyEditable,null,null);%></td>
  </tr>
  <tr>
  	<% controller.writeFormField(out,"imSpesaFinanziatoRimodulatoAC");%>
  	<% controller.writeFormField(out,"imSpesaCofinanziatoRimodulatoAC");%>
  	<% controller.writeFormField(out,"imTotaleSpesaRimodulato");%>
  </tr>
</table>
</br>

<fieldset class="fieldset">
	<legend class="GroupLabel text-primary">Voci Bilancio Associate</legend>
<%	controllerVoci.writeHTMLTable(pageContext,"voce_bilancio",true,false,true,"100%","auto"); %>
	</br>
	<table class="Panel">
	  <tr>
	  	<td><% controllerVoci.writeFormLabel(out,"elemento_voce");%></td>
	  	<td colspan="3"><% controllerVoci.writeFormInput(out,null,"elemento_voce",false,null,null);%></td>
	  </tr>
	</table>	
</fieldset>