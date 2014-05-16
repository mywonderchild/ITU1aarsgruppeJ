# Gentegning af fragmenter

Når brugeren navigerer kortet og bliver på samme zoom-niveau vil der være dele af kortet som allerede er tegnet, der skal tegnes til skærmen igen. Der er et overlap mellem det gamle og det nye udsnit af kortet (figure 1).

[FIGURE 1]

Til at starte med tegnede vi det hele igen, inklusiv overlap, men det viste sig ikke at være hurtigt nok, og responstiden blev irriterende lang. Det blev derfor besluttet at vi havde brug for en mere effektiv løsning med de følgende egenskaber:

- Tegning af de udsnit af kortet der ikke er blevet tegnet
- Genbrug af de udsnit af kortet der allerede er blevet tegnet.

# Første løsning

Den første løsning der blev overvejet var at gemme et billede af det gamle udsnit og forskyde det således at det lå rigtigt i forhold til det nye udsnit (figure 2). Efterfølgende tegnes den del af udsnittet som ikke er inkluderet i overlappet.

[FIGURE 2]

Denne løsning var meget simpel, og vi formoder den havde været nem at implementere. Den havde dog en række ulemper:

- Hvis brugeren navigerer tilbage til et udsnit de allerede har set skal dette udsnit tegnes igen.
- Der skal gemmes billeddata til en buffer hver gang udsnittet ændrer sig, således at denne data kan gentegnes som en del af det næste udsnit.
- De udsnit der skal tegnes er tynde, hvilket skaber stor ineffektivitet som følge af den måde som vejstykker hentes fra quadtræer og tegnes (figure 3).

[FIGURE 3]

# Anden løsning

Den anden løsning vi overvejede, og den vi endte med at implementere, var inspireret af online kort-programmer som for eksempel Google Maps og OSM. Det centrale koncept i denne løsning er at dele kortet op i en masse små udsnit (tiles). Størrelsen på udsnit forbliver det samme uanset zoom-niveau. Når der er zoomet langt ud udgøres kortet af få tiles, og når der er zoomet langt ind udgøres kortet af mange tiles (figure 4). Når der skal tegnes et udsnit tegnes de tiles der er indenfor udsnittet.

[FIGURE 4]

Denne løsning løser helt eller delvist problemerne ved den ovenstående løsning:

- Da de tegnede data er strukturerede, og det er dermed muligt at gemme dem og vise dem igen når det er relevant.
- Der skal kun gemmes billeddata når der bliver tegnet nye tiles.
- De udsnit der skal tegnes er firkantede og har en vis størrelse, hvilket gør problemet med overflødig data mindre.

# Kort på 3 planer

Når løsningen med tiles anvendes er der tre planer der skal tages højde for:

- Model: Er 1000 enheder langt på den længste led. Ændrer aldrig størrelse.
- Tiles: Er dimensioneret efter et bitmap der ville kunne indeholde tiles for hele det aktuelle zoom-niveau. I praksis bliver sådant et bitmap aldrig oprettet, blot tiles som repræsenterer fragmenter af dette bitmap. Ændrer størrelse afhængigt af zoom-niveau.
- Skærm: Størrelsen er dimensionerne på det vindue som kortet vises i på skærmen. Ændrer størrelse når brugeren ændrer størrelsen på vinduet.

# Hvor kigger brugeren

For at holde styr på hvilket udsnit af kortet brugeren kigger på opdateres to uafhængige variable løbende:

- Center: En relativ vektor der definerer centrum for udsnittet. Værdierne for center løber fra 0 til 1 på både x- og y-aksen – altså er (0.5, 0.5) midten af kortet.
- Zoom: En relativ værdi der definerer hvor stor en andel af kortet der vises indenfor udsnittets længste side.
	- Når zoom er mellem 0 og 1 er der zoomet ind, og udsnittet rummer kun en andel af kortet.
	- Når zoom er 1 vises hele kortet.
	- Når zoom er mellem 1 er der zoomet ud, og kortet dækker kun en andel af skærmen.

Ved hjælp af disse to variable kan det beregnes hvilket udsnit af kortet brugeren har efterspurgt. Vi valgte denne løsning som et alternativ til absolutte koordinater for en boks for at opnå øget fleksibilitet, og fordi denne løsning virkede mere intuitiv for os.

# Når zoom ændrer sig

Når zoom ændrer sig skal størrelsen af tiles opdateres. 

# Hvilke tiles skal vises

Når kortet skal tegnes skal det afgøres hvilke tiles der skal vises på skærmen.

Først beregnes det hvor udsnittet befinder sig på tiles. Dette gøres ved at tage en boks der har samme dimensioner som skærm, og forskyde den i forhold til tiles således at den nu er centeret omkring center. Denne boks kalder vi for sektion (figure 10, venstre).

Dernæst beregnes hvilke tiles som sektion overlapper (figure 10, højre). Det er disse tiles som skal vises på skærmen.

