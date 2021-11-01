package com.shubhamji88.timesnap.data

//data class Animal(
//    val name: String,
//    val height: String,
//    val length: String,
//    val latitude:Double,
//    val longitude:Double,
//    val lifespan: String,
//    val millYearAgo: Long,
//    val picUrl:String,
//    val lens_id:String
//)
val list= listOf<Animal>(Animal(name="INDOMINUS REX","HEIGHT - 5.5 - 6.7 M\n LENGTH - 13.1 - 16.7 M\n LIFESPAN - 59 YEARS\n lived - 66–68 MILLION YEARS AGO(CRETACEOUS PERIOD)", latitude= 62.4114, longitude= 149.0730,"100 Million year ago", picUrl= "https://firebasestorage.googleapis.com/v0/b/timesnap-7fd7d.appspot.com/o/indominus-rex.png?alt=media&token=6bcec545-fe6d-4c03-82fd-774a76f0a804", lens_id= ""))
data class Animal(
    val name: String,
    val text: String,
    val latitude:Double,
    val longitude:Double,
    val timeAgo: String,
    val picUrl:String,
    val lens_id:String
)
