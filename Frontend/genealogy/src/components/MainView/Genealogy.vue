<script setup>

</script>

<template>

  <div class="welcome-container">
    <!-- Lewa sekcja z tekstem powitalnym -->
    <div class="welcome-text">
      <p style="font-weight: bold">Witaj w Genealogii!</p>
      <p>Chcesz poznać historię swojej rodziny?</p>
      <p>Czy kiedykolwiek zastanawiałeś się, skąd pochodzisz?</p>
      <p>Nasza darmowa aplikacja „Genealogia” otwiera przed Tobą świat pełen historii, wspomnień i odkryć.</p>
      <p>Zbuduj drzewo genealogiczne swojej rodziny, odnajdź przodków i zgłęb tajemnice przeszłości, które mogą Cię zaskoczyć.</p>
      <p>Dzięki naszym narzędziom łatwo zaczniesz podróż po śladach swoich przodków, a każdy krok może zaprowadzić Cię w niespodziewane miejsca.</p>
      <p>Odkryj nieznane historie, które kryją się w Twojej rodzinie, połącz się z krewnymi, których istnienia nawet się nie spodziewałeś, i zanurz się w dziedzictwie, które tworzy Twoją tożsamość.</p>
      <p>Przenieś opowieść swojej rodziny na wyższy poziom – z Genealogią odkrywanie historii staje się pasjonującą przygodą!</p>
      <p style="font-weight: bold">Zarejestruj się juz dziś!</p>
      <p>Tworząc konto, zyskasz możliwość dodawania archiwów i dzielenia się swoimi odkryciami z innymi użytkownikami. Dołącz do naszej społeczności i wzbogacaj swoją historię!</p>
    </div>

    <!-- Prawa sekcja ze zdjęciami -->
    <div class="welcome-images">
      <img class="image1" src="../../icons/genealogyTree.jpg" alt="Genealogia - zdjęcie 1" />
      <img class="image2" src="../../icons/family.jpg" alt="Genealogia - zdjęcie 2" />
    </div>
  </div>

</template>

<style scoped>

.welcome-container {
  margin-left: 15px;
  margin-right: 15px;
  display: flex;
  justify-content: space-between;
  background-color: var(--brown);
  margin-top: 20px;
  padding: 20px 30px;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

p {
  margin: 7px 0;
}

.welcome-text {
  flex: 1;
  padding-right: 20px;
  font-size: 1.1rem;
  line-height: 1.6;
}

.welcome-images {
  flex: 1;
  position: relative;
  height: 300px;
}

.welcome-images img {
  border-radius: 10px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  position: absolute;
  max-height: 100%;
}

.image1 {
  width: 50%;
  top: 0;
  left: 0;
  z-index: 1;
}

/* Styl dla drugiego obrazu */
.image2 {
  width: 50%;
  bottom: 0;
  right: 0;
  z-index: 0;
  transform: translateY(20px);
}

</style>
