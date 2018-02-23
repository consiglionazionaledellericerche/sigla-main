<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
	        it.cnr.jada.action.*,
	        java.util.*,
	        it.cnr.jada.util.action.*,
	        it.cnr.contab.config00.bp.*"
%>
<%
	CRUDConfigUnitaOrgBP bp = (CRUDConfigUnitaOrgBP)BusinessProcess.getBusinessProcess(request);
	SimpleDetailCRUDController controller = ( (CRUDConfigUnitaOrgBP)bp ).getCrudAssUoArea();
    controller.writeHTMLTable(pageContext,"associazioneUoArea",true,false,true,"100%","100px"); 
%>

<table class="Panel" cellspacing=2>
	<BR>
	<tr>
		<td><% controller.writeFormLabel(out,"default","find_cds_area_ricerca"); %></td>				 
		<td>
		    <% controller.writeFormInput(out,"default","find_cds_area_ricerca"); %>
		</td>			 
		<td><% controller.writeFormInput(out,"default","crea_cds_area_ricerca"); %></td>			 
	</tr>
	<tr>
  	    <td><% controller.writeFormLabel(out,"default","fl_presidente_area"); %></td>
		<td><% controller.writeFormInput(out,"default","fl_presidente_area"); %></td>
	</tr>	
</table>