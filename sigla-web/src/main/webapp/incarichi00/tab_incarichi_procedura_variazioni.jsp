<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.incarichi00.bulk.*,
		it.cnr.contab.incarichi00.bp.*"
%>
<%
	CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)BusinessProcess.getBusinessProcess(request);
	Incarichi_proceduraBulk procedura = (Incarichi_proceduraBulk)bp.getModel();
    Incarichi_repertorioBulk incarico = (Incarichi_repertorioBulk)bp.getIncarichiColl().getModel();

	SimpleDetailCRUDController controller = bp.getCrudIncarichiVariazioni();

	Incarichi_repertorio_varBulk variazione;

	// Recupero il valore (posizione) del record selezionato
	int  sel = controller.getSelection().getFocus();
	
	/*
	** Quando navigo la prima volta nella tab e non ci sono 
	** record selezionati, il valore restituito è -1. 
	** In questo caso imposto il valore a 0.
	*/
	if (sel == -1)
		variazione = null;
	else
		variazione = (Incarichi_repertorio_varBulk)controller.getModel();	   
    
	controller.writeHTMLTable(pageContext,"variazioni",!bp.isSearching(),false,!bp.isSearching(),"100%","150px"); 
    
    boolean isRODettaglio = procedura==null||incarico==null||variazione==null||(!variazione.isToBeCreated()&&!variazione.isProvvisorio());
%>

<script language="JavaScript">
function doScaricaFile() {	
	doPrint('<%=(variazione==null?null:JSPUtils.getAppRoot(request) + variazione.getDownloadUrl())%>');
}
</script>

<table class="Panel" cellspacing=2>
	<tr>
        <td><% controller.writeFormLabel(out,bp.getFieldTipoVariazione()); %></td>
	    <td colspan=4><% controller.writeFormInput(out,"default",bp.getFieldTipoVariazione(), isRODettaglio||!variazione.isToBeCreated(),null,null); %></td>
	</tr>
<% if (variazione==null || variazione.getTipo_variazione()!=null) {%>
    <% if (variazione!=null && variazione.getTipo_variazione()!=null &&
		   variazione.isVariazioneIntegrazioneIncarico()) {%>
	<tr>
        <td><% controller.writeFormLabel(out,"dt_variazione"); %></td>
	    <td colspan=4><% controller.writeFormInput(out,"default","dt_variazione", isRODettaglio,null,null); %></td>
	</tr>
	<tr>
		<td colspan=5>
	   <fieldset class="fieldset">
    	<legend class="GroupLabel">Dati Incarico da variare:</legend>    
		<table>
		<tr>
	        <td><% controller.writeFormLabel(out,"dt_fine_validita"); %></td>
		    <td><% controller.writeFormInput(out,"default","dt_fine_validita", isRODettaglio,null,null); %></td>
			<td><% controller.writeFormLabel(out,"importo_lordo"); %></td>
			<td><% controller.writeFormInput(out,"default","importo_lordo", isRODettaglio,null,null); %></td>
			<td><% controller.writeFormLabel(out,"importo_complessivo"); %></td>
			<td><% controller.writeFormInput(out,"default","importo_complessivo", isRODettaglio,null,null); %></td>
		</tr>
		</table>
		</fieldset>
		</td>
	</tr>
	<% } else {%>
	<tr>
	    <td><% controller.writeFormLabel(out,"importo_complessivo"); %></td>
	    <td colspan=4><% controller.writeFormInput(out,"default","importo_complessivo", isRODettaglio,null,null); %></td>
	</tr>
	<% } %>

	<tr>
	    <td><% controller.writeFormLabel(out,"ds_variazione"); %></td>
	    <td colspan=4><% controller.writeFormInput(out,"default","ds_variazione", isRODettaglio,null,null); %></td>
	</tr>

   <% if (variazione!=null && variazione.getTipo_variazione()!=null &&
    	   variazione.isVariazioneIntegrazioneIncarico()) {%>
	<tr>
		<td colspan=5>
		<div class="Group"><table>
			<tr><td valign=top>
		    	<span class="FormLabel" style="color:red">Attenzione:</span>
		    </td>
		    <td valign=top>
		    	<span class="FormLabel">
				é obbligatorio allegare un unico file in formato PDF che contenga il contratto sottoscritto dalle parti e <br>
				la successiva modifica del contratto stesso. <br>
				<i><u>Tale file sarà l'unico ad essere pubblicato sul sito istituzionale dell'Ente.</u></i> <br>
				Si rammenta che a seguito del salvataggio non sarà più possibile apportare alcuna modifica ai documenti allegati.<br> 
				Si prega pertanto di porre la massima attenzione.<br><br>
				</span>
			</td></tr>
			<tr><td valign=top>
		    	<span class="FormLabel" style="color:red">Attenzione:</span>
		    </td>
		    <td valign=top>
		    	<span class="FormLabel">
					al fine di rispettare le norme in materia di tutela dei dati personali, <br>
					prima di allegare il file del contratto da pubblicare sul sito internet istituzionale del CNR <br>
					e' necessario verificare che lo stesso esponga <b><i><u>esclusivamente</u></i></b> i seguenti dati personali: <br>
					<b><i>Nome, Cognome, Luogo e Data di nascita, Codice Fiscale</i></b> <br>
					Ogni altro dato personale dovrà essere <b><i>"<u>oscurato</u>"</i></b><br><br>
				</span>
			</td></tr>
			<tr><td valign=top>
		    	<span class="FormLabel" style="color:red">Attenzione:</span>
		    </td>
		    <td valign=top>
		    	<span class="FormLabel">
				ai fini della pubblicazione sul sito internet istituzionale del CNR, <i><u>si raccomanda di usare file in formato PDF</u></i><br> 
				e di <i><u>controllare sempre</u></i>, dopo il salvataggio, la leggibilità dell'allegato utilizzando il bottone "Apri file"<br>
				</span>
			</td></tr>
		</table></div>
		</td>
	</tr>

	<% if (!bp.isSearching() && (((bp.isSuperUtente() || bp.isUtenteAbilitatoModificaAllegatoContratto()) && 
									variazione!=null && variazione.getTipo_variazione()!=null && variazione.isVariazioneIntegrazioneIncarico()) ||
								 (!(bp.isSuperUtente() || bp.isUtenteAbilitatoModificaAllegatoContratto()) && !isRODettaglio))) { %>
	<tr>
        <td><% controller.writeFormLabel(out,"blob"); %></td>
        <td colspan=4><% controller.writeFormInput(out,"blob"); %></td>
    </tr>
	<% } %>
	<tr>
        <td><% controller.writeFormLabel(out,"nome_file"); %></td>
        <td colspan=4><% controller.writeFormInput(out,"nome_file"); %>
	<% if (variazione!=null && !variazione.isToBeCreated()) {
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
	<% } %>
<% } %>
</table>