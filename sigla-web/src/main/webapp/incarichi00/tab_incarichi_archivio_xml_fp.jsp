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
%>
<script language="JavaScript">
function doScaricaFile(path) {	
   larghFinestra=800;
   altezFinestra=500;
   sinistra=300;
   alto=150;
   doOpenWindow(path,"DOWNLOAD","left="+sinistra+",top="+alto+",width="+larghFinestra+", height="+altezFinestra+",menubar=no,toolbar=no,location=no");
}
</script>
<table class="Panel">
	<tr>
		<td><% bp.getController().writeFormLabel( out, "id_archivio"); %></td>
		<td colspan=3><% bp.getController().writeFormInput( out, "id_archivio"); %></td>
	</tr>
	<tr>
		<td><% bp.getController().writeFormLabel( out, "ds_archivio"); %></td>
		<td colspan=3><% bp.getController().writeFormInput( out, "ds_archivio"); %></td>
	</tr>
	<tr>
		<td><% bp.getController().writeFormLabel( out, "fl_perla"); %></td>
		<td><% bp.getController().writeFormInput( out,"default", "fl_perla", true, null, null); %></td>
		<td><% bp.getController().writeFormLabel( out, "fl_merge_perla"); %>
			<% bp.getController().writeFormInput( out,"default", "fl_merge_perla", true, null, null); %></td>
	</tr>
	
	<% if (!bp.isInserting() && !bp.isSearching()) {%>
		<% if (bulk==null || bulk.getFl_merge_perla()==null || !bulk.getFl_merge_perla()) {%>
			<tr><td colspan=3>
				<fieldset class="fieldset">
					<legend class="GroupLabel">Incarichi Comunicati</legend>
					<fieldset class="fieldset">
						<legend class="GroupLabel" style="color:red">File XML </legend>
						<table class="Panel">
							<tr>
								<td><% bp.getController().writeFormLabel( out, "nome_file_inv"); %></td>
								<td><% bp.getController().writeFormInput( out, "nome_file_inv"); %></td>
								<% if (bulk!=null && bulk.getFile_inv()!=null) {%>
									<td align="left"><% JSPUtils.button(out,bp.encodePath("img/open24.gif"),bp.encodePath("img/open24.gif"),"","javascript:doScaricaFile('tmp/"+bulk.getNome_file_inv()+"')",true,bp.getParentRoot().isBootstrap()); %></td>
								<% } %>
							</tr>
						</table>
					</fieldset>
					<fieldset class="fieldset">
						<legend class="GroupLabel" style="color:red">Riepilogo</legend>
					   	<table class="Panel">
							<tr>
								<td colspan=3>
									<table>
										<tr>
										<td><% bp.getController().writeFormField( out, "countInsIncarichiComunicati"); %></td>
										<td><% bp.getController().writeFormField( out, "countUpdIncarichiComunicati"); %></td>
										<td><% bp.getController().writeFormField( out, "countDelIncarichiComunicati"); %></td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td colspan=3>
									<table>
										<tr>
										<td><% bp.getController().writeFormLabel( out, "totImportoNuoviIncarichiComunicato"); %></td>
										<td><% bp.getController().writeFormInput( out, "totImportoNuoviIncarichiComunicato"); %></td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</fieldset>
				</fieldset>
			</td></tr>
			<tr><td colspan=3>
				<fieldset class="fieldset">
					<legend class="GroupLabel">Incarichi Ricevuti</legend>
					<fieldset class="fieldset">
						<legend class="GroupLabel" style="color:red">File XML </legend>
						<table class="Panel">
							<tr>
								<td><% bp.getController().writeFormLabel( out, "nome_file_ric"); %></td>
								<td><% bp.getController().writeFormInput( out, "nome_file_ric"); %></td>
								<% if (bulk!=null && bulk.getFile_ric()!=null) {%>
									<td align="left"><% JSPUtils.button(out,bp.encodePath("img/open24.gif"),bp.encodePath("img/open24.gif"),"","javascript:doScaricaFile('tmp/"+bulk.getNome_file_ric()+"')",true,bp.getParentRoot().isBootstrap()); %></td>
								<% } %>
							</tr>
						</table>
					</fieldset>
					<fieldset class="fieldset">
						<legend class="GroupLabel" style="color:red">Riepilogo Incarichi Corretti</legend>
					   	<table class="Panel">
							<tr>
								<td colspan=3>
									<table>
										<tr>
											<td><% bp.getController().writeFormField( out, "countInsIncarichiOkRicevuti"); %></td>
											<td><% bp.getController().writeFormField( out, "countUpdIncarichiOkRicevuti"); %></td>
											<td><% bp.getController().writeFormField( out, "countDelIncarichiOkRicevuti"); %></td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td colspan=3>
									<table>
										<tr>
											<td><% bp.getController().writeFormLabel( out, "totImportoNuoviIncarichiOkRicevuti"); %></td>
											<td><% bp.getController().writeFormInput( out, "totImportoNuoviIncarichiOkRicevuti"); %></td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</fieldset>
					<fieldset class="fieldset">
						<legend class="GroupLabel" style="color:red">Riepilogo Incarichi Errati</legend>
					   	<table class="Panel">
							<tr>
								<td colspan=3>
									<table>
										<tr>
											<td><% bp.getController().writeFormField( out, "countInsIncarichiErrRicevuti"); %></td>
											<td><% bp.getController().writeFormField( out, "countUpdIncarichiErrRicevuti"); %></td>
											<td><% bp.getController().writeFormField( out, "countDelIncarichiErrRicevuti"); %></td>
										</tr>
									</table>
								</td>
							</tr>
							<tr>
								<td colspan=3>
									<table>
										<tr>
											<td><% bp.getController().writeFormLabel( out, "totImportoNuoviIncarichiErrRicevuti"); %></td>
											<td><% bp.getController().writeFormInput( out, "totImportoNuoviIncarichiErrRicevuti"); %></td>
										</tr>
									</table>
								</td>
							</tr>
						</table>
					</fieldset>
				</fieldset>
			</td></tr>
		<% } else {%>	
			<tr><td colspan=3>
				<fieldset class="fieldset">
					<legend class="GroupLabel">Merge con sistema PERLA</legend>
					<fieldset class="fieldset">
						<legend class="GroupLabel" style="color:red">File CSV</legend>
						<table class="Panel">
							<tr>
								<td><% bp.getController().writeFormLabel( out, "nome_file_ric"); %></td>
								<td><% bp.getController().writeFormInput( out, "nome_file_ric"); %></td>
								<% if (bulk!=null && bulk.getFile_ric()!=null) {%>
									<td align="left"><% JSPUtils.button(out,bp.encodePath("img/open24.gif"),bp.encodePath("img/open24.gif"),"","javascript:doScaricaFile('tmp/"+bulk.getNome_file_ric()+"')",true, bp.getParentRoot().isBootstrap()); %></td>
								<% } %>
							</tr>
						</table>
					</fieldset>
				</fieldset>
			</td></tr>
		<% } %>
	<% } else if (!bp.isSearching()) {%>	
		<% if (bulk==null || bulk.getFl_merge_perla()==null || !bulk.getFl_merge_perla()) {%>
			<tr>
		        <td><% bp.getController().writeFormLabel(out,"default","blob_inv"); %></td>
		        <td colspan=2><% bp.getController().writeFormInput(out,"default","blob_inv"); %></td>
			</tr>
		<% } %>
		<tr>
		    <td><% bp.getController().writeFormLabel(out,"default","blob_ric"); %></td>
		    <td colspan=2><% bp.getController().writeFormInput(out,"default","blob_ric"); %></td>
		</tr>
	<% } %>
</table>
