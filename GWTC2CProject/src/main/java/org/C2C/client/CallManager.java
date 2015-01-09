/**
 * Copyright 2014-2015 C2C Software, LLC. All rights reserved. C2C SOFTWARE
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.C2C.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Class CallManager
 *
 * @author C2C Software, LLC
 */
public class CallManager {

    public static Map<String, Object> SERVER_CLIENT_OBJECT_MAP
            = new HashMap<String, Object>();
    public static final String RETURN_VALUE_1 = "param1";
    public static final String RETURN_VALUE_2 = "param2";
    public static final String RETURN_VALUE_3 = "Wait";
    public static final String RETURN_VALUE_COMMAND_TYPE = "Type";
    public static final String RETURN_VALUE_OBJECT_TYPE = "ObjectType";
    public static final String OBJECT_TYPE_NULL = "##NULL";
    public static final String OBJECT_TYPE_TIDS = "##NEW_TIDS";
    public static final String COMMAND_GET_BROWSER_ID = "GetBrowserId";
    public static final String COMMAND_REGISTER_CLASS = "RegisterClass";
    public static final String COMMAND_NEW_OBJECT = "NewObject";
    public static final String COMMAND_CALL_METHOD = "Call";
    public static final String COMMAND_CALL_WAIT = "Wait";
    public static final String COMMAND_LOGIN = "Login";
    public static final String COMMAND_LOGOUT = "Logout";
    public static final String TASK_ID_PREFIX = "!!@!!";
    public static final String LINE_SPLITTER = "#@";
    static AsyncCallback<List<Map<String, List<String>>>> MAIN_CALLBACK = null;
    static AsyncCallback<Map<String, String>> WAIT_CALLBACK = null;
    private static String BROWSER_ID = null;
    private static final List<String> DEBUG_TASK_LIST = new ArrayList<String>();
    public static Integer TICKS = 0;
    public static List<Integer> PROFILER = new ArrayList<Integer>();
    private static final List<LoginLogoutListener> m_loginLogoutListeners = new ArrayList<LoginLogoutListener>();

    /**
     * The default constructor for class CallManager
     */
    private CallManager() {
        MAIN_CALLBACK = new AsyncCallback<List<Map<String, List<String>>>>() {

            public void onFailure(Throwable caught) {
                //SC.say("MAIN_CALLBACK onFailure: " + caught.getMessage());
            }

            public void onSuccess(List<Map<String, List<String>>> result) {
                for (Map<String, List<String>> res : result) {
                    String get = res.get(RETURN_VALUE_COMMAND_TYPE).get(0);
                    if (get.equals(COMMAND_REGISTER_CLASS)) {
                        REGISTER_CLASS_CALLBACK(res);
                    } else if (get.equals(COMMAND_CALL_METHOD)) {
                        CALL_METHOD_CALLBACK(res);
                    } else if (get.equals(COMMAND_NEW_OBJECT)) {
                        NEW_OBJECT_CALLBACK(res);
                    } else if (get.equals(COMMAND_LOGIN)) {
                        LOGIN_CALLBACK(res);
                    } else if (get.equals(COMMAND_LOGOUT)) {
                        LOGOUT_CALLBACK(res);
                    } else if (get.equals(COMMAND_GET_BROWSER_ID)) {
                        GET_BROWSER_ID_CALLBACK(res);
                    }
                }
                TaskManager.TRANSFERING = false;
            }
        };

        WAIT_CALLBACK = new AsyncCallback<Map<String, String>>() {

            public void onFailure(Throwable caught) {
                //SC.say("WAIT_CALLBACK onFailure: " + caught.getMessage());
                Task.TASK_WAIT.fireCompleted(false);
            }

            public void onSuccess(Map<String, String> result) {
                TICKS++;
                Set<String> keySet = result.keySet();
                for (String tid : keySet) {
                    Task task = TaskManager.GET_TASK_BY_ID(tid);
                    ArrayList<String> lst = new ArrayList<String>();
                    lst.add(tid);
                    task.setResultValue(lst);
                    task.fireCompleted(false);
                }
            }
        };
    }

    public static void INIT() {
        new CallManager();
        INIT_BROWSER_ID();
    }

    private static void INIT_BROWSER_ID() {
        TaskManager.TRANSFERING = true;
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add(COMMAND_GET_BROWSER_ID);
        ((callServiceAsync) GWT.create(callService.class)).callService(arrayList, MAIN_CALLBACK);
    }

    public static String GET_BROWSER_ID() {
        return BROWSER_ID;
    }

