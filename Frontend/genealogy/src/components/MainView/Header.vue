<script setup>

</script>

<template>
  <header>
    <h1>Genealogia</h1>
    <nav>
      <ul>
        <li><router-link to="/documents">Dokumenty</router-link></li>
        <li><router-link to="/people">Ludzie</router-link></li>
        <li><router-link to="/notifications">Powiadomienia</router-link></li>
      </ul>
    </nav>
  </header>
</template>

<style scoped>
header {
  width: 100%; /* Nagłówek zajmuje całą szerokość strony */
  position: fixed; /* Nagłówek przypięty do góry */
  top: 0; /* Przyklejony do górnej części strony */
  left: 0;
  background-color: white; /* Kolor tła nagłówka */
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1); /* Delikatny cień pod nagłówkiem */
  z-index: 1000; /* Zapewnia, że nagłówek jest na wierzchu */
  padding: 1rem; /* Odstęp wewnętrzny dla nagłówka */
}

h1 {
  margin: 0;
  font-size: 24px;
}

nav {
  display: flex;
  justify-content: center;
  margin-top: 10px;
}

ul {
  display: flex; /* Ustawia linki w jednym rzędzie */
  list-style-type: none;
  padding: 0;
}

li {
  margin: 0 15px;
}

a {
  text-decoration: none;
  color: var(--vt-c-text-light-1); /* Kolor linków */
  font-weight: bold;
}

a:hover {
  color: var(--vt-c-text-light-2); /* Zmiana koloru na hover */
}
</style>
