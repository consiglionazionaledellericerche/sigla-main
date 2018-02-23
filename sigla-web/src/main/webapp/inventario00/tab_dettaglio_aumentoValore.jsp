<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.inventario00.tabrif.bulk.*,
		it.cnr.contab.inventario01.bulk.*,
		it.cnr.contab.inventario01.bp.*"
%>
<% CRUDCaricoInventarioBP bp = (CRUDCaricoInventarioBP)BusinessProcess.getBusinessProcess(request); 
   Buono_carico_scaricoBulk buonoCarico = (Buono_carico_scaricoBulk)bp.getModel(); 
   Buono_carico_scarico_dettBulk riga = (Buono_carico_scarico_dettBulk)bp.getDettaglio().getModel(); %>
   
   	<% bp.getDettaglio().writeHTMLTable(pageContext,"righeSetConCodice",bp.isCRUDAddButtonEnabled(),false,bp.isCRUDDeleteButtonEnabled(),"100%","200px"); %>

  	<table>	
	  <tr>
		<td><% bp.getDettaglio().writeFormLabel(out,"find_categoria_bene"); %></td>
		<td><% bp.getDettaglio().writeFormInput(out,null,"find_categoria_bene",true,null,null); %></td>
	  </tr>
		<td><% bp.getDettaglio().writeFormLabel(out,"ds_bene"); %></td>
		<td colspan=2><% bp.getDettaglio().writeFormInput(out,null,"ds_bene",true,null,null); %></td>
	</table>

	<table>	
	  <tr>
		<td><% bp.getDettaglio().writeFormLabel(out,"valoreInizialeBene"); %></td>
		<td><% bp.getDettaglio().writeFormInput(out,"valoreInizialeBene"); %></td>
		<td><% bp.getDettaglio().writeFormLabel(out,"valoreBene"); %></td>
		<td><% bp.getDettaglio().writeFormInput(out,"valoreBene"); %></td>
	  </tr>
	  <tr>
		<td><% bp.getDettaglio().writeFormLabel(out,"valoreDaCaricare"); %></td>
		<td><% bp.getDettaglio().writeFormInput(out,null,"valoreDaCaricare",(riga!=null && (riga.isTotalmenteScaricato() || riga.isROValore_unitario()))|| bp.isBy_documento()||bp.isBy_fattura()|| (bp.isAssociata(HttpActionContext.getUserContext(session),riga)
				||bp.isNonUltimo(HttpActionContext.getUserContext(session),riga)||!bp.isModValore_unitario())&&!bp.isInserting(),null,null); %></td>		
	  </tr>
	</table>	