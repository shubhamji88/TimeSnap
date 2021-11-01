package com.shubhamji88.timesnap.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.shubhamji88.timesnap.data.model.Item

@Dao
interface CacheDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(token: List<Item>)
    @Query("select * from Item")
    fun getAllItem(): LiveData<List<Item>>
    @Query("select * from Item where name==:name")
    fun getItemByName(name:String): Item
    @Query("delete from Item")
    fun clear()
}