[FIGURE 10]

# Forudtegning og centraliseret lagring af tiles

De løsninger vi er inspireret gør to ting markant anderledes end vi gør:

- De begrænser antallet af zoom niveauer. Hvis man ikke gør dette er der uendeligt mange zoom-niveauer, og dermed uendeligt mange tiles der potentielt skal lagres.
- De har en vedligeholdt database over tiles. Ved at gøre dette undgår de at tegne nye tiles når et udsnit efterspørges – I stedet hentes tiles fra den centrale database.

Vi overvejede en kort overgang at implementere en database løsning, men besluttede os for at det ville være for stor en opgave. Dette ville være en oplagt mulighed for senere optimering af programmet, og kunne både implementeres med en lokal eller en ekstern database.

Eftersom vi ikke anvender en databaseløsning giver det ikke mening at reducere antallet af zoom-niveauer. Da tiles alligevel skal tegnes løbende, ville dette kun være en fordel hvis brugeren vendte tilbage til et udsnit på et zoom-niveau som vedkommende allerede havde set. Da vi ikke tænker at dette scenarie opstår særlig tit, fravalgte vi at begrænse antallet af zoom-niveauer og dermed forringe programmets funktionalitet unødvendigt.

# Gruppering

Som nævnt tidligere skal tiles tegnes løbende som følge af at vi ikke har en database hvor vi kan hente dem fra. Til at starte med tegnede vi tiles enkeltvis, hvilket viste sig at være meget ineffektivt. En stor andel af de vejstykker der hentes fra modellen, og efterfølgende skaleres og tegnes, er slet ikke er indenfor tilen. Dette problem kan reduceres hvis tiles så vidt muligt grupperes og tegnes i rektangulære blokke. Hermed bliver mængden af spild minimeret (figure 7).

[FIGURE 7]

Efter at gruppen af tiles er blevet tegnet samlet, klippes tiles ud og gemmes individuelt i datastrukturen.

## Definition af algoritme

Vi fik hermed brug for en algoritme der kunne gruppere de tiles der endnu ikke var tegnet i rektangulære grupperinger. Algoritmen tager en liste af tiles der er er sorteret rækkevis, og efterfølgende kolonnevis, som input. Algoritmen finder det størst mulige rektangel, og tilføjer det til en liste over rektangler. Algoritmen gentager denne procedure indtil alle tiles er grupperet, og returnerer en liste af rektangler (figure 5).

[FIGURE 5]

## Brute force: Lang tid

Vores første løsningforslag var en simpel brute force algoritme. Indledningsvist opbygger algoritmen en tabel hvor det markeres hvilke tiles der mangler at blive tegnet. Den naive løsning til dette problem er en brute-force algoritme der undersøger alle mulige rektangler i denne tabel. Hvis rektanglet er det største indtil videre og kun består af tiles der mangler at blive tegnet gemmes rektanglets position. Denne procedure gentages indtil alle mulige rektanglet er undersøgt.

## Dynamic programming: Lineær tid

Dernæst fandt vi frem tile en algoritme der løser problemet mere effektivt ved hjælp af dynamic programming. Ligesom brute force algoritmen opbygger den en tabel hvor det markeres hvilke tiles der mangler at blive tegnet. Herefter opbygges endnu en tabel over sammenhængende søjler ved at traversere kolonnerne i tabellen nedefra og op, og beregne højden af den enkelte søjle dynamisk (figure 8).

[FIGURE 8]

Herefter skannes rækkerne i tabellen fra venstre mod højre, og arealet af det størst mulige rektangel der indeholder denne celle af tabellen beregnes ved hjælp af data fra søjle tabellen (figure 9). Undervejs i skanningen gemmes positionen af det største rektangel. Hermed en gennemgang af de første tre skridt.

- Cursoren er ved celle (1,1). Værdien for dette felt i søjle-tabellen er 3, og er nu højden af vores rektangel. Bredden af vores rektangel er 1, eftersom vi har undersøgt 1 felt uden at løbe ind i et felt med værdien 0 i søjle-tabellen. Arealet af det største rektangel der indeholder dette punkt er 1 * 3 = 3, og positionen af rektanglet gemmes da det er det største indtil videre.
- Cursoren er ved celle (2,1). Værdien for dette felt i søjle-tabellen er 2, og er nu højden af vores rektangel. Bredden af vores rektangel er 2, eftersom vi har undersøgt 1 felt uden at løbe ind i et felt med værdien 0 i søjle-tabellen. Arealet af det største rektangel der indeholder dette punkt er 2 * 2 = 4, og positionen af rektanglet gemmes da det er det største indtil videre.
- Cursoren er ved celle (3,1). Værdien for dette felt i søjle-tabellen er 0, og højden og bredden af vores rektangel nulstilles.

[FIGURE 9]

Afsluttende tilføjes det største rektangel til listen af rektangler, og de tiles der udgør rektanglet markeres som tegnede.

