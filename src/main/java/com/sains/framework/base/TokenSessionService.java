/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sains.framework.base;

import com.sains.framework.model.TokenSessionModel;

/**
 *
 * @author User
 */
public class TokenSessionService extends BaseDAOImpl implements java.io.Serializable{
    public Boolean createSession(String token, String us_id) throws Exception{
        TokenSessionModel tokenSession = new TokenSessionModel();
        try {
            ModelBase.updateCurrentRequestObject("antiCSRF", "ignoreCSRF");
            ModelBase.updateCurrentRequestObject("ignoreCsrfCheck", "true");
            tokenSession.setToken(token);
            tokenSession.setUs_id(us_id);
            tokenSession.setToken_last_active_datetime(tokenSession.getCreated_date());
            //getSession().save(tokenSession);
            insert(tokenSession);
            return Boolean.TRUE;
        } catch (Exception e) {
            throw e;
        }
    }
    
    public Boolean isSessionExist(String token) {
        try {
            TokenSessionModel tokenSession = (TokenSessionModel) getSession().getNamedQuery("TokenSessionModel.findBy_token").setParameter("token", token).uniqueResult();
            return tokenSession != null;
        } catch (Exception e) {
        }
        return Boolean.FALSE;
    }
    
    public TokenSessionModel getSession(String token) {
        try {
            return (TokenSessionModel) getSession().getNamedQuery("TokenSessionModel.findBy_token").setParameter("token", token).uniqueResult();
        } catch (Exception e) {
        }
        return null;
    }
    public Boolean updateSession(String token) {
        try {
            TokenSessionModel tokenSession = (TokenSessionModel) getSession().getNamedQuery("TokenSessionModel.findBy_token").setParameter("token", token).uniqueResult();
            if (tokenSession != null) {
                try {
                    beginBatchTransaction();
                    tokenSession.defaultUpdateProperties();
                    tokenSession.setToken_last_active_datetime(tokenSession.getUpdated_date());
                    getSession().update(tokenSession);
                    commitBatchTransaction();
                    return Boolean.TRUE;
                } catch (Exception e) {
                    rollbackBatchTransaction();
                }
            }
        } catch (Exception e) {
        }
        return Boolean.FALSE;
    }
    
    public Boolean deleteSession(String token) {
        try {
            ModelBase.updateCurrentRequestObject("antiCSRF", "ignoreCSRF");
            ModelBase.updateCurrentRequestObject("ignoreCsrfCheck", "true");
            TokenSessionModel tokenSession = (TokenSessionModel) getSession().getNamedQuery("TokenSessionModel.findBy_token").setParameter("token", token).uniqueResult();
            if (tokenSession != null) {
                try {
                    beginBatchTransaction();
                    getSession().delete(tokenSession);
                    commitBatchTransaction();
                    return Boolean.TRUE;
                } catch (Exception e) {
                    rollbackBatchTransaction();
                }
            }
        } catch (Exception e) {
        }
        return Boolean.FALSE;
    }
}
