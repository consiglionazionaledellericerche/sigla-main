<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.progettiric00.bp.*,
		it.cnr.contab.progettiric00.core.bulk.*"
%>

<%
	TestataProgettiRicercaBP bp = (TestataProgettiRicercaBP)BusinessProcess.getBusinessProcess(request);
	SimpleDetailCRUDController controller = ( (TestataProgettiRicercaBP)bp ).getCrudPianoEconomicoAltriAnni();
	SimpleDetailCRUDController controllerVoci = ( (TestataProgettiRicercaBP)bp ).getCrudPianoEconomicoVoceBilancioAnnoCorrente();
	boolean isKeyEditable = controller.getModel()!=null && controller.getModel().isNotNew();
%>

<%	controller.writeHTMLTable(pageContext,"piano_economico",true,false,true,"100%","100px"); %>
<table class="Panel">
  <TR>
  	<TD><% controller.writeFormLabel(out,"esercizio_piano");%></TD>
  	<TD colspan="3"><% controller.writeFormInput(out,null,"esercizio_piano",isKeyEditable,null,null);%></TD>
  </TR>
  <TR>
  	<TD><% controller.writeFormLabel(out,"voce_piano");%></TD>
  	<TD colspan="3"><% controller.writeFormInput(out,null,"voce_piano",isKeyEditable,null,null);%></TD>
  </TR>
  <TR>
  	<% controller.writeFormField(out,"im_spesa_finanziato");%>
  	<% controller.writeFormField(out,"im_spesa_cofinanziato");%>
  	<% controller.writeFormField(out,"imTotaleSpesa");%>
  </TR>
</table>
</br>
<fieldset class="fieldset">
	<legend class="GroupLabel">Voci Bilancio Associate</legend>
<%	controllerVoci.writeHTMLTable(pageContext,"voce_bilancio",true,false,true,"100%","100px"); %>
	</br>
	<table class="Panel">
	  <TR>
	  	<% controllerVoci.writeFormField(out,"elemento_voce");%>
	  </TR>
	</table>
</fieldset>