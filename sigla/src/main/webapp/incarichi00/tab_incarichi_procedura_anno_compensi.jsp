<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.contab.incarichi00.bp.*"
%>

<%
CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)BusinessProcess.getBusinessProcess(request);
%>
<table class="Panel">
		<TR>
		 	<TD colspan="4">
<% bp.getCompensiAllegati().writeHTMLTable(
								pageContext,
								"PROCEDURA_INCARICHI",
								false,
								false,
								false,
								"100%",
								"150px"); %>
			</TD>
		</TR>
	</table>
