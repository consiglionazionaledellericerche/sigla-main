<!-- 
 ?ResourceName ""
 ?ResourceTimestamp ""
 ?ResourceEdition ""
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.doccont00.bp.*,
		it.cnr.contab.doccont00.intcass.bulk.*"		
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
	<head>
		<script language="JavaScript" src="scripts/util.js"></script>
		<% JSPUtils.printBaseUrl(pageContext);%>
	</head>
	<script language="javascript" src="scripts/css.js"></script>
	<title>Dettaglio Totali Trasmessi</title>
	
	<body class="Form">
	<% ViewDettaglioTotaliBP bp = (ViewDettaglioTotaliBP)BusinessProcess.getBusinessProcess(request); %>	
	<% bp.openFormWindow(pageContext); %>
	<% Distinta_cassiereBulk distinta = (Distinta_cassiereBulk)bp.getModel(); %>		


<div class="shadow col-md-6 mx-auto mt-3 px-0">
    <table border=1 align=center class="w-100 table-bordered my-3">
        <tr></tr>
        <tr>
            <td class="text-primary font-weight-bold"><big>Totale Mandati Trasmessi</big></td>
            <td><% bp.getController().writeFormInput( out, "totStoricoMandatiTrasmessi"); %></td>
        </tr>
        <tr>
            <td><% bp.getController().writeFormLabel( out, "totStoricoMandatiPagamentoTrasmessi"); %></td>
            <td><% bp.getController().writeFormInput( out, "totStoricoMandatiPagamentoTrasmessi"); %></td>
        </tr>
        <tr>
            <td><% bp.getController().writeFormLabel( out, "totStoricoMandatiRegSospesoTrasmessi"); %></td>
            <td><% bp.getController().writeFormInput( out, "totStoricoMandatiRegSospesoTrasmessi"); %></td>
        </tr>
        <% if ( distinta.getCd_cds().equals( distinta.getCd_cds_ente() )) { %>
        <tr>
            <td><% bp.getController().writeFormLabel( out, "totStoricoMandatiAccreditamentoTrasmessi"); %></td>
            <td><% bp.getController().writeFormInput( out, "totStoricoMandatiAccreditamentoTrasmessi"); %></td>
        </tr>
        <%}%>
        <tr></tr>
        <tr>
            <td class="text-primary font-weight-bold"><big>Totale Reversali Trasmesse</big></td>
            <td><% bp.getController().writeFormInput( out, "totStoricoReversaliTrasmesse"); %></td>
        </tr>
        <tr>
            <td><% bp.getController().writeFormLabel( out, "totStoricoReversaliRegSospesoTrasmesse"); %></td>
            <td><% bp.getController().writeFormInput( out, "totStoricoReversaliRegSospesoTrasmesse"); %></td>
        </tr>
        <tr>
            <td><% bp.getController().writeFormLabel( out, "totStoricoReversaliRitenuteTrasmesse"); %></td>
            <td><% bp.getController().writeFormInput( out, "totStoricoReversaliRitenuteTrasmesse"); %></td>
        </tr>
        <% if ( !distinta.getCd_cds().equals( distinta.getCd_cds_ente() )) { %>
        <tr>
            <td><% bp.getController().writeFormLabel( out, "totStoricoReversaliTrasferimentoTrasmesse"); %></td>
            <td><% bp.getController().writeFormInput( out, "totStoricoReversaliTrasferimentoTrasmesse"); %></td>
        </tr>
        <%}%>
    </table>
</div>
</body>
</html>