    public static void LOGIN(String user, String password) {
        TaskManager.TRANSFERING = true;
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add(COMMAND_LOGIN);
        arrayList.add(user);
        arrayList.add(password);
        ((callServiceAsync) GWT.create(callService.class)).callService(arrayList, MAIN_CALLBACK);
    }

    public static void LOGOUT() {
        TaskManager.TRANSFERING = true;
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add(COMMAND_LOGOUT);
        ((callServiceAsync) GWT.create(callService.class)).callService(arrayList, MAIN_CALLBACK);
    }

    public static String REGISTER_CLASS(Task parentTask, String serverSideClassName, Class localClass) {
        ArrayList<Object> arrayList = new ArrayList<Object>();
        arrayList.add(COMMAND_REGISTER_CLASS);
        arrayList.add(parentTask.getId());
        arrayList.add(serverSideClassName);
        arrayList.add(localClass.getName());
        return LIST_TO_STRING(arrayList);
    }

    public static String CALL_METHOD(Task task, TaskObject parentTask, String method, List<TaskObject> args) {
        ArrayList<Object> arrayList = new ArrayList<Object>();
        arrayList.add(COMMAND_CALL_METHOD);
        arrayList.add(task.getId());
        arrayList.add(parentTask.getId());
        arrayList.add(method);
        for (Task tsk : args) {
            if (null == tsk) {
                arrayList.add(TaskNull.GET_NULL().getId());
            } else {
                arrayList.add(tsk.getId());
            }
            //DEBUG_TASK_LIST.add(method);
        }
        return LIST_TO_STRING(arrayList);
    }

    public static String NEW_OBJECT_METHOD(TaskObject parentTask, List<Object> args) {
        ArrayList<Object> arrayList = new ArrayList<Object>();
        arrayList.add(COMMAND_NEW_OBJECT);
        arrayList.add(parentTask.getId());
        arrayList.add(parentTask.getClass().getName());

        for (Object tsk : args) {
            if (null == tsk) {
                arrayList.add(CallManager.OBJECT_TYPE_NULL);
            } else if (tsk instanceof TaskObject) {
                arrayList.add(TASK_ID_PREFIX + ((TaskObject) tsk).getId());
            } else {
                arrayList.add(tsk.toString());
            }
            //DEBUG_TASK_LIST.add(parentTask.getClass().getName());
        }
        return LIST_TO_STRING(arrayList);
    }

    public static void NEW_OBJECT_CALLBACK(Map<String, List<String>> result) {
        String id = (String) result.get(RETURN_VALUE_1).get(0);
        Task task = TaskManager.GET_TASK_BY_ID(id);
        List<String> wid = result.get(RETURN_VALUE_3);
        if (null == wid) {
            task.fireCompletedAndUpdate(false);
        } else {
            task.fireWait();
        }
    }

    public static void GET_BROWSER_ID_CALLBACK(Map<String, List<String>> result) {
        BROWSER_ID = result.get(RETURN_VALUE_1).get(0);
        //login("", true);
    }

    public static void REGISTER_CLASS_CALLBACK(Map<String, List<String>> result) {
        String id = result.get(RETURN_VALUE_1).get(0);
        Task task = TaskManager.GET_TASK_BY_ID(id);
        task.fireCompleted(false);
    }

    public static void CALL_METHOD_CALLBACK(Map<String, List<String>> result) {
        String id = (String) result.get(RETURN_VALUE_1).get(0);
        Task task = TaskManager.GET_TASK_BY_ID(id);
        List<String> val = result.get(RETURN_VALUE_2);
        List<String> wid = result.get(RETURN_VALUE_3);
        String objType = result.get(RETURN_VALUE_OBJECT_TYPE).get(0);
        task.setObjectType(objType);
        task.setResultValue(val);
        if (null == wid) {
            if (objType.equals(OBJECT_TYPE_TIDS)) {
                CREATE_TIDS(val);
            }
            task.fireCompletedAndUpdate(true);
        } else {
            task.fireWait();
        }
    }

    public static void LOGOUT_CALLBACK(Map<String, List<String>> result) {
        String message = (String) result.get(RETURN_VALUE_1).get(0);
        logout(message);
    }

    private static void logout(String message) {
        IS_LOGGEDIN = false;
        hideTheUserPresetativeName();
        fireUserLoggedInOrLoggedOut(false);
        //login(message, false);
    }

