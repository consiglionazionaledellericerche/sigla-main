<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.incarichi00.bp.*,
		it.cnr.contab.incarichi00.bulk.*"
%>

<%
CRUDConfigRepertorioLimitiBP bp = (CRUDConfigRepertorioLimitiBP)BusinessProcess.getBusinessProcess(request);
SimpleDetailCRUDController controller = bp.getIncarichiXUo();
%>
<div class="Group">
<table>
	<tr>
        <td>
        	<% controller.writeFormLabel(out,"cd_unita_organizzativa"); %>
            <% controller.writeFormInput(out,"cd_unita_organizzativa"); %>
            <% controller.writeFormInput(out,"ds_unita_organizzativa"); %>
        </td>
    </tr>	
	<tr>
	    <td>
			<% JSPUtils.tabbed(
							pageContext,
							"tabDettaglio",
							bp.getTabsDettaglioIncarichi(),
							bp.getTab("tabDettaglio"),
							"center",
							"100%",
							null ); %>
	    </td>
	</tr>
</table>
<% if (((Repertorio_limitiBulk)bp.getRepertorioLimiti().getModel()) != null &&
		((V_incarichi_uoBulk)controller.getModel())!=null) { %>
<fieldset>
<legend class="GroupLabel">Totali Incarichi per anno <%=((Repertorio_limitiBulk)bp.getRepertorioLimiti().getModel()).getEsercizio().toString() %> e UO <%=((V_incarichi_uoBulk)controller.getModel()).getCd_unita_organizzativa().toString()%></legend>
    <table class="Panel">
    <tr>
        <td><% controller.writeFormLabel(out,"tot_incarichi_assegnati"); %></td>
        <td><% controller.writeFormInput(out,"tot_incarichi_assegnati"); %></td>
        <td>&nbsp;</td>
        <td><% controller.writeFormLabel(out,"tot_incarichi_validi"); %></td>
	    <td><% controller.writeFormInput(out,"tot_incarichi_validi"); %></td>
 	    <td>&nbsp;</td>
	    <td><% controller.writeFormLabel(out,"tot_incarichi_scaduti"); %></td>
        <td><% controller.writeFormInput(out,"tot_incarichi_scaduti"); %></td>
    </tr>	
	</table>
</fieldset>
<% } %>