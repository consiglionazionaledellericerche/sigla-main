<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		it.cnr.contab.config00.bp.*,
		it.cnr.contab.config00.contratto.bulk.*"
%>
<%
	CRUDConfigAnagContrattoBP bp = (CRUDConfigAnagContrattoBP)BusinessProcess.getBusinessProcess(request);
	SimpleDetailCRUDController controller = bp.getCrudArchivioAllegati();
	AllegatoContrattoDocumentBulk bulk = (AllegatoContrattoDocumentBulk)controller.getModel();
%>
<script language="JavaScript">
function doScaricaAllegato() {	
	  doPrint('<%=JSPUtils.getAppRoot(request)%>genericdownload/<%=bp.getNomeAllegato()!=null?bp.getNomeAllegato().replace("'", "_"):""%>?methodName=scaricaAllegato&it.cnr.jada.action.BusinessProcess=<%=bp.getPath()%>');
}
</script>
	<table width=100%>
	  <tr>
	  	<td>
	  		<%controller.writeHTMLTable(pageContext,"archivioAllegati",!bp.isSearching(),false,!bp.isSearching(),"90%","100px");%>
	  	</td>
	  </tr>
	  <tr>
	  	<td>
			<table class="Panel" cellspacing=2>
				<tr><td  colspan="3">
					<div class="Group Group card p-2 bg-danger text-white h5">
                        <table>
                            <tr><td valign=top>
                                <span class="FormLabel">Attenzione:</span>
                            </td>
                            <td valign=top>
                                <span class="FormLabel">
                                al fine di rispettare le norme in materia di tutela dei dati personali, <br>
                                prima di allegare il file del contratto da pubblicare sul sito internet istituzionale del CNR <br>
                                e' necessario verificare che lo stesso esponga <b><i><u>esclusivamente</u></i></b> i seguenti dati personali: <br>
                                <b><i>Nome, Cognome, Luogo e Data di nascita, Codice Fiscale</i></b> <br>
                                Ogni altro dato personale dovrà essere <b><i>"<u>oscurato</u>"</i></b><br><br>
                                </span>
                            </td></tr>
                            <tr><td valign=top>
                                <span class="FormLabel">Attenzione:</span>
                            </td>
                            <td valign=top>
                                <span class="FormLabel">
                                ai fini della pubblicazione sul sito internet istituzionale del CNR, <i><u>si raccomanda di usare file in formato PDF</u></i><br>
                                e di <i><u>controllare sempre</u></i>, dopo il salvataggio, la leggibilità dell'allegato utilizzando il bottone "Apri file"<br>
                                </span>
                            </td></tr>
                        </table>
					</div>
				</td></tr>

				<tr>
					<td>
						<%
							controller.writeFormLabel(out, "default", "type");
						%>
					</td>
					<td colspan="2">
						<%
							controller.writeFormInput(out, "default", "type", !bp.isAllegatiEnabled() && !bulk.isToBeCreated(), null, null);
						%>
					</td>
				</tr>
				<% if (bulk != null && AllegatoContrattoDocumentBulk.PROGETTO.equals(bulk.getType())){%>
				<tr>
			        <td><% controller.writeFormLabel(out,"default","link"); %></td>
			        <td colspan="2"><% controller.writeFormInput(out,"default","link", !bp.isAllegatiEnabled() && !bulk.isToBeCreated(),null,null); %></td>
				</tr>
			    <%}%>
				<% if (bp.isAllegatiEnabled()){%>
					<tr>
				        <td><% controller.writeFormLabel(out,"default","file"); %></td>
				        <td colspan="2"><% controller.writeFormInput(out,"default","file", !bp.isAllegatiEnabled() && !bulk.isToBeCreated(),null,null); %></td>
				    </tr>
			    <%}%>
				<tr>
			        <td><% controller.writeFormLabel(out,"default","nome"); %></td>
			        <td><% controller.writeFormInput(out,"default","nome"); %></td>
			        <% if (bulk != null && bulk.isContentStreamPresent()){%>
			        	<td align="left"><% controller.writeFormInput(out,"default","attivaFile");%></td>
			        <%}%>
			    </tr>
				<tr>
			        <td><% controller.writeFormLabel(out,"default","descrizione"); %></td>
			        <td colspan="2"><% controller.writeFormInput(out,"default","descrizione", !bp.isAllegatiEnabled() && !bulk.isToBeCreated(),null,null); %></td>
				</tr>
				<tr>
			        <td><% controller.writeFormLabel(out,"default","titolo"); %></td>
			        <td colspan="2"><% controller.writeFormInput(out,"default","titolo", !bp.isAllegatiEnabled() && !bulk.isToBeCreated(),null,null); %></td>
				</tr>
			</table>  	
	  	</td>
	  </tr>
	</table>
