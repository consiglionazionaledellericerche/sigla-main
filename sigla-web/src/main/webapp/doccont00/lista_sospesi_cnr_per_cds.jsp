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
<% JSPUtils.printBaseUrl(pageContext);%>
</head>
<script language="javascript" src="scripts/css.js"></script>

<% ListaSospesiCNRPerCdsBP bp = (ListaSospesiCNRPerCdsBP)BusinessProcess.getBusinessProcess(request); %>

<title>Ricerca Sospesi CNR</title>

<body class="Form">

<% bp.openFormWindow(pageContext); %>
	<table align="center" class="Panel">
		<tr>
			<td>
                <% JSPUtils.button(out,
                        bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-2x fa-search" : bp.encodePath("img/find24.gif"),
                        bp.encodePath("img/find24.gif"),
                        "Ricerca",
                        "javascript:submitForm('doCercaSospesiCNR')",
                        "btn-outline-primary btn-title",
                        true,
                        bp.getParentRoot().isBootstrap()); %>
			</td>
		</tr>
	</table>
	<table align="center" class="Panel">
		<tr>
		<td>
            <fieldset>
                <legend class="GroupLabel h2 text-primary">Entrata/Spesa</legend>
                <table align="center" class="Panel card p-2">
                    <tr>
                       <td><% bp.getController().writeFormInput( out, "ti_entrata_spesa"); %></td>
                    </tr>
                </table>
            </fieldset>
		</td>
		<td>
            <fieldset>
                <legend class="GroupLabel h2 text-primary">Stato Sospesi</legend>
                <table align="center" class="Panel card p-2">
                    <tr>
                       <td><% bp.getController().writeFormInput( out, "ricercaSospesiAssegnati"); %></td>
                       <td><% bp.getController().writeFormLabel( out, "ricercaSospesiAssegnati"); %></td>
                    </tr>
                    <tr>
                       <td><% bp.getController().writeFormInput( out, "ricercaSospesiInSospesoSelezionati"); %></td>
                       <td><% bp.getController().writeFormLabel( out, "ricercaSospesiInSospesoSelezionati"); %></td>
                    </tr>
                    <tr>
                       <td><% bp.getController().writeFormInput( out, "ricercaSospesiInSospeso"); %></td>
                       <td><% bp.getController().writeFormLabel( out, "ricercaSospesiInSospeso"); %></td>
                    </tr>
                    <tr>
                       <td><% bp.getController().writeFormInput( out, "ricercaSospesiRiaccredito"); %></td>
                       <td><% bp.getController().writeFormLabel( out, "ricercaSospesiRiaccredito"); %></td>
                    </tr>
                </table>
            </fieldset>
		</td>
		</tr>
	</table>
<%bp.closeFormWindow(pageContext); %>
</body>
</html>