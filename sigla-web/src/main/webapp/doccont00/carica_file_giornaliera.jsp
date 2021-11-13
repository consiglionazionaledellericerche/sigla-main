<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.doccont00.bp.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<title>Carica File Giornaliera</title>

<% JSPUtils.printBaseUrl(pageContext);%>
</head>
<% CaricaFileGiornalieraBP bp = (CaricaFileGiornalieraBP)BusinessProcess.getBusinessProcess(request); %>
<body class="Form">
<% bp.openFormWindow(pageContext); %>
    <span class="FormLabel h1 text-primary" style="color:blue">Carica Nuovo File xml per la Giornaliera di Cassa</span>
	<div class="Group card">
        <table style="width:100%">
            <tr>
                <td>
                    <label class="custom-file">
                        <input type="file" name="fileGiornaliera" onchange="inputFileName(this);"
                            class="custom-file-input"
                            maxlength="400"
                            size="40" onclick="cancelBubble(event)">
                            <span id="span-fileGiornaliera" class="custom-file-control" title="Scegli file..."></span>
                    </label>
                </td>
                <td>
                    <% JSPUtils.button(out,
                        bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-2x fa-cloud-upload" : null,
                        bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-2x fa-cloud-upload" : null,
                        "Invia File",
                        "javascript:submitForm('doCaricaFile')",
                        "btn-outline-primary btn-title btn-sm ml-2",
                        true,
                        bp.getParentRoot().isBootstrap()); %>
                </td>
            </tr>
        </table>
	</div>
<%bp.closeFormWindow(pageContext); %>
</body>