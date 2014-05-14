# Tekstuel repræsentation af ruter

## Meter og kilometer, enheder

For at vise længden af et vejstykke på en komprimeret og brugervenlig måde, valgte vi at angive længden i forskellige enheder afhængigt af vejstykkets længde. Udgangspunktet var at vise længden i meter uanset længde, men det viste sig ikke at være optimalt, da det både fyldte meget og var unødigt præcist. Derfor vedtog vi følgende:

- Længde vises i meter hvis længden er mindre end 1000 meter. L < 1000.
- Længde vises i kilometer med to decimaler hvis længden er mere end 1000 meter. L >= 1000.

## Retning af vejskifte

Vores første idé var at beregne vinklen mellem de to veje der udgør det enkelte vejskifte, og på den måde afgøre om der er tale om et sving, og i så fald, hvilken retning der drejes i. Vi var ikke særlig glade for denne løsning til at starte med, da vi fandt det problematisk vi selv skulle lave definitioner for de forskellige typer sving, frem for at aflæse det fra vores data. Denne skepsis forsvandt efter at vi blev enige om at vi ikke kunne finde nogen data om typen af vejskifte. Valget faldt altså på denne metode, eftersom vi ikke havde noget værdigt alternativ.

Vi besluttede os for følgende definitioner for de forskellige typer vejskifte:

- Vinklen mellem to vejstykker bliver beregnet således, at rotation med uret angives med en vinkel med positivt fortegn, og rotation mod uret angives med negativt fortegn.
- Hvis vinklen er mindre end 45 grader stor, uanset fortegn, er der ikke tale om et sving. |A| < PI / 4.
- Hvis vinklen er større end 45 grader og er positiv er der tale om et højre sving. A >= PI / 4.
- Hvis vinklen er mindre end 45 grader og er negativ er der tale om et venstre sving. A <= -PI / 4.