

## HTML templating

Egy lehetséges definíció: olyan folyamat, melynek kimenete HTML dokumentum, bemenete adathalmaz.
Azt szeretnénk, ha a folyamatra igaz lenne:

* a HTML dinamikus, azaz függ a bemeneti adatoktól
* a kimenetet egy program állítja össze, ami gyors
* a kimenet szép

A folyamat kialakítának résztvevői:

A Designer: szépérzékkel megáldott ember, HTML és CSS segítségével egy statikus adathalmazból képes szép HTML dokumentumot előállítani.

A Programozó: egy nagyon okos ember, aki egy olyan programot tud írni, amely dinamikus bemeneti adatokból, gyorsan csúnya HTML tud előállítani.

A probléma: két emberünk a saját eszközeit és legjobb tudását összerakva minden a folyamatra előírt összes igényt ki tudja elégíteni.

### Megoldások evolúciója

HTML generálása programkódból.

HTML generálása programkód beágyazásával.

HTML generálása DSL beágyazásával.

Enlive: HTML generálása HTML-ből

### Hogy működik az Enlive?

Kezdetben vala a statikus HTML

* Designer készítette, ezért az szép. Átadta a Programozónak, további feldolgozásra.
* Programozónk szereti a Clojure-t és az Enlive-ot, ezért abban dolgozik.
* Áttekintjük, milyen lépéseken keresztül megy át a Designer által elkészített statikus HTML, amígy dinamikussá válik.

Az adatstruktúra

* A html tag-ek Map-ként vannak reprezentálva.
* 3 fontos kulcs: `:tag` - milyen tag-ről beszélünk, `:attrs` - a tag attribútumait tartalmazó map, `:content` - további tag-ek szekvenciája.
* Rekurzív adatstruktúra.
* Legfeleső szinten szekvencia, akkor is, ha csak egy elemet tartalmaz.
* Enlive séma / enlive adatstruktúra.

Szelekció és transzformáció

* Kiindulása Enlive adatstruktúrából előállítja a cél enlive adatstruktúrát.

Deszerializáció (Emit fázis)

* Eredményül megkapjuk a transzformált HTML struktúrát, ami már dinamikus adatokat tartalmaz.

Hogyan kell ezt Enliveban leírni.

* A lenti kódrészletben láthajuk a `deftemplate` makró használatát.
* Magas szintű Enlive építőelem.
* A felvázolt folyamatot valósítja meg,

### Miért jó az Enlive?

1. A programozó és a designer teljes szeparációja. Az interfész: HTML. Ez az, ami az Enlive-ot egyedivé teszi.
2. A jólismert CSS selectorok szintakszisát használja.
3. Template inheritance lehetséges.
4. A Clojure teljes arzenálja rendelkezésünkre áll a transzformáció során.

## Parsing

## Selection

## Transformation

## Snippets, templates

## Demo
