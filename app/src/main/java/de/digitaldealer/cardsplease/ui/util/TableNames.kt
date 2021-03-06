package de.digitaldealer.cardsplease.ui.util

import kotlin.random.Random

object TableNames {
    private val names = listOf(
        "Zion",
        "Kai",
        "Maeve",
        "Luca",
        "Nova",
        "Mia",
        "Aaliyah",
        "Mila",
        "Aurora",
        "Quinn",
        "Ezra",
        "Eliana",
        "Ivy",
        "Jayden",
        "Amara",
        "Kayden",
        "Lilibet",
        "Isabella",
        "Alina",
        "Elliot",
        "River",
        "Xavier",
        "Zoey",
        "Isla",
        "Lyla",
        "Alex",
        "Molly",
        "Andrea",
        "Remi",
        "Rowan",
        "Elias",
        "Alice",
        "Hayden",
        "Rohan",
        "Ophelia",
        "Kyle",
        "Jude",
        "Mya",
        "Shia",
        "Cecilia",
        "Milo",
        "Finn",
        "Leilani",
        "Aria",
        "Atlas",
        "Evan",
        "Millie",
        "Axel",
        "Urban",
        "Amaya",
        "Kayla",
        "Jesse",
        "Ian",
        "Riley",
        "Bailey",
        "Julia",
        "Blake",
        "Ari",
        "Savannah",
        "Freya",
        "Ira",
        "Sharon",
        "Sydney",
        "Raya",
        "Skylar",
        "Marcus",
        "Marie",
        "Malachi",
        "Brianna",
        "Rachel",
        "Brielle",
        "Silas",
        "Hudson",
        "Mika",
        "Kian",
        "Arlo",
        "Charlie",
        "Theo",
        "Mateo",
        "Aiden",
        "Kyra",
        "Arthur",
        "Reese",
        "Thea",
        "Zoe",
        "Valerie",
        "Rae",
        "Leo",
        "Mina",
        "Camille",
        "Sean",
        "Ayla",
        "Asa",
        "Zara",
        "Alaina",
        "Luna",
        "Ava",
        "Natasha",
        "Nancy",
        "Nia",
        "Myra",
        "Dante",
        "Evie",
        "Everly",
        "Everett",
        "Alana",
        "Elise",
        "Jasmine",
        "Louise",
        "Skyler",
        "Margot",
        "Sloane",
        "Alyssa",
        "Kieran",
        "Hailey",
        "Vivian",
        "Hadassah",
        "Octavia",
        "Isabelle",
        "Maria",
        "Damien",
        "Sasha",
        "Lara",
        "Nolan",
        "Adira",
        "Camila",
        "Rhea",
        "Lyra",
        "Jalen",
        "Maverick",
        "Finley",
        "Elaine",
        "Khadijah",
        "Harlow",
        "Lennox",
        "Morgan",
        "Ariella",
        "Wren",
        "Miles",
        "Lisa",
        "Jade",
        "Amelia",
        "Dior",
        "Elodie",
        "Lea",
        "Mackenzie",
        "Josie",
        "Yara",
        "Otis",
        "Elora",
        "Alan",
        "Giselle",
        "Gigi",
        "Damian",
        "Gianna",
        "Shelby",
        "Zayne",
        "Monica",
        "Rhys",
        "Sage",
        "Rebecca",
        "Arden",
        "Caleb",
        "Giovanni",
        "Aditya",
        "Emerson",
        "Kimberly",
        "Lila",
        "Aubrey",
        "Sloan",
        "Arianna",
        "Leia",
        "Lorenzo",
        "Rayne",
        "Rafael",
        "Michelle",
        "Aliyah",
        "Callan",
        "Aleena",
        "Ellis",
        "Beau",
        "Orion",
        "Xander",
        "Avi",
        "Madeline",
        "Marley",
        "Adeline",
        "Liam",
        "Kaiden",
        "Ximena",
        "Anya",
        "Avery",
        "Amira",
        "Jean",
        "Eloise",
        "Rylee",
        "Jocelyn",
        "Maira",
        "Zuri",
        "Nyla"
    )

    fun getRandomTableName() = names[Random.nextInt(names.count() - 1)]
}

