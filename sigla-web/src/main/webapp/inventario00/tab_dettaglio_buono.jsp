<%@ page pageEncoding="UTF-8"
	import="
		it.cnr.jada.action.*,
		it.cnr.contab.inventario01.bulk.*,
		it.cnr.contab.inventario00.docs.bulk.Inventario_beniBulk,
		it.cnr.contab.inventario01.bp.*"
%>
<% CRUDCaricoInventarioBP bp = (CRUDCaricoInventarioBP)BusinessProcess.getBusinessProcess(request); 
   Buono_carico_scarico_dettBulk riga = (Buono_carico_scarico_dettBulk)bp.getDettaglio().getModel();    
   Inventario_beniBulk bene = null; 
   if (riga != null) bene = riga.getBene(); %>   
   	<%bp.getDettaglio().writeHTMLTable(pageContext,"righeSetConCodice",bp.isCRUDAddButtonEnabled(),false,bp.isCRUDDeleteButtonEnabled(),"100%","200px"); %>
	<table>			
		<tr>
			<td>
				<% bp.getDettaglio().writeFormLabel(out,"find_categoria_bene"); %>
			</td>
			<td colspan="3">
				<% bp.getDettaglio().writeFormInput(out,null,"find_categoria_bene",(bp.isEditing() || bp.isBy_fattura()||(riga!=null && riga.isBeneAccessorio())),null,null); %>
			</td>
			<td>
				<% bp.getDettaglio().writeFormLabel(out,"targa"); %>
				<% bp.getDettaglio().writeFormInput(out,null,"targa",(bp.isEditing()|| (bene!=null && bene.isROtarga())),null,null); %>
			</td>		
				<td>
				<% bp.getDettaglio().writeFormLabel(out,"seriale"); %>
				<% bp.getDettaglio().writeFormInput(out,null,"seriale",(bp.isEditing()|| (bene!=null && bene.isROseriale())),null,null); %>
			</td>	
		</tr>
		<tr>
			<td>
				<% bp.getDettaglio().writeFormLabel(out,"ds_bene"); %>
			</td>
			<td colspan="3">
				<% bp.getDettaglio().writeFormInput(out,null,"ds_bene",bp.isEditing(),null,null); %>
			</td>
		</tr>
		<% 
			if (bp.isInserting()){ 
		%>
		<tr>
			<td>
				<% bp.getDettaglio().writeFormLabel(out,"condizione"); %>
			</td>
			<td>
				<% bp.getDettaglio().writeFormInput(out,"condizione"); %>				
			</td>			
			<td>
				<% bp.getDettaglio().writeFormLabel(out,"cd_barre"); %>
			</td>
			<td>
				<% bp.getDettaglio().writeFormInput(out,"cd_barre"); %>
			</td>
			<%
            	if (bp.isAttivaEtichettaInventarioBene()){
            %>
			<td>
                <% bp.getDettaglio().writeFormLabel(out,"etichetta"); %>
            </td>
            <td>
                <% bp.getDettaglio().writeFormInput(out,"etichetta"); %>
            </td>
			<%
                }
             %>
		</tr>
		<%
			}
			else {
		%>
		<tr>
			<td>
				<% bp.getDettaglio().writeFormLabel(out,"condizione_notIns"); %>
			</td>
			<td>
				<% bp.getDettaglio().writeFormInput(out,"condizione_notIns"); %>				
			</td>			
			<td>
				<% bp.getDettaglio().writeFormLabel(out,"cd_barre"); %>
			</td>
			<td>
				<% bp.getDettaglio().writeFormInput(out,null,"cd_barre",bp.isEditing(),null,null); %>				
			</td>
			<%
               if (bp.isAttivaEtichettaInventarioBene()){
            %>
                <td>
                    <% bp.getDettaglio().writeFormLabel(out,"etichetta"); %>
                </td>
                <td>
                    <% bp.getDettaglio().writeFormInput(out,null,"etichetta",bp.isEditing(),null,null); %>
                </td>
            <%
               }
            %>
		</tr>
		<% }  %>
		<tr>
			<td>
				<% bp.getDettaglio().writeFormLabel(out,"ti_istituzionale_commerciale"); %>
			</td>
			<td>
				<% bp.getDettaglio().writeFormInput(out,null,"ti_istituzionale_commerciale",(bp.isEditing() || bp.isBy_fattura()||bp.isBy_documento()),null,"onClick=\"submitForm('doDefault')\""); %>
			</td>	
					
		</tr>
		<%
			if (!riga.getBuono_cs().isByOrdini()){
		%>
		<tr>
			<td>
				<% bp.getDettaglio().writeFormLabel(out,"fl_bene_accessorio"); %>
			</td>
			<td>
				<% bp.getDettaglio().writeFormInput(out,null,"fl_bene_accessorio",(bp.isEditing() || riga!=null && riga.isROfl_accessorio()),null,((riga!=null && riga.isBeneAccessorio())?"onClick=\"submitForm('doDeselezionaBeneAccessorio')\"":"onClick=\"submitForm('doSelezionaBeneAccessorio')\"")); %>
			</td>			

		<% if (riga!=null && riga.isBeneAccessorio() && (!bp.isEditing()) && (!bp.isBy_fattura() && !bp.isBy_documento())){ %>

			<td>
				<% bp.getDettaglio().writeFormLabel(out,"fl_accessorio_contestuale"); %>
			</td>
			<td>
				<% bp.getDettaglio().writeFormInput(out,null,"fl_accessorio_contestuale",bp.isEditing(),null,(!riga.isAccessorioContestuale()?"onClick=\"submitForm('doFindAccessoriContestuali')\"":"onClick=\"submitForm('doDeselezionaAccessoriContestuali')\"")); %>
			</td>			
		</tr>
		<% } %>	
		<% } %>
		<% 	if (riga!=null && riga.isBeneAccessorio() && !riga.isAccessorioContestuale()){ %>
			<tr>
				<td>
					<% bp.getDettaglio().writeFormLabel(out,"find_bene_principale"); %>
				</td>	
				<td colspan="3">
					<% bp.getDettaglio().writeFormInput(out,null,"find_bene_principale",bp.isEditing(),null,null); %>
				</td>
			</tr>

	<% } 
	   else if (riga!=null && riga.isBeneAccessorio() && riga.isAccessorioContestuale()){ %>		

			<tr>
				<td>
					<% bp.getDettaglio().writeFormLabel(out,"ds_bene_principale_contestuale"); %>
				</td>	
				<td colspan="3">	
					<% bp.getDettaglio().writeFormInput(out,null,"ds_bene_principale_contestuale",bp.isEditing(),null,null); %>
				</td>
			</tr>

	<% } %>
		<tr>
			<td>
				<% bp.getDettaglio().writeFormLabel(out,"find_ubicazione"); %>
			</td>	
			
			<td colspan="3">
				<% bp.getDettaglio().writeFormInput(out,null,"find_ubicazione",bp.isEditing(),null,null); %>
			</td>
		</tr>
		
		<tr>
			<td>
				<% bp.getDettaglio().writeFormLabel(out,"collocazione"); %>
			</td>
			<td>	
				<% if (riga!=null) bene = riga.getBene(); 
				
				   bp.getDettaglio().writeFormInput(out,null,"collocazione",(bp.isEditing() ||  (bene != null?!bene.isPubblicazione():true)),null,null); %>
			</td>			
		</tr>	
		<tr>
			<td>
				<% bp.getDettaglio().writeFormLabel(out,"ti_utilizzatore"); %>
				<% bp.getDettaglio().writeFormInput(out,null,"ti_utilizzatore",bp.isEditing(),null,null); %>
			</td>			
		</tr>
		<tr>
			<td>
				<% bp.getDettaglio().writeFormLabel(out,"find_assegnatario"); %>
			</td>
			<td colspan="3">
				<% bp.getDettaglio().writeFormInput(out,null,"find_assegnatario",bp.isEditing(),null,null); %>
			</td>
		</tr>		
		<tr>
			<td>
				<% bp.getDettaglio().writeFormLabel(out,"quantita"); %>
			</td>	
			<td>				
				<% bp.getDettaglio().writeFormInput(out,null,"quantita",(bp.isEditing() || bp.isBy_fattura()||bp.isBy_documento() || bp.isAttivaEtichettaInventarioBene() ),null,"");%>
			</td>		
			<td>
				<% bp.getDettaglio().writeFormLabel(out,"valore_unitario"); %>
			</td>	
			<td>
				 <% bp.getDettaglio().writeFormInput(out,null,"valore_unitario",(riga!=null && riga.isTotalmenteScaricato())|| bp.isBy_fattura()||bp.isBy_documento()||(!bp.isModValore_unitario()||bp.isAssociata(HttpActionContext.getUserContext(session),riga)||
						 (riga!=null &&	 riga.getBuono_cs().getTipoMovimento().getFl_buono_per_trasferimento().booleanValue())|| bp.isNonUltimo(HttpActionContext.getUserContext(session),riga))&&!bp.isInserting(),null,"");%>
			</td>	
		</tr>
		<tr>
			<td>
				<% bp.getDettaglio().writeFormLabel(out,"valore_totale"); %>
			</td>
			<td>
				<% bp.getDettaglio().writeFormInput(out,"valore_totale"); %>
			</td>	
		</tr>
		<%
			if (!riga.getBuono_cs().isByOrdini()){
		%>
		<tr>
			<td>
				<% bp.getDettaglio().writeFormLabel(out,"imponibile_ammortamento"); %>
			</td>
			<td>
				<% bp.getDettaglio().writeFormInput(out,null,"imponibile_ammortamento",(riga!=null && riga.isTotalmenteScaricato()),null,null); %>
			</td>	
		</tr>	
		<%
			}
		%>
	 </table>