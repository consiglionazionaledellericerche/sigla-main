<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
	    it.cnr.jada.action.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.incarichi00.bulk.*,
		it.cnr.contab.incarichi00.bp.*"
%>
<%
	CRUDIncarichiProceduraBP bp = (CRUDIncarichiProceduraBP)BusinessProcess.getBusinessProcess(request);
	Incarichi_proceduraBulk procedura = (Incarichi_proceduraBulk)bp.getModel();

	boolean multi_incarico = false;

	if (procedura!=null && procedura.getNr_contratti()!=null && procedura.getNr_contratti().compareTo(new Integer(1))==1)
		multi_incarico=true;

	SimpleDetailCRUDController controller = multi_incarico?bp.getCrudArchivioAllegati():bp.getCrudArchivioAllegatiMI();

	Incarichi_archivioBulk allegato;

	// Recupero il valore (posizione) del record selezionato
	int  sel = controller.getSelection().getFocus();
	
	/*
	** Quando navigo la prima volta nella tab e non ci sono 
	** record selezionati, il valore restituito è -1. 
	** In questo caso imposto il valore a 0.
	*/
	if (sel == -1)
	   allegato = null;
	else
	   allegato = (Incarichi_archivioBulk)controller.getModel();	   
    
	controller.writeHTMLTable(pageContext,"archivioAllegati",!bp.isSearching(),false,!bp.isSearching(),"100%","100px"); 
    
	boolean isRODettaglio = false;
	if (allegato!=null && ((allegato.isContratto() && (bp.isSuperUtente() || bp.isUtenteAbilitatoModificaAllegatoContratto())) ||
						    allegato.isAllegatoGenerico() ||
						    allegato.isCurriculumVincitore() || allegato.isAggiornamentoCurriculumVincitore() || allegato.isProgetto() ||
						    allegato.isConflittoInteressi())) {
		isRODettaglio = procedura==null||allegato==null||!allegato.isToBeCreated()||
						!allegato.isAllegatoValido()||
						procedura.getFaseProcesso().compareTo(Incarichi_proceduraBulk.FASE_PUBBLICAZIONE)==0||
						(procedura.getFaseProcesso().compareTo(Incarichi_proceduraBulk.FASE_INSERIMENTO_CONTRATTO)==0 &&
	 					!allegato.isToBeCreated());
	} else {
		isRODettaglio = procedura==null||allegato==null||!allegato.isToBeCreated()||
				        procedura.isROProcedura()||
						!allegato.isAllegatoValido()||
						procedura.getFaseProcesso().compareTo(Incarichi_proceduraBulk.FASE_PUBBLICAZIONE)==0||
						(procedura.getFaseProcesso().compareTo(Incarichi_proceduraBulk.FASE_INSERIMENTO_CONTRATTO)==0 &&
		 				!allegato.isToBeCreated()) ||
						(procedura.isProceduraInviataCorteConti() && 
		 				!allegato.isAttoEsitoControllo() && !allegato.isAllegatoGenerico());
	}

	boolean isFileDaAllegare = allegato!=null && allegato.isFileRequired();
	boolean isUrlDaIndicare = allegato!=null && allegato.isUrlRequired();
%>

