<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.*,
		it.cnr.contab.doccont00.bp.*,
		it.cnr.contab.util00.bp.*"
%>
<%
	AllegatiDocContBP bp = (AllegatiDocContBP)BusinessProcess.getBusinessProcess(request);
%>
<script language="JavaScript">
function doScaricaFile() {
  doPrint('<%=JSPUtils.getAppRoot(request)%>genericdownload/<%=bp.getNomeAllegatoAmministrativo()!=null?bp.getNomeAllegatoAmministrativo().replace("'", "_"):""%>?methodName=scaricaAllegatoAmministrativo&it.cnr.jada.action.BusinessProcess=<%=bp.getPath()%>');
}
</script>
<%
    bp.getDocumentiPassiviSelezionati().writeHTMLTable(
        pageContext,
        null,
        false,false,false,
        "100%","200px", true); %>
<div class="GroupLabel h2 text-primary pl-2">Allegati</div>
<div class="Group card p-1">
    <%
        bp.getDettaglioAllegati().writeHTMLTable(
            pageContext,
            null,
            false,false,false,
            "100%","300px", true); %>
    <div class="Group card p-2 mt-1">
      <table>
        <% bp.getDettaglioAllegati().writeForm(out, "readonly");  %>
      </table>
    </div>
</div>
