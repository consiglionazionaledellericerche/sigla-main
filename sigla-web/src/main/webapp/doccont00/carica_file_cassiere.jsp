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
<title>Carica File Cassiere</title>

<% JSPUtils.printBaseUrl(pageContext);%>
</head>


<% CaricaFileCassiereBP bp = (CaricaFileCassiereBP)BusinessProcess.getBusinessProcess(request); %>


<body class="Form">


<% bp.openFormWindow(pageContext); %>

    <span class="FormLabel h1 text-primary" style="color:blue">Carica Nuovo File</span>
	<div class="Group card">
        <table style="width:100%">
            <tr>
                <td>
                    <label class="custom-file">
                        <input type="file" name="fileCassiere" onchange="inputFileName(this);"
                            class="custom-file-input"
                            maxlength="400"
                            size="40" onclick="cancelBubble(event)">
                            <span id="span-fileCassiere" class="custom-file-control" title="Scegli file..."></span>
                    </label>
                </td>
                <td>
                    <% JSPUtils.button(out,
                        bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-2x fa-cloud-upload" : "",
                        bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-2x fa-cloud-upload" : "",
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