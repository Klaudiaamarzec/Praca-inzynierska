<script setup>

</script>

<template>

  <header>
    <section class="header-section">
      <div class="left-section-header">
        <router-link to="/homeGenealogist"><img src="../../icons/tree.png" alt="Logo" class="logo-image"/></router-link>
        <router-link to="/homeGenealogist"><h1>Genealogia</h1></router-link>
      </div>
      <div class="right-section">
        <router-link to="/about"><p>O serwisie</p></router-link>
        <router-link to="/help"><p>Pomoc</p></router-link>
        <router-link to="/"><button class="main-button">Wyloguj się</button></router-link>
      </div>
    </section>

    <hr class="divider" />

    <section class="menu">
      <nav>
        <ul>
          <li class="nav-bar"><router-link to="/genealogist/documents">Dokumenty</router-link></li>
          <li class="nav-bar"><router-link to="/genealogist/myDocuments">Moje dokumenty</router-link></li>
          <li class="nav-bar"><router-link to="/genealogist/people">Osoby</router-link></li>
          <li class="nav-bar"><router-link to="/genealogist/locations">Moje lokalizacje</router-link></li>
          <li class="nav-bar"><router-link to="/genealogist/addDocument">Dodaj dokument</router-link></li>
          <li class="nav-bar"><router-link to="/genealogist/addDocumentTyp">Dodaj nowy rodzaj dokumentu</router-link></li>
          <li class="nav-bar"><router-link to="/genealogist/notifications">Powiadomienia</router-link></li>
        </ul>
      </nav>
    </section>
  </header>

</template>

<style scoped>

</style>