/**
//201
//Niko
//202
//Amelie
//203
//Katherine
//204
//Eden
//205
//Piper
//206
//Olivia
//207
//Trisha
//208
//Harry
//209
//Lucian
//210
//Kevin
//211
//Helena
//212
//Ashton
//213
//Ayana
//214
//Liana
//215
//Cali
//216
//Noelle
//217
//Kaira
//218
//Delilah
//219
//Emery
//220
//Ada
//221
//Malia
//222
//Asher
//223
//Azariah
//224
//Seraphina
//225
//Justin
//226
//Adrian
//227
//Aisha
//228
//Lilah
//229
//Penny
//230
//Brayden
//231
//Chase
//232
//Nola
//233
//Pia
//234
//Kylie
//235
//Judah
//236
//Alani
//237
//Chelsea
//238
//Colin
//239
//Rory
//240
//Ivan
//241
//Annalise
//242
//Mariam
//243
//Lachlan
//244
//Brooks
//245
//Lynn
//246
//Callie
//247
//Lucas
//248
//Darcy
//249
//Nikita
//250
//Harper
//251
//Remy
//252
//Grayson
//253
//Colette
//254
//Enoch
//255
//Ryder
//256
//Wesley
//257
//Keanu
//258
//Sahil
//259
//Elena
//260
//Nicholas
//261
//Kaya
//262
//Sienna
//263
//Amos
//264
//Quincy
//265
//Alma
//266
//Ana
//267
//Naomi
//268
//Juno
//269
//Adriel
//270
//Lee
//271
//Oliver
//272
//Nevaeh
//273
//Hadley
//274
//Darren
//275
//Stella
//276
//Tessa
//277
//Juliana
//278
//Azalea
//279
//Sunny
//280
//Phoenix
//281
//Rai
//282
//Braxton
//283
//Makayla
//284
//Kali
//285
//Colton
//286
//Collin
//287
//Tara
//288
//Akira
//289
//Alaia
//290
//Amani
//291
//Brooke
//292
//Sawyer
//293
//Lorraine
//294
//Kaden
//295
//Kalani
//296
//Dennis
//297
//Beckett
//298
//Imani
//299
//Dillon
//300
//Mabel
//301
//Cole
//302
//Aliza
//303
//Cecil
//304
//Ace
//305
//Ethan
//306
//Huxley
//307
//Oakley
//308
//Gracie
//309
//Maren
//310
//Sophie
//311
//Sanjana
//312
//Reyna
//313
//Zaid
//314
//Flora
//315
//Ronan
//316
//Mona
//317
//Rhiannon
//318
//Cairo
//319
//Olive
//320
//Scott
//321
//Elina
//322
//Skye
//323
//Alexandra
//324
//Parker
//325
//Rian
//326
//Willa
//327
//Alena
//328
//Lauren
//329
//Margaret
//330
//Tiana
//331
//Claudia
//332
//Emmet
//333
//Elia
//334
//Devin
//335
//Nora
//336
//Mimi
//337
//Sadie
//338
//Cooper
//339
//Ambrose
//340
//Joan
//341
//Chiara
//342
//Zola
//343
//Ellie
//344
//Ravi
//345
//Rylan
//346
//Noa
//347
//Amaris
//348
//Ines
//349
//Hunter
//350
//George
//351
//Raine
//352
//Stanley
//353
//Levi
//354
//Heather
//355
//May
//356
//Rosie
//357
//Winston
//358
//Nellie
//359
//Amora
//360
//Lily
//361
//Blair
//362
//Bryce
//363
//Lorelei
//364
//August
//365
//Declan
//366
//Kelvin
//367
//Amir
//368
//Rocco
//369
//Israel
//370
//Alayna
//371
//Kendall
//372
//Shay
//373
//Adalyn
//374
//Francis
//375
//Misha
//376
//Mikayla
//377
//Ellen
//378
//Elisa
//379
//Zane
//380
//Lilian
//381
//Rei
//382
//Gemma
//383
//Abel
//384
//Noah
//385
//Marina
//386
//Ayden
//387
//Davina
//388
//Lola
//389
//Mayra
//390
//Veda
//391
//Marion
//392
//Killian
//393
//Imogen
//394
//Preston
//395
//June
//396
//Maureen
//397
//Christine
//398
//Mae
//399
//Bennett
//400
//Lilith
//401
//Fallon
//402
//Isaac
//403
//Remington
//404
//Adaline
//405
//Leon
//406
//Fiona
//407
//Nisha
//408
//Della
//409
//Cruz
//410
//Mira
//411
//Iva
//412
//Joyce
//413
//Winifred
//414
//Kailani
//415
//Harley
//416
//Theodore
//417
//Tyson
//418
//Thalia
//419
//Reece
//420
//Emelia
//421
//Leila
//422
//Rio
//423
//Leigh
//424
//Ahmed
//425
//Maximus
//426
//Romy
//427
//Emmett
//428
//Reya
//429
//Dakota
//430
//Genevieve
//431
//Sabine
//432
//Cheryl
//433
//Odette
//434
//Maya
//435
//Javier
//436
//Sana
//437
//Gia
//438
//Azriel
//439
//Sergio
//440
//Harvey
//441
//Ali
//442
//Jael
//443
//Jenny
//444
//Kaleb
//445
//Zia
//446
//Paxton
//447
//Jolene
//448
//Charlotte
//449
//Roman
//450
//Zena
//451
//Raina
//452
//Teddy
//453
//Cayden
//454
//Amyra
//455
//Melissa
//456
//Kyler
//457
//Kenji
//458
//Felix
//459
//Jasper
//460
//Maxine
//461
//Daisy
//462
//Colby
//463
//Vera
//464
//Sidney
//465
//Adonis
//466
//Martha
//467
//Kye
//468
//Kathleen
//469
//Liya
//470
//Lyric
//471
//Mavis
//472
//Kayleigh
//473
//Kane
//474
//Jayce
//475
//Mara
//476
//Livia
//477
//Elyse
//478
//Elliott
//479
//Theresa
//480
//Briar
//481
//Saira
//482
//Simone
//483
//Magdalena
//484
//Hallie
//485
//Yana
//486
//Keziah
//487
//Nathan
//488
//Cleo
//489
//Karina
//490
//Greyson
//491
//Caiden
//492
//Alisa
//493
//Layla
//494
//Jovi
//495
//Kit
//496
//Hayley
//497
//Sutton
//498
//Cain
//499
//Presley
 */
