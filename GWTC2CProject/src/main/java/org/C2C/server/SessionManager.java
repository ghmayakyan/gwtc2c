/**
 * Copyright 2014-2015 C2C Software, LLC. All rights reserved. C2C SOFTWARE
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.C2C.server;

import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.http.HttpSession;

/**
 * Class SC2CUserAuthentication
 *
 * @author C2C Software, LLC
 */
public class SessionManager {

    public static String getUserPresentativeName(String email) {
//        try {
//            Connection con = getConnection();
//            Statement st = con.createStatement();
//            String sql = "select * from C2C_1_0.users where email='" + email + "'";
//            ResultSet rs = st.executeQuery(sql);
//            boolean b = rs.first();
//            if (b) {
//                String ret = rs.getString("first_name") + " " + rs.getString("second_name");
//                rs.close();
//                st.close();
//                con.close();
//                return ret;
//            }
//            rs.close();
//            st.close();
//            con.close();
//            return "Unknown user name";
//        } catch (SQLException ex) {
//            Exceptions.printStackTrace(ex);
//        }
        return "a";
    }

    public static LOGIN_RESULTY logIn(String email, String password) {
//        try {
//            String mpass = MD5.Encrypt(password);
//            Connection con = getConnection();
//            Statement st = con.createStatement();
//            String sql = "select * from C2C_1_0.users where email='" + email + "'";
//            ResultSet rs = st.executeQuery(sql);
//            boolean b = rs.first();
//            if (!b) {
//                rs.close();
//                st.close();
//                con.close();
//                return LOGIN_RESULTY.WRONG_USER_OR_PASSWORD;
//            }
//            rs.first();
//            String e = rs.getString("email");
//            String p = rs.getString("password");
//            rs.close();
//            st.close();
//            con.close();
//            if (email.equals(e) && mpass.equals(p)) {
//                return LOGIN_RESULTY.SUCCEED;
//            } else {
//                return LOGIN_RESULTY.WRONG_USER_OR_PASSWORD;
//            }
//        } catch (SQLException ex) {
//            return LOGIN_RESULTY.DATADASE_ERROR;
//        }
        return LOGIN_RESULTY.SUCCEED;
    }

    /* public static boolean isUserLoggedIn(String email)
     {
     try {
     int userID = getUserID(email);
     Connection connection = getConnection();
     Statement st = connection.createStatement();
     ResultSet rs = st.executeQuery("select * from C2C_1_0.sessions where user_id='" + userID + "'");
     boolean first = rs.first();
     rs.close();
     st.close();
     connection.close();
     return first;
     } catch (SQLException ex) {
     Exceptions.printStackTrace(ex);
     return false;
     }
     }*/

    /* public static String getUserSessionID(String email)
     {
     if (isUserLoggedIn(email)) {
     try {
     int userID = getUserID(email);
     Connection connection = getConnection();
     Statement st = connection.createStatement();
     ResultSet rs = st.executeQuery("select * from C2C_1_0.sessions where user_id='" + userID + "'");
     boolean first = rs.first();
     assert (first);
     String sessionId = rs.getString("id");
     rs.close();
     st.close();
     connection.close();
     return sessionId;
     } catch (SQLException ex) {
     Exceptions.printStackTrace(ex);
     return null;
     }
     } else {
     return null;
     }
     }*/
    public static void putTheUserSessionStartInDB(String email, HttpSession session, String ip) {
//        try {
//            Connection connection = getConnection();
//            Statement st = connection.createStatement(
//                    ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
//            ResultSet rs = st.executeQuery("select * from C2C_1_0.sessions where id='" + session.getId() + "'");
//            if (!rs.first()) {
//                rs.moveToInsertRow();
//                rs.updateString("id", session.getId());
//                rs.updateLong("user_id", getUserID(email));
//                rs.updateString("session_client_ip", ip);
//                Long l = (Long) session.getAttribute(callServiceImpl.SESSION_ATTRIBUTE_CREATION_TIME);
//                rs.updateTimestamp("session_start", new Timestamp(l));
//                rs.updateString("session_user_agent", "C2C_web_1_0");
//                rs.insertRow();
//                rs.close();
//                st.close();
//                connection.close();
//            } else {
//                //Exceptions.printStackTrace(new Exception("error: session duplication!!!! or reopened the C2C"));
//            }
//        } catch (SQLException ex) {
//            Exceptions.printStackTrace(ex);
//        }
    }

    public static void putTheUserSessionEndInDB(String sessionID) {
//        try {
//            Connection conn = getConnection();
//            Statement st = conn.createStatement(
//                    ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
//            String sql = "select * from C2C_1_0.sessions where id='" + sessionID + "'";
//            ResultSet rs = st.executeQuery(sql);
//            if (rs.first()) {
//                rs.updateTimestamp("session_end", new Timestamp(System.currentTimeMillis()));
//                rs.updateRow();
//                rs.close();
//                st.close();
//                conn.close();
//                System.out.println("putTheUserSessionEndInDB->" + sessionID);
//            }
//        } catch (SQLException ex) {
//            Exceptions.printStackTrace(new Throwable("Method putTheUserSessionEndInDB: Error DB!"));
//        }
    }

    public static long getUserID(String email) {
//        try {
//            Connection connection = getConnection();
//            Statement st = connection.createStatement();
//            String sql = "select * from C2C_1_0.users where email='" + email + "'";
//            ResultSet rs = st.executeQuery(sql);
//            if (rs.first()) {
//                return rs.getLong("id");
//            }
//        } catch (SQLException ex) {
//            Exceptions.printStackTrace(ex);
//        }
        return 1;
    }
    private static Connection CONNECTION = null;

    private static Connection getConnection() throws SQLException {
//        if (CONNECTION == null || CONNECTION.isClosed()) {
//            Driver d = new Driver();
//            Properties p = new Properties();
//            p.put("user", "root");
//            p.put("password", "testmachine");
//            CONNECTION = d.connect("jdbc:mysql://pdb.C2Csoftware.com:3306", p);
//        }
//        return CONNECTION;
        return null;
    }

    public enum LOGIN_RESULTY {

        SUCCEED, WRONG_USER_OR_PASSWORD, DATADASE_ERROR,
        USER_IS_ALREADY_LOGGEDIN_WITH_SAME_SESSION, SYSTEM_ERROR_SEE_SERVER_LOG
    }
}
