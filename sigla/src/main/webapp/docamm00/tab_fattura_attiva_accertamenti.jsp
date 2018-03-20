<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.util.jsp.*,
		it.cnr.jada.action.*,
		java.util.*,
		it.cnr.jada.util.action.*,
		it.cnr.contab.docamm00.tabrif.bulk.*,
		it.cnr.contab.docamm00.docs.bulk.*,
		it.cnr.contab.doccont00.core.bulk.*,
		it.cnr.contab.docamm00.bp.*"
%>

<%	CRUDFatturaAttivaBP bp = (CRUDFatturaAttivaBP)BusinessProcess.getBusinessProcess(request);
	Fattura_attivaBulk fatturaAttiva = (Fattura_attivaBulk)bp.getModel(); %>
	<div class="Group card" style="width:100%">
		<table width="100%">
			<tr>
			  	<td colspan="2">
				  	<span class="FormLabel" style="color:black">Accertamenti</span>
			  	</td>
			</tr>
			<tr>
			  	<td colspan="2">
					<%	boolean canAdd = true;
						boolean canDel = true;
						if (fatturaAttiva instanceof Nota_di_credito_attivaBulk || fatturaAttiva instanceof Nota_di_debito_attivaBulk)
							canAdd = false;
						if (fatturaAttiva.getStato_cofi() != null && fatturaAttiva.getStato_cofi().equals(fatturaAttiva.STATO_ANNULLATO))
							canDel = false;
						bp.getAccertamentiController().writeHTMLTable(pageContext,"accertamentoFatturaAttiva",canAdd,false,canDel,"100%","100px"); %>
			  	</td>
			</tr>
			<tr>
			  	<td>
		   			<% bp.writeFormLabel(out,"importoTotalePerAccertamento"); %>
			  	</td>
			  	<td>
				  	<% bp.writeFormInput(out,null,"importoTotalePerAccertamento",false,null,"style=\"color:black\"");%>
			  	</td>
	   		</tr>
		</table>
	</div>
	<div class="Group card" style="width:100%">
   		<table width="100%">
			<tr>
			  	<td>
		   			<%	Accertamento_scadenzarioBulk scadenza = (Accertamento_scadenzarioBulk)bp.getAccertamentiController().getModel();
		   				if (scadenza == null) { %>
						  	<span class="FormLabel" style="color:black">Dettagli fattura contabilizzati</span>
		   			<%	} else { %>
						  	<span class="FormLabel" style="color:black">Dettagli fattura contabilizzati su "<%=scadenza.getDs_scadenza()%>"</span>
		   			<%	} %>
			  	</td>
			</tr>
			<tr>
			  	<td>
					<% bp.getDettaglioAccertamentoController().writeHTMLTable(pageContext,"righiSet",true,false,true,"100%","150px"); %>
			  	</td>
			</tr>
   		</table>
	</div>