<% if (allegato != null) { %>
<script language="JavaScript">
function doScaricaFile() {	
	doPrint('<%=JSPUtils.getAppRoot(request)%>genericdownload/<%=allegato.getNomeAllegato()%>?methodName=scaricaAllegato&it.cnr.jada.action.BusinessProcess=<%=bp.getPath()%>');
}
</script>
<% } %>
<div class="card p-2 mt-2 ">
<table class="Panel w-100">
	<tr>
        <td><% controller.writeFormLabel(out,"tipo_archivio"); %></td>
        <td colspan=4><% controller.writeFormInput(out,"tipo_archivio"); %></td>
	</tr>
    <% if (allegato==null || allegato.getTipo_archivio()!=null) {%>
	    <% if (allegato!=null && allegato.getTipo_archivio()!=null &&
	    	  (allegato.isBando() || allegato.isCurriculumVincitore() || allegato.isAggiornamentoCurriculumVincitore() || allegato.isConflittoInteressi())) {%>
		<tr>
			<td colspan=5>
			<div class="Group card m-2 border-warning"><table>
				<% if (allegato.isCurriculumVincitore() || allegato.isAggiornamentoCurriculumVincitore() || allegato.isConflittoInteressi()) { %>
				<tr><td valign=top>
			    	<span class="FormLabel" style="color:red">Attenzione:</span>
			    </td>
			    <td valign=top>
			    	<span class="FormLabel">
					al fine di rispettare le norme in materia di tutela dei dati personali, <%if (!bp.getParentRoot().isBootstrap()) { %><br><% } %>
					prima di allegare il file
					<%if (allegato.isConflittoInteressi()) {%>dell'<b><i>Attestazione di avvenuta verifica insussistenza cause di conflitto di interesse'</i></b>
                    <%} else {%>del curriculum
                    <%}%>
                    da pubblicare sul sito internet istituzionale del CNR <%if (!bp.getParentRoot().isBootstrap()) { %><br><% } %>
					in base alle disposizioni sulla trasparenza, e' necessario verificare che lo stesso esponga solo <b><i><u>dati personali minimi necessari</u></i></b> per le finalità della trasparenza.
					<br><br>
					<b><i>Si raccomanda di oscurare il codice fiscale, eventuali recapiti, la firma autografa e ogni altro dato utilizzabile per furti di identità</i></b>
					<br><br>
					</span>
				</td></tr>
				<% } %>
 				<% if (allegato.isBando() || allegato.isCurriculumVincitore() || allegato.isAggiornamentoCurriculumVincitore() || allegato.isConflittoInteressi()) { %>
				<tr><td valign=top>
			    	<span class="FormLabel" style="color:red">Attenzione:</span>
			    </td>
			    <td valign=top>
			    	<span class="FormLabel">
					ai fini della pubblicazione sul sito internet istituzionale del CNR, <i><u>si raccomanda di usare file in formato PDF</u></i><%if (!bp.getParentRoot().isBootstrap()) { %><br><% } %> 
					e di <i><u>controllare sempre</u></i>, dopo il salvataggio, la leggibilità dell'allegato utilizzando il bottone "Apri file"<%if (!bp.getParentRoot().isBootstrap()) { %><br><% } %>
					<br><br>
					</span>
				</td></tr>
				<% } %>
 				<% if (allegato.isAggiornamentoCurriculumVincitore()) { %>
				<tr>
					<td valign=top>
				    	<span class="FormLabel" style="color:red">Attenzione:</span>
				    </td>
				    <td valign=top>
				    	<span class="FormLabel">
						In caso di inserimento di una <b><i>integrazione/aggiornamento</i></b> dei dati personali contenuti nel curriculum presentato 
						dall’interessato al momento dell’incarico e già pubblicato, si raccomanda l’inserimento di un nuovo documento, datato e 
						distinto dal precedente curriculum che resterà comunque invariato.
						</span>
					</td>
				</tr>
				<% } %>
			</table></div>
			</td>
		</tr>
		<% } %>

		<% if (!bp.isSearching() && !isRODettaglio && isFileDaAllegare) { %>
			<tr>
		        <td><% controller.writeFormLabel(out,"default","blob"); %></td>
		        <td colspan=4><% controller.writeFormInput(out,"default","blob"); %></td>
		    </tr>
		<% } %>
		<tr>
	        <td><% controller.writeFormLabel(out,"default","ds_file"); %></td>
	        <td colspan=4><% controller.writeFormInput(out,"default","ds_file", isRODettaglio,null,null); %></td>
		</tr>
		<% if (isFileDaAllegare) { %>
			<tr>
		        <td><% controller.writeFormLabel(out,"default","nome_file"); %></td>
		        <td><% controller.writeFormInput(out,"default","nome_file"); %>
			<% if (allegato!=null && !allegato.isToBeCreated()) {
					controller.writeFormField(out,"default","attivaFile_blob");
			 } %>
				</td>
			</tr>
		<% } %>
		<% if (isUrlDaIndicare) { %>
			<tr>
		        <td><% controller.writeFormLabel(out,"default","url_file"); %></td>
		        <td colspan=4><% controller.writeFormInput(out,"default","url_file", isRODettaglio,null,null); %></td>
			</tr>
		<% } %>	
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