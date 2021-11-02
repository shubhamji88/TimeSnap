package com.shubhamji88.timesnap.data.dao

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot

class ItemFirebaseDao {
    private val db = FirebaseFirestore.getInstance()
    private val itemCollection =db.collection("items")
    fun getAllItems(): Task<QuerySnapshot> {
        return itemCollection.get()
    }

}