<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.incarichi00.bulk.*,
		it.cnr.contab.incarichi00.bp.*"
%>
<%
	CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)BusinessProcess.getBusinessProcess(request);
	Incarichi_procedura_noteBulk nota = (Incarichi_procedura_noteBulk)bp.getCrudNote().getModel();

	SimpleDetailCRUDController controller = bp.getCrudNote();

	controller.writeHTMLTable(pageContext,null,true,false,true,"100%","100px"); 
%>

<div class="card m-2 p-2">
<table class="Panel" cellspacing=2>
	<tr>
        <td><% controller.writeFormLabel(out,"nota"); %></td>
        <td colspan=4><% controller.writeFormInput(out,"default","nota", !(nota!=null && nota.isToBeCreated()),null,null); %></td>
	</tr>
</table>
</div>