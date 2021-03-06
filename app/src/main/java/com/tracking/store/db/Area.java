package com.tracking.store.db;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by ZASS on 3/20/2018.
 */

@Table(name = "Area")
public class Area extends Model {

    @Column(name = "areaId")
    public int areaId;

    @Column(name = "townId")
    public int townId;

    @Column(name = "name")
    public String name;
}
