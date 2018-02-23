<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.util.jsp.*,
	    it.cnr.contab.incarichi00.bulk.*,
	    it.cnr.contab.incarichi00.bp.CRUDIncarichiRichiestaBP"
%>

<%
	CRUDIncarichiRichiestaBP bp = (CRUDIncarichiRichiestaBP)BusinessProcess.getBusinessProcess(request);
 	Incarichi_richiestaBulk  model = (Incarichi_richiestaBulk)bp.getModel();
%>
<table>
    <tr>
      <td><% bp.getIncarichiProceduraColl().writeHTMLTable(pageContext,null,false,false,false,"100%","300px"); %></td>
    </tr>  	      
</table>
