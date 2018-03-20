<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.contab.incarichi00.bp.*"
%>

<%
CRUDConfigRepertorioLimitiBP bp = (CRUDConfigRepertorioLimitiBP)BusinessProcess.getBusinessProcess(request);
%>
<table class="w-100">
	<tr><td colspan=2>
		<% bp.getIncarichiXCds().writeHTMLTable(
								pageContext,
								"incarichiCds",
								false,
								false,
								false,
								"100%",
								"150px"); 
		%>
	</td></tr>
	<TR>
	   <TD><% bp.getRepertorioLimiti().writeFormLabel(out,"tot_importi_x_cds");%>
	       <% bp.getRepertorioLimiti().writeFormInput(out,"tot_importi_x_cds");%></TD>
	   <TD><% bp.getRepertorioLimiti().writeFormLabel(out,"tot_prc_x_cds");%>
	       <% bp.getRepertorioLimiti().writeFormInput(out,"tot_prc_x_cds");%></TD>
	</TR>
</table>