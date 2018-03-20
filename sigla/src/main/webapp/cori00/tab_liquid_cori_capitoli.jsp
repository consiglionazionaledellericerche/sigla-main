<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.cori00.docs.bulk.*,
		it.cnr.contab.cori00.bp.*"
%>
<% CRUDLiquidazioneCORIBP bp = (CRUDLiquidazioneCORIBP)BusinessProcess.getBusinessProcess(request);	
	 Liquid_coriBulk liquid_cori = (Liquid_coriBulk)bp.getModel(); %>

	<div class="Group">	
	<% if (bp.isTabCapitoliVisible()){ %>
		<table class="Panel">
	       <tr>
	    	<td>
			  <span class="FormLabel" style="color:blue">Gruppi:</span>
			</td>
	       </tr>
	       <tr>
	         <td>
			   	<% bp.getGruppi().writeHTMLTable(pageContext,"v_liquid_gruppo",false,false,false,"100%","200px"); %>
		     </td>
	       </tr>
	       <tr>
	    	<td>
			  <span class="FormLabel" style="color:blue">Capitoli:</span>
			</td>
	       </tr>
	       <tr>
	         <td>
			   	<% bp.getCapitoli().writeHTMLTable(pageContext,"default",false,false,false,"100%","200px"); %>
			 </td>
	       </tr>
	     </table>
	  <% } %>
	</div>