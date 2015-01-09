/**
 * Copyright 2014-2015 C2C Software, LLC. All rights reserved.
 * C2C SOFTWARE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.C2C.client;


import com.google.gwt.user.client.ui.DialogBox;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Interface Task
 * @author C2C Software, LLC
 */
public class Task
{
    private Set<TaskCompletedListener> m_listenerList = null;
    private String m_id;
    private boolean m_completed;
    private boolean m_executed;
    private List<String> m_result = null;
    private String m_objectType = null;
    private static Integer TID = 0;
    private static final Integer MAX_TID  = Integer.MAX_VALUE>>1;
    public static TaskWait TASK_WAIT = null;
    protected List<Task> m_children = new ArrayList<Task>();
    protected Task m_parent = null;
    public static Task RUNNING_TASK = null;
    public static Task ACTIVE_TASK = null;
    public static String OUT = "";
    public String m_name = "";

    public static void INIT()
    {
        CallManager.INIT();
        TASK_WAIT = new TaskWait();
        TASK_WAIT.run();
        DialogBox dialogbox = new DialogBox(false);
        dialogbox.setStyleName("demo-DialogBox");
        dialogbox.show();
        ClassRegistry.INIT_CLASSES();
    }

    public Task()
    {
        setId(TID.toString());
        TID++;
        if(MAX_TID<=TID) {
            //SC.say("Error!!! Too many tasks");
        }
        m_result = new ArrayList<String>();
        m_executed = false;
        m_listenerList = new HashSet<TaskCompletedListener>();
        if (null != RUNNING_TASK) {
            RUNNING_TASK.m_children.add(this);
            m_parent = RUNNING_TASK;
        } else {

        }

    }

    public String getResultValue()
    {
        if(m_result.size()>0) {
            return m_result.get(0);
        } else {
            return "";
        }
    }

    public List<String> getResultValueList()
    {
        return m_result;
    }

    public void setResultValue(List<String> result)
    {
        m_result = result;
    }

    public void setId(String id)
    {
        m_id = id;
    }

    public String getDebugName()
    {
        return m_name.equals("") ? getId() : m_name;
    }

    public String getId()
    {
        return m_id;
    }

    public void setObjectType(String type)
    {
        m_objectType = type;
    }

    public String getObjectType()
    {
        return m_objectType;
    }

    public void addListener(TaskCompletedListener l)
    {
        m_listenerList.add(l);
    }

    public void fireCompletedAndUpdate(boolean remove)
    {
        if (this instanceof TaskObject) {
            ACTIVE_TASK = this;
            RUNNING_TASK = this;
        }
        for (TaskCompletedListener l : m_listenerList) {
            if (null != l) {
                l.completed(this);
            }
        }
        setExecuted(true);
        if (this instanceof TaskObject) {
            ACTIVE_TASK = getIncompleteParent();
            RUNNING_TASK = null;
        }

        if (remove) {
            TaskManager.GET_TASKS().remove(new Integer(getId()));
        }

    }

    public void fireCompleted(boolean remove)
    {
        for (TaskCompletedListener l : m_listenerList) {
            l.completed(this);
        }
        setExecuted(true);
        if (remove) {
            TaskManager.GET_TASKS().remove(new Integer(getId()));
        }
    }

    public void fireWait()
    {
    }

    protected boolean hasIncompleteChildren()
    {
        for (Task t : m_children) {
            if (!t.isCompleted()) {
                return true;
            } else {
            }
        }
        return false;
    }

    protected boolean hasUnexecutedChildren()
    {
        for (Task t : m_children) {
            if (!t.isExecuted()) {
                return true;
            } else {
            }
        }
        return false;
    }

    protected Task getIncompleteParent()
    {
        Task p = this;
        while (!p.hasUnexecutedChildren()) {
            p = p.m_parent;
            if (null == p) {
                return null;
            }
        }
        return p;
    }

    public boolean isActiveTaskChild()
    {
        if (null == ACTIVE_TASK || !ACTIVE_TASK.hasUnexecutedChildren()) {
            return true;
        }
        for (Task tsk : ACTIVE_TASK.m_children) {
            if (tsk.equals(this)) {
                return true;
            }
            if (!tsk.isExecuted()) {
                return false;
            }
        }
        return false;
    }

    public void setCompleted(boolean completed)
    {
        if (!m_completed && completed) {
            TaskManager.ACTIVE_COUNT--;
        }

        m_completed = completed;
    }

    public void setExecuted(boolean executed)
    {
        m_executed = executed;
    }

    public boolean isCompleted()
    {
        return m_completed;
    }

    public boolean isExecuted()
    {
        return m_executed;
    }

    public String run()
    {
        setCompleted(true);
        setExecuted(true);
        return "";
    }
}
