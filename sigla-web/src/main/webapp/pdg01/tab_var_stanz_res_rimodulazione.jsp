<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.varstanz00.bp.*,
		it.cnr.contab.varstanz00.bulk.*"
%>

<%
	CRUDVar_stanz_resBP bp = (CRUDVar_stanz_resBP)BusinessProcess.getBusinessProcess(request);
%>
<div class="GroupLabel font-weight-bold text-primary ml-2">Rimodulazione Progetto</div>
<div class="Group card p-3 m-1 w-100" style="width:100%">
	<table class="Panel w-100" cellspacing=1 cellpadding=1>
	  <tr>
        <TD><% bp.getController().writeFormLabel(out,"findProgettoRimodulato");%></TD>
        <TD><% bp.getController().writeFormInput(out,"findProgettoRimodulato");%></TD>
      </tr>
	  <tr>
        <TD><% bp.getController().writeFormLabel(out,"pgRimodulazione");%></TD>
        <TD><% bp.getController().writeFormInput(out,"default","pgRimodulazione",true,null,null);%></TD>
      </tr>
   	</table>
</div>	
