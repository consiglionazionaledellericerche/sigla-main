<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.incarichi00.bp.*"
%>

<%
	CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)BusinessProcess.getBusinessProcess(request);
%>
<table class="Form" width="100%">
  <tr>
		<td><span class="GroupLabel h5 font-weight-bold text-primary">Unità organizzative disponibili</span></td>
		<td></td>
		<td><span class="GroupLabel h5 font-weight-bold text-primary">Unità organizzative assegnate</span></td>
  </tr>
  <tr>
		<td rowspan="2">
        <%bp.getCrudAssUODisponibili().writeHTMLTable(pageContext,"codice_descrizione",false,false,false,"100%","300px"); %>
		</td>
		<td>
		<% JSPUtils.button(out,
				bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-2x fa-long-arrow-right faa-passing" : bp.encodePath("img/doublerightarrow24.gif"),
				bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-2x fa-long-arrow-right" : bp.encodePath("img/doublerightarrow24.gif"),
				null,
				"javascript:submitForm('doAggiungiUO')",
				"btn-outline-primary faa-parent animated-hover btn-title btn-block",
				bp.getCrudAssUO().isEnabled()&& bp.getStatus()!=bp.VIEW,
				bp.getParentRoot().isBootstrap()); %>
		</td>
		<td rowspan="2">
          <%bp.getCrudAssUO().writeHTMLTable(pageContext,null,false,false,false,"100%","300px"); %>
		</td>
	</tr>
	<tr>
		<td>
		<% JSPUtils.button(out,
				bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-2x fa-long-arrow-left faa-passing-reverse" : bp.encodePath("img/doubleleftarrow24.gif"),
				bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-2x fa-long-arrow-left" : bp.encodePath("img/doubleleftarrow24.gif"),
				null,
				"javascript:submitForm('doRimuoviUO')",
				"btn-outline-primary faa-parent animated-hover btn-title btn-block",
				bp.getCrudAssUO().isEnabled()&& bp.getStatus()!=bp.VIEW,
				bp.getParentRoot().isBootstrap()); %>		
		</td>
	</tr>
</table>