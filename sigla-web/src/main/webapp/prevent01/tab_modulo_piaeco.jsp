<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.prevent01.bp.*"
%>

<%
	CRUDDettagliModuloCostiBP bp = (CRUDDettagliModuloCostiBP)BusinessProcess.getBusinessProcess(request);
	SimpleDetailCRUDController controller = bp.getPianoEconomicoAnnoCorrente();
	SimpleDetailCRUDController controllerVoci = bp.getPianoEconomicoVoceBilancioAnnoCorrente();
%>

<%	controller.writeHTMLTable(pageContext,"piano_economico1",false,false,false,"100%","100px"); %>
<table class="Panel">
  <TR>
  	<TD><% controller.writeFormLabel(out,"voce_piano");%></TD>
  	<TD colspan="3"><% controller.writeFormInput(out,null,"voce_piano",true,null,null);%></TD>
  </TR>
  <TR>
  	<TD><% controller.writeFormLabel(out,"im_spesa_finanziato");%></TD>
  	<TD><% controller.writeFormInput(out,null,"im_spesa_finanziato",true,null,null);%></TD>
  	<TD><% controller.writeFormLabel(out,"im_spesa_cofinanziato");%></TD>
  	<TD><% controller.writeFormInput(out,null,"im_spesa_cofinanziato",true,null,null);%></TD>
  	<TD><% controller.writeFormLabel(out,"imTotaleSpesa");%></TD>
  	<TD><% controller.writeFormInput(out,null,"imTotaleSpesa",true,null,null);%></TD>
  </TR>
</table>
</br>
<fieldset class="fieldset">
	<legend class="GroupLabel">Voci Bilancio Associate</legend>
<%	controllerVoci.writeHTMLTable(pageContext,"voce_bilancio",false,false,false,"100%","100px"); %>
	</br>
	<table class="Panel">
	  <TR>
	  	<TD><% controller.writeFormLabel(out,"elemento_voce");%></TD>
	  	<TD><% controller.writeFormInput(out,null,"elemento_voce",true,null,null);%></TD>
	  </TR>
	</table>	
</fieldset>