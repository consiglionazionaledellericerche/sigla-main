<%@ page 
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.contab.docamm00.docs.bulk.*,
		it.cnr.jada.util.action.*"
%>


<%	CRUDBP bp = (CRUDBP)BusinessProcess.getBusinessProcess(request);
	Documento_genericoBulk documento = (Documento_genericoBulk)bp.getModel();
%>

<div class="Group">
	<table class="Panel">
	  <% if (documento.isRiportataInScrivania() && !bp.isSearching()) { %>	
	      <tr>
			<span class="FormLabel" style="color:red">
				<%=documento.getRiportataKeys().get(documento.getRiportataInScrivania()) %>
			</span>
	      </tr>
	  <% } %>
		<tr>
		 	<% bp.getController().writeFormField(out,"pg_documento_generico"); %>
		 	<% bp.getController().writeFormField(out,"esercizio"); %>
		</tr>
	    <% if (!bp.isSearching()) { %>	 
			<tr>
				<td>
					<% bp.getController().writeFormLabel(out,"tipo_doc");%>
				</td>
				<td colspan="3">
					<% bp.getController().writeFormInput(out, null,"tipo_doc",false,null,"onChange=\"submitForm('doOnTipoDocumentoChange')\"");%>
				</td>
			</tr>
			<tr>
			 	<% bp.getController().writeFormField(out,"stato_cofi");%>
				<% bp.getController().writeFormField(out,"ti_associato_manrev");%>
			</tr>
			<tr>
				<td>
					<% bp.getController().writeFormLabel(out,"flagEnte");%>
				</td>
				<td>
					<% bp.getController().writeFormInput(out,null,"flagEnte",false,null,"onClick=\"submitForm('doOnFlagEnteChange')\"");%>
				</td>
			</tr>
		<% } else { %>
			<tr>
				<td>
					<% bp.getController().writeFormLabel(out,"tipo_doc_for_search");%>
				</td>
				<td colspan="3">
					<% bp.getController().writeFormInput(out, null,"tipo_doc_for_search",false,null,"");%>
				</td>
			</tr>
			<tr>
				<% bp.getController().writeFormField(out,"stato_cofiForSearch");%>
				<% bp.getController().writeFormField(out,"ti_associato_manrevForSearch");%>
			</tr>
			<tr>
				<td>
					<% bp.getController().writeFormLabel(out,"flagEnte");%>
				</td>
				<td>
					<% bp.getController().writeFormInput(out,null,"flagEnte",false,null,"onClick=\"submitForm('doOnFlagEnteChange')\"");%>
				</td>
			</tr>
		<% } %>
	</table>
</div>
<div class="Group">
	<table>
		<% if (!bp.isSearching()) { %>
			<tr>
		     	<td>
		      		<% bp.getController().writeFormLabel(out,"ti_istituz_commerc");%>
		      	</td>      	
		     	<td>
		      		<% bp.getController().writeFormInput(out,null,"ti_istituz_commerc",false,null,"onChange=\"submitForm('doOnIstituzionaleCommercialeChange')\"");%>
		      	</td>
			</tr>
			<tr>
				<% bp.getController().writeFormField(out,"data_registrazione"); %>
				<% bp.getController().writeFormField(out,"dt_scadenza");%>
			</tr>
		<% } else { %>
			<tr>
	      		<% bp.getController().writeFormField(out,"ti_istituz_commercSearch"); %>
			</tr>
			<tr>
				<% bp.getController().writeFormField(out,"data_registrazioneForSearch"); %>
				<% bp.getController().writeFormField(out,"dt_scadenza");%>
			</tr>
		<% } %>
		<tr>
			<% bp.getController().writeFormField(out,"im_totale");%>
		</tr>  
		<tr>      	
			<% bp.getController().writeFormField(out,"dt_da_competenza_coge");%>			  
			<% bp.getController().writeFormField(out,"dt_a_competenza_coge");%>			  
		</tr>
	</table>
	<table>
		<tr>
			<% bp.getController().writeFormField(out,"ds_documento_generico");%>
		</tr>  
	</table>
</div>
 
<div class="Group">
    <table>
       <tr>
			<td>
				<% bp.getController().writeFormLabel(out,"valutaFissa");%>			  
			</td>
			<td>
				<% bp.getController().writeFormInput(out,null,"valutaFissa",false,null,"");%>
			</td>
			<td>
				<% bp.getController().writeFormLabel(out,"cambio");%>			  
			</td>
			<td>
				<% bp.getController().writeFormInput(out,null,"cambio",false,null,"");%>
			</td>
      </tr>
    </table>
    </div>