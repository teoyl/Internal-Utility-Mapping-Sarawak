/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sains.framework.base.web;

import com.sains.framework.model.User;
import java.util.Map;

/**
 *
 * @author User
 */
public interface IBubbleShell {
    public Boolean isLogined();
    public Boolean loginBs(User user);
    public void logoutBs();
    public Map populateBsLoginSession(Map userInfo);
}
