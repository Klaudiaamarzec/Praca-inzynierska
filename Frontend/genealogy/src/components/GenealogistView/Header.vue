<script setup>

</script>

<template>
  <header>
    <section>
      <div class="left-section">
        <router-link to="/homeGenealogist"><img src="../../icons/tree.png" alt="Logo" /></router-link>
        <router-link to="/homeGenealogist"><h1>Genealogia</h1></router-link>
      </div>
      <div class="right-section">
        <router-link to="/about"><p>O serwisie</p></router-link>
        <router-link to="/help"><p>Pomoc</p></router-link>
        <button>Wyloguj się</button>
      </div>
    </section>

    <hr class="divider" />

    <section class="menu">
      <nav>
        <ul>
          <li><router-link to="/documents">Dokumenty</router-link></li>
          <li><router-link to="/people">Osoby</router-link></li>
          <li><router-link to="/notifications">Powiadomienia</router-link></li>
        </ul>
      </nav>
    </section>
  </header>

</template>

<style scoped>

button {
  padding: 10px 15px;
  background-color: #c9ad6e;
  color: #000000;
  border: none;
  font-weight: bold;
  border-radius: 30px;
  cursor: pointer;
}

button:hover {
  background-color: rgba(68, 51, 13, 0.6);
  color: #eeebe9;
}

</style>
