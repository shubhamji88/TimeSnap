package com.shubhamji88.timesnap.repo

import com.shubhamji88.timesnap.data.Animal
import com.shubhamji88.timesnap.data.list

class AppRepository {
    suspend fun getItemDetails(name:String): Animal? {
        list.forEach {
            if(name==it.name)
                return it
        }
        return null
    }
}