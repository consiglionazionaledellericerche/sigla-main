<%@page import="it.cnr.contab.ordmag.ordini.bulk.ParametriSelezioneOrdiniAcqBulk"%>
<%@page import="it.cnr.contab.ordmag.ordini.bp.ParametriSelezioneOrdiniAcqBP"%>
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
<%	ParametriSelezioneOrdiniAcqBP bp = (ParametriSelezioneOrdiniAcqBP)BusinessProcess.getBusinessProcess(request);
	ParametriSelezioneOrdiniAcqBulk parametri = (ParametriSelezioneOrdiniAcqBulk)bp.getModel();
  bp.openFormWindow(pageContext);%>

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
						bp.getController().writeFormLabel(out, "daProgressivo");
					%>
				</td>
				<td>
					<%
						bp.getController().writeFormInput(out, "daProgressivo");
					%>
				</td>
				<td class="pl-5">
					<%
						bp.getController().writeFormLabel(out, "aProgressivo");
					%>
				</td>
				<td>
					<%
						bp.getController().writeFormInput(out, "aProgressivo");
					%>
				</td>
			</tr>

			<tr>
				<td>
					<%
						bp.getController().writeFormLabel(out, "findUnitaOperativaOrdine");
					%>
				</td>
				<td>
					<%
						bp.getController().writeFormInput(out, "findUnitaOperativaOrdine");
					%>
				</td>
				<td class="pl-5">
					<%
						bp.getController().writeFormLabel(out, "findNumerazioneOrd");
					%>
				</td>
				<td>
					<%
						bp.getController().writeFormInput(out, "findNumerazioneOrd");
					%>
				</td>
			</tr>
			<tr>
				<td>
					<%
						bp.getController().writeFormLabel(out, "daDataOrdine");
					%>
				</td>
				<td>
					<%
						bp.getController().writeFormInput(out, "daDataOrdine");
					%>
				</td>
				<td class="pl-5">
					<%
						bp.getController().writeFormLabel(out, "aDataOrdine");
					%>
				</td>
				<td>
					<%
						bp.getController().writeFormInput(out, "aDataOrdine");
					%>
				</td>
			</tr>
			<tr>
				<td>
					<%
						bp.getController().writeFormLabel(out, "daDataOrdineDef");
					%>
				</td>
				<td>
					<%
						bp.getController().writeFormInput(out, "daDataOrdineDef");
					%>
				</td>
				<td class="pl-5">
					<%
						bp.getController().writeFormLabel(out, "aDataOrdineDef");
					%>
				</td>
				<td>
					<%
						bp.getController().writeFormInput(out, "aDataOrdineDef");
					%>
				</td>
			</tr>
			<tr>
				<td>
					<%
						bp.getController().writeFormLabel(out, "daNumeroOrdine");
					%>
				</td>
				<td>
					<%
						bp.getController().writeFormInput(out, "daNumeroOrdine");
					%>
				</td>
				<td class="pl-5">
					<%
						bp.getController().writeFormLabel(out, "aNumeroOrdine");
					%>
				</td>
				<td>
					<%
						bp.getController().writeFormInput(out, "aNumeroOrdine");
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

			</tr>
			</tr>
		</table>
	</div>
	<% bp.closeFormWindow(pageContext); %>
</body>