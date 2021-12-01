<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
			it.cnr.jada.action.*,
			java.util.*,
			it.cnr.jada.util.action.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
</head>
<title>Stampa Consumi</title>

<body class="Form">

<%	BulkBP bp = (BulkBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext); %>

<div class="Group card p-2" style="width:100%">
		<table width="100%">
			<tr>
                <td>
                    <%
                        bp.getController().writeFormLabel(out, "daDataMovimento");
                    %>
                </td>
                <td>
                    <%
                        bp.getController().writeFormInput(out, "daDataMovimento");
                    %>
                </td>
                <td class="pl-5">
                    <%
                        bp.getController().writeFormLabel(out, "aDataMovimento");
                    %>
                </td>
                <td>
                    <%
                        bp.getController().writeFormInput(out, "aDataMovimento");
                    %>
                </td>
            </tr>

			<tr>
				<td >
					<%
						bp.getController().writeFormLabel(out, "dataRiferimento");
					%>
				</td>
				<td colspan="3">
					<%
						bp.getController().writeFormInput(out, "dataRiferimento");
					%>
				</td>
			</tr>
			<tr>
				<td>
					<%
						bp.getController().writeFormLabel(out, "findMagazzino");
					%>
				</td>
				<td colspan="3">
					<%
						bp.getController().writeFormInput(out, "findMagazzino");
					%>
				</td>
			</tr>

			<tr>
                <td>
                    <%
                        bp.getController().writeFormLabel(out, "findDaUnitaOperativa");
                    %>
                </td>
                <td>
                    <%
                        bp.getController().writeFormInput(out, "findDaUnitaOperativa");
                    %>
                </td>
                <td class="pl-5">
                    <%
                        bp.getController().writeFormLabel(out, "findAUnitaOperativa");
                    %>
                </td>
                <td>
                    <%
                        bp.getController().writeFormInput(out, "findAUnitaOperativa");
                    %>
                </td>
            </tr>
            <tr>
                <td>
                    <%
                        bp.getController().writeFormLabel(out, "findDaCatGrp");
                    %>
                </td>
                <td>
                    <%
                        bp.getController().writeFormInput(out, "findDaCatGrp");
                    %>
                </td>
                <td class="pl-5">
                    <%
                        bp.getController().writeFormLabel(out, "findACatGrp");
                    %>
                </td>
                <td>
                    <%
                        bp.getController().writeFormInput(out, "findACatGrp");
                    %>
                </td>
            </tr>
            <tr>
                <td>
                    <%
                        bp.getController().writeFormLabel(out, "findDaBeneServizio");
                    %>
                </td>
                <td>
                    <%
                        bp.getController().writeFormInput(out, "findDaBeneServizio");
                    %>
                </td>
                <td class="pl-5">
                    <%
                        bp.getController().writeFormLabel(out, "findABeneServizio");
                    %>
                </td>
                <td>
                    <%
                        bp.getController().writeFormInput(out, "findABeneServizio");
                    %>
                </td>
            </tr>
        </table>
</div>

</body>