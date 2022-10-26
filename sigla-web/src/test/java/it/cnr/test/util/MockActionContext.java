/*
 * Copyright (C) 2020  Consiglio Nazionale delle Ricerche
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package it.cnr.test.util;

import it.cnr.jada.UserContext;
import it.cnr.jada.action.*;
import it.cnr.jada.bulk.UserInfo;

import java.text.ParseException;
import java.util.Enumeration;

public class MockActionContext implements ActionContext {

    private UserContext usercontext;

    public MockActionContext(UserContext usercontext) {
        this.usercontext = usercontext;
    }

    @Override
    public BusinessProcess addBusinessProcess(BusinessProcess businessprocess) throws BusinessProcessException {
        return null;
    }

    @Override
    public HookForward addHookForward(BusinessProcess businessprocess, String s, Action action) {
        return null;
    }

    @Override
    public HookForward addHookForward(BusinessProcess businessprocess, String s, Action action, String s1) {
        return null;
    }

    @Override
    public HookForward addHookForward(String s, Action action) {
        return null;
    }

    @Override
    public HookForward addHookForward(String s, Action action, String s1) {
        return null;
    }

    @Override
    public HookForward addHookForward(String s, Forward forward) {
        return null;
    }

    @Override
    public void addRequestTracingUser(String s) {

    }

    @Override
    public BusinessProcess closeBusinessProcess() throws BusinessProcessException {
        return null;
    }

    @Override
    public BusinessProcess closeBusinessProcess(BusinessProcess businessprocess) throws BusinessProcessException {
        return null;
    }

    @Override
    public BusinessProcess createBusinessProcess(String s) throws BusinessProcessException {
        return null;
    }

    @Override
    public BusinessProcess createBusinessProcess(String s, Object[] aobj) throws BusinessProcessException {
        return null;
    }

    @Override
    public boolean fill(Object obj) throws ParseException {
        return false;
    }

    @Override
    public boolean fill(Object obj, String s) throws ParseException {
        return false;
    }

    @Override
    public boolean fillProperty(Object obj, String s, String s1) throws ParseException {
        return false;
    }

    @Override
    public Forward findActionForward(String s) {
        return null;
    }

    @Override
    public Forward findDefaultForward() {
        return null;
    }

    @Override
    public Forward findForward(String s) {
        return null;
    }

    @Override
    public String getApplicationId() {
        return null;
    }

    @Override
    public BusinessProcess getBusinessProcess() {
        return null;
    }

    @Override
    public void setBusinessProcess(BusinessProcess businessprocess) {

    }

    @Override
    public BusinessProcess getBusinessProcess(String s) {
        return null;
    }

    @Override
    public BusinessProcess getBusinessProcessRoot(boolean flag) {
        return null;
    }

    @Override
    public Forward getCaller() {
        return null;
    }

    @Override
    public String getCurrentCommand() {
        return null;
    }

    @Override
    public Enumeration getRequestTracingUsers() {
        return null;
    }

    @Override
    public String getSessionId() {
        return null;
    }

    @Override
    public String getTracingSessionDescription() {
        return null;
    }

    @Override
    public void setTracingSessionDescription(String s) {

    }

    @Override
    public UserContext getUserContext(boolean createSession) {
        return null;
    }

    @Override
    public UserContext getUserContext() {
        return usercontext;
    }

    @Override
    public void setUserContext(UserContext usercontext) {
        this.usercontext = usercontext;
    }

    @Override
    public UserInfo getUserInfo() {
        return null;
    }

    @Override
    public void setUserInfo(UserInfo userinfo) {

    }

    @Override
    public void invalidateSession() {

    }

    @Override
    public boolean isRequestTracingUser() {
        return false;
    }

    @Override
    public boolean isRequestTracingUser(String s) {
        return false;
    }

    @Override
    public void perform(Action action, ActionMapping actionmapping, String s) {

    }

    @Override
    public void removeHookForward(String s) {

    }

    @Override
    public void removeRequestTracingUser(String s) {

    }

    @Override
    public void traceException(Throwable throwable) {

    }

    @Override
    public void saveFocusedElement() {

    }

    @Override
    public ActionMapping getActionMapping() {
        return null;
    }

    @Override
    public BusinessProcess getCurrentBusinessProcess() {
        return null;
    }
}
