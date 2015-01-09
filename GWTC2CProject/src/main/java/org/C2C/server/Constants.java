/*
 * Copyright 2014-2015 C2C Software, LLC. All rights reserved.
 * C2C SOFTWARE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package org.C2C.server;



/**
 * Class Constants.
 * @author C2C Software, LLC
 */
public class Constants
{
    private static final String version_v1_0 = "1.0";

    public static final String fs = Environment.fileSeparator();

    public static final String version = version_v1_0;

    public static final String BASE_PATH = Environment.userHomeDir() + fs;

    public static final String INSTALL_PATH = BASE_PATH + "C2C" + fs + "C2C" + fs + version + fs;

    public static final String WEB_PATH = "";
  
    public static final String PROLOG_PATH = INSTALL_PATH + "kb" + fs;

    public static final String FREE_MARKER_TEMPLATE_PATH = INSTALL_PATH + "reports" + fs;

    public static final String CONFIGURATION_TEMPLATES_PATH = INSTALL_PATH + "configurationTemplates" + fs;

    public static final String DEFAULT_PROFILE_PATH = INSTALL_PATH + "profiles" + fs;

    public static final String HARDWARE_LIB_PATH = INSTALL_PATH + "hardware" + fs;
    
    public static final String SOFTWARE_LIB_PATH = INSTALL_PATH + "software" + fs;

    public static final String POLICY_LIB_PATH = INSTALL_PATH + "policies" + fs;

    public static final String DEVICE_LIB_PATH = INSTALL_PATH + "devices" + fs;

    public static final String IMAGE_PATH = INSTALL_PATH + "images" + fs;

    public static final String ICON_PATH = IMAGE_PATH + "icons" + fs;

    public static final String SMALL_ICON_PATH = ICON_PATH + "small" + fs;

    public static final String LARGE_ICON_PATH = ICON_PATH + "large" + fs;

    public static final String DEVICE_IMAGE_PATH = IMAGE_PATH + "devices" + fs;

    public static final String USERS_DATA_PATH = INSTALL_PATH + "users_data" + fs;

    public static final String USER_DATA_TEMP_FOLDER_NAME = "tmp";

    public static final String USER_DATA_REPORTS_FOLDER_NAME = "reports";

    public static final String USER_DATA_POLICIES_FOLDER_NAME = "policies";

    public static final String USER_DATA_TEMPLATES_FOLDER_NAME = "templates";

    public static final String USER_DATA_PROJECTS_FOLDER_NAME = "project";

    public static final String USER_DATA_DASHBOARD_FOLDER_NAME = "dashboard";

    public static final String USER_DATA_PROFILE_FOLDER_NAME = "profile";

    public static final String USER_DATA_GLOBAL_SEARCH_FOLDER_NAME = "global_search";

    public static final String USER_DATA_IMAGES_FOLDER_NAME = "images";

    public static final String WEB_IMAGE_PATH = WEB_PATH + "images" + fs;

    public static final String WEB_ICON_PATH = WEB_IMAGE_PATH + "icons" + fs;

    public static final String WEB_SMALL_ICON_PATH = WEB_ICON_PATH + "small" + fs;

    public static final String WEB_LARGE_ICON_PATH = WEB_ICON_PATH + "large" + fs;

    public static final String WEB_DEVICE_IMAGE_PATH = WEB_IMAGE_PATH + "devices" + fs;

    public static final String RESOURCE_PATH = "C2C" + fs + "resources" + fs;

    public static final String RESOURCE_IMAGE_PATH = RESOURCE_PATH + "images" + fs;

    public static final String RESOURCE_ICON_PATH = RESOURCE_IMAGE_PATH + "icons" + fs;

    public static final String RESOURCE_SMALL_ICON_PATH = RESOURCE_ICON_PATH + "small" + fs;

    public static final String RESOURCE_LARGE_ICON_PATH = RESOURCE_ICON_PATH + "large" + fs;

    public static final ClassLoader LOADER = Constants.class.getClassLoader();

    public static final String EMPTY_ICON = "empty.xyz";

    public static final String CLOUD_ICON = "cloud.png";

    public static final String BLADE_ICON = "blade.png";

    public static final String CHASSIS_ICON = "chassis.png";

    public static final String ROUTER_ICON = "router.png";

    public static final String SOFTWARE_ICON = "software.png";

    public static final String HARDWARE_ICON = "hardware.png";
    
    public static final String POLICY_ICON = "policy.png";

    public static final String SITE_ICON = "site.png";

    public static final String SWITCH_STACK_ICON = "switch_stack.png";

    public static final String FOLDER_ICON = "folder.png";

    public static final String CLOUD_LARGE_ICON = "cloud_large.png";

    public static final String SITE_LARGE_ICON = "site_large.png";

    public static final String SWITCH_STACK_LARGE_ICON = "switch_stack_large.png";
}
