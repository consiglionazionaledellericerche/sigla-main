<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
	        it.cnr.jada.action.*,
	        java.util.*, 
	        it.cnr.jada.util.action.*,
	        it.cnr.contab.config00.bp.*"
%>


<%  
		CRUDConfigCdSBP bp = (CRUDConfigCdSBP)BusinessProcess.getBusinessProcess(request);
		it.cnr.contab.config00.sto.bulk.CdsBulk cds = (it.cnr.contab.config00.sto.bulk.CdsBulk)bp.getModel();
%>

<table border="0" cellspacing="0" cellpadding="2" align="center">

		<tr>
			<td colspan=2>
				  <b ALIGN="CENTER"><font size=2>Percentuali</font></b>
			      <% bp.getPercentuali().writeHTMLTable(pageContext,null,bp.isAddPercentualeButtonEnaled(),false,false,"100%","100px", true); %>
			</td>
		</tr>
		<tr>
		   <td><% bp.getPercentuali().writeFormLabel(out,"esercizio"); %></td>
		   <td><% bp.getPercentuali().writeFormInput( out,"default","esercizio",true,null,null); %></td>			      
		</tr>
		<tr>		
		   <td><% bp.getPercentuali().writeFormLabel(out,"prc_copertura_obblig_2"); %></td>
		   <td><% bp.getPercentuali().writeFormInput( out,"default","prc_copertura_obblig_2",!bp.isPercentualeFieldEnaled(),null,null); %></td>			      
		</tr>
		<tr>		
		   <td><% bp.getPercentuali().writeFormLabel(out,"prc_copertura_obblig_3"); %></td>
		   <td><% bp.getPercentuali().writeFormInput( out,"default","prc_copertura_obblig_3",!bp.isPercentualeFieldEnaled(),null,null); %></td>			      
		</tr>		   
</table>