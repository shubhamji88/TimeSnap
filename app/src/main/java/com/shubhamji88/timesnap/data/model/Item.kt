package com.shubhamji88.timesnap.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

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
    Item(" Mesopotamian civilization"
        , "Original Location: Northeast by the Zagros mountains, southeast by the Arabian plateau\n Current Location: Iraq, Syria, and Turkey\n Major Highlights: First civilization in the world\n Meaning: Land between rivers (ancient Greek)"
        , 33.2232
        , 43.6793
        , "500 BC–3500 BC"
        , "https://firebasestorage.googleapis.com/v0/b/timesnap-7fd7d.appspot.com/o/Mesopotamian-Civilization.jpg?alt=media&token=3c2ba482-4ac9-4546-90b5-94f0fb3ba7bb"
        , "8ccebe8f396c4f90b0d7b1ca5317da6e",
        "30-3500 BC")

,
Item(" Indus Valley civilization"
, "Original Location: Northeast by the Zagros mountains, southeast by the Arabian plateau\n Current Location: Iraq, Syria, and Turkey\n Major Highlights: First civilization in the world\n Meaning: Land between rivers (ancient Greek)"
, 28.7186
, 77.0685
, "1900 BC–3300 BC"
, "https://firebasestorage.googleapis.com/v0/b/timesnap-7fd7d.appspot.com/o/Indus-Valley-Civilization.jpg?alt=media&token=f7120e81-8774-4868-b1a8-1baa14b2e663"
, "8ccebe8f396c4f90b0d7b1ca5317da6e",
"30-3500 BC"
)

,
Item(" Greek civilization"
, "Original Location: Italy, Sicily, North Africa, and as far west as France\n Current Location: Greece\n Major Highlights: Concepts of democracy and the Senate, the Olympics"
, 39.0742
, 21.8243
, "479 BC–2700 BC"
, "https://firebasestorage.googleapis.com/v0/b/timesnap-7fd7d.appspot.com/o/The%20Ancient%20Greek%20Civilization.jpg?alt=media&token=e60ac3a0-7767-4e06-b138-9198d963c464"
, "8ccebe8f396c4f90b0d7b1ca5317da6e",
"30-3500 BC"
)
,

Item(" Persian civilization"
, "Original Location: Egypt in the west to Turkey in the north, and through Mesopotamia to the Indus river in the east\n Current Location: Modern-day Iran\n Major Highlights: Royal road"
, 32.4279
, 53.6880
, "331 BC–550 BC"
, "https://firebasestorage.googleapis.com/v0/b/timesnap-7fd7d.appspot.com/o/The-Persian-civilization.jpg?alt=media&token=9dd93066-cf92-4774-9b4e-36934b64e762"
, "8ccebe8f396c4f90b0d7b1ca5317da6e",
"30-3500 BC"
)

,
Item(" Roman civilization"
, "Original Location: Village of the Latini\n Current Location: Rome\n Major Highlights: Most powerful ancient civilization"
, 41.9028
, 12.4964
, "465 AD–550 BC"
, "https://firebasestorage.googleapis.com/v0/b/timesnap-7fd7d.appspot.com/o/Roman-civilization.jpg?alt=media&token=3bcebe66-2b38-41f6-bd79-81bba687cedf"
, "8ccebe8f396c4f90b0d7b1ca5317da6e",
"30-3500 BC"
)
,

Item(" Aztec civilization"
, "Original Location: Southcentral region of pre-Columbian Mexico\n Current Location: Mexico\n Major Highlights: Nahuatl became the major language"
, 23.6345
, 102.5528
, "1521 AD–1345 AD"
, "https://firebasestorage.googleapis.com/v0/b/timesnap-7fd7d.appspot.com/o/The-Aztecs-Pyramid.jpg?alt=media&token=53d3fdf4-abd3-4c4d-9e17-1e022a18ce65"
, "8ccebe8f396c4f90b0d7b1ca5317da6e",
"465-1532 AD"
)
,

Item(" Chinese civilization"
, "Original Location: Yellow River and Yangtze region\n Current Location: Country of China\n Major Highlights: Invention of paper and silk"
, 35.8617
, 104.1954
, "1046 BC–1600 BC"
, "https://firebasestorage.googleapis.com/v0/b/timesnap-7fd7d.appspot.com/o/ancient-chinese-civilization.jpg?alt=media&token=ae7ed725-bceb-4ea2-af41-f2a8e829d79e"
, "8ccebe8f396c4f90b0d7b1ca5317da6e",
"30-3500 BC"
),



Item(" Maya civilization"
, "Original Location: Around present-day Yucatan\n Current Location: Yucatan, Quintana Roo, Campeche, Tabasco, and Chiapas in Mexico and south through Guatemala, Belize, El Salvador, and Honduras\n Major Highlights: Complex understanding of astronomy"
, 23.6345
, 102.5528
, "900 AD–2600 BC"
, "https://firebasestorage.googleapis.com/v0/b/timesnap-7fd7d.appspot.com/o/Mayan-civilization-2600-BC.jpg?alt=media&token=41c530c2-bca2-4bad-ab3e-9b9b18dc9d65"
, "8ccebe8f396c4f90b0d7b1ca5317da6e",
"465-1532 AD"
)

)

