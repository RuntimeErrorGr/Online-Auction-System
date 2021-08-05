In implementarea proiectului am facut urmatoarele presupuneri pentru a concretiza activitatea casei de licitatii:
    - Clientii, brokerii si produsele sunt citite din fisiere text separate si introduse in listele
     specifice ale casei de licitatii prin parsare.
    - Pentru a-si putea incepe activitatea, o casa de licitatii trebuie mai intai sa angajeze cel
    putin un broker care are rolul de a intermedia licitatiile, abia apoi managerul poate adauga
    produse si clientii pot fi intampinati si invitati sa liciteze.
    - Atributiile managerului sunt de a angaja brokeri si de a adauga produse in stoc.
    - Se pot incarca simultan atat produse cat si clienti, dar exista si posibilitatea de a se
    incarca doar produse sau doar clienti.
    - Un client poate licita numai si numai pentru produsele disponibile in stock la momentul in
    care acesta contacteaza casa de licitatii si care nu fac deja obiectul unei licitatii in curs
    de desfasurare. Astfel, daca mai sunt adaugate alte produse dupa ce clientul si-a exprimat deja
    lista de dorinte catre broker, clientul nu va mai licita pentru ele.
    - Un client are un singur broker, dar un broker poate reprezenta mai multi clienti.
    - Un client nu ofera niciodata mai putin decat pretul de start al unui produs atunci cand
    incepe sa liciteze.
    - Un client poate licita pentru oricate produse disponibile. Din aceasta cauza am considerat ca
    nu ar trebui sa intrerup colaborarea dintre client si broker dupa finalizarea unei licitatii
    deoarece este posibil sa mai existe licitatii la care clientul respectiv este inscris si la care
    va fi reprezentat de acelasi broker.
    - O licitatie se incheie dupa un numar maxim de pasi stabilit la crearea obiectului de tip
    licitatie sau daca intr-o runda a ramas un singur client. Daca toti clientii se retrag intr-o
    runda, atunci produsul nu este vandut si licitatia se incheie.
    - Daca ramane un singur client intr-o runda, chiar daca acesta face o oferta mai mare decat cea
    din runda anterioara, fara sa stie ca ceilalti s-au retras, produsul ii este vandut la pretul
    ofertat cu o runda inainte de runda finala.
    - Majoritatea valorilor numerice sunt alese folosind Random, asa ca simularea casei de licitatii
    difera de la rulare la rulare.
    - Algoritmul de random nu va da niciodata o oferta mai mare decat suma maxima pe care clientul
    a stabilit-o si i-a comunicat-o brokerului la inscrierea la licitatie (oferta se plafoneaza
    mereu la oferta maxima).
    - Se pot derula mai multe licitatii in acelasi timp pe threaduri separate.
    - Produsele vandute sunt sterse din lista de produse a casei de licitatii si adaugate intr-o
    lista istoric.
    - Asa cum mentioneaza si cerinta proiectului, din lista de produse pot sterge doar brokerii.
    Deci un produs este sters de catre brokerul clientului castigator la incheierea unei licitatii
    in cadrul threadului corespunzator licitatiei.
    - Casa de licitatii poate primi clienti chiar si daca stocul ei este gol.

    Folosirea aplicatiei - consola interactiva:
        Pentru implementarea consolei am folosit design patternul Command ce presupune existenta unei
    interfete "Command" si a cate unei clase responsabile pentru implementarea executarii comenzilor
    ce pot fi date de utilizator si o clasa Parser.
    Comenzile implementate care pot fi date de utilizator sunt:
    - hire <nume_fisier_brokeri> -> obligatoriu prima comanda ce trebuie data la deschiderea
    programului deoarece casa de licitatii nu poate functiona fara existenta brokerilor. Este apelata
    metoda execute() a unui obiect de tip HireCommand.
    - list <products/clients/employees/auctions> -> afiseaza lista ceruta din cadrul casei de
    licitatii cu toate informatiile aferente. Afisarea se realizeaza cu ajutorul unei metode generice
    din cadrul casei de licitatii. Comanda este utila pentru a verifica starea listelor la inceputul
    si la sfarsitul unei simulari.
    - startactivity <nume_fisier_produse nume_fisier_clienti> -> incepe efectiv simularea
    comportamentului casei de licitatii. Produsele incep sa fie adaugate de catre manager pe un
    thread, iar clientii sunt primiti de catre casa de licitatii pe alt thread. De asemenea, threadul
    casei de licitatii poate lansa alte threaduri (licitatiile). Daca utilizatorul doreste, poate
    incarca in cadrul simularii doar date de produse sau doar date de clienti (se pune "-" in locul
    argumentului lipsa).
    Utilizatorul poate incepe oricate simulari doreste in cadrul unei rulari prin apelul comenzii
    startactivity. Totusi este recomandata folosirea de fisiere de intrare cu date diferite pentru
    ca daca se incearca adaugarea de 2 ori a aceluiasi produs sau aceluiasi client va fi aruncata
    o exceptie de duplicat.
    Am ales aceasta implementare cu o singura comanda de start pentru a putea pune in evidenta faptul
    ca produsele si clientii pot fi adaugati in acelasi timp in casa de licitatii si ca simularea este
    multithreading.
    - quit/exit -> se iese din program.

    Functionalitati de baza:
    - Citirea/parsarea/instantierea produselor, clientilor si angajatilor (broekerilor) se face
    cu ajutorul unui obiect de tip Parser si a claselor Builder si Factory specifice fiecarei
    instante concrete in parte.
    - Partajarea listei de produse a casei de licitatii este realizata prin folosirea
    Collection.SyncronizedList.
    - Adaugarea de produse: produsele sunt citite pe rand dintr-un fisier text datele produselor.
    Managerul are rolul de a mentine stocul, asa ca atunci cand se incepe o simulare un nou fisier
    de intrare cu produse, este creat un obiect de tipul AddProductCommand prin executia metodei
    sincronizate de adaugare produse a managerului.
    - Vizualizarea listei de produse de catre clienti: atunci cand un client nou este adaugat casei
    de licitatii prin intermediul unui obiect AddClientCommand, acesta automat consulta lista de
    produse disponibile si face oferte pentru un numar random de produse disponibile. Casa de licitatii
    ii asociaza un broker si pe baza dorintelor exprimate de noul client, licitatiile sunt actualizate:
            - se poate crea o noua licitatie pentru un obiect dorit de client, in cazul in care
            acesta este primul care isi exprima interesul fata de acel obiect. Clientul este pus
            in asteptare pana se aduna numarul tinta de participanti pentru licitatia respectiva.
            Acest numar tinta de participanti cat si numarul maxim de runde sunt stabilite aleator.
            - se poate adauga clientul in asteptare la o licitatie deja existenta
            - daca in urma adaugarii noului client, licitatia si-a indeplinit numarul tinta de
            participanti, atunci licitatia este lansata.
    - Stergerea produselor vandute de catre brokeri: la incheierea cu succes a unei licitatii,
    brokerul care a reprezentat clientul castigator sterge produsul din lista de produse disponibile
    in stoc si ii cere clientului un comision stabilit pe baza algoritmilor descrisi in enunt.

    Mecanismul de licitatie:
        In cadrul unei licitatii clientii sunt reprezentati de brokeri si brokerii interactioneaza
        atat cu licitatia cat si cu clientii. Pentru a simula acest comportament am implementat
        design patternul Observer atat intre brokeri (subiect) - clienti (observator) cat si intre
        licitatie (subiect) - broker (observator). Astfel brokerul actioneaza ca un mediator intre
        cele 2 instante licitatie - client.
        In momentul in care o licitatie si-a atins numarul tinta de participanti, casa de licitatii
        creeaza un thread anonim in care se desfasoara licitatia. In acest thread avem o bucla for cu
        un numar maxim de pasi stabilit aleator.
        In fiecare runda procesul este urmatorul:
            - licitatia isi informeaza brokerii inscrisi ca o noua runda a licitatiei a inceput si
            le solicita o lista cu ofertele clientilor pe care ii reprezinta
            - brokerii isi informeaza la randul lor clientii ca o noua runda a inceput si solicita
            fiecarui client in parte sa faca o noua oferta mai mare decat pretul maxim din runda
            anterioara
            - clientul decide daca vrea sa participe la runda ce urmeaza sa inceapa prin cresterea
            ofertei pe care i-o comunica brokerului, sau in caz contrar comunica oferta -1, care
            semnifica faptul ca acesta doreste sa se retraga din licitatie. Suma licitata de client
            la fiecare runda este stabilita aleator, dar nu este niciodata mai mica decat pretul
            minim al produsului (pretul de start) si nu este mai mare decat suma maxima stabilita
            la inscrierea in licitatie.
            - brokerii colecteaza ofertele de la toti clientii pe care ii reprezinta in cadrul
            licitatiei si transmit mai departe casei de licitatie o mapa de tip client - oferta.
            - in cadrul rundei sunt centralizate informatiile oferite de brokeri, este calculata
            oferta maxima si este stabilit castigatorul rundei. Daca aceasta era ultima runda, atunci
            castigatorul ultimei runde este declarat si castigatorul licitatiei. De asemenea, un
            client poate castiga licitatia daca este singurul ramas in cadrul unei runde.
        La inchiderea unei licitatii, produsul, daca a fost vandut este scos din lista de produse
        disponibile si sunt actualizate starile licitatiei (este trecuta ca fiind finished),
        produsului (este trecut ca fiind sold), brokerului (este platit cu comisionul corespunzator)
        si a clientului castigator (este incrementat numarul de licitatii castigate).

    Modelare si concepte POO folosite:
        1) Incapsularea -> se pastreaza date si functiile separate intre ele si de exterior prin
        repartizarea functionalitatilor in clase specifice: clase Builder, clase Factory, clase Command etc.
        2) Abstractizarea -> ascunderea anumitor functionalitati si implementari: folosirea claselor
        abstracte pentru clienti, angajati si produse (aceste clase nu pot avea instante de sine
        statatoare in cadrul programului, ci sunt extinse de clase concrete) si a interfetelor
        specifice design patternurilor implementate.
        3) Mostenire -> clasele concrete preiau informatiile claselor abstracte la care mai adauga
        informatiile si metodele specifice.
        4) Polimorfism -> conform explicatiei din din Laborator 2: "Polimorfismul se referă la
        faptul că un obiect din clasa derivată poate fi folosita ca parametru al unei metode ce
        primește la definire un obiect al clasei de bază.". Astfel, am folosit numeroase metode, in
        special specifice casei de licitatii care primesc ca parametru un obiect de tip Client, dar
        ele sunt apelate in functie de situatie primind un parametru Individaul/JuridicalPerson, care
        sunt clase ce extind clasa de baza Client. Acelasi principiu a fost folosit si in ceea ce
        priveste metodele ce opereaza cu obiecte de tip Product.
        5) Genericitatea -> am folosit o metoda generica specifica casei de licitatii in implementarea
        comenzii de afisare "list". Aceasta metoda primeste ca parametru un tip generic de date care
        poate fi dupa caz, o lista de clienti, de produse, de licitatii sau de angajati.
        De asemenea, interfetele IObserver si IObservable specifice design patternului Observer
        sunt interfete generice. Metodele din cadrul acestor interfete pot primi si pot returna tipuri
        diferite de date deoarece avem 2 cazuri de implementare a observerului: licitatie <-> broker
        si broker <-> client.
        6) Multithreading -> pentru a implementa multithreading, am folosit un obiect ExecutorService
        cu un pool de 2 threaduri lansate la intalnirea comenzii startactivity: unul pentru adaugare
        produse si unul pentru adaugare clienti. De asemenea, in cadrul threadului de adaugare
        clienti se pot lansa alte threaduri anonime ce reprezinta licitatiile.
        7) Design patternuri implementate:
            7.1) Singleton: casa de licitatii si managerul sunt intantiate cu singleton deoarece
            enuntul specifica la sectiunea constrangeri ca pe fiecare simulare exista un manager
            si o casa de licitatii. Cu ajutorul acestui design pattern am instantiat cele 2 entitati,
            dar le-am si referentiat in diferite situatii.
            7.2) Builder: implementat pe clasele concrete Painting, Furniture, Jewelry, Individual
            si JuridicalPerson. obiectele de tip builder sunt folosite pentru instantierea mai
            usoara a obiectelor parsate din fisiere text.
            7.3) Factory: implementat pentru clasele abstracte Client si Product, incapsuleaza
            in cadrul unor blocuri switch - case obiectele builder. Am folosit aceasta implementare
            pentru a organiza mai bine codul responsabil pentru instantierea obiectelor de tip
            clase concrete ce extind niste clase abstracte mai mari.
            7.4) Command: metoda execute() din cadrul interfetei Command este implementata in cadrul
            claselor responsabile pentru executia comenzilor date de utilizator de la tastatura.
            Acest lucru ajuta la organizarea mai eficienta a parsarii comenzilor.
            7.5) Observer: cele 2 interfete generice IObservable si IObserver sunt implementate de
            clasele ale caror instante sunt reponsabile pentru desfasurarea mecanismului de
            licitatie: Auction <- IObservable (licitatiile sunt mereu subiecti), Broker <- IObservable
            (brokerii sunt subiecti pentru clientii lor), <- IObserver (dar si observatori pentru
            licitatii), Client <- IObserver (clientii sunt observatori pentru brokeri). In acest
            fel se asigura dinamica licitatiei de oferte - comunicare - centralizare,
            folosind o instanta mediatoare, brokerul.
