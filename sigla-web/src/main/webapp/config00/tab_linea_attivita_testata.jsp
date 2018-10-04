<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,it.cnr.jada.action.*,java.util.*,it.cnr.jada.util.action.*,it.cnr.contab.config00.latt.bulk.*"
%>

<% 
	it.cnr.contab.config00.bp.CRUDWorkpackageBP bp = (it.cnr.contab.config00.bp.CRUDWorkpackageBP)BusinessProcess.getBusinessProcess(request); 
	WorkpackageBulk bulk = (WorkpackageBulk)bp.getModel();
	boolean isMapNotEditable = false;
	if (!bp.isFlNuovoPdg())
		isMapNotEditable = bp.isUoArea();
	boolean isProgettoDisable = bp.isFieldProgettoDisable();
%>
<table class="Panel">
	<tr>
		<% bp.getController().writeFormField(out,"esercizio_inizio"); %>
	</tr>
	<tr>
		<% bp.getController().writeFormField(out,bp.isInserting() ? "cd_linea_attivita7" : "cd_linea_attivita"); %>
	</tr>
	<tr>
		<% if (!bp.isInserting()) 
			bp.getController().writeFormField(out,"tipo_linea_attivita"); %>
	</tr>
	<% if (bp.isSearching() || !bp.isFlNuovoPdg() || (bulk!=null && bulk.getEsercizio_inizio()!=null && bulk.getEsercizio_inizio().compareTo(Integer.valueOf(2016))==-1)) { %>	
		<tr>
		  <td>
			<% bp.getController().writeFormLabel(out,"cd_modulo2015");%>
		  </td>
		   <td colspan="3">	
			<% bp.getController().writeFormInput(out,null,"cd_modulo2015",bp.isUoArea()||bp.isFlNuovoPdg(),null,null); %>
			<% bp.getController().writeFormInput(out,null,"ds_modulo2015",bp.isUoArea()||bp.isFlNuovoPdg(),null,null); %>
			<% bp.getController().writeFormInput(out,null,"find_nodo_padre_2015",bp.isUoArea()||bp.isFlNuovoPdg(),null,null); %>
		  </td>
		</tr>
	<% } %>
	<% if (bp.isSearching() || bp.isFlNuovoPdg() || bp.getEsercizioScrivania().compareTo(Integer.valueOf(2015))==0) { %>	
		<tr>
		  <td>
			<% bp.getController().writeFormLabel(out,"cd_progetto2016");%>
		  </td>
		   <td colspan="3">	
			<% bp.getController().writeFormInput(out,null,"cd_progetto2016",isProgettoDisable,null,null); %>
			<% bp.getController().writeFormInput(out,null,"ds_progetto2016",isProgettoDisable,null,null); %>
			<% bp.getController().writeFormInput(out,null,"find_nodo_padre_2016",isProgettoDisable,null,null); %>
		  </td>
		</tr>
	<% } %>
	<tr>
	 <td>
		<% bp.getController().writeFormLabel(out,"centro_responsabilita"); %>
	</td>
	 <td colspan="3">	
		<% bp.getController().writeFormInput(out,null,"centro_responsabilita",
				isMapNotEditable||bp.isMapFromPianoGestioneSpese()||bp.isMapFromPianoGestioneEntrate()||
				bp.isMapFromVariazioneGestioneSpese()||bp.isMapFromVariazioneGestioneEntrate(),null,null); %> 
	 </td>
	</tr>
	
	<tr>
	 <td>
		<% bp.getController().writeFormLabel(out,"responsabile"); %>
	</td>
	 <td colspan="3">	
		<% bp.getController().writeFormInput(out,"responsabile"); %> 
	 </td>
	</tr>
		
	<tr>
	 <td>
		<% bp.getController().writeFormLabel(out,"insieme_la"); %>
	</td>
	 <td colspan="3">	
		<% bp.getController().writeFormInput(out,null,"insieme_la",isMapNotEditable,null,null); %> 
	 </td>
	</tr>
	<tr>
		<td>
		<% bp.getController().writeFormLabel(out,"ti_gestione"); %>
		</td>
		<td>
		<% if (bp.isFlTiGestioneES()) 
				bp.getController().writeFormInput(out,null,"ti_gestioneES",
						bp.isSearching()&&(bp.isMapFromPianoGestioneSpese()||bp.isMapFromPianoGestioneEntrate()||
						bp.isMapFromVariazioneGestioneSpese()||bp.isMapFromVariazioneGestioneEntrate()),
						null,"onclick=\"submitForm('doCambiaGestione')\"");
		   else
				bp.getController().writeFormInput(out,null,"ti_gestione",
						bp.isSearching()&&(bp.isMapFromPianoGestioneSpese()||bp.isMapFromPianoGestioneEntrate()||
						bp.isMapFromVariazioneGestioneSpese()||bp.isMapFromVariazioneGestioneEntrate()),
						null,"onclick=\"submitForm('doCambiaGestione')\""); 
		%>
		</td>
	</tr>
	<% if (WorkpackageBulk.TI_GESTIONE_SPESE.equals(bulk.getTi_gestione()) || 
		   WorkpackageBulk.TI_GESTIONE_ENTRAMBE.equals(bulk.getTi_gestione())) { %> 
	<tr>
		<% bp.getController().writeFormField(out,"funzione"); %>
	</tr>
	<% } %>
	<tr>
		<% bp.getController().writeFormField(out,"natura"); %>
	</tr>
	<tr>
    <td>
		<% bp.getController().writeFormLabel(out,"gruppo_linea_attivita"); %>
	</td>
	 <td colspan="3">	
		<% bp.getController().writeFormInput(out,null,"gruppo_linea_attivita",isMapNotEditable,null,null); %> 
	 </td>
	 </tr>
	<tr>
	 <td>
		<% bp.getController().writeFormLabel(out,"denominazione"); %>
	</td>
	 <td colspan="3">	
		<% bp.getController().writeFormInput(out,null,"denominazione",isMapNotEditable,null,null); %> 
	 </td>
	</tr>
	
	<tr>
	 <td>
		<% bp.getController().writeFormLabel(out,"ds_linea_attivita"); %>
	</td>
	 <td colspan="3">	
		<% bp.getController().writeFormInput(out,null,"ds_linea_attivita",isMapNotEditable,null,null); %> 
	 </td>
	</tr>
	
	<tr>
		<% bp.getController().writeFormField( out, "esercizio_fine"); %>
	</tr>
	<tr>
		<% bp.getController().writeFormField( out, "fl_limite_ass_obblig"); %>
	</tr>
	<% if (WorkpackageBulk.TI_GESTIONE_SPESE.equals(bulk.getTi_gestione()) || 
			WorkpackageBulk.TI_GESTIONE_ENTRAMBE.equals(bulk.getTi_gestione())) { %>
	<tr>
		<td>
			<% bp.getController().writeFormLabel(out,"cofog"); %>
		</td>
	 	<td colspan="3">	
			<% bp.getController().writeFormInput(out,null,"cofog",bp.isMapFromPianoGestioneSpese(),null,null); %>
		</td> 
	</tr>
	<% } %>
	<tr>
		<td>
			<% bp.getController().writeFormLabel(out,"pdgProgramma"); %>
		</td>
	 	<td colspan="3">	
			<% bp.getController().writeFormInput(out,null,"pdgProgramma",bp.isMapFromPianoGestioneSpese(),null,null); %>
		</td> 
	</tr>	
	<tr>
		<td>
			<% bp.getController().writeFormLabel(out,"pdgMissione"); %>
		</td>
	 	<td colspan="3">	
			<% bp.getController().writeFormInput(out,null,"pdgMissione",bp.isMapFromPianoGestioneSpese(),null,null); %> 
		</td> 
	</tr>

	<% if (bp.getUoScrivania().getCd_unita_organizzativa().equals("999.000")) {%>
		<tr>
		  <td>
			<% bp.getController().writeFormLabel(out,"cd_progetto_padre");%>
		  </td>
		  <td>	
			<% bp.getController().writeFormInput(out,"cd_progetto_padre");%>
			<% bp.getController().writeFormInput(out,"ds_progetto_padre");%>
			<% bp.getController().writeFormInput(out,"find_nodo_progettopadre");%>
		  </td>
		</tr>
		<tr>
		  <td>
			<% bp.getController().writeFormLabel(out,"cd_progetto");%>
		  </td>
		   <td colspan="3">	
			<% bp.getController().writeFormInput(out,null,"cd_progetto",isMapNotEditable,null,null); %>
			<% bp.getController().writeFormInput(out,null,"ds_progetto",isMapNotEditable,null,null); %>
			<% bp.getController().writeFormInput(out,null,"find_nodo_padre",isMapNotEditable,null,null); %>
		  </td>
		</tr>
	<% } %>
</table>