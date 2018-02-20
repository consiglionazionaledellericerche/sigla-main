<%@ page 
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.config00.blob.bulk.*,
		it.cnr.contab.progettiric00.bp.*,
		it.cnr.contab.progettiric00.core.bulk.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.config00.latt.bulk.*"
%>

<% 
	SimpleCRUDBP bp = (SimpleCRUDBP)BusinessProcess.getBusinessProcess(request);
	SimpleDetailCRUDController controller =  bp.getCrudDettagliPostIt();
	/* 
	** Variabile in cui Scaricare l'Id del PostIt 
	** (solo se ci sono PostIt associati)
	*/
	Integer idPost = new Integer(-1);
	String nomePost = null;
	// Recupero il valore (posizione) del record selezionato
	int  sel = controller.getSelection().getFocus();
	
	/*
	** Quando navigo la prima volta nella tab e non ci sono 
	** record selezionati, il valore restituito è -1. 
	** In questo caso imposto il valore a 0.
	*/
	if (sel == -1)
	   sel = 0;	
	
	/*   
	** Flag di controllo sui dettagli usato per disabilitare il bottone
	** di creazione (Nuovo)
	*/
	boolean inseribile = true;	

     // Postit legati ai progetti
     if ( bp.getModel() instanceof ProgettoBulk)
     {
	    // Ciclo sui postit legati al progetto di ricerca 
	    for(int i = 0; ((ProgettoBulk)bp.getModel()).getDettagliPostIt().size() > i; i++) 
	    {    
	      // Recupero l'id del postit selezionato
		  idPost = ((PostItBulk) ((ProgettoBulk)bp.getModel()).getDettagliPostIt().get(sel)).getId();	   
	      // Recupero il nome del file del postit selezionato
	      nomePost = ((PostItBulk) ((ProgettoBulk)bp.getModel()).getDettagliPostIt().get(sel)).getNome_file();
	       // Se c'è un record in insert valorizzo con false il flag di gestione dell'inserimento multiplo senza salvare
	       if (((PostItBulk) ((ProgettoBulk)bp.getModel()).getDettagliPostIt().get(i)).getCrudStatus()!= it.cnr.jada.bulk.OggettoBulk.NORMAL )    
	          inseribile = false;
	    }
	  } else if ( bp.getModel() instanceof WorkpackageBulk) { 
	    // Ciclo sui postit legati al workpackage
	    for(int i = 0; ((WorkpackageBulk)bp.getModel()).getDettagliPostIt().size() > i; i++) 
	    {
	      // Recupero l'id del postit selezionato sul Wp
		  idPost = ((PostItBulk) ((WorkpackageBulk)bp.getModel()).getDettagliPostIt().get(sel)).getId();	       
	      // Recupero il nome del file del postit selezionato
	      nomePost = ((PostItBulk) ((WorkpackageBulk)bp.getModel()).getDettagliPostIt().get(sel)).getNome_file();		  
	      // Se c'è un record in insert valorizzo con false il flag di gestione dell'inserimento multiplo senza salvare
	      if (((PostItBulk) ((WorkpackageBulk)bp.getModel()).getDettagliPostIt().get(i)).getCrudStatus()!= it.cnr.jada.bulk.OggettoBulk.NORMAL )    
	          inseribile = false;	      
	    }   
	  } 	    
%>
<script language="JavaScript">
function doScaricaFile() {	
		window.open("download_blobPostIt/"+<%=idPost%>+"/"+"<%=nomePost%>","DOWNLOAD",false)
}
</script>
<%	controller.writeHTMLTable(pageContext,"post_it",inseribile,false,true,"100%","100px"); %>

	<table class="Panel">
		<tr>
           <% controller.writeFormField(out,"default","blob"); %>
		<tr>
			<% controller.writeFormField(out,"default","ds_file"); %>
		</tr>
		<tr>
			<% controller.writeFormField(out,"default","nome_file"); %>
            <td><center><%JSPUtils.button(out, "img/import24.gif", "img/import24.gif", "Attiva File", "if (disableDblClick()) javascript:doScaricaFile()", null, bp.isActive(bp.getModel(),sel), bp.getParentRoot().isBootstrap());%></center></td> 		</tr>		
	</table>