/**
 * Copyright © 2008-2009 C2C Software, LLC. All rights reserved.
 * C2C SOFTWARE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.C2C.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.List;
import java.util.Map;

/**
 * Interface SynchronizationObjects
 * @author C2C Software, LLC
 */
@RemoteServiceRelativePath("synchronizationobject")
public interface SynchronizationObjects extends RemoteService
{
    public Map<String, String> callService(List<String> cmds);
}
