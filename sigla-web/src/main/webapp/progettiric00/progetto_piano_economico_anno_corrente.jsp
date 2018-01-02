<%@ page 
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.progettiric00.bp.*,
		it.cnr.contab.progettiric00.core.bulk.*"
%>

<%
	TestataProgettiRicercaBP bp = (TestataProgettiRicercaBP)BusinessProcess.getBusinessProcess(request);
	SimpleDetailCRUDController controller = ( (TestataProgettiRicercaBP)bp ).getCrudPianoEconomicoAnnoCorrente();
	boolean isKeyEditable = controller.getModel()!=null && controller.getModel().isNotNew();
%>

<%	controller.writeHTMLTable(pageContext,"piano_economico1",true,false,true,"100%","200px"); %>

	<table class="Panel">
 	  <TR><TD>
	  	<% controller.writeFormLabel(out,"voce_piano");%>
	  	</TD><TD colspan="3">
	  	<% controller.writeFormInput(out,null,"voce_piano",isKeyEditable,null,null);%>
	  </TD></TR>
  	  <TR><TD>
	  	<% controller.writeFormLabel(out,"im_entrata");%>
	  	</TD><TD colspan="3">
	  	<% controller.writeFormInput(out,"im_entrata");%>
	  </TD></TR>
  	  <TR><TD>
	  	<% controller.writeFormLabel(out,"im_spesa");%>
	  	</TD><TD colspan="3">
	  	<% controller.writeFormInput(out,"im_spesa");%>
	  </TD></TR>
  	  <TR><TD>
	  	<% controller.writeFormLabel(out,"fl_ctrl_disp");%>
	  	</TD><TD colspan="3">
	  	<% controller.writeFormInput(out,"fl_ctrl_disp");%>
	  </TD></TR>
	</table>