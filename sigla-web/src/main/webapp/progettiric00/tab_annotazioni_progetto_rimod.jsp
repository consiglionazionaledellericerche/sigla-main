<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		java.util.Optional,
        it.cnr.contab.progettiric00.bp.*,
		it.cnr.contab.progettiric00.core.bulk.*"
%>
<%  RimodulaProgettiRicercaBP bp = (RimodulaProgettiRicercaBP)BusinessProcess.getBusinessProcess(request);
	Progetto_rimodulazioneBulk model = (Progetto_rimodulazioneBulk)bp.getModel();
%>
<div class="Group" style="width:100%;padding:0px">
	<table class="Panel">
  	  	<% if (bp.getUoScrivania().isUoEnte()) { %>
      	  	<tr><% bp.getController().writeFormField(out,"note");%></tr>
  	  	<% } %>
  	  	<% if (Optional.ofNullable(model)
  	  	        .map(el->(bp.getUoScrivania().isUoEnte() && el.isStatoDefinitivo())||el.isStatoRespinto())
                .orElse(Boolean.FALSE)) { %>
  	  	    <tr><% bp.getController().writeFormField(out,"motivoRifiuto");%></tr>
  	  	<% } %>
	</table>
</div>