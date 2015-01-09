/**
 * Copyright 2014-2015 C2C Software, LLC. All rights reserved. C2C SOFTWARE
 * PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.C2C.client;

import com.google.gwt.user.client.ui.DialogBox;

/**
 * Class TaskWait
 *
 * @author C2C Software, LLC
 */
public class TaskWait extends Task {

    protected static TaskCompletedListener m_listener = null;

    /**
     * The default constructor for class TaskWait
     */
    public TaskWait() {
        super();
        setCompleted(true);
        m_listener = new TaskCompletedListener() {
            public void completed(Task task) {
                if (!TaskManager.TRANSFERING) {
                    //TaskManager.RUN();
                    DialogBox dialogbox = new DialogBox(false);
                    dialogbox.setStyleName("demo-DialogBox");
                    dialogbox.setText("TaskWait callback");
                    dialogbox.show();

                }
                //((TaskWait) task).run();
            }
        };
//        DialogBox dialogbox = new DialogBox(false);
//        dialogbox.setStyleName("demo-DialogBox");
//        dialogbox.setText("TaskWait");
//        dialogbox.show();
        addListener(m_listener);
        TaskManager.ADD_TASK(this);
    }

    @Override
    public String run() {
        //CallManager.IDLE(this);
        return "";
    }
}
