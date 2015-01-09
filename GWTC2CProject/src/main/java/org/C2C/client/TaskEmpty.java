/**
 * Copyright 2014-2015 C2C Software, LLC. All rights reserved.
 * C2C SOFTWARE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.C2C.client;

import java.util.ArrayList;
import java.util.List;

/**
 * Class TaskEmpty
 * @author C2C Software, LLC
 */
public class TaskEmpty extends TaskObject
{
    private boolean m_disabled = false;
    private boolean m_waitForPendingTasks = false;
    public static List<TaskEmpty> EMPTY_TASKS = new ArrayList<TaskEmpty>();

    public TaskEmpty(TaskCompletedListener l)
    {
        this(false, false, l);
    }

    public TaskEmpty(boolean disabled, TaskCompletedListener l)
    {
        this(disabled, false, l);
    }

    public TaskEmpty(boolean disable, boolean pending, TaskCompletedListener l)
    {
        this("", disable, pending, l);
    }

    public TaskEmpty(String name, boolean disable, boolean pending, TaskCompletedListener l)
    {
        super();
        m_name = name;
        m_disabled = disable;
        m_waitForPendingTasks = pending;
        EMPTY_TASKS.add(this);
        m_newInstanceRequired = false;
        addListener(l);
    }

    @Override
    public String run()
    {
        boolean disabled = false;
//        if (TaskManager.DEBUG) {
//            //WindowManager.getInstance().setStatusBarText("TE Started " + getDebugName() + " : Pending Count = " + TaskManager.PENDING_COUNT);
//        }
        for (TaskEmpty te : EMPTY_TASKS) {
            if (te.equals(this)) {
                break;
            }

            if (te.m_disabled) {
                disabled = true;
                break;
            }
        }
        if (!m_disabled && !disabled) {
            if ((m_waitForPendingTasks && TaskManager.PENDING_COUNT == 0)
                    || !m_waitForPendingTasks) {
                if (TaskManager.DEBUG) {
                    //WindowManager.getInstance().setStatusBarText("TE Running " + getDebugName());
//                    OUT += "(RE)" + getDebugName() + ":";
                }
                setCompleted(true);
                setExecuted(true);
                fireCompletedAndUpdate(false);
                EMPTY_TASKS.remove(this);
                //SC.say(OUT);
            }
        }
//        if (TaskManager.DEBUG) {
//            //WindowManager.getInstance().setStatusBarText("TE Exitting " + getDebugName() + " : Pending Count = " + TaskManager.PENDING_COUNT);
//        }
        return "";
    }

    public void enable()
    {
        m_disabled = false;
    }
}
