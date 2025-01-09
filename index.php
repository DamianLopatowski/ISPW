-<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Interfaccia Cicli</title>
    <link href="https://fonts.googleapis.com/css2?family=Dancing+Script:wght@400;700&display=swap" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
    <link rel="stylesheet" href="styles.css">
    <link rel="stylesheet" href="contatti.css">
    <link rel="stylesheet" href="bicicletta.css">
    <style>
    /* Posizionamento della dropdown a sinistra */
.dropdown-left {
    right: 0; /* Posiziona il lato destro della tendina rispetto al contenitore */
    transform: translateX(-100%); /* Sposta la tendina completamente a sinistra */
    max-width: calc(100vw - 50px); /* Imposta la larghezza massima per evitare che esca dalla finestra */
}
    .cart-container {
    position: relative;
    display: inline-block;
}

.cart-count {
    position: absolute;
    top: 0;
    right: -10px; /* Regola questa distanza per posizionare il numero accanto all'icona */
    background-color: #e74c3c; /* Rosso per evidenziare */
    color: white;
    font-size: 14px;
    font-weight: bold;
    padding: 4px 8px;
    border-radius: 50%;
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
}
    </style>
</head>
<body>

<nav>
    <div class="logo">Bike Garage</div>
    <div class="navbar-links">
        <div class="dropdown">
            <a href="#">Biciclette</a>
            <div class="dropdown-content">
                <ul>
                    <li><a href="#">Tutte</a></li>
                    <li><a href="mountain.php">Mountain Bike</a></li>
                    <li><a href="corsastrada.php">Corsa Strada</a></li>
                    <li><a href="#">Ciclocross/Gravel</a></li>
                    <li><a href="#">eBike Sport</a></li>
                    <li><a href="#">eBike Urban/Pieghevoli</a></li>
                </ul>
            </div>
        </div>
        <a href="#">Accessori e Ricambi</a>
        <a href="#">Abbigliamento</a>
        <a href="contatti.html">Contatti</a>
    </div>
    <div class="hamburger" onclick="toggleMobileMenu()">
        <div></div>
        <div></div>
        <div></div>
    </div>
    <div class="cart-container">
        <a href="carrello.php" class="cart-link">
            <i class="fas fa-shopping-cart cart-icon"></i> <!-- Icona del carrello -->
            <span id="cartCount" class="cart-count">0</span> <!-- Numero degli articoli -->
        </a>
    </div>
    <div class="cart-container">
        <?php
        session_start(); // Assicurati che la sessione sia avviata
        if (isset($_SESSION['username'])): ?>
            <div class="dropdown">
                <a href="javascript:void(0)" class="cart-link" onclick="toggleDropdown()"> 
                    <i class="fas fa-user login-icon"></i> <!-- Icona per il profilo -->
                </a>
                <div id="userDropdown" class="dropdown-content dropdown-left">
                    <ul>
                        <li><a href="dati.php">I tuoi dati</a></li>
                        <li><a href="ordini.php">I tuoi ordini</a></li>
                        <li><a href="schede.php">Le tue schede</a></li>
                        <li><a href="logout.php">Logout</a></li>
                    </ul>
                </div>
            </div>
        <?php else: ?>
            <a href="login.php" class="cart-link">
                <i class="fas fa-user login-icon"></i> <!-- Icona per il login -->
            </a>
        <?php endif; ?>
    </div>
</nav>


<div id="benvenuti">
    <h1><span>Benvenuti nel mondo virtuale di</span><br><span class="bike-garage">Bike Garage</span></h1>
    <img src="entrata.jpg" alt="Entrata" class="entrata-img">
</div>


<!-- Menu mobile visibile solo quando attivo -->
<div class="mobile-menu" id="mobileMenu">
    <div class="dropdown-mobile">
        <a href="#">Biciclette</a>
        <div class="dropdown-content-mobile">
        	<a href="#">Tutte</a>
            <a href="mountain.php">Mountain Bike</a>
            <a href="corsastrada.php">Corsa Strada</a>
            <a href="#">Ciclocross/Gravel</a>
            <a href="#">eBike Sport</a>
            <a href="#">eBike Urban/Pieghevoli</a>
        </div>
    </div>
    <a href="#">Accessori e Ricambi</a>
    <a href="#">Abbigliamento</a>
    <a href="contatti.html">Contatti</a>
</div>

