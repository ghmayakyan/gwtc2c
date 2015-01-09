/**
 * Copyright 2014-2015 C2C Software, LLC. All rights reserved.
 * C2C SOFTWARE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package org.C2C.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.C2C.client.SynchronizationObjects;

/**
 * Class SynchronizationObjectsImpl
 * @author C2C Software, LLC
 */
public class SynchronizationObjectsImpl extends RemoteServiceServlet implements SynchronizationObjects
{
    public static boolean THREAD_STARTED = false;
    public static volatile Integer callCount = 0;
    private static Map<String, String> WAKEUP_LIST = new HashMap<String, String>();


    public static Thread th = new Thread(new Runnable()
    {
        public void run()
        {
//            while (true) {
//                try {
//                    Thread.sleep(10000);
//                    System.out.println("Call Count is: " + callCount);
//                } catch (InterruptedException ex) {
//                    Exceptions.printStackTrace(ex);
//                }
//            }
        }
    });

    public static synchronized void call_count_increment()
    {
        callCount++;
    }

    /**
     * The default constructor for class SynchronizationObjectsImpl
     */
    public SynchronizationObjectsImpl()
    {
        th.start();
    }

    public static void wakeUp(String tid, String result) {
        WAKEUP_LIST.put(tid, result);
    }

    public  Map<String, String> callService(List<String> cmds)
    {
        String cmd = (String) cmds.get(0);
        String tid = (String) cmds.get(1);
        Map<String, String> ret = new HashMap<String, String>();
        ret.put(tid, cmd);
        //try {
            call_count_increment();
            Set<String> keySet = WAKEUP_LIST.keySet();
            for(String s: keySet) {
                String get = WAKEUP_LIST.get(s);
                ret.put(s, get);
            }
            WAKEUP_LIST.clear();
            //Thread.sleep(100);
//        } catch (InterruptedException ex) {
//            Exceptions.printStackTrace(ex);
//        }
        return ret;
    }
}
