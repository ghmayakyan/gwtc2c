/**
 * Copyright 2014-2015 C2C Software, LLC. All rights reserved.
 * C2C SOFTWARE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.C2C.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpSession;
import org.C2C.client.callService;
import org.C2C.server.SessionManager.LOGIN_RESULTY;

/**
 * Class callServiceImpl
 * @author C2C Software, LLC
 */
public class callServiceImpl extends RemoteServiceServlet implements callService
{

    public static Integer MAX_BROWSER_COUNT = 100;
    public static Class CLASSES[] = {
        //Basic types
        String.class, Integer.class, Boolean.class, Double.class};
        //Libraries};
    //The map of remote and local calss. As a key the remote class id is stored.
    public static Map<String, Map<String, Class>> SERVER_CLIENT_CLASS_MAP = new HashMap<String, Map<String, Class>>();
    public static Map<String, Map<String, ServerTask>> TASK_LIST = new HashMap<String, Map<String, ServerTask>>();
    public static final String RETURN_VALUE_1 = "param1";
    public static final String RETURN_VALUE_2 = "param2";
    public static final String RETURN_VALUE_3 = "Wait";
    public static final String RETURN_VALUE_COMMAND_TYPE = "Type";
    public static final String RETURN_VALUE_OBJECT_TYPE = "ObjectType";
    public static final String OBJECT_TYPE_NULL = "##NULL";
    public static final String OBJECT_TYPE_TIDS = "##NEW_TIDS";
    public static final String COMMAND_GET_BROWSER_ID = "GetBrowserId";
    public static final String COMMAND_IS_LOGGEDIN = "GetBrowserId";
    public static final String COMMAND_REGISTER_CLASS = "RegisterClass";
    public static final String COMMAND_NEW_OBJECT = "NewObject";
    public static final String COMMAND_GET_RESULT = "GetResult";
    public static final String COMMAND_CALL_METHOD = "Call";
    public static final String COMMAND_CALL_WAIT = "Wait";
    public static final String COMMAND_LOGIN = "Login";
    public static final String COMMAND_LOGOUT = "Logout";
    public static final String TASK_ID_PREFIX = "!!@!!";
    public static final String LINE_SPLITTER = "#@";
    public static boolean TASK_STATE_WAIT = false;
    public static String TID = null;
    private static Long BROWSER_ID = 0L;
    private static Integer SERVER_TID = Integer.MAX_VALUE >> 1;
    private static int INACTIVITY_TIME_OUT_SECONDS = 2 * 60;
    private static final Map<String, HttpSession> m_liveSessions =
            new HashMap<String, HttpSession>();
    public static final String SESSION_ATTRIBUTE_IS_LOGGEDIN = "loggedin";
    public static final String SESSION_ATTRIBUTE_BROWSER_ID = "browser_id";
    public static final String SESSION_ATTRIBUTE_LAST_ACTIVITY_MILIS = "last_activity_milis";
    public static final String SESSION_ATTRIBUTE_CREATION_TIME = "creation_time";
    //public static final String SESSION_ATTRIBUTE_END_TIME = "end_time";
    public static final String SESSION_ATTRIBUTE_USER_EMAIL = "user";
    public static final String SESSION_ATTRIBUTE_LOGOUT_REASON = "Loggedout_reason";

