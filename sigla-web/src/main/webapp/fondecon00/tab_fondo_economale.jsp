<%@ page pageEncoding="UTF-8"
	import="it.cnr.jada.action.*,
		it.cnr.jada.bulk.*,
		it.cnr.jada.util.action.*,
		it.cnr.jada.util.jsp.*,
		java.util.*,
		it.cnr.contab.fondecon00.bp.*,
		it.cnr.contab.fondecon00.core.bulk.*,
		it.cnr.contab.anagraf00.core.bulk.*,
		it.cnr.contab.anagraf00.tabrif.bulk.*"
%>

<%
	FondoEconomaleBP bp = (FondoEconomaleBP)BusinessProcess.getBusinessProcess(request);
	Fondo_economaleBulk fondo = (Fondo_economaleBulk)bp.getModel();
	TerzoBulk economo = fondo.getEconomo();
	boolean roField = fondo.isOnlyForClose();
%>

<div class="Group">
	<table class="Panel">
		<tr>
			<td>
				<table class="Panel">
					<tr>
						<td>
							<% bp.getController().writeFormLabel(out,"esercizio"); %>
						</td>
						<td colspan="2">
							<% bp.getController().writeFormInput(out,"esercizio"); %>
						</td>
					</tr>
					<tr>
						<td>
							<% bp.getController().writeFormLabel(out,"cd_unita_organizzativa"); %>
						</td>
						<td>
 							<% bp.getController().writeFormInput(out,"cd_unita_organizzativa"); %>
						</td>
						<td>
							<% bp.getController().writeFormInput(out,"ds_unita_organizzativa");%>
						</td>
					</tr>
					<tr>
						<td>
							<% bp.getController().writeFormLabel(out,"cd_codice_fondo"); %>
						</td>
						<td>
							<% bp.getController().writeFormInput(out,"cd_codice_fondo"); %>
						</td>
						<td>
							<% bp.getController().writeFormInput(out, null, "ds_fondo", roField, null, ""); %>
						</td>
					</tr>
					<tr>
						<% bp.getController().writeFormField(out,"fl_aperto");%>
					</tr>
					<tr>
						<td>
							<% bp.getController().writeFormLabel(out,"cd_sospeso_di_chiusura"); %>
						</td>
						<td colspan="2">
							<% bp.getController().writeFormInput(out,"cd_sospeso_di_chiusura"); %>
						</td>
					</tr>
					<tr>
						<td>
							<% bp.getController().writeFormLabel(out,"pg_reversale"); %>
						</td>
						<td colspan="2">
							<% bp.getController().writeFormInput(out,"pg_reversale"); %>
							<% bp.getController().writeFormInput(out,"esercizio_reversale"); %>
						</td>
					</tr>
				</table>
			</td>
			<td>
				<div class="Group">
					<table class="Panel">
						<tr>
							<td>
								<center>
									<%JSPUtils.button(out, "img/preferences16.gif", "img/preferences16.gif", "Associa/Disassocia Spese", "if (disableDblClick()) javascript:submitForm('doApriRicercaObbScad')", null, bp.isEditing() && !fondo.isChiuso(), bp.getParentRoot().isBootstrap()); %>
								</center>
							</td>
						</tr>
						<tr>
							<td>
								<center>
									<%JSPUtils.button(out, "img/import16.gif", "img/import16.gif", "Reintegro Spese Associate", "if (disableDblClick()) javascript:submitForm('doReintegro')", null, bp.isEditing() && !fondo.isChiuso() && !fondo.isOnlyForClose(), bp.getParentRoot().isBootstrap()); %>
								</center>
							</td>
						</tr>
						<tr>
							<td>
								<center>
									<% JSPUtils.button(out, "img/refresh16.gif", "img/refresh16.gif", "Chiudi Spese", "if (disableDblClick()) javascript:submitForm('doChiudiSpese')", null, bp.isEditing() && !fondo.isChiuso(), bp.getParentRoot().isBootstrap()); %>
								</center>
							</td>
						</tr>
						<tr>
							<td>
								<center>
									<% JSPUtils.button(out, "img/stop16.gif", "img/stop16.gif", "Chiudi Fondo", "if (disableDblClick()) javascript:submitForm('doChiudiFondo')", null, bp.isEditing() && fondo.isChiuso() && fondo.isReversaleNecessaria(), bp.getParentRoot().isBootstrap()); %>
								</center>
							</td>
						</tr>
					</table>
				</div>
			</td>
		</tr>
		<tr>
			<td colspan="4">
				<center>
				<%
					String [][] defaultPages = new String[][] {
								{ "tabFondoEconomaleEconomo","Economo","/fondecon00/tab_fondo_economale_economo.jsp" },
								{ "tabFondoEconomaleImporti","Importi","/fondecon00/tab_fondo_economale_importi.jsp" }
							};
					
					JSPUtils.tabbed(
									pageContext,
									"subtab",
									defaultPages,
									bp.getTab("subtab"),
									"center",
									"100%",
									null );
				%>
				</center>
			</td>
		</tr>
	</table>
</div>