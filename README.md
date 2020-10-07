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
