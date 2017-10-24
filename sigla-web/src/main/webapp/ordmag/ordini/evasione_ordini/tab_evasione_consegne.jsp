<%@page import="it.cnr.contab.ordmag.ordini.bp.CRUDEvasioneOrdineBP,
it.cnr.contab.ordmag.ordini.bulk.OrdineAcqConsegnaBulk"%>
<%@ page 
	import="it.cnr.jada.util.jsp.*,
			it.cnr.jada.action.*,
			java.util.*,
			it.cnr.jada.util.action.*"
%>
 <% CRUDEvasioneOrdineBP bp= (CRUDEvasioneOrdineBP)BusinessProcess.getBusinessProcess(request); 
 	OrdineAcqConsegnaBulk consegna = (OrdineAcqConsegnaBulk)bp.getConsegne().getModel();
 %>
	<table border="0" cellspacing="2" cellpadding="4">
		<tr>
			<td><% bp.getController().writeFormLabel( out, "find_esercizio_ordine"); %></td>
			<td><% bp.getController().writeFormInput( out, "find_esercizio_ordine"); %></td>
			<td><% bp.getController().writeFormLabel( out, "find_data_ordine"); %></td>
			<td><% bp.getController().writeFormInput( out, "find_data_ordine"); %></td>
			<td><% bp.getController().writeFormLabel( out, "find_cd_numeratore_ordine"); %></td>
			<td><% bp.getController().writeFormInput( out, "find_cd_numeratore_ordine"); %></td>
		</tr>
		<tr>
			<td><% bp.getController().writeFormLabel( out, "find_numero_ordine"); %></td>
			<td><% bp.getController().writeFormInput( out, "find_numero_ordine"); %></td>
			<td><% bp.getController().writeFormLabel( out, "find_riga_ordine"); %></td>
			<td><% bp.getController().writeFormInput( out, "find_riga_ordine"); %></td>
			<td><% bp.getController().writeFormLabel( out, "find_consegna_ordine"); %></td>
			<td><% bp.getController().writeFormInput( out, "find_consegna_ordine"); %></td>
		</tr>
		<tr>
			<td><% bp.getController().writeFormLabel( out, "find_cd_terzo"); %></td>
			<td><% bp.getController().writeFormInput( out, "find_cd_terzo"); %></td>
			<td><% bp.getController().writeFormLabel( out, "find_cd_precedente"); %></td>
			<td><% bp.getController().writeFormInput( out, "find_cd_precedente"); %></td>			
			<td><% bp.getController().writeFormLabel( out, "find_ragione_sociale"); %></td>
			<td colspan=3><% bp.getController().writeFormInput( out, "find_ragione_sociale"); %></td>			
		</tr>
	</table>
	
		<tr>
			<td colspan=2 align="center">
				<% JSPUtils.button(out,bp.encodePath("img/find24.gif"),bp.encodePath("Ricerca"), "javascript:submitForm('doCercaConsegneDaEvadere')",null, bp.getParentRoot().isBootstrap()); %>
			</td>
		</tr>
		<tr>
			<td colspan=2>
			      <% bp.getConsegne().writeHTMLTable(pageContext,"consegneSet",false,false,false,"100%","300px", true); %>
			</td>
		</tr>

      <div>
	      <table>
		  <tr>
	         <td><% bp.getConsegne().writeFormLabel(out,"quantita");%></td>
	         <td><% bp.getConsegne().writeFormInput(out,"quantita");%></td>
				 <%    if (bp.isConsegnaEvasaMinoreQuantitaOrdinata()) {
				    	bp.getConsegne().writeFormField(out, "operazioneQuantitaEvasaMinore");
				    }
				    if (bp.isConsegnaEvasaMaggioreQuantitaOrdinata()) {
				    	bp.getConsegne().writeFormField(out, "autorizzaQuantitaEvasaMaggioreOrdinata");
				    }%>
	         <td><% bp.getConsegne().writeFormLabel(out,"lottoFornitore");%></td>
	         <td><% bp.getConsegne().writeFormInput(out,"lottoFornitore");%></td>
	         <td><% bp.getConsegne().writeFormLabel(out,"dtScadenza");%></td>
	         <td><% bp.getConsegne().writeFormInput(out,"dtScadenza");%></td>
	      </tr>  	      
		  <tr>
	         <td><% bp.getConsegne().writeFormLabel(out,"quantitaOriginaria");%></td>
	         <td><% bp.getConsegne().writeFormInput(out,"default","quantitaOriginaria",true, null, null);%></td>
	         <td><% bp.getConsegne().writeFormLabel(out,"tipoConsegna");%></td>
	         <td><% bp.getConsegne().writeFormInput(out,"default","tipoConsegna",true, null, null);%></td>
	         <td><% bp.getConsegne().writeFormLabel(out,"dtPrevConsegna");%></td>
	         <td><% bp.getConsegne().writeFormInput(out,"default","dtPrevConsegna",true, null, null);%></td>
	      </tr>  	
		  <tr>         
	         <td><% bp.getConsegne().writeFormLabel(out,"cdBeneServizio");%></td>
	         <td><% bp.getConsegne().writeFormInput(out,"default","cdBeneServizio",true, null, null);%></td>
	         <td><% bp.getConsegne().writeFormLabel(out,"dsBeneServizio");%></td>
	         <td><% bp.getConsegne().writeFormInput(out,"default","dsBeneServizio",true, null, null);%></td>
	         <td><% bp.getConsegne().writeFormLabel(out,"voceIvaCompleto");%></td>
	         <td><% bp.getConsegne().writeFormInput(out,"default","voceIvaCompleto",true, null, null);%></td>
	      </tr>            
		  <tr>         
	         <td><% bp.getConsegne().writeFormLabel(out,"notaRigaEstesa");%></td>
	         <td><% bp.getConsegne().writeFormInput(out,"default","notaRigaEstesa",true, null, null);%></td>
	      </tr>            
		  <tr>         
	         <td><% bp.getConsegne().writeFormLabel(out,"uopDestCompleta");%></td>
	         <td><% bp.getConsegne().writeFormInput(out,"default","uopDestCompleta",true, null, null);%></td>
	         <td><% bp.getConsegne().writeFormLabel(out,"descObbligazioneScadenzario");%></td>
	         <td><% bp.getConsegne().writeFormInput(out,"default","descObbligazioneScadenzario", true,null,null);%></td>
	      </tr>            
		  <tr>         
	         <td><% bp.getConsegne().writeFormLabel(out,"imImponibile");%></td>
	         <td><% bp.getConsegne().writeFormInput(out,"default","imImponibile",true, null, null);%></td>
	         <td><% bp.getConsegne().writeFormLabel(out,"imIva");%></td>
	         <td><% bp.getConsegne().writeFormInput(out,"default","imIva",true, null, null);%></td>
	         <td><% bp.getConsegne().writeFormLabel(out,"imIvaD");%></td>
	         <td><% bp.getConsegne().writeFormInput(out,"default","imIvaD",true, null, null);%></td>
	         <td><% bp.getConsegne().writeFormLabel(out,"imTotaleConsegna");%></td>
	         <td><% bp.getConsegne().writeFormInput(out,"default","imTotaleConsegna",true, null, null);%></td>
	      </tr>
	      </table>
      </div>