/**
 * Copyright 2014-2015 C2C Software, LLC. All rights reserved.
 * C2C SOFTWARE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.C2C.client;

import java.util.List;

/**
 * Class TaskCallMethod
 * @author C2C Software, LLC
 */
public class TaskCallMethod extends TaskObject
{
    private String m_method;
    private TaskObject m_task;
    private List<TaskObject> m_args;

    public TaskCallMethod(TaskObject tsk, String method, List<TaskObject> args)
    {
        super();
        m_method = method;
        m_args = args;
        m_task = tsk;
        m_newInstanceRequired = false;
    }

    @Override
    public String run()
    {
        String ret = CallManager.CALL_METHOD(this, m_task, m_method, m_args);
        setCompleted(true);
        return ret;
    }
}
