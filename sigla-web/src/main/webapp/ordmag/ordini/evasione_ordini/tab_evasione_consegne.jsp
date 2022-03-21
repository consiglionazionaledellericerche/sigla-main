<%@page import="it.cnr.contab.ordmag.ordini.bp.CRUDEvasioneOrdineBP,
it.cnr.contab.ordmag.ordini.bulk.OrdineAcqConsegnaBulk"%>
<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
			it.cnr.jada.action.*,
			java.util.*,
			it.cnr.jada.util.action.*"
%>
 <% CRUDEvasioneOrdineBP bp= (CRUDEvasioneOrdineBP)BusinessProcess.getBusinessProcess(request); 
 	OrdineAcqConsegnaBulk consegna = (OrdineAcqConsegnaBulk)bp.getConsegne().getModel();
	String collapseIconClass = bp.isCriteriRicercaCollapse() ? "fa-chevron-circle-down" : "fa-chevron-circle-up";
 %>

<div class="Group card border-primary">
	<%if (bp.getParentRoot().isBootstrap()) { %>  
    <div class="card-header">
        <h5 class="mb-0">
            <a onclick="submitForm('doToggle(criteriRicerca)')" class="text-primary"><i aria-hidden="true" class="fa <%=collapseIconClass%>"></i> Criteri di Ricerca</a>
        </h5>
    </div>
    <% } %>
    <div class="card-block">
        <% if (!bp.isCriteriRicercaCollapse() || !bp.getParentRoot().isBootstrap()) { %>
            <table cellpadding="5px">
				<tr>
					<% bp.getController().writeFormField( out, "find_esercizio_ordine"); %>
					<% bp.getController().writeFormField( out, "find_data_ordine"); %>
					<% bp.getController().writeFormField( out, "find_cd_numeratore_ordine"); %>
				</tr>
				<tr>
					<% bp.getController().writeFormField( out, "find_numero_ordine"); %>
					<% bp.getController().writeFormField( out, "find_riga_ordine"); %>
					<% bp.getController().writeFormField( out, "find_consegna_ordine"); %>
				</tr>
				<tr>
					<% bp.getController().writeFormField( out, "find_cd_terzo"); %>
					<% bp.getController().writeFormField( out, "find_cd_precedente"); %>
					<% bp.getController().writeFormField( out, "find_ragione_sociale"); %>
				</tr>
            </table>
			<table width="100%">
				<tr>
					<td align="center">
					    <% JSPUtils.button(out,
		                    bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-search" : "img/find24.gif",
		                    bp.getParentRoot().isBootstrap() ? "fa fa-fw fa-search" : "img/find24.gif",
		                    bp.encodePath("Ricerca"),
		                    "javascript:submitForm('doCercaConsegneDaEvadere')",
		                    "btn-outline-primary btn-title",
		                    bp.isEditable(),
		                    bp.getParentRoot().isBootstrap()); %>
					</td>
				</tr>
			</table>
        <% } %>
    </div>
</div>

