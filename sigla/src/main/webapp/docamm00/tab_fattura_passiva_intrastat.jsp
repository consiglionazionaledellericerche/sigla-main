<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.docamm00.tabrif.bulk.*,
		it.cnr.contab.docamm00.intrastat.bulk.*,
		it.cnr.contab.docamm00.docs.bulk.*,
		it.cnr.contab.doccont00.core.bulk.*,
		it.cnr.contab.docamm00.bp.*"
%>

<%	CRUDFatturaPassivaBP bp = (CRUDFatturaPassivaBP)BusinessProcess.getBusinessProcess(request);
	Fattura_passivaBulk fatturaPassiva = (Fattura_passivaBulk)bp.getModel();
	Fattura_passiva_intraBulk intra=null; 
	boolean noninviato=true;
	if(bp.getDettaglioIntrastatController().getModel()!=null){
		intra=(Fattura_passiva_intraBulk)bp.getDettaglioIntrastatController().getModel();
	 	if(intra!=null&& intra.getFl_inviato()!=null) 
			noninviato=!(intra.getFl_inviato().booleanValue());
		if(intra!=null && intra.getFl_inviato()!=null && 
		  intra.getFl_inviato().booleanValue() && intra.getNr_progressivo()==null)
			noninviato=false;
		else if(intra!=null && intra.getFl_inviato()!=null && 
		  intra.getFl_inviato().booleanValue() && intra.getNr_progressivo()!=null)
			noninviato=true;
	}
	%>
	<% if (Fattura_passivaBulk.FATTURA_DI_BENI.equals(fatturaPassiva.getTi_bene_servizio())) { %>
	<div class="Group card" style="width:100%">
		<table width="100%">
			<tr>
			  	<td colspan="2">
				  	<span class="FormLabel" style="color:black">Dettagli intrastat</span>
			  	</td>
			</tr>
			<tr>
			  	<td colspan="2">
					<%	bp.getDettaglioIntrastatController().writeHTMLTable(pageContext,"BeneSet",true,false,noninviato,"100%","100px"); %>
			  	</td>
			</tr>
		</table>
	</div>
	<div class="Group card" style="width:100%">
		<table width="100%">
			<tr>      			
				<td>
					<% bp.getDettaglioIntrastatController().writeFormLabel(out,"esercizio"); %>
				</td>
				<td colspan="5">
					<% bp.getDettaglioIntrastatController().writeFormInput(out,"esercizio"); %>
				</td>
			</tr>
			<tr>      			
				<td>
					<% bp.getDettaglioIntrastatController().writeFormLabel(out,"ds_bene"); %>
				</td>
				<td colspan="5">
					<% bp.getDettaglioIntrastatController().writeFormInput(out,"ds_bene"); %>
				</td>
			</tr>
			<tr>      			
				<td>
					<% bp.getDettaglioIntrastatController().writeFormLabel(out,"natura_transazione"); %>
				</td>
				<td colspan="3">
					<% bp.getDettaglioIntrastatController().writeFormInput(out, "natura_transazione"); %>
				</td>
				<% bp.getDettaglioIntrastatController().writeFormField(out,"modalita_trasporto"); %>
			</tr>
			<tr>   
				<td>
					<% bp.getDettaglioIntrastatController().writeFormLabel(out,"nazione_origine"); %>
				</td>
				<td colspan="3">
					<% bp.getDettaglioIntrastatController().writeFormInput(out, "nazione_origine"); %>
				</td>
				<% bp.getDettaglioIntrastatController().writeFormField(out,"condizione_consegna"); %>
			</tr>
			<tr>
				<td>
					<% bp.getDettaglioIntrastatController().writeFormLabel(out,"nazione_provenienza"); %>
				</td>
				<td colspan="3">
					<% bp.getDettaglioIntrastatController().writeFormInput(out, "nazione_provenienza"); %>
				</td>
			</tr> 
			<tr>      			
				<td>
					<% bp.getDettaglioIntrastatController().writeFormLabel(out,"provincia_destinazione"); %>
				</td>
				<td colspan="3">
					<% bp.getDettaglioIntrastatController().writeFormInput(out, "provincia_destinazione"); %>
				</td>
			</tr>
			<tr>
				<td>
					<% bp.getDettaglioIntrastatController().writeFormLabel(out,"nomenclatura_combinata"); %>
				</td>
				<td colspan="3">
					<% bp.getDettaglioIntrastatController().writeFormInput(out, "nomenclatura_combinata"); %>
				</td>
			</tr>
			<tr>      			
				<td>
					<% bp.getDettaglioIntrastatController().writeFormLabel(out,"unita_supplementari"); %>
				</td>
				<td colspan="3">
					<% bp.getDettaglioIntrastatController().writeFormInput(out,"unita_supplementari"); %>
				</td>
				<% bp.getDettaglioIntrastatController().writeFormField(out,"valore_statistico"); %>
			</tr>
			<tr>      			
			   <td>
					<% bp.getDettaglioIntrastatController().writeFormLabel(out,"ammontare_euro"); %>
				</td>
				<td colspan="3">
					<% bp.getDettaglioIntrastatController().writeFormInput(out,"ammontare_euro"); %>
				</td>
				<% bp.getDettaglioIntrastatController().writeFormField(out,"ammontare_divisa"); %>
			</tr>
			<tr>      			
				<td>
					<% bp.getDettaglioIntrastatController().writeFormLabel(out,"massa_netta"); %>
				</td>
				<td colspan="3">
					<% bp.getDettaglioIntrastatController().writeFormInput(out,"massa_netta"); %>
				</td>
			</tr>
		</table>
	</div>
	<% } else { %>
	<div class="Group card" style="width:100%">
		<table width="100%">
			<tr>
			  	<td colspan="2">
				  	<span class="FormLabel" style="color:black">Dettagli intrastat</span>
			  	</td>
			</tr>
			<tr>
			  	<td colspan="2">
					<%	bp.getDettaglioIntrastatController().writeHTMLTable(pageContext,"ServizioSet",true,false,noninviato,"100%","100px"); %>
			  	</td>
			</tr>
		</table>
	</div>
	<div class="Group card" style="width:100%">
		<table width="100%">
			<tr>      			
				<td>
					<% bp.getDettaglioIntrastatController().writeFormLabel(out,"esercizio"); %>
				</td>
				<td colspan="3">
					<% bp.getDettaglioIntrastatController().writeFormInput(out,"esercizio"); %>
				</td>
			</tr>
			<tr>      			
				<td>
					<% bp.getDettaglioIntrastatController().writeFormLabel(out,"ds_bene"); %>
				</td>
				<td colspan="3">
					<% bp.getDettaglioIntrastatController().writeFormInput(out,"ds_bene"); %>
				</td>
			</tr>
			<tr>
				<td>      			
					<% bp.getDettaglioIntrastatController().writeFormLabel(out,"modalita_incasso"); %>
				</td>
				<td colspan="3">      			
					<% bp.getDettaglioIntrastatController().writeFormInput(out,"modalita_incasso"); %>
				</td>
			</tr>
			<tr>
				<td>      			
					<% bp.getDettaglioIntrastatController().writeFormLabel(out,"modalita_erogazione"); %>
				</td>
				<td colspan="3">      			
					<% bp.getDettaglioIntrastatController().writeFormInput(out,"modalita_erogazione"); %>
				</td>			
			</tr>
			<tr>
				<td>
					<% bp.getDettaglioIntrastatController().writeFormLabel(out,"codici_cpa"); %>
				</td>
				<td colspan="3">
					<% bp.getDettaglioIntrastatController().writeFormInput(out, "codici_cpa"); %>
				</td>
			</tr>
			<tr>
				<td>
					<% bp.getDettaglioIntrastatController().writeFormLabel(out,"nazione_provenienza"); %>
				</td>
				<td colspan="3">
					<% bp.getDettaglioIntrastatController().writeFormInput(out, "nazione_provenienza"); %>
				</td>
			</tr>
			<tr>      			
				<td>
					<% bp.getDettaglioIntrastatController().writeFormLabel(out,"ammontare_euro"); %>
				</td>
				<td>
					<% bp.getDettaglioIntrastatController().writeFormInput(out,"ammontare_euro"); %>
				</td>
			 	<% bp.getDettaglioIntrastatController().writeFormField(out,"ammontare_divisa"); %>
			</tr> 
			
		</table>
	</div>	
	<%}%>