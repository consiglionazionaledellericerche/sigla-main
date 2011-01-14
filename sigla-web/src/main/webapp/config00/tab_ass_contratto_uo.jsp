<%@ page 
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.config00.bp.*"
%>

<%
	CRUDConfigAnagContrattoBP bp = (CRUDConfigAnagContrattoBP)BusinessProcess.getBusinessProcess(request);
%>
<table class="Form" width="100%">
  <tr>
		<td><span class="GroupLabel">Unità organizzative disponibili</span></td>
		<td></td>
		<td><span class="GroupLabel">Unità organizzative assegnate</span></td>
  </tr>
  <tr>
		<td rowspan="2">
        <%bp.getCrudAssUODisponibili().writeHTMLTable(pageContext,"codice_descrizione",false,false,false,"100%","300px"); %>
		</td>
		<td>
		<% JSPUtils.button(out,bp.encodePath("img/doublerightarrow24.gif"),bp.encodePath("img/doublerightarrow24.gif"),null,"javascript:submitForm('doAggiungiUO')",bp.getCrudAssUO().isEnabled()&& bp.getStatus()!=bp.VIEW); %>
		</td>
		<td rowspan="2">
          <%bp.getCrudAssUO().writeHTMLTable(pageContext,null,false,false,false,"100%","300px"); %>
		</td>
	</tr>
	<tr>
		<td>
		<% JSPUtils.button(out,bp.encodePath("img/doubleleftarrow24.gif"),bp.encodePath("img/doubleleftarrow24.gif"),null,"javascript:submitForm('doRimuoviUO')",bp.getCrudAssUO().isEnabled()&& bp.getStatus()!=bp.VIEW); %>		
		</td>
	</tr>
</table>