<!-- Sezione Modelli di Spicco -->
<section class="featured-models nuovo">
    <h2>Il nostro nuovo</h2>
    <div class="carousel-container" id="carousel-nuovo">
        <div class="carousel-items">
            <div class="carousel-item">
                <img src="montagna.jpg" alt="Montagna">
                <h3>Mountain Bike</h3>
                <p>La bici da montagna ideale per terreni impervi e sentieri.</p>
                <button class="scopri-piu" onclick="window.location.href='mountain.php';">Scopri di più</button> <!-- Pulsante scopri di più -->
            </div>
            <div class="carousel-item">
                <img src="ebikes.jpg" alt="eBike">
                <h3>eBike Sport</h3>
                <p>La bici perfetta per gli spostamenti urbani e lo sport.</p>
                <button class="scopri-piu">Scopri di più</button> <!-- Pulsante scopri di più -->
            </div>
            <div class="carousel-item">
                <img src="strada.jpg" alt="Strada">
                <h3>Corsa Strada</h3>
                <p>Perfetta per lunghe percorrenze su strada con il massimo comfort.</p>
                <button class="scopri-piu">Scopri di più</button> <!-- Pulsante scopri di più -->
            </div>
        </div>
    </div>
    <button class="arrow arrow-left" onclick="moveCarousel('carousel-nuovo', -1)">&#8592;</button>
    <button class="arrow arrow-right" onclick="moveCarousel('carousel-nuovo', 1)">&#8594;</button>
</section>

<section class="featured-models usato">
    <h2>Il nostro usato certificato</h2>
    <div class="carousel-container" id="carousel-usato">
        <div class="carousel-items">
            <div class="carousel-item">
                <img src="montagnausato.jpg" alt="Usato Montagna">
                <h3>Usato Mountain Bike</h3>
                <p>Bici da montagna revisionata e certificata per nuove avventure.</p>
                <button class="scopri-piu">Scopri di più</button> <!-- Pulsante scopri di più -->
            </div>
            <div class="carousel-item">
                <img src="ebikeusato.jpg" alt="Usato eBike">
                <h3>Usato eBike Sport</h3>
                <p>eBike come nuove, pronte per percorrere la città.</p>
                <button class="scopri-piu">Scopri di più</button> <!-- Pulsante scopri di più -->
            </div>
            <div class="carousel-item">
                <img src="stradausato.jpg" alt="Usato Strada">
                <h3>Usato Corsa Strada</h3>
                <p>Bici da corsa usate ma in perfette condizioni per gli amanti della strada.</p>
                <button class="scopri-piu">Scopri di più</button> <!-- Pulsante scopri di più -->
            </div>
        </div>
    </div>
    <button class="arrow arrow-left" onclick="moveCarousel('carousel-usato', -1)">&#8592;</button>
    <button class="arrow arrow-right" onclick="moveCarousel('carousel-usato', 1)">&#8594;</button>
</section>

<section id="vieni-a-trovarci" class="vieni-a-trovarci">
    <div class="container">
        <h2 class="title">Vieni a trovarci</h2>
        <a class="text">Siamo sempre pronti ad accoglierti nel nostro negozio! Puoi venire a trovarci durante i nostri orari di apertura</a>
        <p class="text">Ti aspettiamo!</p>
        <div class="orari">
            <ul>
                <li><span class="day">Lunedì - Venerdì </span><strong>9:00-13:00; 15:00-19:00</strong></li>
                <li><span class="day">Sabato </span><strong>9:00-13:00</strong></li>
                <li><span class="day">Domenica </span><strong>Chiuso</strong></li>
            </ul>
        </div>

        <h2>Trova il nostro negozio</h2>
        <p class="text">Utilizza la mappa sottostante per localizzarci facilmente.</p>
        <iframe
                src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d2648.405438627086!2d12.4231887!3d41.9264561!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x132f5e3a5c077f19%3A0x1671611d5ee65f1d!2sBike%20Garage!5e0!3m2!1sit!2sit!4v1690416939283!5m2!1sit!2sit"
                width="100%"
                height="400"
                style="border:0;"
                allowfullscreen=""
                loading="lazy"
                referrerpolicy="no-referrer-when-downgrade">
        </iframe>
    </div>
</section>
<footer>
    <p>&copy; 2024 Bike Garage. Tutti i diritti riservati.</p>
</footer>
<script>
function toggleDropdown() {
    const dropdown = document.getElementById('userDropdown');
    dropdown.classList.toggle('show');
}

// Chiude la tendina se si clicca fuori
window.onclick = function(event) {
    if (!event.target.matches('.cart-link')) {
        const dropdowns = document.getElementsByClassName("dropdown-content");
        for (let i = 0; i < dropdowns.length; i++) {
            const openDropdown = dropdowns[i];
            if (openDropdown.classList.contains('show')) {
                openDropdown.classList.remove('show');
            }
        }
    }
};

    // Funzione per aggiornare il conteggio del carrello
    function updateCartCount() {
        fetch('carrello.php?action=getCartCount') // Endpoint per ottenere il conteggio
            .then(response => response.json())
            .then(data => {
                const cartCountElement = document.getElementById('cartCount');
                if (cartCountElement) {
                    cartCountElement.textContent = data.count; // Aggiorna il numero degli articoli
                }
            })
            .catch(error => console.error('Errore durante l\'aggiornamento del conteggio del carrello:', error));
    }

    // Esegui la funzione al caricamento della pagina
    document.addEventListener('DOMContentLoaded', updateCartCount);
</script>

<script src="scripts.js"></script>

</body>
</html>