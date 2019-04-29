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
	SimpleDetailCRUDController controller = bp.getCrudPianoEconomicoAltriAnni();
	SimpleDetailCRUDController controllerVoci = bp.getCrudPianoEconomicoVoceBilancioAltriAnni();
	boolean isKeyEditable = controller.getModel()!=null && controller.getModel().isNotNew();
	Progetto_rimodulazioneBulk model = (Progetto_rimodulazioneBulk)bp.getModel();	
	String viewCurrent = Optional.ofNullable(model).filter(Progetto_rimodulazioneBulk::getFlViewCurrent).map(el->"_current").orElse("");
%>

<%	controller.writeHTMLTable(pageContext,"piano_economico_rimodulato"+viewCurrent,true,false,true,"100%","auto"); %>
<table class="Panel card p-2 mt-1">
  <TR>
  	<TD><% controller.writeFormLabel(out,"esercizio_piano");%></TD>
  	<TD colspan="3"><% controller.writeFormInput(out,null,"esercizio_piano",isKeyEditable,null,null);%></TD>
  </TR>
  <TR>
  	<TD><% controller.writeFormLabel(out,"voce_piano");%></TD>
  	<TD colspan="3"><% controller.writeFormInput(out,null,"voce_piano",isKeyEditable,null,null);%></TD>
  </TR>
  <TR>
  	<% controller.writeFormField(out,"imSpesaFinanziatoRimodulatoAA");%>
  	<% controller.writeFormField(out,"imSpesaCofinanziatoRimodulatoAA");%>
  	<% controller.writeFormField(out,"imTotaleSpesaRimodulato");%>
  </TR>
</table>
</br>
<fieldset class="fieldset">
	<legend class="GroupLabel text-primary">Voci Bilancio Associate</legend>
<%	controllerVoci.writeHTMLTable(pageContext,"voce_bilancio_rimodulato",true,false,true,"100%","auto"); %>
	</br>
	<table class="Panel">
	  <tr><% controllerVoci.writeFormField(out,"elemento_voce");%></tr>
	</table>
</fieldset>