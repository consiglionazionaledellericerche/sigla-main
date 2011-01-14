<%@ page 
	import="it.cnr.jada.action.*,
		it.cnr.contab.anagraf00.bp.*,
		it.cnr.jada.util.jsp.*"
%>

<% CRUDTerzoBP bp = (CRUDTerzoBP)BusinessProcess.getBusinessProcess(request);%>

<table class="Form" width="100%">
  <tr>
		<td><span class="GroupLabel">Termini disponibili</span></td>
		<td></td>
		<td><span class="GroupLabel">Termini selezionati</span></td>
  </tr>
  <tr>
		<td rowspan="2">
      <%	bp.getCrudTermini_pagamento_disponibili().writeHTMLTable(pageContext,null,false,false,false,"100%","200px"); %>
		</td>
		<td>
		<% JSPUtils.button(out,bp.encodePath("img/doublerightarrow24.gif"),bp.encodePath("img/doublerightarrow24.gif"),null,"javascript:submitForm('doAggiungiTermini_pagamento')",bp.getStatus()!=bp.VIEW); %>
		</td>
		<td rowspan="2">
      <%	bp.getCrudTermini_pagamento().writeHTMLTable(pageContext,null,false,false,false,"100%","200px"); %>
		</td>
	</tr>
	<tr>
		<td>
		<% JSPUtils.button(out,bp.encodePath("img/doubleleftarrow24.gif"),bp.encodePath("img/doubleleftarrow24.gif"),null,"javascript:submitForm('doRimuoviTermini_pagamento')",bp.getStatus()!=bp.VIEW); %>
		</td>
	</tr>
</table>