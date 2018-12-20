package com.tracking.store.db;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Date;

/**
 * Created by ZASS on 3/20/2018.
 */

@Table(name = "Product")
public class Product extends Model {

    @Column(name = "ProductID")
    public int ProductID;

    @Column(name = "Product")
    public String Product;


}
