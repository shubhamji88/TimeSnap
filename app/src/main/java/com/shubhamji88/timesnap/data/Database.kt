package com.shubhamji88.timesnap.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.shubhamji88.timesnap.data.dao.CacheDAO
import com.shubhamji88.timesnap.data.model.Item

@Database(entities = [Item::class], version = 1,exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract val cacheDao: CacheDAO
}

private lateinit var INSTANCE: AppDatabase

fun getDatabase(context: Context): AppDatabase {
    synchronized(AppDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                    AppDatabase::class.java,
                    "items")
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    return INSTANCE
}


// Login
//{
//    "status": 200,
//    "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1c2VyIjp7ImVtYWlsIjoidGVzdGRldkBwdW5hci5pbiIsIm1vYmlsZSI6OTk5OTk5OTk5OSwiZ2VuZGVyIjoiTSJ9LCJleHAiOjE2MTI5NjU4Nzd9.GCCxKG1NfSt4rlvXrIrBWSAOpzLOgopHX_t5ATdgXAE"
//}
//{
//    "status": 400,
//    "error": "invalid",
//    "message": "Password Credentials Not Matched"
//}

//List patient
//{
//    "status": 200,
//    "patients": [
//    {
//        "patient_id": 41,
//        "patient_height": "23.99",
//        "patient_weight": "45.00",
//        "patient_condition": "Good",
//        "Trainer": 7,
//        "User": {
//        "name": "Webster.Jast8",
//        "email": "Antoinette19@gmail.com",
//        "mobile_no": 784512369,
//        "gender": "M",
//        "dateofbirth": "1789-10-01"
//    }
//    },
//    {
//        "patient_id": 4,
//        "patient_height": "99.99",
//        "patient_weight": "1.00",
//        "patient_condition": "Good",
//        "Trainer": 7,
//        "User": {
//        "name": "p0tient",
//        "email": "patient0.testdev@punar.in",
//        "mobile_no": 9999999991,
//        "gender": "Male",
//        "dateofbirth": "2017-09-01"
//    }
//    },
//    {
//        "patient_id": 2,
//        "patient_height": "66.00",
//        "patient_weight": "80.00",
//        "patient_condition": "normal",
//        "Trainer": 7,
//        "User": {
//        "name": "Alankrit",
//        "email": "alankritbhardwaj478@gmail.com",
//        "mobile_no": 9166096808,
//        "gender": "Male",
//        "dateofbirth": "2000-07-01"
//    }
//    }
//    ]
//}