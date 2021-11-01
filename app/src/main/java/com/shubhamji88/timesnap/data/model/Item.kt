package com.shubhamji88.timesnap.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

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
//val list= listOf<Animal>(Animal(name="INDOMINUS REX","HEIGHT - 5.5 - 6.7 M\n LENGTH - 13.1 - 16.7 M\n LIFESPAN - 59 YEARS\n lived - 66–68 MILLION YEARS AGO(CRETACEOUS PERIOD)", latitude= 62.4114, longitude= 149.0730,"100 Million year ago", picUrl= "https://firebasestorage.googleapis.com/v0/b/timesnap-7fd7d.appspot.com/o/indominus-rex.png?alt=media&token=6bcec545-fe6d-4c03-82fd-774a76f0a804", lens_id= ""))
@Entity(tableName = "Item")
data class Item(
    @PrimaryKey
    val name: String,
    val text: String?,
    val latitude:Double?,
    val longitude:Double?,
    val timeAgo: String?,
    val picUrl:String?,
    val lens_id:String?,
    val title:String?
){
    constructor():this("null",null,null,null,null,null,null,null)
}
val list1= listOf<Item>(
Item(" INDOMINUS REX"
, "HEIGHT - 5.5 - 6.7 M\nLENGTH - 13.1 - 16.7 M\nLIFESPAN - 59 YEARS\nlived - 66–68 MILLION YEARS AGO(CRETACEOUS PERIOD)"
, 62.4114
, 149.0730
, "66-68 Million year ago"
, "https://firebasestorage.googleapis.com/v0/b/timesnap-7fd7d.appspot.com/o/indominus-rex.png?alt=media&token=6bcec545-fe6d-4c03-82fd-774a76f0a804",
"",""),



Item(" TARBOSAURUS"
, "HEIGHT - 4.3 - 7 M\nLENGTH - 10 - 12 M\nLIFESPAN - 25 YEARS"
, 42.0790
, 104.1901
, "72-68 MILLION YEARS AGO"
, "https://firebasestorage.googleapis.com/v0/b/timesnap-7fd7d.appspot.com/o/Tarbosaurus.png?alt=media&token=c02ca1b5-b45c-44c1-bd11-d0d67fbf51cf",
"",""),



Item(" Quetzalcoatlus "
, "Wingspan: 10 – 11 M\nMass: 200 – 250 kg\nLength: 10 – 11 M\nGenus: Quetzalcoatlus, Lawson, 1975"
, 54.5260
, 105.2551
, "66-72 Million year ago"
, "https://firebasestorage.googleapis.com/v0/b/timesnap-7fd7d.appspot.com/o/Quetzalcoatlus.png?alt=media&token=57f8ff86-0058-40b6-bd13-93f94f231129",
"",""),



Item(" Ankylosaurus"
, "Height: 1.7 M\nMass: 4,800 – 8,000 kg\nLength: 6 – 8\nScientific name: Ankylosaurus (Fused lizard)"
, 53.9333
, 116.5765
, "66-83 Million years ago (Campanian - Maastrichtian)"
, "https://firebasestorage.googleapis.com/v0/b/timesnap-7fd7d.appspot.com/o/Ankylosaurus.png?alt=media&token=f31df4ed-af15-416d-bfef-fd1a9045eb2d",
"","")
,



Item(" Ichthyornis"
, "Wingspan: 43 cm\nLength: 24 cm\nScientific name: Ichthyornis (Fish bird‭)\nKingdom: Animalia"
, 53.9333
, 116.5765
, "70-99 Million years ago (Cenomanian - Campanian)"
, "https://firebasestorage.googleapis.com/v0/b/timesnap-7fd7d.appspot.com/o/Ichthyornis.png?alt=media&token=c587335a-1a58-48be-96ef-1b5d00c7107b",
"","") )