Som følge af at vores tiles er 256 * 256 pixels store, må det formodes at algoritmen typisk køres med N der ~12*8 = 96 eller mindre, forudsat at programmet køres i en opløsning på (256*12) * (256*8) ~= 2880 * 1800 pixels (MacBook Pro 15" Retina skærm) eller mindre.

Den valgte algoritme kører i lineær tid O(N) hvor N er antallet af tiles, og blev derfor valgt til fordel for den før-nævnte brute-force algoritme som vi formoder ville have resulteret i langt længere køretider. Vi ville gerne have beregnet køretiden for brute-force algoritmen, og sammenlignet de to køretider, men dette viste sig at være meget kompliceret at beregne køretiden for brute-force algoritmen korrekt.

Et logisk argument for at den dynamiske algoritme er væsentligt hurtigere er at den efter et lineært gennemløb af cellerne kun undersøger N forskellige rektangler, hvilket skal ses i kontrast til at undersøge alle mulige rektangler. Ydermere behøver algoritmen ikke tjekke om rektanglerne udelukkende består af tiles der mangler at blive tegnet, da dette på forhånd er garanteret.

## Case analyse: Konstant tid

Efter at have implementeret algoritmen der løser opgaven i alle tænkelige cases, gik vi i gang med at analysere hvilke cases der typisk opstår ofte når programmet køres i praksis, og hvilke der sjældent eller aldrig opstår (figure 11).

[FIGURE 11]

Som det kan ses på figuren har vi inddelt cases i dem der er typiske og ofte sker, dem der atypiske og opstår sjældent, og dem der aldrig opstår fordi de er umulige. Korte forklaringer af udvalgte cases:

- Typisk, øverst tv.: Brugeren har skiftet zoom-niveau, og alle tiles skal tegnes.
- Typisk, øverst th.: Brugeren har bevæget udsnittet mod nord, og en række af tiles skal tegnes.
- Atypisk, øverst th.: Brugeren har bevæget udsnittet mod nordvest, og en vinkel af tiles skal tegnes. Dette sker relativt sjældent, da det kræver at udsnittet bryder en utegnet række og kolonne samtidig.
- Atypisk, nederst tv.: Brugeren har bevæget udsnittet mod øst så hurtigt at udsnittet har brudt to utegnede kolonner samtidigt. Vi har ikke observeret dette case, men det er en mulighed.
- Umulige cases: Disse cases er umulige fordi der er huller i dem, hvilket ikke kan lade sig gøre hvis brugeren bevæger udsnittet rundt i en kontinuerlig bane.

Følgende algoritme reducerer løser problemet i konstant tid i typiske cases, og falder tilbage på en mere tidskrævende algoritme i tilfælde af at der er tale om en atypisk case (figure 12):

- Listen af tiles filtreres, og der oprettes en ny liste af tiles som endnu ikke er blevet tegnet.
- Fordi tiles er sorteret på den måde de er i den oprindelige liste, kan man lave et rektangel der går fra den første til den sidste tile fra den nye liste.
- Hvis antallet af tiles som rektanglet dækker over er det samme som der er utegnede tiles er problemet løst i konstant tid (figure 12 til venstre). Hvis dette ikke er tilfældet er den pågældende case atypisk (figure 12 til højre), og der faldes tilbage til den mere tidskrævende algoritme.

[FIGURE 12]

Der er værd at nævne at nogle atypiske cases også løses i konstant tid af denne algoritme (f.eks. atypisk case nederst til venstre på figure 11). Ligeledes er det værd at nævne at algoritmen giver det forkerte svar hvis en af de "umulige" cases opstår, og at dette ville resultere i en fejl i programmet.

Altså endte vi i sidste ende med en algoritme der er optimeret til de specifikke forhold der gælder for vores program, og som langt det meste af tiden tager konstant tid. Det er ikke muligt at beregne en gennemsnitskøretid for algoritmen eftersom vi ikke kan forudse hvordan brugeren interagerer med kortet (hvilket afgør hvor mange atypiske cases der opstår), og det eneste der kan garanteres er derfor at køretiden i værste fald er lineær.

# Tegning af tiles

Når listen af rektangler er fundet, skal indholdet af hver rektangel tegnes. Følgende procedure gentages for hvert af rektanglerne (figure 13):

- Der beregnes en boks som repræsenterer rektanglet på tiles planet.
- Rektanglets boks kopieres og skaleres således at den repræsenterer rektanglet på model planet. Modellen forespørges efter vejstykker der ligger indenfor denne boks og returnerer dem.
- Vejstykkernes koordinater skaleres til map planet, forskydes i forhold til rektanglets position, og gemmes som linjer der er klar til at blive tegnet.
- Linjerne tegnes til en buffer.
- Bufferen opdeles i tiles der gemmes i et lager.

[FIGURE 13]

# Visning af tiles

Endelig, når det er sikret at alle tiles der skal vises er tegnet, hentes de relevante tiles fra lageret og vises på skærmen.