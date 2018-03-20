<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
	        it.cnr.jada.action.*,
	        java.util.*, 
	        it.cnr.jada.util.action.*"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<% JSPUtils.printBaseUrl(pageContext);%>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
</head>
<title>Gestione CDR</title>
<body class="Form">

	<% CRUDBP bp = (CRUDBP)BusinessProcess.getBusinessProcess(request);
	 bp.openFormWindow(pageContext); %>
	<div class="card p-2">
		<table class="Panel w-100">
			<tr>
				<td>
					<% bp.getController().writeFormLabel( out, "cd_unita_organizzativa"); %>
				</td>
				<td>
					<% bp.getController().writeFormInputByStatus( out, "find_unita_padre");%>
				</td>
			</tr>
			<tr>
				<td>
					<% bp.getController().writeFormLabel( out, "cd_proprio_cdr"); %>
				</td>
				<td>
					<% bp.getController().writeFormInputByStatus( out, "cd_proprio_cdr"); %>
				</td>
			</tr>
			<tr>
				<td>
					<% bp.getController().writeFormLabel( out, "esercizio_inizio"); %>
				</td>
				<td>
					<% bp.getController().writeFormInputByStatus( out, "esercizio_inizio"); %>
				</td>
			</tr>

			<tr>
			<tr>
				<td>
					<% bp.getController().writeFormLabel( out, "cd_centro_responsabilita"); %>
				</td>
				<td>
					<% bp.getController().writeFormInput( out, "cd_centro_responsabilita"); %>
				</td>
			</tr>
			<tr>
				<td>
					<% bp.getController().writeFormLabel( out, "livello"); %>
				</td>
				<td>
					<% bp.getController().writeFormInput( out, "livello"); %>
				</td>
			</tr>
			<tr>
				<td>
					<% bp.getController().writeFormLabel( out, "cd_cdr_afferenza"); %>
				</td>
				<td>
					<% bp.getController().writeFormInput( out, "cd_cdr_afferenza"); %>
				</td>
			</tr>
			<tr>
				<td>
					<% bp.getController().writeFormLabel( out, "ds_cdr"); %>
				</td>
				<td>
					<% bp.getController().writeFormInput( out, "ds_cdr"); %>
				</td>
			</tr>
			<tr>
				<td>
					<% bp.getController().writeFormLabel( out, "cd_responsabile"); %>
				</td>
				<td> 
					<% bp.getController().writeFormInput( out, "find_responsabile");%>
				</td>
			</tr>
			<tr>
				<td>
					<% bp.getController().writeFormLabel( out, "indirizzo"); %>
				</td>
				<td>
					<% bp.getController().writeFormInput( out, "indirizzo"); %>
				</td>
			</tr>
			<tr>
				<td>
					<% bp.getController().writeFormLabel( out, "esercizio_fine"); %>
				</td>
				<td>
					<% bp.getController().writeFormInput( out, "esercizio_fine"); %>
				</td>
			</tr>
		</table>
	</div>
	<%	bp.closeFormWindow(pageContext); %>
</body>