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
<title>Stampa Bolla Scarico Magazzino</title>

<body class="Form">

<%	BulkBP bp = (BulkBP)BusinessProcess.getBusinessProcess(request);
	bp.openFormWindow(pageContext); %>

<div class="Group card p-2" style="width:100%">
		<table width="100%">
		    <tr>
                <td>
                    <%
                        bp.getController().writeFormLabel(out, "findUnitaOperativaAbilitata");
                    %>
                </td>
                <td colspan="3">
                    <%
                        bp.getController().writeFormInput(out, "findUnitaOperativaAbilitata");
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
                        bp.getController().writeFormLabel(out, "findUnitaOperativaDest");
                    %>
                </td>
                <td colspan="3">
                    <%
                        bp.getController().writeFormInput(out, "findUnitaOperativaDest");
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
			<tr>
                <td>
                    <%
                        bp.getController().writeFormLabel(out, "daData");
                    %>
                </td>
                <td>
                    <%
                        bp.getController().writeFormInput(out, "daData");
                    %>
                </td>
                <td class="pl-5">
                    <%
                        bp.getController().writeFormLabel(out, "aData");
                    %>
                </td>
                <td>
                    <%
                        bp.getController().writeFormInput(out, "aData");
                    %>
                </td>
            </tr>
            <tr>
                <td>
                    <%
                        bp.getController().writeFormLabel(out, "daNumBolla");
                    %>
                </td>
                <td>
                    <%
                        bp.getController().writeFormInput(out, "daNumBolla");
                    %>
                </td>
                <td class="pl-5">
                    <%
                        bp.getController().writeFormLabel(out, "aNumBolla");
                    %>
                </td>
                <td>
                    <%
                        bp.getController().writeFormInput(out, "aNumBolla");
                    %>
                </td>
            </tr>
         </table>
</div>

</body>