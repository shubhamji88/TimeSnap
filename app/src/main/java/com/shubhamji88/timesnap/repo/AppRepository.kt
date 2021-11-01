package com.shubhamji88.timesnap.repo

//import com.shubhamji88.timesnap.data.Animal
//import com.shubhamji88.timesnap.data.list
import androidx.lifecycle.MutableLiveData
import com.shubhamji88.timesnap.data.dao.CacheDAO
import com.shubhamji88.timesnap.data.dao.ItemFirebaseDao
import com.shubhamji88.timesnap.data.model.Item
import com.shubhamji88.timesnap.data.model.list1
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppRepository(val dao: CacheDAO) {
    val allItems by lazy { dao.getAllItem() }
    suspend fun getItemDetails(name:String): Item? {
        allItems.value?.forEach {
            if(name.toString().lowercase()==it.name!!.lowercase())
                return it
        }
        return null
    }
    suspend fun cacheAllItems(){
        withContext(Dispatchers.IO){
            ItemFirebaseDao().getAllItems().addOnSuccessListener {
                val recieved=it.toObjects(Item::class.java)
                val list= mutableListOf<Item>()
                recieved.forEach { rec->
                    if(rec!=null && !rec.name.isNullOrBlank() && !rec.text.isNullOrBlank())
                        list.add(rec)
                }
                GlobalScope.launch(Dispatchers.IO) {
                    dao.insert(list)
                }
            }
        }
    }
}