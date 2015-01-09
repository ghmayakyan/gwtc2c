/**
 * Copyright 2014-2015 C2C Software, LLC. All rights reserved.
 * C2C SOFTWARE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.C2C.client;

import java.util.ArrayList;
import java.util.List;

/**
 * Class TaskObject
 * @author C2C Software, LLC
 */
public class TaskObject extends Task
{
    private List<Object> m_args = null;
    protected boolean m_newInstanceRequired = true;
    protected TaskObject m_parentTask;
    private static final TaskCompletedListener DEFAULT_LISTENER = new TaskCompletedListener()
    {
        public void completed(Task task)
        {
            if (!(task instanceof TaskEmpty)) {
                TaskManager.PENDING_COUNT--;
            }
        }
    };

    /**
     * The default constructor for class TaskObject
     */
    public TaskObject(Object... args)
    {
        super();
        m_parentTask = null;
        m_args = new ArrayList<Object>();
        for (Object o : args) {
            if (o instanceof Object[]) {
                Object[] x = (Object[]) o;
                for (Object ox : x) {
                    m_args.add(ox);
                }
            } else {
                m_args.add(o);
            }
        }
        setCompleted(false);
        TaskManager.ADD_TASK(this);
        addListener(DEFAULT_LISTENER);
    }

    public void getWaitResult(TaskCompletedListener l)
    {
        if (null != m_parentTask) {
            m_parentTask.addListener(l);
        }
    }

    @Override
    public String run()
    {
        String s = "";
        if (m_newInstanceRequired) {
            s = CallManager.NEW_OBJECT_METHOD(this, m_args);
        } else {
            if (!isCompleted()) {
                fireCompleted(false);
            }
        }
        setCompleted(true);
        return s;
    }

    public TaskObject getParentTask()
    {
        return m_parentTask;
    }

    public TaskObject getCastTaskObject()
    {
        if (m_parentTask == null) {
            return this;
        }
        return m_parentTask;
    }

    public String getExistingResultValue()
    {
        return getCastTaskObject().getResultValue();
    }

    public String getExistingTaskType()
    {
        return getCastTaskObject().getObjectType();
    }

    public Boolean hasCorrectType()
    {
        return getClass().getName().equals(getExistingTaskType());
    }

    public boolean isNull()
    {
        String type = getExistingTaskType();
        if (type.equals(CallManager.OBJECT_TYPE_NULL)) {
            return true;
        }
        return false;
    }
}
