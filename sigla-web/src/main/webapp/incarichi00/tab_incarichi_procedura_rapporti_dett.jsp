<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.incarichi00.bulk.*,
		it.cnr.contab.incarichi00.bp.*"
%>
<%
	CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)BusinessProcess.getBusinessProcess(request);
	SimpleDetailCRUDController controller = bp.getIncarichiRappDetColl();
	Incarichi_repertorioBulk incarico = (Incarichi_repertorioBulk)bp.getIncarichiColl().getModel();

	Incarichi_repertorio_rapp_detBulk rapportoDet;

	// Recupero il valore (posizione) del record selezionato
	int  sel = controller.getSelection().getFocus();

	/*
	** Quando navigo la prima volta nella tab e non ci sono 
	** record selezionati, il valore restituito è -1. 
	** In questo caso imposto il valore a 0.
	*/
	if (sel == -1)
		rapportoDet = null;
	else
		rapportoDet = (Incarichi_repertorio_rapp_detBulk)controller.getModel();	   

	boolean isEditable = false;
	if (incarico!=null && rapportoDet!=null)
		if (rapportoDet.isToBeCreated() && !bp.isSearching()&&!rapportoDet.isAnnullato())
			isEditable = true;

	SimpleDetailCRUDController controllerDet = bp.getIncarichiRappDetColl();
	controllerDet.writeHTMLTable(pageContext,null,true,false,true,"100%","100px"); 
%>
<div class="card">
<table class="Panel m-2 p-2" cellspacing=2>
	<tr>
        <td><% controllerDet.writeFormLabel(out,"default","conferente_rapporto"); %></td>
        <td><% controllerDet.writeFormInput(out,"default","conferente_rapporto", !isEditable,null,null); %></td>
	</tr>
	<tr>
	    <td><% controllerDet.writeFormLabel(out,"default","natura_rapporto"); %></td>
	    <td><% controllerDet.writeFormInput(out,"default","natura_rapporto", !isEditable,null,null); %></td>
	</tr>
	<tr>
	    <td><% controllerDet.writeFormLabel(out,"default","dt_ini_rapporto"); %></td>
	    <td><% controllerDet.writeFormInput(out,"default","dt_ini_rapporto", !isEditable,null,null); %></td>
	</tr>
	<tr>
  	    <td><% controllerDet.writeFormLabel(out,"default","importo_rapporto"); %></td>
		<td><% controllerDet.writeFormInput(out,"default","importo_rapporto", !isEditable,null,null); %></td>
	</tr>
</table>
</div>