    public static void LOGIN_CALLBACK(Map<String, List<String>> result) {
        String loginStatus = (String) result.get(RETURN_VALUE_1).get(0);
        //String sid = (String) result.get(RETURN_VALUE_3).get(0);
        LOGIN_RESULTX loginStat = LOGIN_RESULTX.valueOf(loginStatus);
        String userPresentativeName = "";

        switch (loginStat) {
            case SUCCEED:
                IS_LOGGEDIN = true;
                if (m_tempLogin != null) {
                    Cookies.setCookie(COOKIE_UID, m_tempLogin);
                }
                m_tempLogin = null;
                userPresentativeName = (String) result.get(RETURN_VALUE_2).get(0);
                showTheUserPresetativeNameOnThePage(userPresentativeName);
                fireUserLoggedInOrLoggedOut(true);
                break;
            case DATADASE_ERROR:
                IS_LOGGEDIN = false;
                hideTheUserPresetativeName();
                //login("Database Error!", true);
                break;
            case WRONG_USER_OR_PASSWORD:
                IS_LOGGEDIN = false;
                //clearCookies();
                hideTheUserPresetativeName();
                //login("Wrong user/password!", true);
                break;
            case SYSTEM_ERROR_SEE_SERVER_LOG:
                IS_LOGGEDIN = false;
                clearCookies();
                hideTheUserPresetativeName();
                //login("System error: contact to Administrator!", false);
                break;
            case USER_IS_ALREADY_LOGGEDIN_WITH_SAME_SESSION:
                Window.alert("USER_IS_ALREADY_LOGGEDIN_WITH_SAME_SESSION");
                IS_LOGGEDIN = true;
                userPresentativeName = (String) result.get(RETURN_VALUE_2).get(0);
                showTheUserPresetativeNameOnThePage(userPresentativeName);
                fireUserLoggedInOrLoggedOut(true);
                break;
        }
    }

    private static void showTheUserPresetativeNameOnThePage(String pName) {
//        WindowManager.getInstance().setUserNameLabelVisible(true);
//        WindowManager.getInstance().setUserNameLabelTitle(pName);
    }

    private static void hideTheUserPresetativeName() {
//        WindowManager.getInstance().setUserNameLabelVisible(false);
//        WindowManager.getInstance().setUserNameLabelTitle("");
    }

    public static void clearCookies() {
        Cookies.removeCookie(COOKIE_UID);
    }

    public static void SEND_TO_SERVER(String s) {
        TaskManager.TRANSFERING = true;
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add(BROWSER_ID);
        arrayList.add(s);
        ((callServiceAsync) GWT.create(callService.class)).callService(arrayList, MAIN_CALLBACK);
    }

    public static void IDLE(Task parentTask) {
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add(COMMAND_CALL_WAIT);
        arrayList.add(parentTask.getId());
        ((SynchronizationObjectsAsync) GWT.create(SynchronizationObjects.class)).callService(arrayList, WAIT_CALLBACK);
    }

    private static String LIST_TO_STRING(List<Object> lst) {
        String ret = "";
        Integer sz = lst.size();
        ret += sz;
        for (Object s : lst) {
            ret += LINE_SPLITTER + (String) s;
        }
        return ret + LINE_SPLITTER;
    }

    public String DEBUG_PRINT_LOG() {
        String ret = "";

        for (String s : DEBUG_TASK_LIST) {
            ret += s + "; ";
        }
        return ret;
    }

    public void DEBUG_CLEANUP_LOG() {
        DEBUG_TASK_LIST.clear();
    }

    public static Integer getTicks() {
        return TICKS;
    }

    private static void CREATE_TIDS(List<String> val) {
        for (int i = 0; i < val.size(); i += 2) {
            TaskManager.ADD_TASK_OBJECT(val.get(i), val.get(i + 1));
        }
    }

    public static void addLoginLogoutListener(LoginLogoutListener l) {
        if (!m_loginLogoutListeners.contains(l)) {
            m_loginLogoutListeners.add(l);
        }
    }

    public static void removeLoginLogoutListener(LoginLogoutListener l) {
        if (m_loginLogoutListeners.contains(l)) {
            m_loginLogoutListeners.remove(l);
        }
    }

    private static void fireUserLoggedInOrLoggedOut(boolean loggedInOrLoggedOut) {
        for (LoginLogoutListener l : m_loginLogoutListeners) {
            if (loggedInOrLoggedOut) {
                l.onUserLoggedIn();
            } else {
                l.onUserLoggedOut();
            }
        }
    }

    private static String m_tempLogin = null;

    public enum LOGIN_RESULTX {

        SUCCEED, WRONG_USER_OR_PASSWORD, DATADASE_ERROR,
        USER_IS_ALREADY_LOGGEDIN_WITH_SAME_SESSION, SYSTEM_ERROR_SEE_SERVER_LOG
    }
    private static final String COOKIE_UID = "C2C_uid";
    public static boolean IS_LOGGEDIN = false;
}
