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
	<title>Dettaglio Totali per Distinta</title>
	
	<body class="Form">
	<% ViewDettaglioTotaliBP bp = (ViewDettaglioTotaliBP)BusinessProcess.getBusinessProcess(request); %>	
	<% bp.openFormWindow(pageContext); %>
	<% Distinta_cassiereBulk distinta = (Distinta_cassiereBulk)bp.getModel(); %>			

<div class="shadow col-md-6 mx-auto mt-3 px-0">
    <table border=1 align=center class="w-100 table-bordered my-3">
    <!--
        <tr>
            <td colspan=2 align=center><b><big>Mandati</big></b></td></tr>
        </tr>
    -->
        <tr></tr>
        <tr>
            <td class="text-primary font-weight-bold"><big>Totale Mandati</big></td>
            <td><% bp.getController().writeFormInput( out, "totMandati"); %></td>
        </tr>
        <tr>
            <td><% bp.getController().writeFormLabel( out, "totMandatiPagamento"); %></td>
            <td><% bp.getController().writeFormInput( out, "totMandatiPagamento"); %></td>
        </tr>
        <tr>
            <td><% bp.getController().writeFormLabel( out, "totMandatiRegSospeso"); %></td>
            <td><% bp.getController().writeFormInput( out, "totMandatiRegSospeso"); %></td>
        </tr>
        <% if ( distinta.getCd_cds().equals( distinta.getCd_cds_ente() )) { %>
        <tr>
            <td><% bp.getController().writeFormLabel( out, "totMandatiAccreditamento"); %></td>
            <td><% bp.getController().writeFormInput( out, "totMandatiAccreditamento"); %></td>
        </tr>
        <%}%>
        <tr></tr>
        <tr>
            <td class="text-primary font-weight-bold"><big>Totale Mandati Annullati</big></td>
            <td><% bp.getController().writeFormInput( out, "totMandatiAnnullati"); %></td>
        </tr>

        <tr>
            <td><% bp.getController().writeFormLabel( out, "totMandatiPagamentoAnnullati"); %></td>
            <td><% bp.getController().writeFormInput( out, "totMandatiPagamentoAnnullati"); %></td>
        </tr>
        <tr>
            <td><% bp.getController().writeFormLabel( out, "totMandatiRegSospesoAnnullati"); %></td>
            <td><% bp.getController().writeFormInput( out, "totMandatiRegSospesoAnnullati"); %></td>
        </tr>
        <% if ( distinta.getCd_cds().equals( distinta.getCd_cds_ente() )) { %>
        <tr>
            <td><% bp.getController().writeFormLabel( out, "totMandatiAccreditamentoAnnullati"); %></td>
            <td><% bp.getController().writeFormInput( out, "totMandatiAccreditamentoAnnullati"); %></td>
        </tr>
        <%}%>

        <tr></tr>
        <tr>
            <td class="text-primary font-weight-bold"><big>Totale Reversali</big></td>
            <td><% bp.getController().writeFormInput( out, "totReversali"); %></td>
        </tr>
        <tr>
            <td><% bp.getController().writeFormLabel( out, "totReversaliRegSospesoCC"); %></td>
            <td><% bp.getController().writeFormInput( out, "totReversaliRegSospesoCC"); %></td>
        </tr>
        <tr>
            <td><% bp.getController().writeFormLabel( out, "totReversaliRegSospesoBI"); %></td>
            <td><% bp.getController().writeFormInput( out, "totReversaliRegSospesoBI"); %></td>
        </tr>
        <tr>
            <td><% bp.getController().writeFormLabel( out, "totReversaliRitenute"); %></td>
            <td><% bp.getController().writeFormInput( out, "totReversaliRitenute"); %></td>
        </tr>
        <% if ( !distinta.getCd_cds().equals( distinta.getCd_cds_ente() )) { %>
        <tr>
            <td><% bp.getController().writeFormLabel( out, "totReversaliTrasferimento"); %></td>
            <td><% bp.getController().writeFormInput( out, "totReversaliTrasferimento"); %></td>
        </tr>
        <%}%>
        <tr></tr>
        <tr>
            <td class="text-primary font-weight-bold"><big>Totale Reversali Annullate</big></td>
            <td><% bp.getController().writeFormInput( out, "totReversaliAnnullate"); %></td>
        </tr>

        <tr>
            <td><% bp.getController().writeFormLabel( out, "totReversaliRegSospesoCCAnnullate"); %></td>
            <td><% bp.getController().writeFormInput( out, "totReversaliRegSospesoCCAnnullate"); %></td>
        </tr>
        <tr>
            <td><% bp.getController().writeFormLabel( out, "totReversaliRegSospesoBIAnnullate"); %></td>
            <td><% bp.getController().writeFormInput( out, "totReversaliRegSospesoBIAnnullate"); %></td>
        </tr>
        <tr>
            <td><% bp.getController().writeFormLabel( out, "totReversaliRitenuteAnnullate"); %></td>
            <td><% bp.getController().writeFormInput( out, "totReversaliRitenuteAnnullate"); %></td>
        </tr>

        <% if ( !distinta.getCd_cds().equals( distinta.getCd_cds_ente() )) { %>
        <tr>
            <td><% bp.getController().writeFormLabel( out, "totReversaliTrasferimentoAnnullate"); %></td>
            <td><% bp.getController().writeFormInput( out, "totReversaliTrasferimentoAnnullate"); %></td>
        </tr>
        <%}%>

    </table>
</div>
</body>
</html>