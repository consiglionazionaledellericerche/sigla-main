<%@ page 
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.ordmag.ordini.bp.CRUDOrdineAcqBP,
		it.cnr.contab.ordmag.ordini.bulk.OrdineAcqBulk,
		it.cnr.contab.ordmag.ordini.bulk.OrdineAcqRigaBulk,
		it.cnr.contab.doccont00.core.bulk.Obbligazione_scadenzarioBulk,
		it.cnr.contab.ordmag.anag00.*"
%>

<%	CRUDOrdineAcqBP bp = (CRUDOrdineAcqBP)BusinessProcess.getBusinessProcess(request);
OrdineAcqBulk ordine = (OrdineAcqBulk)bp.getModel(); %>
	<div class="Group" style="width:100%">
		<table width="100%">
			<tr>
			  	<td colspan="3">
				  	<span class="FormLabel" style="color:black">Impegni</span>
			  	</td>
			</tr>
			<tr>
			  	<td colspan="3">
					<%	boolean canAdd = true;
						bp.getObbligazioniController().writeHTMLTable(pageContext,"default",canAdd,false,true,"100%","100px"); %>
			  	</td>
			</tr>
			<tr>
			  	<td>
		   			<% bp.writeFormLabel(out,"importoTotalePerObbligazione"); %>
			  	</td>
			  	<td>
				  	<% bp.writeFormInput(out,null,"importoTotalePerObbligazione",false,null,"style=\"color:black\"");%>
			  	</td>
	   		</tr>
		</table>
	</div>
	<div class="Group" style="width:100%">
   		<table width="100%">
			<tr>
			  	<td>
		   			<%	Obbligazione_scadenzarioBulk obbl = (Obbligazione_scadenzarioBulk)bp.getObbligazioniController().getModel();
		   				if (obbl == null) { %>
						  	<span class="FormLabel" style="color:black">Dettagli ordine</span>
		   			<%	} else { %>
						  	<span class="FormLabel" style="color:black">Dettagli ordine su "<%=obbl.getDs_scadenza()%>"</span>
		   			<%	} %>
			  	</td>
			</tr>
			<tr>
			  	<td>
					<% bp.getDettaglioObbligazioneController().writeHTMLTable(pageContext,"consegneSet",true,false,true,"100%","200px"); %>
			  	</td>
			</tr>
   		</table>
	</div>