<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.contab.anagraf00.bp.*,
		it.cnr.jada.util.jsp.*"
%>

<% CRUDTerzoBP bp = (CRUDTerzoBP)BusinessProcess.getBusinessProcess(request);%>

<table class="Form" width="100%">
  <tr>
		<td><span class="GroupLabel h3 text-primary">Termini disponibili</span></td>
		<td></td>
		<td><span class="GroupLabel h3 text-primary">Termini selezionati</span></td>
  </tr>
  <tr>
		<td rowspan="2">
      <%	bp.getCrudTermini_pagamento_disponibili().writeHTMLTable(pageContext,null,false,false,false,"100%","200px"); %>
		</td>
		<td align="center">
		<% JSPUtils.button(out,
					bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-2x fa-long-arrow-right faa-passing" : "img/doublerightarrow24.gif",
					bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-2x fa-long-arrow-right" : "img/doublerightarrow24.gif",
					null,
					"javascript:submitForm('doAggiungiTermini_pagamento')",
					"btn-outline-primary faa-parent animated-hover",
					bp.getStatus()!=CRUDTerzoBP.VIEW,
					bp.getParentRoot().isBootstrap()); %>
		</td>
		<td rowspan="2">
      <%	bp.getCrudTermini_pagamento().writeHTMLTable(pageContext,null,false,false,false,"100%","200px"); %>
		</td>
	</tr>
	<tr>
		<td align="center">
		<% JSPUtils.button(out,
			bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-2x fa-long-arrow-left faa-passing-reverse" : "img/doubleleftarrow24.gif",
			bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-2x fa-long-arrow-left" : "img/doubleleftarrow24.gif",
			null,
			"javascript:submitForm('doRimuoviTermini_pagamento')",
			"btn-outline-primary faa-parent animated-hover",
			bp.getStatus()!=CRUDTerzoBP.VIEW,
			bp.getParentRoot().isBootstrap()); %>
		</td>
	</tr>
</table>