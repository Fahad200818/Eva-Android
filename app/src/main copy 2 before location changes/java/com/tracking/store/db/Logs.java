package com.tracking.store.db;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by ZASS on 3/20/2018.
 */

@Table(name = "Logs")
public class Logs extends Model {

    @Column(name = "logID")
    public int logID;

    @Column(name = "logStatus")
    public int logStatus;

    @Column(name = "Log")
    public String Log;

    @Column(name = "LogDate")
    public String LogDate;
}
