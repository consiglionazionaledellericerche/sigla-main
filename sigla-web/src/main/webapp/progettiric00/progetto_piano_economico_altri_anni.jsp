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
	TestataProgettiRicercaBP bp = (TestataProgettiRicercaBP)BusinessProcess.getBusinessProcess(request);
	SimpleDetailCRUDController controller = ( (TestataProgettiRicercaBP)bp ).getCrudPianoEconomicoAltriAnni();
	SimpleDetailCRUDController controllerVoci = ( (TestataProgettiRicercaBP)bp ).getCrudPianoEconomicoVoceBilancioAltriAnni();
    String fieldAmm = Optional.ofNullable(bp)
                .filter(AmministraTestataProgettiRicercaBP.class::isInstance)
                .map(amm -> "_amm")
                .orElse("");
	boolean isKeyEditable = controller.getModel()!=null && controller.getModel().isNotNew();
%>

<%	controller.writeHTMLTable(pageContext,"piano_economico",true,false,true,"100%","100px"); %>
<table class="Panel card p-2 mt-1">
  <TR>
  	<TD><% controller.writeFormLabel(out,"esercizio_piano");%></TD>
  	<TD colspan="3"><% controller.writeFormInput(out,null,"esercizio_piano",isKeyEditable,null,null);%></TD>
  </TR>
  <TR>
  	<TD><% controller.writeFormLabel(out,"voce_piano");%></TD>
  	<TD colspan="3"><% controller.writeFormInput(out,null,"voce_piano" + fieldAmm,isKeyEditable,null,null);%></TD>
  </TR>
  <TR>
  	<% controller.writeFormField(out,"im_spesa_finanziato" + fieldAmm);%>
  	<% controller.writeFormField(out,"im_spesa_cofinanziato" + fieldAmm);%>
  	<% controller.writeFormField(out,"imTotaleSpesa");%>
  </TR>
</table>
</br>
<fieldset class="fieldset">
	<legend class="GroupLabel text-primary">Voci Bilancio Associate</legend>
<%	controllerVoci.writeHTMLTable(pageContext,"voce_bilancio",true,false,true,"100%","100px"); %>
	</br>
	<table class="Panel">
	  <tr><% controllerVoci.writeFormField(out,"elemento_voce");%></tr>
	</table>
</fieldset>