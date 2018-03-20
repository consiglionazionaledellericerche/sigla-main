<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*,it.cnr.jada.util.action.*,it.cnr.contab.coepcoan00.bp.*,it.cnr.contab.coepcoan00.core.bulk.*"
%>
<%CRUDScritturaAnaliticaBP bp = (CRUDScritturaAnaliticaBP)BusinessProcess.getBusinessProcess(request);%>

<table class="Panel">
<tr>
	<td><% bp.getMovimenti().writeHTMLTable(pageContext,"latt", bp.isInserting(),false,bp.isInserting(),"100%","100px", true); %></td>
</tr>
</table>
<table class="Panel">
<tr>
	<td><% bp.getMovimenti().writeFormLabel(out, "cd_voce_ep"); %></td>
	<td><% bp.getMovimenti().writeFormInput(out, "cd_voce_ep");
	       bp.getMovimenti().writeFormInput(out, "ds_voce_ep");
	       bp.getMovimenti().writeFormInput(out,"default", "find_voce_ep", bp.isEditing(), null, null);
	       bp.getMovimenti().writeFormLabel(out, "sezione");
	       bp.getMovimenti().writeFormInput(out,"default", "sezione",bp.isEditing(),null,null);%></td>
</tr>
<tr>
	<td><% bp.getMovimenti().writeFormLabel(out, "cd_linea_attivita"); %></td>
	<td><% bp.getMovimenti().writeFormInput(out, "cd_centro_responsabilita");
		   bp.getMovimenti().writeFormInput(out, "cd_linea_attivita");
		   bp.getMovimenti().writeFormInput(out, "ds_linea_attivita");
		   bp.getMovimenti().writeFormInput(out,"default", "find_linea_attivita", bp.isEditing(), null, null);%></td>	
</tr>
<tr>
	<td><% bp.getMovimenti().writeFormLabel(out, "im_movimento"); %></td>
	<td><% bp.getMovimenti().writeFormInput(out,"default", "im_movimento", bp.isEditing(), null,null);%></td>	
</tr>

</table>