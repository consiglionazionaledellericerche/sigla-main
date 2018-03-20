<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.fondecon00.bp.*,
		it.cnr.contab.fondecon00.core.bulk.*"
%>

<html>

<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="javascript" src="scripts/css.js"></script>
<script language="JavaScript" src="scripts/util.js"></script>
<title>Ricerca Spese del Fondo Economale</title>
</head>

<body class="Form">
<%	RicercaSpeseBP bp = (RicercaSpeseBP)BusinessProcess.getBusinessProcess(request);
	Filtro_ricerca_speseVBulk filtro = (Filtro_ricerca_speseVBulk)bp.getModel();

	bp.openFormWindow(pageContext); %>

	<TABLE class="Panel">
		<TR>
			<% bp.getController().writeFormField(out,"default","pg_fondo_spesa"); %>
		</TR>
		<TR>
			<% bp.getController().writeFormField(out,"default","fl_documentata"); %>
		</TR>
		<TR>
			<% bp.getController().writeFormField(out,"default","im_ammontare_spesa"); %>
			<% bp.getController().writeFormField(out,"default","dt_spesa"); %>
		</TR>
		<TR>
			<TD colspan="4">
				<DIV class="GroupLabel">Dati Fornitore</div><div class="Group">
					<TABLE class="Panel">
						<TR>
							<TD><% bp.getController().writeFormLabel(out,"default","denominazione_fornitore"); %></TD>
							<TD colspan="3"><% bp.getController().writeFormInput(out,"default","denominazione_fornitore"); %></TD>
						</TR>
						<TR>
							<% bp.getController().writeFormField(out,"default","codice_fiscale"); %>
							<% bp.getController().writeFormField(out,"default","partita_iva"); %>
						</TR>
					</TABLE>
				</DIV>
			</TD>
		</TR>
	</TABLE>

	<% bp.closeFormWindow(pageContext); %>
</body>