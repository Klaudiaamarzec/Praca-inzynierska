<script setup>

</script>

<template>
  <header>
    <section class="header-section">
      <div class="left-section-header">
        <router-link to="/"><img src="../../icons/tree.png" alt="Logo" class="logo-image"/></router-link>
        <router-link to="/"><h1>Genealogia</h1></router-link>
      </div>
      <div class="right-section">
        <router-link to="/about"><p>O serwisie</p></router-link>
        <router-link to="/help"><p>Pomoc</p></router-link>
        <router-link to="/login"><button class="main-button">Zaloguj się</button></router-link>
        <router-link to="/register"><button class="main-button">Zarejestruj się</button></router-link>
      </div>
    </section>

    <hr class="divider" />

    <section class="menu">
        <nav>
          <ul>
            <li class="nav-bar"><router-link to="/documents">Dokumenty</router-link></li>
            <li class="nav-bar"><router-link to="/people">Osoby</router-link></li>
          </ul>
        </nav>
    </section>
  </header>

</template>

<style scoped>

</style>
