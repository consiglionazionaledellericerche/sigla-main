<%@page import="it.cnr.contab.ordmag.magazzino.bulk.ParametriSelezioneMovimentiBulk"%>
<%@page import="it.cnr.contab.ordmag.magazzino.bp.ParametriSelezioneMovimentiMagBP"%>
<%@page import="it.cnr.contab.ordmag.magazzino.bulk.CaricoMagazzinoRigaBulk"%>
<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*"
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="javascript" src="scripts/css.js"></script>
<script language="JavaScript" src="scripts/util.js"></script>


<title>Parametri di Ricerca Movimenti di Magazzino</title>

</head>
<body class="Form">
<%	ParametriSelezioneMovimentiMagBP bp = (ParametriSelezioneMovimentiMagBP)BusinessProcess.getBusinessProcess(request);
	ParametriSelezioneMovimentiBulk parametri = (ParametriSelezioneMovimentiBulk)bp.getModel();
	bp.openFormWindow(pageContext); %>

	<div class="Group card p-2 mt-2" style="width:100%">
		<table>
			<tr>
				<td>
					<%
						bp.getController().writeFormLabel(out, "findUnitaOperativaOrd");
					%>
				</td>
				<td colspan="5">
					<%
						bp.getController().writeFormInput(out, "findUnitaOperativaOrd");
					%>
				</td>
			</tr>
			<tr>
				<td>
					<%
						bp.getController().writeFormLabel(out, "findMagazzino");
					%>
				</td>
				<td colspan="5">
					<%
						bp.getController().writeFormInput(out, "findMagazzino");
					%>
				</td>
			</tr>
		</table>
	</div>

	<div class="Group card p-2" style="width:100%">
		<table width="100%">
			<tr>
				<td>
					<%
						bp.getController().writeFormLabel(out, "daDataCompetenza");
					%>
				</td>
				<td>
					<%
						bp.getController().writeFormInput(out, "daDataCompetenza");
					%>
				</td>
				<td class="pl-5">
					<%
						bp.getController().writeFormLabel(out, "aDataCompetenza");
					%>
				</td>
				<td>
					<%
						bp.getController().writeFormInput(out, "aDataCompetenza");
					%>
				</td>
			</tr>
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
						bp.getController().writeFormLabel(out, "findDaUnitaOperativaOrd");
					%>
				</td>
				<td>
					<%
						bp.getController().writeFormInput(out, "findDaUnitaOperativaOrd");
					%>
				</td>
				<td class="pl-5">
					<%
						bp.getController().writeFormLabel(out, "findAUnitaOperativaOrd");
					%>
				</td>
				<td>
					<%
						bp.getController().writeFormInput(out, "findAUnitaOperativaOrd");
					%>
				</td>
			</tr>
			<tr>
				<td>
					<%
						bp.getController().writeFormLabel(out, "findTipoMovimentoMag");
					%>
				</td>
				<td>
					<%
						bp.getController().writeFormInput(out, "findTipoMovimentoMag");
					%>
				</td>
				<td class="pl-5">
					<%
						bp.getController().writeFormLabel(out, "tipoMovimento");
					%>
				</td>
				<td>
					<%
						bp.getController().writeFormInput(out, "tipoMovimento");
					%>
				</td>
			</tr>
			<tr>
				<td>
					<%
						bp.getController().writeFormLabel(out, "findTerzo");
					%>
				</td>
				<td>
					<%
						bp.getController().writeFormInput(out, "findTerzo");
					%>
				</td>
				<td class="pl-5">
					<%
						bp.getController().writeFormLabel(out, "lottoFornitore");
					%>
				</td>
				<td>
					<%
						bp.getController().writeFormInput(out, "lottoFornitore");
					%>
				</td>
			</tr>
			<tr>
				<td>
					<%
						bp.getController().writeFormLabel(out, "dataBolla");
					%>
				</td>
				<td>
					<%
						bp.getController().writeFormInput(out, "dataBolla");
					%>
				</td>
				<td class="pl-5">
					<%
						bp.getController().writeFormLabel(out, "numeroBolla");
					%>
				</td>
				<td>
					<%
						bp.getController().writeFormInput(out, "numeroBolla");
					%>
				</td>
			</tr>
		</table>
	</div>
	<% bp.closeFormWindow(pageContext); %>
</body>