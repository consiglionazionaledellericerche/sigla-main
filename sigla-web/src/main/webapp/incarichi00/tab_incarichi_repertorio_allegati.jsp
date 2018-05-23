<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.incarichi00.bulk.*,
		it.cnr.contab.incarichi00.bp.*"
%>
<%
	CRUDIncarichiRepertorioBP bp = (CRUDIncarichiRepertorioBP)BusinessProcess.getBusinessProcess(request);
	SimpleDetailCRUDController controller = bp.getCrudArchivioAllegati();
	Incarichi_repertorioBulk incarico = (Incarichi_repertorioBulk)bp.getModel();

	Incarichi_archivioBulk incaricoAllegato;

	// Recupero il valore (posizione) del record selezionato
	int  sel = controller.getSelection().getFocus();
	
	/*
	** Quando navigo la prima volta nella tab e non ci sono 
	** record selezionati, il valore restituito è -1. 
	** In questo caso imposto il valore a 0.
	*/
	if (sel == -1)
	   incaricoAllegato = null;
	else
	   incaricoAllegato = (Incarichi_archivioBulk)controller.getModel();
    
    controller.writeHTMLTable(pageContext,"archivioAllegati",!incarico.isROIncaricoDefinitivo()&&!bp.isSearching(),false,!incarico.isROIncaricoDefinitivo()&&!bp.isSearching(),"100%","100px"); 
    
    boolean isRODettaglio = incarico==null||incaricoAllegato==null||
    						incarico.isROIncaricoDefinitivo()||
    						!incaricoAllegato.isAllegatoValido()||
    						incarico.getIncarichi_procedura().getFaseProcesso().compareTo(Incarichi_proceduraBulk.FASE_PUBBLICAZIONE)==0||
    						(incarico.getIncarichi_procedura().getFaseProcesso().compareTo(Incarichi_proceduraBulk.FASE_INSERIMENTO_CONTRATTO)==0 &&
							 !incaricoAllegato.isToBeCreated());
%>

<script language="JavaScript">
function doScaricaFile() {	
	doPrint('<%=(incaricoAllegato==null?null:JSPUtils.getAppRoot(request) + incaricoAllegato.getDownloadUrl())%>');
}
</script>

<table class="Panel" cellspacing=2>
	<tr>
        <td><% controller.writeFormLabel(out,"default","tipo_archivio"); %></td>
        <td colspan=4><% controller.writeFormInput(out,"default","tipo_archivio", isRODettaglio,null,null); %></td>
	</tr>
    <% if (incaricoAllegato==null || incaricoAllegato.getTipo_archivio()!=null) {%>
	    <% if (incaricoAllegato!=null && incaricoAllegato.getTipo_archivio()!=null &&
	    	  (incaricoAllegato.isBando() || incaricoAllegato.isContratto())) {%>
		<tr>
			<td colspan=5>
			<div class="Group"><table>
				<% if (incaricoAllegato.isContratto()) { %>
				<tr><td valign=top>
			    	<span class="FormLabel" style="color:red">Attenzione:</span>
			    </td>
			    <td valign=top>
			    	<span class="FormLabel">
					al fine di rispettare le norme in materia di tutela dei dati personali, <br>
					prima di allegare il file del contratto da pubblicare sul sito internet istituzionale del CNR <br>
					e' necessario <i><u>"oscurare"</u></i> eventuali dati quali ad esempio le coordinate bancarie, la residenza dell'incaricato, ecc. <br>
					(cap.13 Manuale operativo)<br><br>
					</span>
				</td></tr>
				<% } %>
				<% if (incaricoAllegato.isBando() || incaricoAllegato.isContratto()) { %>
				<tr><td valign=top>
			    	<span class="FormLabel" style="color:red">Attenzione:</span>
			    </td>
			    <td valign=top>
			    	<span class="FormLabel">
					ai fini della pubblicazione sul sito internet istituzionale del CNR, <i><u>si raccomanda di usare file in formato PDF</u></i><br> 
					e di <i><u>controllare sempre</u></i>, dopo il salvataggio, la leggibilità dell'allegato utilizzando il bottone "Apri file"<br>
					</span>
				</td></tr>
				<% } %>
			</table></div>
			</td>
		</tr>
		<% } %>

		<% if (!incarico.isROIncaricoDefinitivo()&&
			   !bp.isSearching() && incarico.getIncarichi_procedura().getFaseProcesso().compareTo(Incarichi_proceduraBulk.FASE_PUBBLICAZIONE)!=0 && !isRODettaglio &&
			   incarico.getIncarichi_procedura().getFaseProcesso().compareTo(Incarichi_proceduraBulk.FASE_DEFINITIVA)!=0) {%>
		<tr>
	        <td><% controller.writeFormLabel(out,"default","blob"); %></td>
	        <td colspan=4><% controller.writeFormInput(out,"default","blob"); %></td>
	    </tr>
		<% } %>
		<tr>
	        <td><% controller.writeFormLabel(out,"default","ds_file"); %></td>
	        <td colspan=4><% controller.writeFormInput(out,"default","ds_file", isRODettaglio,null,null); %></td>
		</tr>
		<tr>
	        <td><% controller.writeFormLabel(out,"default","nome_file"); %></td>
	        <td><% controller.writeFormInput(out,"default","nome_file"); %>
		<% if (incaricoAllegato!=null && !incaricoAllegato.isToBeCreated()) {
				controller.writeFormField(out,"default","attivaFile_blob");
		 } %>
			</td>
		</tr>
		<tr>
	  	    <td><% controller.writeFormLabel(out,"default","utcr"); %></td>
			<td colspan=4><% controller.writeFormInput(out,"default","utcr",true,null,null); %></td>
		</tr>
		<tr>
	  	    <td><% controller.writeFormLabel(out,"default","data_creazione"); %></td>
			<td colspan=4><% controller.writeFormInput(out,"default","data_creazione",true,null,null); %></td>
		</tr>
	<% } %>
</table>