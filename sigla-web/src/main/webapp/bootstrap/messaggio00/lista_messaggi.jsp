<%@ page 
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*,it.cnr.jada.util.action.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<title>Messaggi</title>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
</head>
<body class="Form">

<% SelezionatoreListaBP bp = (SelezionatoreListaBP)BusinessProcess.getBusinessProcess(request);
	 bp.openForm(pageContext); %>

		<% bp.writeHTMLTable(pageContext,"100%","h-25"); %>
        <% bp.writeHTMLNavigator(out); %>
        <div class="form-group row">
            <label class="col-sm-3 col-form-label"><% bp.getController().writeFormLabel(out,"view", "soggetto"); %></label>
            <div class="col-sm-9">
              <% bp.getController().writeFormInput(out, "view", "soggetto"); %>
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-3 col-form-label"><% bp.getController().writeFormLabel(out,"view", "priorita"); %></label>
            <div class="col-sm-9">
              <% bp.getController().writeFormInput(out, "view", "priorita"); %>
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-3 col-form-label"><% bp.getController().writeFormLabel(out,"view", "corpo"); %></label>
            <div class="col-sm-9">
              <% bp.getController().writeFormInput(out, "view", "corpo"); %>
            </div>
        </div>
<%	bp.closeForm(pageContext); %>
</body>
</html>