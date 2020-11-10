[![Build Status](https://travis-ci.com/Problem-Solved-Group/munkahelyi-nyilvantarto-rendszer.svg?branch=master)](https://travis-ci.com/Problem-Solved-Group/munkahelyi-nyilvantarto-rendszer)
# Munkahelyi nyilvántartó rendszer

*Egy olyan rendszer, mely segít a dolgozók szabadságát, illetve munkaidejét nyilvántartani.*

## Funkcionális követelmények
- Szabadság nyilvántartás - nyilvántartja a dolgozók szabadságigényléseit és az elfogadott/elutasított igényléseket
  - A dolgozó új igénylést tud benyújtani/szerkeszteni/törölni a Nyilvántartások menüpontból elérhető oldalon
  - A vezetők el tudják fogadni vagy utasítani a dolgozók igényléseit a Nyilvántartás bírálása menüpontból elérhető oldalon
- Munkaidő nyilvántartás - nyilvántartja a dolgozók munkahelyen töltött idejét
  - A dolgozó beírhatja az adott napra, hogy mettől meddig volt ott a Nyilvántartások menüpontból elérhető oldalon
  - A vezetők ellenőrizhetik az adott dolgozó által megadott időt a Nyilvántartás bírálása menüpontból elérhető oldalon
- Hírdetmények - A főoldalon megjelenő legújabb fontos események/változások
  - Admin/Vezető beosztású személy tud csak kiírni új hirdetményt a főoldalon, melyet mindenki láthat a főoldalon
  - Admin/Vezető beosztású személy részére külön oldal a hirdetmények szerkesztéséhez és törléséhez
- Üzenetek - Személyreszabott üzenet a munkahelyi dolgozói között az Üzenetek menüpontból elérhető oldalon
  - Bármelyik személy bárkinek tud új üzenetet írni

## Nem funkcionális követelmények
- Felhasználóbarát (Egyszerű ergonómikus felület)
- Biztonságos (jelszóval védett felület/adatok)

## Használt programozási eszközök:
 - Java Spring Boot
 - Rest API
 - Angular
 - H2

## Adatbázis 
- Adatbázis szerkezete(várható):
  - szabadsagnyilvantartas (id, felhasznalo_id, datum, igeny, elfogadott)
  - munkaidonyilvantartas (id, felhasznalo_id, datum, kezdete, vege, ellenorzott)
  - hirdetmenyek (id, iro_felhasznalo_id, cim, leiras, vege) - (vezető/admin írhat)
  - telephelyek (id, nev, hely)
  - uzenetek (id, iro_felhasznalo_id, cim_felhasznalo_id, cim, uzenet)
  - munkahely (felhasznalo_id, telephely_id)
  - felhasznalok (id, nev, email, jelszo, beosztas)
- Relációk:
  - felhasznalok (n <> n) telephelyek
  - felhasznalok (1 <> n) uzenetek
  - felhasznalok (1 <> n) szabadsagnyilvantartas
  - felhasznalok (1 <> n) munkaidonyilvantartas
  - felhasznalok (1 <> n) hirdetmeny 
  
## Szakterületi fogalmak
 - Szabadság nyilvántartás
 - Munkaidő nyilvántartás
 - Hirdetmények
 - Telephely
 - Beosztás
## Beosztás
- Admin - Minden vezetői funkció és:
  - Jogosultság állítás
  - Új felhasználó hozzáadása
- Vezető - Minden dolgozói funcikó és:
  - Hirdetmény kiírása
  - Szabadság igény elbírálása
  - Munkaidő ellenőrzése
- Dolgozó - Korlátozott hozzáférése van:
  - Új szabdság igénylés
  - Új bejegyzés a munkaidő nyilvántartásba
  - Hirdetmény olvasása
  - Üzenet küldése
### Fejlesztők
  - Nagy Viktor
  - Jobbágyi Dominik


## Backend
 - Fejlesztői környezet:
   - Netbeans IDE
   - IntelliJ IDE
 - Adatbázis terv:
   - [Kapcsolati diagram](https://dbdiagram.io/d/5faa9bd63a78976d7b7b4bcd)
 - Alkalmazott könyvtárstruktúra:
   - main
     - java
       - problemsolved
         - filingsystem
           - controllers - Kontroller osztályok csomag
           - entities - Entitás osztályok csomag
           - repositories - Tároló osztályok csomag
           - security - Biztonsági olsztályok csomag
     - resources - Kiegésztő fájlok
   - test
     - java - Teszt osztályok csomag
     - resources - Kiegészítő fájlok
 - Végpont tervek és leírásuk:
   - `GET /` - Hirdetmenyek listázása
   - `GET /announcements/{id}` - Az adott indexű hirdetmény megtekintése
   - `POST /announcements` - Új hirdetmeny létrehozása
   - `PUT /announcements/{id}` - Az adott indexű hirdetmény módosítása 
   - `DELETE /announcements/{id}` - Az adott indexű hirdetmény törlése
   - `GET /holiday` - Szabadságigények listázása
   - `GET /holiday/{id}` - Az adott indexű szabadságigény megtekintése
   - `POST /holiday` - Új szabadségigény létrehozása
   - `PUT /holiday/{id}` - Az adott indexű szabadságigény módosítása
   - `DELETE /holiday/{id}` - Az adott indexű szabadságigény törlése
   - `GET /messages/sent` - Az elküldött üzenetek
   - `GET /messages/received` - A fogadott üzenetek
   - `POST /messages` - Új üzenet létrehozása
   - `DELETE /messages/{id}` - Küldött üzenet törlése
   - `GET /site` - Telephelyek listázása
   - `GET /site/{id}` - Az adott indexű telephely megtekintése
   - `POST /site` - Új telephely felvétele
   - `POST /site/{id}/add` - Felhasználó hozzáadása telephelyhez
   - `POST /users` - Regisztráció
   - `GET /wt` - Munkaidők kilistázása
   - `GET /wt/{id}` - Az adott indexű munkaidő megtekintése
   - `POST /wt` - Új munkaidő létrehozása
   - `DELETE /wt/{id}` - Az adott indexű munkaidő törlése
 - Végpont bemutatása (`GET /holiday`)
   - A felhasználó egy GET kérést küld a /holiday végpontra, amennyiben érvényes tokent is tartalmaz, a szerver válaszul visszaküldi a felhasználó által látható
   - [Szekvencia diagram](https://ikelte-my.sharepoint.com/:i:/g/personal/w57a8i_inf_elte_hu/EcNqE9jsfohNqvC0FQJ0j2kB9A4AaQVJRDZOYluxeDszsw?e=WEjYWg)
