/*
 * Copyright 2021 (C) VinBrain
 */

package com.quizlet.vbmda.workcase.constant;

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * @author Nguyen Hoang Nam
 */
public interface MedicalConstants {

    String MINUS = "-";
    String AIVICARE_PATIENT_TENANT_CODE = "VBSCP";
    String EMPTY_BORDER = "[]";
    String DRAID_SYSTEM = "DrAid_system";
    String APP_CHANNEL = "app";
    String EMAIL_CHANEL = "email";
    String AT_SEPARATOR = "@";
    String COMMA_SEPARATOR = ",";
    String SLASH_SEPARATOR = "/";
    String TUMOR_SEGMENTATION_SEPARATOR = "@@";
    String SEMICOLON_SEPARATOR = ";";
    String SPECIAL_SEPARATOR = "@_@";
    String SMILE_SEPARATOR = "^_^";
    String COUNT_OVER_NAME = "count_over";
    String COUNT_OVER_METHOD = COUNT_OVER_NAME + "()";
    String DEFAULT_BIRADS = "0";
    String VNA_ISSUER = "vna";
    String DRAID_ISSUER = "draid";
    String ACCESS_TOKEN = "accessToken";
    String AZURE_OPENAI = "AZURE_OPENAI";
    String NA_VALUE = "NA";
    String LINE_BREAK = "\n";
    String COMMA_SPACE = ", ";
    String SPACE = " ";
    String UNDERSCORE = "_";
    String X_SPACE = " x ";
    String DOT_SPACE = ". ";
    int DEFAULT_OFFSET = 0;
    int DEFAULT_LIMIT = 30;
    int DEFAULT_SERIES_NUMBER = 1;
    String KEY_IMAGES = "Key Images";
    String BEARER_PREFIX = "Bearer ";
    String PENDING_URL = "%s/pending/%d";

    /**
     * DICOM Metadata
     */
    String AP_VIEW_POSITION = "AP";

    /**
     * Error messages
     */
    String ERROR_MSG_INTERNAL_SERVER_ERROR = "Internal server error.";

    /**
     * Liver Cancer
     */
    String LIVER_CANCER = "LIVER_CANCER";
    String RECTAL_CANCER = "RECTAL_CANCER";
    String PATIENT_PROTOCOL = "Patient Protocol";
    String AISCALER_LIVER_COPY_MODE = "AISCALER_LIVER";

    /**
     * Executor
     */
    String GENERAL_TASK_EXECUTOR = "generalTaskExecutor";
    String PRIORITY_TASK_EXECUTOR = "priorityTaskExecutor";
    String NOTIFICATION_TASK_EXECUTOR = "notificationTaskExecutor";
    String AI_PROCESSING_TASK_EXECUTOR = "aiProcessingTaskExecutor";

    /**
     * HTML
     */
    String LI_OPEN_TAG = "<li>";
    String LI_CLOSE_TAG = "</li>";
    String UL_OPEN_TAG = "<ul>";
    String UL_CLOSE_TAG = "</ul>";
    String B_OPEN_TAG = "<b>";
    String B_CLOSE_TAG = "</b>";
    String UL_LI_OPEN_TAG = "<ul><li>";
    String UL_LI_CLOSE_TAG = "</li></ul>";
    String SPAN_OPEN_TAG_WITH_COLOR_AND_BOLD = "<span style=\"color:%s;font-weight:bold\">";
    String U_OPEN_TAG = "<u>";
    String U_CLOSE_TAG = "</u>";
    String SPAN_OPEN_TAG = "<span>";
    String SPAN_CLOSE_TAG = "</span>";
    String P_OPEN_TAG = "<p>";
    String P_CLOSE_TAG = "</p>";

    /**
     * Caching
     */
    String USER_QUERY_BY_ID_CACHE = "userQueryByIdCache";
    String INTERNAL_USER_QUERY_BY_ID_CACHE = "internalUserQueryByIdCache";

    /**
     * Logging
     */
    String HIS_LOGGING = "HIS"; // HIS integration
    String AIP_LOGGING = "AIP"; // AI processing
    String DPET_LOGGING = "DPET"; // DICOM processing estimation time
    String CDRO_LOGGING = "CDRO"; // Chest-detection request output
    String LIVER_CANCER_LOGGING = "LCP"; // Liver Cancer processing
    String RECTAL_CANCER_LOGGING = "RCP"; // Rectal Cancer processing
    String REPORTING_LOGGING = "RPT"; // Rectal Cancer processing

    /**
     * Plugin
     */
    String PLUGIN_USER_TRIAL_ACCOUNT = "USER_TRIAL_ACCOUNT";
    String PLUGIN_GET_WORKLIST_FROM_HIS = "WORKLIST_FROM_HIS";
    String PLUGIN_REPORT_LINK_VIEWER = "REPORT_LINK_VIEWER";
    String PLUGIN_METADATA_LOADING_V2 = "METADATA_LOADING_V2";
    String PLUGIN_INCREMENTAL_PID_SUFFIX = "INCREMENTAL_PID_SUFFIX";
    String PLUGIN_NOTIFY_WITHOUT_WORKLIST = "NOTIFY_WITHOUT_WORKLIST";
    String PLUGIN_PUBLIC_VIEWER_SHOW_TENANT = "PUBLIC_VIEWER_SHOW_TENANT";
    String PLUGIN_PUBLIC_VIEWER_NO_AI_RESULT = "PUBLIC_VIEWER_NO_AI_RESULT";
    String PLUGIN_CUSTOMIZED_STUDY_LOCAL_MODE = "CUSTOMIZED_STUDY_LOCAL_MODE";

    /**
     * Permission
     */
    String PERMISSION_TELE_RADIOLOGIST = "TELE_RADIOLOGIST";

    /**
     * System
     */
    String SYSTEM_USER = "SYSTEM_USER";

    Set<String> EXCLUDE_URIS = Sets.newHashSet(
            "/actuator/health",
            "/actuator/prometheus",
            "/b2b-auth",
            "/report/patient/**",
            "/v2/report/patient/**",
            "/v3/report/patient/**",
            "/v3/studies/public/**",
            "/observations-creation",
            "/v3/patients/public-studies",
            "/v4/patients/public-studies",
            "/v5/patients/public-studies",
            "/v4/workcases/*/observations",
            "/v5/studies/generate-link",
            "/v6/studies/static-files/**",
            "/v1/authentication/**",
            "/v7/studies/*/reports",
            "/cognitive/**",
            "/viewer/**",
            "/studies/pending/ai-process/cancer",
            "/clear-cache/**",
            "/bypass/**"
    );

}