    public List<Map<String, List<String>>> callService(List<String> params)
    {
        List<Map<String, List<String>>> ret = new ArrayList<Map<String, List<String>>>();
        String p0 = (String) params.get(0);
        if (p0.equals(COMMAND_GET_BROWSER_ID)) {
            Map<String, List<String>> r = getBrowserId();
            ret.add(r);
        } else if (p0.equals(COMMAND_IS_LOGGEDIN)) {
            // Map<String, List<String>> r = isloggedin();
            // ret.add(r);
        } else if (p0.equals(COMMAND_LOGIN)) {
            Map<String, List<String>> r = login(params.subList(1, params.size()));
            ret.add(r);
        } else if (p0.equals(COMMAND_LOGOUT)) {
            Map<String, List<String>> r = logout("Logged out by user.");
            ret.add(r);
            HttpSession s = m_liveSessions.get(getThreadLocalRequest().getSession().getId());
            logoutSession(s);
        } else {
            HttpSession s = m_liveSessions.get(getThreadLocalRequest().getSession().getId());
            List<Map<String, List<String>>> doUserQueries = doUserQueries(params);
            ret.addAll(doUserQueries);
            if (s == null || !isSessionValid(s) || !isSessionLoggedIn(s)) {
                Map<String, List<String>> t = logout(getLogoutReason());
                ret.add(t);
                logoutSession(s);
            }
        }
        clearInvalidatedSessions();
        return ret;
    }

    private static synchronized void clearInvalidatedSessions()
    {
        Object[] keySet = m_liveSessions.keySet().toArray();
        for (int i = 0;i<keySet.length;i++) {
            String sid = (String)keySet[i];
            HttpSession s = m_liveSessions.get(sid);
            if (!isSessionValid(s)) {
                SessionManager.putTheUserSessionEndInDB(sid);
                m_liveSessions.remove(sid);
            }
        }
    }

    private List<Map<String, List<String>>> doUserQueries(List<String> params)
    {
        List<Map<String, List<String>>> ret = new ArrayList<Map<String, List<String>>>();
        String bid = (String) params.get(0);
        List<List<String>> PARAMS = STRING_TO_LIST((String) params.get(1));
        for (List<String> p : PARAMS) {
            Map<String, List<String>> r = callServiceProcess(bid, p);
            ret.add(r);
        }
        return ret;
    }

    public Map<String, List<String>> callServiceProcess(String bid, List<String> params)
    {
        System.out.println(bid + ": " + params.get(1) + "-" + params.get(0));
        String command = params.get(0);
        if (command.equals(COMMAND_REGISTER_CLASS)) {
            return registerClass(bid, params.subList(1, params.size()));
        } else if (command.equals(COMMAND_NEW_OBJECT)) {
            return newObject(bid, params.subList(1, params.size()));
        } else if (command.equals(COMMAND_CALL_METHOD)) {
            return callMethod(bid, params.subList(1, params.size()));
        }
        Map<String, List<String>> m = new HashMap<String, List<String>>();
        return m;
    }

    public Map<String, List<String>> getBrowserId()
    {
        registerSession();
        String browserId = BROWSER_ID.toString();
        HashMap<String, List<String>> ret = new HashMap<String, List<String>>();
        HashMap<String, ServerTask> hashMap0 = new HashMap<String, ServerTask>();
        TASK_LIST.put(browserId, hashMap0);
        HashMap<String, Class> hashMap1 = new HashMap<String, Class>();
        SERVER_CLIENT_CLASS_MAP.put(browserId, hashMap1);
        List<String> lstr1 = new ArrayList<String>();
        lstr1.add(browserId.toString());
        ret.put(RETURN_VALUE_1, lstr1);
        List<String> lstr2 = new ArrayList<String>();
        lstr2.add(COMMAND_GET_BROWSER_ID);
        ret.put(RETURN_VALUE_COMMAND_TYPE, lstr2);
        BROWSER_ID++;
        return ret;
    }

    private boolean registerSession()
    {
        HttpSession session = getThreadLocalRequest().getSession();
        if (!isSessionValid(session)) {
//            Exceptions.printStackTrace(
//                    new Exception("Session is null or invalidated!"));
            return false;
        }
        m_liveSessions.put(session.getId(), session);
        session.setMaxInactiveInterval(INACTIVITY_TIME_OUT_SECONDS);
        session.setAttribute(SESSION_ATTRIBUTE_IS_LOGGEDIN, false);
        return true;
    }

