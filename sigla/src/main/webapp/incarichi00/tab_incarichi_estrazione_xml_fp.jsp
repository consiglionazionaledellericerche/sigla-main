<!-- 
 ?ResourceName "TemplateForm.jsp"
 ?ResourceTimestamp "08/11/00 16.43.22"
 ?ResourceEdition "1.0"
-->

<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
	        it.cnr.jada.action.*,
	        java.util.*, 
	        it.cnr.jada.util.action.*,
			it.cnr.contab.incarichi00.xmlfp.bulk.Incarichi_archivio_xml_fpBulk"
%>
<%
	SimpleCRUDBP bp = (SimpleCRUDBP)BusinessProcess.getBusinessProcess(request);
    Incarichi_archivio_xml_fpBulk bulk = (Incarichi_archivio_xml_fpBulk)bp.getModel();
    int maxButton=5;
%>
<script language="JavaScript">
function doScaricaFile(path) {	
   larghFinestra=800;
   altezFinestra=500;
   sinistra=300;
   alto=150;
//   window.open("/SIGLA/RicercaIncarichiRichiesta.do?tipofile=1","DOWNLOAD","left="+sinistra+",top="+alto+",width="+larghFinestra+", height="+altezFinestra+",menubar=no,toolbar=no,location=no")
//   window.open("tmp/EstrazioneFp20091_1.xml","DOWNLOAD","left="+sinistra+",top="+alto+",width="+larghFinestra+", height="+altezFinestra+",menubar=no,toolbar=no,location=no")
   window.open(path,"DOWNLOAD","left="+sinistra+",top="+alto+",width="+larghFinestra+", height="+altezFinestra+",menubar=no,toolbar=no,location=no")
}
</script>
	<table class="Panel">
	<tr>
	<td><% bp.getController().writeFormLabel( out, "esercizio"); %></td>
	<td><% bp.getController().writeFormInput( out, "esercizio"); %></td>
	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "semestre"); %></td>
	<td><% bp.getController().writeFormInput( out, "semestre"); %></td>
	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "dt_calcolo"); %></td>
	<td><% bp.getController().writeFormInput( out, "dt_calcolo"); %></td>
	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "tipo_estrazione_pagamenti"); %></td>
	<td><% bp.getController().writeFormInput( out, "tipo_estrazione_pagamenti"); %></td>
	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "num_max_file_record"); %></td>
	<td><% bp.getController().writeFormInput( out, "num_max_file_record"); %></td>
	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "fl_crea_file_modifiche"); %></td>
	<td><% bp.getController().writeFormInput( out, "fl_crea_file_modifiche"); %></td>
	</tr>
    <% if (bulk!=null && !bulk.isROSelezione() && bulk.isFl_crea_file_modifiche()) { %> 
		<tr>
		<td><% bp.getController().writeFormLabel( out, "esercizio_inizio"); %></td>
		<td><% bp.getController().writeFormInput( out, "esercizio_inizio"); %></td>
		</tr>
		<tr>
		<td><% bp.getController().writeFormLabel( out, "semestre_inizio"); %></td>
		<td><% bp.getController().writeFormInput( out, "semestre_inizio"); %></td>
		</tr>
	<% } %>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "fl_crea_file_perla"); %></td>
	<td><% bp.getController().writeFormInput( out, "fl_crea_file_perla"); %></td>
	</tr>
    <% if (bulk!=null && !bulk.isFl_crea_file_perla()) { %>
		<tr>
		<td><% bp.getController().writeFormLabel( out, "fl_crea_file_per_tipologia"); %></td>
		<td><% bp.getController().writeFormInput( out, "fl_crea_file_per_tipologia"); %></td>
		</tr>
	<% } %>
		
    <% if (bulk!=null && !bulk.isROSelezione()) { %> 
		<tr>
		<td><% bp.getController().writeFormLabel( out, "fl_crea_file_da_file"); %></td>
		<td><% bp.getController().writeFormInput( out, "fl_crea_file_da_file"); %></td>
		</tr>
	    <% if (bulk.isFl_crea_file_da_file()) { %>
			<tr>
		    <td><% bp.getController().writeFormLabel(out,"default","blob_ric_err"); %></td>
		    <td><% bp.getController().writeFormInput(out,"default","blob_ric_err"); %></td>
			</tr>
		<% } %>
	<% } %>
	
    <% if (bulk!=null && !bulk.isROSelezione()) { %>
		<tr>
			<td align="center"> 
				<% JSPUtils.button(out,bp.encodePath("img/application-xml.png"),bp.encodePath("img/application-xml.png"),
						"Genera XML","javascript:submitForm('doGeneraXML')",true,
						bp.getParentRoot().isBootstrap()); %>
			</td>	
		    <% if (!bulk.isFl_crea_file_da_file()) { %>
				<td align="center"> 
					<% JSPUtils.button(out,bp.encodePath("img/configure.png"),bp.encodePath("img/configure.png"),
							"Seleziona Incarichi","javascript:submitForm('doSelezionaIncarichidaEstrarre')",true,
							bp.getParentRoot().isBootstrap()); %>
				</td>	
			<% } %>
		</tr>
	<% } else { %>
		<tr>
			<td align="center"> 
				<% JSPUtils.button(out,bp.encodePath("img/application-xml.png"),bp.encodePath("img/application-xml.png"),
						"Nuova Estrazione","javascript:submitForm('doClearSelection')",true,
						bp.getParentRoot().isBootstrap()); %>
			</td>	
			<td align="center"> 
				<% JSPUtils.button(out,bp.encodePath("img/zip.png"),bp.encodePath("img/zip.png"),"Archivio Estrazione",
						"javascript:doScaricaFile('"+bulk.getPathFileZip()+"')",true,
						bp.getParentRoot().isBootstrap()); %>
			</td>	
		</tr>		
<!--
		<tr>
			<td><% bp.getController().writeFormLabel( out, "fl_visualizza_file_xml"); %></td>
			<td><% bp.getController().writeFormInput( out, "fl_visualizza_file_xml"); %></td>
		</tr>
-->		
	<% } %>
	</table>

    <% if (bulk!=null && bulk.isROSelezione()) { %>
	<fieldset class="fieldset">
    <% if (bulk.isFl_visualizza_file_xml()) { %>
		<legend class="GroupLabel">Incarichi Estratti</legend>
		<fieldset class="fieldset">
			<legend class="GroupLabel" style="color:red">Riepilogo</legend>
	<% } else { %>
		<legend class="GroupLabel">Riepilogo Incarichi Estratti</legend>
	<% } %>
		   	<table class="Panel">
				<tr>
					<td>
						<table>
						<tr>
							<td><% bp.getController().writeFormField( out, "countInsAllIncarichiOkEstratti"); %></td>
							<td><% bp.getController().writeFormField( out, "countUpdAllIncarichiOkEstratti"); %></td>
							<td><% bp.getController().writeFormField( out, "countDelAllIncarichiOkEstratti"); %></td>
						</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>
						<table>
						<tr>
							<td><% bp.getController().writeFormField( out, "totImportoAllNuoviIncarichiOkEstratti"); %></td>
						</tr>
						</table>
					</td>
				</tr>
			</table>
    <% if (bulk.isFl_visualizza_file_xml()) { %>
		</fieldset>
	    <% if (bulk!=null && bulk.getElencoPathFileNuovoIncaricoXML()!=null && !bulk.getElencoPathFileNuovoIncaricoXML().isEmpty()) { %>
		<fieldset class="fieldset">
			<% if (bulk.isFl_crea_file_per_tipologia()) { %>
			<legend class="GroupLabel" style="color:red">File XML Incarichi Nuovi</legend>
		    <% } else { %>
			<legend class="GroupLabel" style="color:red">File XML</legend>
		    <% } %>
			<table class="Panel">
				<% int i=0;
				   for (java.util.Iterator iterator = bulk.getElencoPathFileNuovoIncaricoXML().iterator();iterator.hasNext();) { 
				   		String filePath = (String)iterator.next();%>
				   		<% i++;
						if (i==1) {%>
						<tr>
						<% } %>
				   		<% if (i<=maxButton) {%>
							<td> 
								<% JSPUtils.button(out,bp.encodePath("img/application-xml.png"),bp.encodePath("img/application-xml.png"),
										null,"javascript:doScaricaFile('"+filePath+"')",true,
										bp.getParentRoot().isBootstrap()); %>
					      	</td>
						<% } %>
				   		<% if (i==maxButton) {%>
						</tr>
						<%
						i=0;
			 			} %>
				<% } %>
			</table>
		</fieldset>
		<% } %>

	    <% if (bulk!=null && bulk.getElencoPathFileModificaIncaricoXML()!=null && !bulk.getElencoPathFileModificaIncaricoXML().isEmpty()) { %>
		<fieldset class="fieldset">
			<legend class="GroupLabel" style="color:red">File XML Incarichi Aggiornati</legend>
			<table class="Panel">
				<% int i=0;
				   for (java.util.Iterator iterator = bulk.getElencoPathFileModificaIncaricoXML().iterator();iterator.hasNext();) { 
				   		String filePath = (String)iterator.next();%>
				   		<% i++;
						if (i==1) {%>
						<tr>
						<% } %>
				   		<% if (i<=maxButton) {%>
							<td> 
								<% JSPUtils.button(out,bp.encodePath("img/application-xml.png"),bp.encodePath("img/application-xml.png"),
										null,"javascript:doScaricaFile('"+filePath+"')",true,
										bp.getParentRoot().isBootstrap()); %>
					      	</td>
						<% } %>
				   		<% if (i==maxButton) {%>
						</tr>
						<%
						i=0;
			 			} %>
				<% } %>
			</table>
		</fieldset>
		<% } %>

    	<% if (bulk!=null && bulk.getElencoPathFileCancellaIncaricoXML()!=null && !bulk.getElencoPathFileCancellaIncaricoXML().isEmpty()) { %>
		<fieldset class="fieldset">
			<legend class="GroupLabel" style="color:red">File XML Incarichi Cancellati</legend>
			<table class="Panel">
				<% int i=0;
				   for (java.util.Iterator iterator = bulk.getElencoPathFileCancellaIncaricoXML().iterator();iterator.hasNext();) { 
				   		String filePath = (String)iterator.next();%>
				   		<% i++;
						if (i==1) {%>
						<tr>
						<% } %>
				   		<% if (i<=maxButton) {%>
							<td> 
								<% JSPUtils.button(out,bp.encodePath("img/application-xml.png"),bp.encodePath("img/application-xml.png"),
										null,"javascript:doScaricaFile('"+filePath+"')",true,
										bp.getParentRoot().isBootstrap()); %>
					      	</td>
						<% } %>
				   		<% if (i==maxButton) {%>
						</tr>
						<%
						i=0;
			 			} %>
				<% } %>
			</table>
		</fieldset>
		<% } %>
	<% } %>
	</fieldset>
	<% } %>
    
    <% if (bulk!=null && (bulk.getElencoPathFileAnomalieNuovoIncaricoXML()!=null && !bulk.getElencoPathFileAnomalieNuovoIncaricoXML().isEmpty()) ||
    					 (bulk.getElencoPathFileAnomalieModificaIncaricoXML()!=null && !bulk.getElencoPathFileAnomalieModificaIncaricoXML().isEmpty()) ||
    					 (bulk.getElencoPathFileAnomalieCancellaIncaricoXML()!=null && !bulk.getElencoPathFileAnomalieCancellaIncaricoXML().isEmpty())) { %>
	<fieldset class="fieldset">
    <% if (bulk.isFl_visualizza_file_xml()) { %>
		<legend class="GroupLabel">Anomalie Incarichi Estratti</legend>
		<fieldset class="fieldset">
			<legend class="GroupLabel" style="color:red">Riepilogo</legend>
	<% } else { %>
		<legend class="GroupLabel">Riepilogo Anomalie Incarichi Estratti</legend>
	<% } %>
		   	<table class="Panel">
				<tr>
					<td>
						<table>
						<tr>
							<td><% bp.getController().writeFormField( out, "countInsIncarichiErrEstratti"); %></td>
							<td><% bp.getController().writeFormField( out, "countUpdIncarichiErrEstratti"); %></td>
							<td><% bp.getController().writeFormField( out, "countDelIncarichiErrEstratti"); %></td>
						</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td>
						<table>
						<tr>
							<td><% bp.getController().writeFormField( out, "totImportoNuoviIncarichiErrEstratti"); %></td>
						</tr>
						</table>
					</td>
				</tr>
			</table>
    <% if (bulk.isFl_visualizza_file_xml()) { %>
		</fieldset>
    
	    <% if (bulk!=null && bulk.getElencoPathFileAnomalieNuovoIncaricoXML()!=null && !bulk.getElencoPathFileAnomalieNuovoIncaricoXML().isEmpty()) { %>
		<fieldset class="fieldset">
			<legend class="GroupLabel" style="color:red">File XML Incarichi Nuovi Scartati</legend>
			<table class="Panel">
				<% int i=0;
				   for (java.util.Iterator iterator = bulk.getElencoPathFileAnomalieNuovoIncaricoXML().iterator();iterator.hasNext();) { 
				   		String filePath = (String)iterator.next();%>
				   		<% i++;
						if (i==1) {%>
						<tr>
						<% } %>
				   		<% if (i<=maxButton) {%>
							<td> 
								<% JSPUtils.button(out,bp.encodePath("img/application-xml.png"),bp.encodePath("img/application-xml.png"),
										null,"javascript:doScaricaFile('"+filePath+"')",true,
										bp.getParentRoot().isBootstrap()); %>
					      	</td>
						<% } %>
				   		<% if (i==maxButton) {%>
						</tr>
						<%
						i=0;
			 			} %>
				<% } %>
			</table>
		</fieldset>
		<% } %>
	
    	<% if (bulk!=null && bulk.getElencoPathFileAnomalieModificaIncaricoXML()!=null && !bulk.getElencoPathFileAnomalieModificaIncaricoXML().isEmpty()) { %>
		<fieldset class="fieldset">
			<legend class="GroupLabel" style="color:red">File XML Incarichi Aggiornati Scartati</legend>
			<table class="Panel">
				<% int i=0;
				   for (java.util.Iterator iterator = bulk.getElencoPathFileAnomalieModificaIncaricoXML().iterator();iterator.hasNext();) { 
				   		String filePath = (String)iterator.next();%>
				   		<% i++;
						if (i==1) {%>
						<tr>
						<% } %>
				   		<% if (i<=maxButton) {%>
							<td> 
								<% JSPUtils.button(out,bp.encodePath("img/application-xml.png"),bp.encodePath("img/application-xml.png"),
										null,"javascript:doScaricaFile('"+filePath+"')",true,
										bp.getParentRoot().isBootstrap()); %>
					      	</td>
						<% } %>
				   		<% if (i==maxButton) {%>
						</tr>
						<%
						i=0;
			 			} %>
				<% } %>
			</table>
		</fieldset>
		<% } %>

    	<% if (bulk!=null && bulk.getElencoPathFileAnomalieCancellaIncaricoXML()!=null && !bulk.getElencoPathFileAnomalieCancellaIncaricoXML().isEmpty()) { %>
		<fieldset class="fieldset">
			<legend class="GroupLabel" style="color:red">File XML Incarichi Cancellati Scartati</legend>
			<table class="Panel">
				<% int i=0;
				   for (java.util.Iterator iterator = bulk.getElencoPathFileAnomalieCancellaIncaricoXML().iterator();iterator.hasNext();) { 
				   		String filePath = (String)iterator.next();%>
				   		<% i++;
						if (i==1) {%>
						<tr>
						<% } %>
				   		<% if (i<=maxButton) {%>
							<td> 
								<% JSPUtils.button(out,bp.encodePath("img/application-xml.png"),bp.encodePath("img/application-xml.png"),
										null,"javascript:doScaricaFile('"+filePath+"')",true,
										bp.getParentRoot().isBootstrap()); %>
					      	</td>
						<% } %>
				   		<% if (i==maxButton) {%>
						</tr>
						<%
						i=0;
			 			} %>
				<% } %>
			</table>
		</fieldset>
		<% } %>
	<% } %>
	</fieldset>
	<% } %>

			
