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
   doOpenWindow(path,"DOWNLOAD","left="+sinistra+",top="+alto+",width="+larghFinestra+", height="+altezFinestra+",menubar=no,toolbar=no,location=no");
}
</script>
	<table class="Panel">
	<tr>
	<td><% bp.getController().writeFormLabel( out, "esercizio"); %></td>
	<td><% bp.getController().writeFormInput( out, "esercizio"); %></td>
	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "dt_calcolo"); %></td>
	<td><% bp.getController().writeFormInput( out, "dt_calcolo"); %></td>
	</tr>
	<tr>
	<td><% bp.getController().writeFormLabel( out, "fl_crea_file_perla"); %></td>
	<td><% bp.getController().writeFormInput( out, "fl_crea_file_perla"); %></td>
	</tr>

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
	<% } %>
	</table>