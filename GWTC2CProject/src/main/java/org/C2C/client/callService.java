/**
 * Copyright 2014-2015 C2C Software, LLC. All rights reserved.
 * C2C SOFTWARE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.C2C.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.List;
import java.util.Map;

/**
 * Interface callService
 * @author C2C Software, LLC
 */
@RemoteServiceRelativePath("callService")
public interface callService extends RemoteService
{
    public List<Map<String, List<String>>> callService(List<String> cmds);
}
