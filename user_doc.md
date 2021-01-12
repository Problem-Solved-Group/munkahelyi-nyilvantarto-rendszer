# Felhasználói dokumentáció

## Leírás az alkalmazás elindításához

### Backend
 - A szerver tárolja és kezeli az adatokat.
 - Előkövetelmények
   - JAVA JDK
   - (opcionális) Net Beans IDE
 - Telepítésének menete
   - Github repo klónozása: git clone https://github.com/Problem-Solved-Group/munkahelyi-nyilvantarto-rendszer.git
   - Belépés a repo mappájába: cd .\munkahelyi-nyilvantarto-rendszer
   - Ezt követően ./mvnw -DskipTests clean dependency:list install
   - Ezt követően a szerver indítása: java -jar .\target\filingsystem-0.0.1-SNAPSHOT.jar
 

### Frontend
 - Az alkalmazás UI kezeléséhez szükséges része
 - Előkövetelmények
   - Node.js
   - (opcionális) Visual Studio Code
 - Telepítésének menete
   - Github repo klónozása: git clone https://github.com/Problem-Solved-Group/munkahelyi-nyilvantarto-rendszer-kliens.git
   - Belépés a repo mappájába: cd .\munkahelyi-nyilvantarto-rendszer-kliens
   - Ezt követően
     - Telepítsük az Angular CLI-t: npm install -g @angular/cli
     - Telepítsük a többi függőséget: npm install
     - npm postinstall
   - Szerver indítása: npm start
