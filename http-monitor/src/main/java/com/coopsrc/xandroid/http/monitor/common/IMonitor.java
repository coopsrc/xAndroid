package com.coopsrc.xandroid.http.monitor.common;

import com.coopsrc.xandroid.http.monitor.model.HttpInfo;

/**
 * @author tingkuo
 * <p>
 * Datetime: 2019-09-23 11:00
 */
public interface IMonitor {
    long insert(HttpInfo httpInfo);

    void update(HttpInfo httpInfo);
}
