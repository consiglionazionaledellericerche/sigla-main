 <%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.inventario00.tabrif.bulk.*,
		it.cnr.contab.inventario00.docs.bulk.*,
		it.cnr.contab.inventario00.bp.*"
%>

<% CRUDInventarioBeniBP bp = (CRUDInventarioBeniBP)BusinessProcess.getBusinessProcess(request);
   Inventario_beniBulk bene = (Inventario_beniBulk)bp.getModel(); %>
<script language="JavaScript">
function doScaricaFile() {
	doPrint('<%=JSPUtils.getAppRoot(request)%>genericdownload/provvedimentoDenuncia?methodName=scaricaAllegato&it.cnr.jada.action.BusinessProcess=<%=bp.getPath()%>');
}
</script>
    <table>
	<div class="Group">	
		<tr>
			<td>
				<% bp.getController().writeFormLabel(out,"stato"); %>
			</td>
			<td>
				<% bp.getController().writeFormInput(out,"stato"); %>
			</td>
		</tr>
		<% if (bene!=null && bene.isBeneSmarrito()) { %>
            <tr>
                <td colspan=2><% bp.getController().writeFormInput(out,"default","attivaFile_blob"); %></td>
            </tr>
        <% } %>
	</table>
   </div>	 