<% if (bp.isCriteriRicercaCollapse() || !bp.getConsegne().getDetails().isEmpty() || !bp.getParentRoot().isBootstrap()) {
	   bp.getConsegne().writeHTMLTable(pageContext,"consegneSet",false,false,false,"100%","300px", true); 
	
	   if (bp.getConsegne().getSelection().getFocus()>=0 && bp.getConsegne().getSelection().isSelected(bp.getConsegne().getSelection().getFocus())) { %>
		<div class="Group card p-3" width="100%">
			<table width="100%">
				 <tr>
				 	<td><% bp.getConsegne().writeFormLabel(out, "findBeneServizio");	%></td>
					<td colspan="4"><% bp.getConsegne().writeFormInput(out,"default","findBeneServizio",true, null, null);%></td>
					<td class="d-flex flex-row-reverse">
						<table>
							<tr>
								<td class="text-right"><% bp.getConsegne().writeFormLabel(out, "cdUnitaMisuraMinima");	%></td>
								<td><% bp.getConsegne().writeFormInput(out,"default","cdUnitaMisuraMinima",true, null, null);%></td>
							</tr>
						</table>
					</td>
				 </tr>
				 <tr>
			        <td><% bp.getConsegne().writeFormLabel(out,"notaRigaEstesa");%></td>
		    	    <td colspan="5"><% bp.getConsegne().writeFormInput(out,"default","notaRigaEstesa",true, null, null);%></td>
		      	 </tr>
		      	 <tr>
					 <% bp.getConsegne().writeFormField(out, "findUnitaMisuraEvasa"); %>
					 <% bp.getConsegne().writeFormField(out, "coefConvEvasa"); %>
			         <% bp.getConsegne().writeFormField(out, "quantitaEvasa");%>
				 </tr>
				 <tr>
			         <td><% bp.getConsegne().writeFormLabel(out,"lottoFornitore");%></td>
			         <td colspan="5">
			         	<table width="100%">
			         		<tr>
					         	<td><% bp.getConsegne().writeFormInput(out,"lottoFornitore");%></td>
			         			<td colspan="2" class="text-right">
			         				<% bp.getConsegne().writeFormLabel(out,"dtScadenza");%>
			         				<% bp.getConsegne().writeFormInput(out,"dtScadenza");%>
			         			</td>
			         			<td colspan="2" class="text-right">
					         	<% if (consegna.isQuantitaEvasaMinoreOrdine()) {
			         				  bp.getConsegne().writeFormLabel(out,"operazioneQuantitaEvasaMinore");
			         				  bp.getConsegne().writeFormInput(out,"operazioneQuantitaEvasaMinore");
					         	   }
								   if (consegna.isQuantitaEvasaMaggioreOrdine()) {
				         			  bp.getConsegne().writeFormLabel(out,"autorizzaQuantitaEvasaMaggioreOrdinata");
				         			  bp.getConsegne().writeFormInput(out,"autorizzaQuantitaEvasaMaggioreOrdinata");
								   }
								%>
						    </tr>
						</table>
					 </td>
			     </tr>
				 <tr>
			         <td colspan="6" width="100%">
						<table width="100%">
							<tr>
								<td></td>
						        <td width="50%">
									<div class="GroupLabel font-weight-bold text-primary ml-2">Quantità in UM Minima</div>  
									<div class="Group card p-3" width="100%">
									<table width="100%">
									    <tr>
											<% bp.getConsegne().writeFormField(out, "qtEvasaConvertita"); %>
											<td class="pl-5"><% bp.getConsegne().writeFormLabel(out, "qtConvertita"); %></td>
											<td><% bp.getConsegne().writeFormInput(out, "qtConvertita"); %></td>
										</tr>
									</table>
									</div>
								</td>
								<td></td>
							</tr>
						</table>
					</td>
		 	     </tr>
			</table>
		 </div>
		<% } %>
    
	<div class="Group card border-primary">
		<%if (bp.getParentRoot().isBootstrap()) { %>  
	    <div class="card-header">
	        <h5 class="mb-0">
	            <a onclick="submitForm('doToggle(dettConsegne)')" class="text-primary"><i aria-hidden="true" class="fa <%=collapseIconClass%>"></i> Dettagli Riga Consegna</a>
	        </h5>
	    </div>
		<% } %>
	    <div class="card-block">
        <% if (!bp.isDettConsegneCollapse() || !bp.getParentRoot().isBootstrap()) { %>
            <table cellpadding="5px" width="100%">
			  <tr>
		         <td><% bp.getConsegne().writeFormLabel(out,"tipoConsegna");%></td>
		         <td colspan="7">
		         	<table>
		         		<tr>
					         <td><% bp.getConsegne().writeFormInput(out,"default","tipoConsegna",true, null, null);%></td>
					         <td class="pl-3"><% bp.getConsegne().writeFormLabel(out,"dtPrevConsegna");%></td>
					         <td><% bp.getConsegne().writeFormInput(out,"default","dtPrevConsegna",true, null, null);%></td>
					         <td class="pl-3"><% bp.getConsegne().writeFormLabel(out,"findUnitaOperativaOrdDest");%></td>
					         <td><% bp.getConsegne().writeFormInput(out,"default","findUnitaOperativaOrdDest",true, null, null);%></td>
					    </tr>
					</table>
				 </td>
		      </tr>
			  <tr>         
		         <td><% bp.getConsegne().writeFormLabel(out,"imImponibile");%></td>
		         <td colspan="7">
		         	<table class="w-100">
		         		<tr>
					         <td><% bp.getConsegne().writeFormInput(out,"default","imImponibile",true, null, null);%></td>
					         <td><% bp.getConsegne().writeFormLabel(out,"imIva");%></td>
					         <td><% bp.getConsegne().writeFormInput(out,"default","imIva",true, null, null);%></td>
					         <td><% bp.getConsegne().writeFormLabel(out,"imTotaleConsegna");%></td>
					         <td><% bp.getConsegne().writeFormInput(out,"default","imTotaleConsegna",true, null, null);%></td>
					    </tr>
		         	</table>
		         </td>
		      </tr>
			</table>
		<% } %>
		</div>
	</div>
<% } %>