    public Map<String, List<String>> logout(String reason)
    {
        HashMap<String, List<String>> ret = new HashMap<String, List<String>>();
        List<String> lstr1 = new ArrayList<String>();
        lstr1.add(reason);
        ret.put(RETURN_VALUE_1, lstr1);
        List<String> lstr2 = new ArrayList<String>();
        lstr2.add(COMMAND_LOGOUT);
        ret.put(RETURN_VALUE_COMMAND_TYPE, lstr2);
        return ret;
    }

    private String getLogoutReason()
    {
        String id = getThreadLocalRequest().getSession().getId();
        HttpSession s = m_liveSessions.get(id);
        String reasonToLogout = "";
        if (s != null) {
            reasonToLogout = (String) s.getAttribute(SESSION_ATTRIBUTE_LOGOUT_REASON);
        } else {
            reasonToLogout = "Session not exists";
        }
        if (reasonToLogout == null) {
            reasonToLogout = "Unknown logout reason!";
        }
        return reasonToLogout;
    }

    public static void init_manage_session_timeout_thread()
    {
        Thread t = new Thread(new Runnable()
        {

            public void run()
            {
                while (true) {
                    for (String sid : m_liveSessions.keySet()) {
                        HttpSession s = m_liveSessions.get(sid);
                        if (!isSessionValid(s)) {
                            m_liveSessions.remove(sid);
                        }
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException ex) {
//                            Exceptions.printStackTrace(ex);
                        }
                    }
                }
            }
        });
        t.start();
    }

    private static void endSessionBecauseofLoggedinFromOtherPlace(HttpSession s)
    {
        s.setAttribute(SESSION_ATTRIBUTE_LOGOUT_REASON, "User logged in from other place!");
        logoutSession(s);
    }

    private static void logoutSession(HttpSession s)
    {
        if (s != null) {
            SessionManager.putTheUserSessionEndInDB(s.getId());
            if (isSessionValid(s)) {
                s.invalidate();
            }
        }
    }

    public HttpSession findValidSession(String user)
    {
        for (HttpSession s : m_liveSessions.values()) {
            if (isSessionValid(s) && isSessionLoggedIn(s)) {
                String _user = (String) s.getAttribute(SESSION_ATTRIBUTE_USER_EMAIL);
                if (user.equals(_user)) {
                    return s;
                }
            }
        }
        return null;
    }

    private static boolean isSessionValid(HttpSession s)
    {
        try {
            s.getCreationTime();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean isSessionLoggedIn(HttpSession s)
    {
        try {
            Boolean loggedIn = (Boolean) s.getAttribute(SESSION_ATTRIBUTE_IS_LOGGEDIN);
            if (loggedIn != null && loggedIn == true) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

    }

    public static String getUserNameByBrowserID(Long browserID)
    {
        Collection<HttpSession> values = m_liveSessions.values();
        for (HttpSession s : values) {
            if (isSessionValid(s) && isSessionLoggedIn(s)) {
                Long bid = (Long) s.getAttribute(SESSION_ATTRIBUTE_BROWSER_ID);
                if (bid == browserID) {
                    return (String) s.getAttribute(SESSION_ATTRIBUTE_USER_EMAIL);
                }
            }
        }
        return null;
    }

    public Map<String, List<String>> login(List<String> params)
    {
        String id = getThreadLocalRequest().getSession().getId();
        System.out.println("LOGIN++++++++++" + id);
        HttpSession s = m_liveSessions.get(id);
        if (s == null || !isSessionValid(s)) {
            registerSession();
        }
        s = m_liveSessions.get(id);
        LOGIN_RESULTY logIn = null;
        HttpSession sessionToBeClosed = null;
        if (s != null && isSessionValid(s)) {
            System.out.println("Session is Valid");
            boolean sessionLoggedIn = isSessionLoggedIn(s);
            if (sessionLoggedIn) {
                System.out.println("You are already loggedin");
            }
            logIn = SessionManager.logIn(params.get(0), params.get(1));
            sessionToBeClosed = findValidSession(params.get(0));
            if (logIn == LOGIN_RESULTY.SUCCEED) {
                System.out.println("You are successfully logged in!");
            }
            s.setAttribute(SESSION_ATTRIBUTE_IS_LOGGEDIN, logIn == LOGIN_RESULTY.SUCCEED);
            if (logIn == LOGIN_RESULTY.SUCCEED && sessionLoggedIn) {
                logIn = LOGIN_RESULTY.USER_IS_ALREADY_LOGGEDIN_WITH_SAME_SESSION;
            }
            s.setAttribute(SESSION_ATTRIBUTE_BROWSER_ID, BROWSER_ID - 1);
            s.setAttribute(SESSION_ATTRIBUTE_LAST_ACTIVITY_MILIS, System.currentTimeMillis());
            s.setAttribute(SESSION_ATTRIBUTE_CREATION_TIME, System.currentTimeMillis());
            s.setAttribute(SESSION_ATTRIBUTE_USER_EMAIL, params.get(0));
            System.out.println("Attributes set!!!");

        } else {
            if (s != null) {
                s.invalidate();
            }
            logIn = LOGIN_RESULTY.SYSTEM_ERROR_SEE_SERVER_LOG;
            //Exceptions.printStackTrace(new AuthException("Method login: Session is null or invalidated!: username:" + params.get(0)));
        }
        HashMap<String, List<String>> ret = new HashMap<String, List<String>>();
        List<String> lstr1 = new ArrayList<String>();
        lstr1.add(logIn.name());
        ret.put(RETURN_VALUE_1, lstr1);
        if (logIn == LOGIN_RESULTY.SUCCEED || logIn == LOGIN_RESULTY.USER_IS_ALREADY_LOGGEDIN_WITH_SAME_SESSION) {
            String remoteAddr = getThreadLocalRequest().getRemoteAddr();
            int remotePort = getThreadLocalRequest().getRemotePort();
            SessionManager.putTheUserSessionStartInDB(params.get(0), s, remoteAddr + ":" + remotePort);
            System.out.println("Put in DB!!!");
            List<String> userRealVisibleNameInClientSide = new ArrayList<String>();
            String userPresentativeName = SessionManager.getUserPresentativeName(params.get(0));
            userRealVisibleNameInClientSide.add(userPresentativeName);
            ret.put(RETURN_VALUE_2, userRealVisibleNameInClientSide);
            if (sessionToBeClosed != null) {
                endSessionBecauseofLoggedinFromOtherPlace(sessionToBeClosed);
            }
//            boolean checkUserSpecificFolders = UserData.checkUserSpecificFolders(params.get(0));
//            if (!checkUserSpecificFolders) {
////                Exceptions.printStackTrace(new Exception("User data directories are not completly created!"));
//            } else {
//                System.out.println("Successfully created user diractories!");
//            }

        }
        List<String> lstr2 = new ArrayList<String>();
        if (s != null) {
            lstr2.add(s.getId());
        } else {
            lstr2.add("Session is null!");
        }
        ret.put(RETURN_VALUE_3, lstr2);

        List<String> lstr3 = new ArrayList<String>();
        lstr3.add(COMMAND_LOGIN);
        ret.put(RETURN_VALUE_COMMAND_TYPE, lstr3);

        return ret;

    }

    public Map<String, List<String>> registerClass(String bid, List<String> params)
    {
        //The second element of the list should be the server side class name
        //The third element of the list should be the client side class name
        String tid = params.get(0);
        TID = (String) tid;
        Object serverClassName = params.get(1);
        Object clientClassName = params.get(2);

        for (Class c : CLASSES) {
            if (c.getName().equals(serverClassName)) {
                SERVER_CLIENT_CLASS_MAP.get(bid).put((String) clientClassName, c);
            }
        }
        HashMap<String, List<String>> ret = new HashMap<String, List<String>>();
        List<String> lstr1 = new ArrayList<String>();
        lstr1.add(tid);
        ret.put(RETURN_VALUE_1, lstr1);
        List<String> lstr2 = new ArrayList<String>();
        lstr2.add(COMMAND_REGISTER_CLASS);
        ret.put(RETURN_VALUE_COMMAND_TYPE, lstr2);
        return ret;
    }

    public Map<String, List<String>> newObject(String bid, List<String> params)
    {
        String objType = "";
        Map<String, List<String>> ret = new HashMap<String, List<String>>();
        //The first element of the list the client side task id
        //The second element of the list should be the client side object name
        String tid = params.get(0);
        TID = (String) tid;
        Object className = params.get(1);
        try {

            List<String> argList = params.subList(2, params.size());
            Object[] args = new Object[argList.size()];
            Class[] classes = new Class[argList.size()];
            for (int i = 0; i < argList.size(); i++) {
                String sarg = (String) argList.get(i);
                if (sarg.startsWith(TASK_ID_PREFIX)) {
                    sarg = sarg.substring(TASK_ID_PREFIX.length());
                    args[i] = GET_OBJECT_FROM_TASK_ID(bid, sarg);
                    if (null == args[i]) {
                        classes[i] = Object.class;
                    } else {
                        classes[i] = args[i].getClass();
                    }
                } else {
                    if (sarg.equals(OBJECT_TYPE_NULL)) {
                        args[i] = null;
                        classes[i] = Object.class;
                    } else {
                        args[i] = sarg;
                        classes[i] = String.class;
                    }

                }
            }
            Class c = TRANSLATE_CLASS(bid, (String) className);
            if (null == c) {
                throw (new IllegalArgumentException((String) className));
            }
            Constructor<? extends Object> constructor = getConstructor(c, classes);
            if (null == constructor) {
                throw (new NoSuchMethodException((String) className));
            }
            Object newObject = newInstance(constructor, args);
            if (null != newObject) {
                objType = TRANSLATE_CLASS_TO_CLIENT(bid, newObject.getClass().getName());
            }
            ServerTask newElem = ADD_ELEMENT_TO_TASK_LIST(bid, (String) tid);
            newElem.setResult(newObject);
            if (TASK_STATE_WAIT) {
                List<String> lstr0 = new ArrayList<String>();
                lstr0.add(tid);
                ret.put(RETURN_VALUE_3, lstr0);
                TASK_STATE_WAIT = false;
            }
        } catch (IllegalArgumentException ex) {
//            Exceptions.printStackTrace(ex);
        } catch (NoSuchMethodException ex) {
//            Exceptions.printStackTrace(ex);
        } catch (SecurityException ex) {
//            Exceptions.printStackTrace(ex);
        }

        List<String> lstr1 = new ArrayList<String>();
        lstr1.add(tid);
        ret.put(RETURN_VALUE_1, lstr1);
        List<String> lstr2 = new ArrayList<String>();
        lstr2.add(COMMAND_NEW_OBJECT);
        ret.put(RETURN_VALUE_COMMAND_TYPE, lstr2);
        List<String> lstr3 = new ArrayList<String>();
        lstr3.add(objType);
        ret.put(RETURN_VALUE_OBJECT_TYPE, lstr3);
        return ret;
    }

    public Map<String, List<String>> callMethod(String bid, List<String> params)
    {
        Map<String, List<String>> ret = new HashMap<String, List<String>>();
        String objType = "";
        //The first element of the list the client side task id
        //The second element of the list should be the method to call
        String tid = params.get(0);
        TID = (String) tid;
        Object objectTid = params.get(1);
        Object result = null;
        String method = (String) params.get(2);
        System.out.println("method is " + method);
        try {

            //The first element of the list the client side task id
            //The second element of the list should be the client side object name
            //The second element of the list should be the method to call
            List<String> argList = params.subList(3, params.size());
            Object[] args = new Object[argList.size()];
            Class[] classes = new Class[argList.size()];
            for (int i = 0; i < argList.size(); i++) {
                args[i] = GET_OBJECT_FROM_TASK_ID(bid, (String) argList.get(i));
                classes[i] = TRANSLATE_CLASS(bid, args[i].getClass());
            }
            Object serverObject = GET_OBJECT_FROM_TASK_ID(bid, (String) objectTid);
            //Method method1 = serverObject.getClass().getMethod(method, classes);
            Method method1 = null;
            if (null != serverObject) {
                method1 = getMethod(serverObject.getClass(), method, classes);
            } else {
                method1 = getMethod(Object.class, method, classes);
            }
            //Object result = method1.invoke(serverObject, args);
            if (serverObject instanceof ServiceInfo) {
                ((ServiceInfo) serverObject).setBrowserID(bid);
            }
            result = executeMethod(method1, serverObject, args);
            if (null != result) {
                objType = TRANSLATE_CLASS_TO_CLIENT(bid, result.getClass().getName());
            } else {
                objType = OBJECT_TYPE_NULL;
            }
            ServerTask tsk = GET_TASK_FROM_TASK_ID(bid, (String) tid);
            if (null == tsk) {
                tsk = ADD_ELEMENT_TO_TASK_LIST(bid, (String) tid);
            }
            tsk.setResult(result);
            if (TASK_STATE_WAIT) {
                List<String> lstr0 = new ArrayList<String>();
                lstr0.add(tid);
                ret.put(RETURN_VALUE_3, lstr0);
                TASK_STATE_WAIT = false;
            }
        } catch (IllegalArgumentException ex) {
//            Exceptions.printStackTrace(ex);
        } catch (SecurityException ex) {
//            Exceptions.printStackTrace(ex);
        }
        List<String> lstr1 = new ArrayList<String>();
        lstr1.add(tid);
        ret.put(RETURN_VALUE_1, lstr1);
        List<String> lstr2 = new ArrayList<String>();
        lstr2.add(COMMAND_CALL_METHOD);
        ret.put(RETURN_VALUE_COMMAND_TYPE, lstr2);
        List<String> lstr4 = new ArrayList<String>();
        if (null != result) {
            if (result instanceof List<?>) {
                List r = ((List) result);
                int sz = r.size();
                if (sz > 0 && !(r.get(0) instanceof String)) {
                    lstr4 = APPEND_NEW_TASK_GROUP_TO_TASK_LIST(bid, r);
                    objType = OBJECT_TYPE_TIDS;
                } else {
                    lstr4 = (List<String>) result;
                }
            } else {
                lstr4.add((String) result.toString());
            }
        } else {
            lstr4.add("");
        }
        ret.put(RETURN_VALUE_2, lstr4);

        List<String> lstr3 = new ArrayList<String>();
        lstr3.add(objType);
        ret.put(RETURN_VALUE_OBJECT_TYPE, lstr3);
        return ret;
    }

    private static Object GET_OBJECT_FROM_TASK_ID(String bid, String tid)
    {
        ServerTask get = null;
        if (!tid.equals(OBJECT_TYPE_NULL)) {
            get = TASK_LIST.get(bid).get(tid);
        }
        if (null != get) {
            return get.getResult();
        }
        return null;
    }

    private static ServerTask GET_TASK_FROM_TASK_ID(String bid, String tid)
    {
        return TASK_LIST.get(bid).get(tid);
    }

    private List<String> APPEND_NEW_TASK_GROUP_TO_TASK_LIST(String bid, List lst)
    {
        Integer size = lst.size();
        List<String> ret = new ArrayList<String>();
        for (int i = 0; i < size; ++i) {
            SERVER_TID++;
            ServerTask serverTask = new ServerTask();
            serverTask.setResult(lst.get(i));
            TASK_LIST.get(bid).put(SERVER_TID.toString(), serverTask);
            ret.add(SERVER_TID.toString());
            String cName = TRANSLATE_CLASS_TO_CLIENT(bid, lst.get(i).getClass().getName());
            ret.add(cName);
        }
        return ret;
    }

    private static ServerTask ADD_ELEMENT_TO_TASK_LIST(String bid, String tid)
    {
        ServerTask newTask = new ServerTask();
        TASK_LIST.get(bid).put(tid, newTask);
        return newTask;
    }

    private static Class TRANSLATE_CLASS(String bid, Class clientClass)
    {
        Class get = SERVER_CLIENT_CLASS_MAP.get(bid).get(clientClass.getName());
        if (null == get) {
            get = clientClass;
        }
        return get;
    }

    private static Class TRANSLATE_CLASS(String bid, String clientClassName)
    {
        return SERVER_CLIENT_CLASS_MAP.get(bid).get(clientClassName);
    }

    private static String TRANSLATE_CLASS_TO_CLIENT(String bid, String serverClassName)
    {
        Map<String, Class> m = SERVER_CLIENT_CLASS_MAP.get(bid);
        Set<String> keySet = m.keySet();
        for (String s : keySet) {
            if (m.get(s).getName().equals(serverClassName)) {
                return s;
            }
        }
        return "";
    }

    public static void SET_WAIT_STATE()
    {
        TASK_STATE_WAIT = true;
    }

    private static List<List<String>> STRING_TO_LIST(String str)
    {
        List<List<String>> ret = new ArrayList<List<String>>();

        String[] split = str.split(LINE_SPLITTER);
        int arrPos = 0;
        int i = 0;
        int sz = 0;
        do {
            List<String> lst = new ArrayList<String>();
            sz = Integer.parseInt(split[arrPos]);
            arrPos++;
            for (i = 0; i < sz; i++) {
                lst.add(split[arrPos + i]);
            }
            ret.add(lst);
            arrPos += sz;
        } while (arrPos < split.length);
        return ret;
    }

    private Method getMethod(Class c, String method, Class[] classes)
    {
        Method[] declaredMethods = c.getMethods();
        int sz = classes.length;
        for (Method m : declaredMethods) {
            if (!m.getName().equalsIgnoreCase(method)) {
                continue;
            }
            boolean vararg = false;
            Class<?>[] parameterTypes = m.getParameterTypes();
            int i = 0;
            for (Class cl : parameterTypes) {
                Class clc = cl;
                Class clx = cl.getComponentType();
                if (null != clx) {
                    clc = clx;
                    vararg = true;
                }

                if (i < sz && !clc.isAssignableFrom(classes[i])) {
                    break;
                }
                i++;
            }
            if (i == sz) {
                return m;
            }

            boolean isTrue = true;
            if (vararg) {
                for (int j = i - 1; j < sz; j++) {
                    if (classes[j] != classes[i - 1]) {
                        isTrue = false;
                        break;
                    }
                }

                if (isTrue) {
                    return m;
                }
            }
        }
        return null;
    }

    private Constructor getConstructor(Class c, Class[] classes)
    {
        Constructor[] declaredConstructors = c.getConstructors();
        int sz = classes.length;
        for (Constructor cs : declaredConstructors) {
            boolean vararg = false;
            Class<?>[] parameterTypes = cs.getParameterTypes();
            int i = 0;
            for (Class cl : parameterTypes) {
                Class clc = cl;
                Class clx = cl.getComponentType();
                if (null != clx) {
                    clc = clx;
                    vararg = true;
                }
                if (i < sz && !(clc.isAssignableFrom(classes[i]) || classes[i].isAssignableFrom(clc))) {
                    break;
                }
                i++;
            }

            if (i == sz) {
                return cs;
            }

            boolean isTrue = true;
            if (vararg) {
                for (int j = i - 1; j < sz; j++) {
                    if (classes[j] != classes[i - 1]) {
                        isTrue = false;
                        break;
                    }
                }

                if (isTrue) {
                    return cs;
                }
            }
        }
        return null;
    }

    private Object newInstance(Constructor c, Object[] args)
    {
        int sz = args.length;
        try {
            Class<?>[] parameterTypes = c.getParameterTypes();
            int psz = parameterTypes.length;
            int i = 0;
            Object[] execargs = new Object[psz];
            for (Class cl : parameterTypes) {
                if (i == psz) {
                    Object[] execargs1 = new Object[psz];
                    for (int j = 0; j < psz; j++) {
                        execargs1[j] = execargs[j];
                    }
                    execargs = new Object[psz + 1];
                    for (int j = 0; j < psz; j++) {
                        execargs[j] = execargs1[j];
                    }
                }
                if (i < psz && i >= sz) {
                    Class componentType = cl.getComponentType();
                    if (null != componentType) {
                        Object argarr = java.lang.reflect.Array.newInstance(componentType, 0);
                        execargs[i] = argarr;
                    }
                } else if (i < psz && cl.isInstance(args[i])) {
                    execargs[i] = args[i];
                } else {
                    Class componentType = cl.getComponentType();
                    if (null != componentType) {
                        int x = sz - i;
                        Object argarr = java.lang.reflect.Array.newInstance(componentType, x);
                        System.arraycopy(args, i, argarr, 0, x);
                        execargs[i] = argarr;
                    }
                }
                i++;
            }
//            if (execargs.length > 0 && null == execargs[0]) {
//                return null;
//            }
            return c.newInstance(execargs);
        } catch (InstantiationException ex) {
//            Exceptions.printStackTrace(ex);
        } catch (IllegalAccessException ex) {
//            Exceptions.printStackTrace(ex);
        } catch (IllegalArgumentException ex) {
//            Exceptions.printStackTrace(ex);
        } catch (InvocationTargetException ex) {
//            Exceptions.printStackTrace(ex);
        }
        return null;
    }

    private Object executeMethod(Method c, Object o, Object[] args)
    {
        int sz = args.length;
        try {
            if (null == c) {
                return null;
            }
            Class<?>[] parameterTypes = c.getParameterTypes();
            int psz = parameterTypes.length;
            int i = 0;
            Object[] execargs = new Object[psz];
            for (Class cl : parameterTypes) {
                if (i == psz) {
                    Object[] execargs1 = new Object[psz];
                    for (int j = 0; j < psz; j++) {
                        execargs1[j] = execargs[j];
                    }
                    execargs = new Object[psz + 1];
                    for (int j = 0; j < psz; j++) {
                        execargs[j] = execargs1[j];
                    }
                }
                if (i < psz && i >= sz) {
                    Class componentType = cl.getComponentType();
                    if (null != componentType) {
                        Object argarr = java.lang.reflect.Array.newInstance(componentType, 0);
                        execargs[i] = argarr;
                    }
                } else if (i < psz && cl.isInstance(args[i])) {
                    execargs[i] = args[i];
                } else {
                    Class componentType = cl.getComponentType();
                    if (null != componentType) {
                        int x = sz - i;
                        Object argarr = java.lang.reflect.Array.newInstance(componentType, x);
                        System.arraycopy(args, i, argarr, 0, x);
                        execargs[i] = argarr;
                    }
                }
                i++;
            }
            return c.invoke(o, execargs);
        } catch (IllegalAccessException ex) {
//            Exceptions.printStackTrace(ex);
        } catch (IllegalArgumentException ex) {
//            Exceptions.printStackTrace(ex);
        } catch (InvocationTargetException ex) {
//            Exceptions.printStackTrace(ex);
        }
        return null;
    }
}
