/**
 * Copyright 2014-2015 C2C Software, LLC. All rights reserved. C2C SOFTWARE
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.C2C.client;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.DialogBox;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Class TaskManager
 *
 * @author C2C Software, LLC
 */
public class TaskManager {

    private static Map<Integer, Task> TASKS = new HashMap<Integer, Task>();
    public static boolean TRANSFERING = false;
    public static boolean NEW_TASK_AVAILABLE = false;
    protected static Integer ACTIVE_COUNT = 0;
    public static Integer PENDING_COUNT = 0;
    public static boolean DEBUG = false;
    static Timer timer;
    static int TASK_MANAGER_EXEC_TIME = 50;

    public static void ADD_TASK(Task task) {
        if (timer == null) {
            timer = new Timer() {
                @Override
                public void run() {
                    if (!TaskManager.TRANSFERING) {
                        CallManager.TICKS++;
                        TaskManager.RUN();
                    }
                }
            };
            timer.scheduleRepeating(TASK_MANAGER_EXEC_TIME);
        }

        TASKS.put(new Integer(task.getId()), task);
        ACTIVE_COUNT++;
        if (task instanceof TaskObject) {
            if (!(task instanceof TaskEmpty)) {
                PENDING_COUNT++;
            }
        }

    }

    public static void ADD_TASK_OBJECT(String tid, String className) {
        TaskObject taskObject = new TaskObject();
        taskObject.m_newInstanceRequired = false;
        taskObject.setObjectType(className);
        taskObject.setCompleted(true);
        taskObject.setExecuted(true);
        Integer itid = Integer.parseInt(tid);
        TASKS.remove(Integer.parseInt(taskObject.getId()));
        taskObject.setId(itid.toString());
        TASKS.put(itid, taskObject);
    }

    public static Task GET_TASK_BY_ID(String id) {
        return TASKS.get(new Integer(id));
    }

    public static Map<Integer, Task> GET_TASKS() {
        return TASKS;
    }

    public static void RUN() {
        DialogBox dialogbox = new DialogBox(false);
        dialogbox.setStyleName("demo-DialogBox");
        dialogbox.setText("RUNX "+ACTIVE_COUNT+", ");
        dialogbox.show();
        if (ACTIVE_COUNT <= 0 /*|| !CallManager.IS_LOGGEDIN*/) {
            return;
        }
        dialogbox.setText("RUNY");
        dialogbox.show();
        String toServer = "";
        Task prev = null;
        Map<Integer, Task> tasks = GET_TASKS();
        Set<Integer> keySet = tasks.keySet();
        for (Integer key : keySet) {
            final Task tsk = tasks.get(key);
            if (!tsk.isCompleted()) {
                if (tsk instanceof TaskEmpty) {
                    if (tsk.isActiveTaskChild()) {
                        if (!toServer.isEmpty()) {
                            prev.addListener(new TaskCompletedListener() {
                                public void completed(Task task) {
                                    
                                    String s = tsk.run();
                                    
                                }
                            });
                        } else {
                            tsk.run();
                        }
                    }
                } else {
                    String runRes = "";
                    runRes = tsk.run();
                    toServer = toServer.concat(runRes);
                    
                    prev = tsk;

                }
            }
        }
        if (!toServer.isEmpty()) {
            //WindowManager.getInstance().setStatusBarText(toServer.length()+"");
            CallManager.SEND_TO_SERVER(toServer);
        }
    }
}
