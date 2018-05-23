<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.incarichi00.bulk.*,
		it.cnr.contab.incarichi00.bp.*"
%>
<%
	CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)BusinessProcess.getBusinessProcess(request);
	SimpleDetailCRUDController controller = bp.getIncarichiRappColl();
	Incarichi_repertorioBulk incarico = (Incarichi_repertorioBulk)bp.getIncarichiColl().getModel();

	Incarichi_repertorio_rappBulk rapporto;

	// Recupero il valore (posizione) del record selezionato
	int  sel = controller.getSelection().getFocus();

	/*
	** Quando navigo la prima volta nella tab e non ci sono 
	** record selezionati, il valore restituito è -1. 
	** In questo caso imposto il valore a 0.
	*/
	if (sel == -1)
		rapporto = null;
	else
		rapporto = (Incarichi_repertorio_rappBulk)controller.getModel();	   

	boolean isEditable = false;
	if (incarico!=null && rapporto!=null)
		if (rapporto.isToBeCreated() && !bp.isSearching()&&!rapporto.isAnnullato())
			isEditable = true;
	
	controller.writeHTMLTable(pageContext,"dichiarazione",true,false,true,"100%","60px");
%>
<script language="JavaScript">
function doScaricaFile() {	
   larghFinestra=5;
   altezFinestra=5;
   sinistra=(screen.width)/2;
   alto=(screen.height)/2;
   window.open("<%= (rapporto==null?null: JSPUtils.getAppRoot(request) + rapporto.getDownloadUrl()) %>","DOWNLOAD","left="+sinistra+",top="+alto+",width="+larghFinestra+", height="+altezFinestra+",menubar=no,toolbar=no,location=no")
}
</script>
<div class="card">
<table class="Panel m-2 p-2" cellspacing=2>
	<tr>
	    <td><% controller.writeFormLabel(out,"default","anno_competenza"); %></td>
	    <td><% controller.writeFormInput(out,"default","anno_competenza"); %></td>
	</tr>
	<tr>
	    <td><% controller.writeFormLabel(out,"default","dt_dichiarazione"); %></td>
	    <td><% controller.writeFormInput(out,"default","dt_dichiarazione", !isEditable,null,null); %></td>
	</tr>
	<tr>
	    <td><% controller.writeFormLabel(out,"default","fl_altri_rapporti"); %></td>
	    <td><% controller.writeFormInput(out,"default","fl_altri_rapporti", !isEditable,null,null); %></td>
	</tr>
   
   	<% if (!bp.isSearching() && rapporto!=null && !rapporto.isAnnullato() && isEditable) { %>
	<tr>
        <td><% controller.writeFormLabel(out,"blob"); %></td>
        <td colspan=4><% controller.writeFormInput(out,"blob"); %></td>
    </tr>
	<% } %>
	<tr>
        <td><% controller.writeFormLabel(out,"nome_file"); %></td>
        <td colspan=4><% controller.writeFormInput(out,"nome_file"); %>
	<% if (rapporto!=null && !rapporto.isToBeCreated()) {
			controller.writeFormField(out,"attivaFile_blob");
	 } %>
		</td>
	</tr>
	<tr>
  	    <td><% controller.writeFormLabel(out,"utcr"); %></td>
		<td colspan=4><% controller.writeFormInput(out,"default","utcr",true,null,null); %></td>
	</tr>
	<tr>
  	    <td><% controller.writeFormLabel(out,"default","data_creazione"); %></td>
		<td colspan=4><% controller.writeFormInput(out,"default","data_creazione",true,null,null); %></td>
	</tr>
</table>
</div>