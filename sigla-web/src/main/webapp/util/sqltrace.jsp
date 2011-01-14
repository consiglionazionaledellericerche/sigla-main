<!-- 
 ?ResourceName ".jsp"
 ?ResourceTimestamp ""
 ?ResourceEdition "1.0"
-->

<%@ page 
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*,it.cnr.jada.util.action.*"
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">

<html>
<head>
<script language="JavaScript" src="scripts/util.js"></script>
<script language="javascript" src="scripts/css.js"></script>
<% JSPUtils.printBaseUrl(pageContext); %>
</head>
<title>Gestione trace SQL</title>
<body class="Form">

<% it.cnr.contab.devutil00.bp.SQLTraceBP bp = (it.cnr.contab.devutil00.bp.SQLTraceBP)BusinessProcess.getBusinessProcess(request);
	 it.cnr.contab.devutil00.bulk.SQLTracerBulk tracer = (it.cnr.contab.devutil00.bulk.SQLTracerBulk)bp.getModel();
	 bp.openFormWindow(pageContext); %>
	<table class="Panel">
		<tr>
			<td><% bp.getController().writeFormLabel(out,"sqlTracerEnabled"); %></td>
			<td><% bp.getController().writeFormInput(out,null,"sqlTracerEnabled",false,null,"onclick=\"if (disableDblClick()) submitForm('doApplica')\""); %></td>
		</tr>
		<tr>
			<td><% bp.getController().writeFormLabel(out,"dumpStackTraceEnabled"); %></td>
			<td><% bp.getController().writeFormInput(out,null,"dumpStackTraceEnabled",false,null,"onclick=\"if (disableDblClick()) submitForm('doApplica')\""); %></td>
		</tr>
	</table>
	<table class="Panel" height="400px">
		<tr><td colspan="2"><span class="FormLabel">Utenti tracciati</span></td></tr>
		<tr>
			<td><input class="FormInput" type="text" name="newTraceUser"></td>
			<td><input type="submit" name="comando.doAddUser" value="Aggiungi" style="width:7em"></td></tr>
		<tr valign="top">
			<td>
				<select class="FormInput" name="traceUsers" multiple style="width:100%">
				<% for (int i = 0;tracer.getTraceUsers() != null && i < tracer.getTraceUsers().length;i++) { %>
					<option><%= tracer.getTraceUsers()[i]%>
				<% }%>
				</select>
			<td><input type="submit" name="comando.doRemoveUsers" value="Rimuovi" style="width:7em"></td></tr>
		<tr><td colspan="2"><span class="FormLabel">Trace</span></td></tr>
		<tr><td colspan="2" height="100%">
			<div class="ScrollPane" style="overflow:auto;width:400px;height:100%">
			<pre>
<%	if (tracer.getTraceReader() != null) {
                                                      try {
								java.io.BufferedReader reader = new java.io.BufferedReader(tracer.getTraceReader());
								String line;
								while((line = reader.readLine()) != null)
									out.println(line);
						      } catch (java.io.EOFException e) {}
					     } %></pre>
			</div>
		</td></tr>
		<tr><td colspan="2"><input type="submit" name="comando.doAggiorna" value="Aggiorna"></td></tr>
	</table>

<%	bp.closeFormWindow(pageContext); %>
</body>