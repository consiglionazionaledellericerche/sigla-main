<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.pdg00.bp.*,
		it.cnr.contab.pdg00.bulk.*"
%>
<script language="JavaScript" src="scripts/util.js"></script>

<%
	PdGVariazioneBP bp = (PdGVariazioneBP)BusinessProcess.getBusinessProcess(request);
	SimpleDetailCRUDController controller = ( (PdGVariazioneBP)bp ).getCrudArchivioCons();
	Pdg_variazioneBulk pdg = (Pdg_variazioneBulk)bp.getModel();

    Integer esercizioPDG    = new Integer(-1);
    Long    nrVariazionePDG = new Long(-1);
	Long    progPDG         = new Long(-1);
    // Recupero il nome del file selezionato
    String  nomeArchivio    = null;

	// Recupero il valore (posizione) del record selezionato
	int  sel = controller.getSelection().getFocus();
	
	/*
	** Quando navigo la prima volta nella tab e non ci sono 
	** record selezionati, il valore restituito è -1. 
	** In questo caso imposto il valore a 0.
	*/
	if (sel == -1)
	   sel = 0;	
	else
	{
       // Recupero la chiave della consultazione selezionato
       esercizioPDG    = ((Pdg_variazione_archivioBulk) ((Pdg_variazioneBulk)bp.getModel()).getArchivioConsultazioni().get(sel)).getEsercizio();	   
  	   nrVariazionePDG = ((Pdg_variazione_archivioBulk) ((Pdg_variazioneBulk)bp.getModel()).getArchivioConsultazioni().get(sel)).getPg_variazione_pdg();
	   progPDG         = ((Pdg_variazione_archivioBulk) ((Pdg_variazioneBulk)bp.getModel()).getArchivioConsultazioni().get(sel)).getProgressivo_riga();
       // Recupero il nome del file selezionato
	   nomeArchivio    = ((Pdg_variazione_archivioBulk) ((Pdg_variazioneBulk)bp.getModel()).getArchivioConsultazioni().get(sel)).getNomeFile();
    }
    
    // Se c'è un record in insert valorizzo con false il flag di gestione dell'inserimento multiplo senza salvare
	boolean isTableEnabled = (bp.isCdrScrivania() && pdg.isPropostaProvvisoria());
	boolean isFieldEnabled = !isTableEnabled;

    controller.writeHTMLTable(pageContext,"archivioConsultazioni",isTableEnabled,false,isTableEnabled,"100%","100px"); 
%>

<script language="JavaScript">
function doScaricaFile() {	
   larghFinestra=5;
   altezFinestra=5;
   sinistra=(screen.width)/2;
   alto=(screen.height)/2;
   doOpenWindow("download_consArchivioPDG/"+<%=esercizioPDG%>+"/"+<%=nrVariazionePDG%>+"/"+<%=progPDG%>+"/"+"<%=nomeArchivio%>","DOWNLOAD","left="+sinistra+",top="+alto+",width="+larghFinestra+", height="+altezFinestra+",menubar=no,toolbar=no,location=no");
}
</script>

<table class="Panel" cellspacing=2>
	<tr>
  	    <td><% controller.writeFormLabel(out,"default","tipo_archivio"); %></td>
		<td><% controller.writeFormInput(out,"default","tipo_archivio",isFieldEnabled,null,null); %></td>
	</tr>
	<tr>
  	    <td><% controller.writeFormLabel(out,"default","utcr"); %></td>
		<td><% controller.writeFormInput(out,"default","utcr",isFieldEnabled,null,null); %></td>		
	</tr>	
	<tr>
  	    <td><% controller.writeFormLabel(out,"default","data_creazione"); %></td>
		<td><% controller.writeFormInput(out,"default","data_creazione",isFieldEnabled,null,null); %></td>		
	</tr>	
	
	<tr>
		<% controller.writeFormField(out,"default","attivaFile_blob"); %>
	</tr>	
	
</table>