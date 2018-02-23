<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.docamm00.tabrif.bulk.*,
		it.cnr.contab.docamm00.docs.bulk.*,
		it.cnr.contab.docamm00.bp.*"
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<html>
<head>
<% JSPUtils.printBaseUrl(pageContext); %>
<script language="javascript" src="scripts/css.js"></script>
<script language="JavaScript" src="scripts/util.js"></script>
<title>Risultato eliminazione</title>

</head>
<body class="Form">
<%	RisultatoEliminazioneBP bp = (RisultatoEliminazioneBP)BusinessProcess.getBusinessProcess(request);
	//Risultato_eliminazioneVBulk riltatoEliminazione = (Risultato_eliminazioneVBulk)bp.getModel();
	bp.openFormWindow(pageContext); %>

	<div class="Group" style="width:100%">
		<table width="100%">
			<tr>
			  	<td>
				  	<span class="FormLabel" style="color:black">Documenti amministrativi</span>
			  	</td>
			</tr>
			<tr>
			  	<td>
					<%	bp.getDocumentiAmministrativiController().writeHTMLTable(pageContext,"default",false,false,false,"100%","150px"); %>
			  	</td>
			</tr>
			<tr>
			  	<td>
				  	<% bp.writeFormInput(out, "modificaDocAmm");%>
			  	</td>
	   		</tr>
			<tr>
			  	<td>
					<span class="FormLabel" style="color:black">
						Non è stato possibile aggiornare in automatico i documenti contabili dei documenti amministrativi sopra elencati.
						Modificarli manualmente premendo il bottone "Gestisci".
					</span>
			  	</td>
			</tr>
		</table>
	</div>
	<div class="Group" style="width:100%">
   		<table width="100%">
			<tr>
			  	<td>
				  	<span class="FormLabel" style="color:black">Documenti contabili</span>
			  	</td>
			</tr>
			<tr>
			  	<td>
					<% bp.getDocumentiContabiliController().writeHTMLTable(pageContext,"default",false,false,false,"100%","150px"); %>
			  	</td>
			</tr>
			<tr>
			  	<td>
				  	<% bp.writeFormInput(out, "modificaDocCont");%>
			  	</td>
			</tr>
			<tr>
			  	<td>
					<span class="FormLabel" style="color:black">
						I documenti contabili sopra riportati erano stati creati per la nota di credito eliminata.
						E' possibile modificarli o aggiornarli premendo il bottone "Gestisci".
					</span>
			  	</td>
			</tr>
   		</table>
	</div>
	
<% bp.closeFormWindow(pageContext); %>
</body>