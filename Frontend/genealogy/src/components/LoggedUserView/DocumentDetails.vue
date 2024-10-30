<script setup>

</script>

<template>

  <section class="browser">
    <section class="advanced-section-adding">

      <div class="browser-header">
        <p>DOCUMENT DETAILS</p>
      </div>

      <div class="date-section">
        <label for="fromDate">Tytuł <span class="required-asterisk">*</span></label>
        <input class="input-title" type="text" id="title" v-model="title" />
      </div>

      <p>Opis</p>

      <textarea class="main-textarea"
                id="description"
                v-model="description"
                placeholder="Wpisz opis dokumentu"
                @input="autoResize"
      ></textarea>

    </section>

  </section>

</template>

<style scoped>

.browser {
  display: flex;
  background: var(--light-white);
  width: 60%;
  margin: auto;
  background: var(--light-white);
  padding-top: 15px;
  padding-bottom: 15px;
}

p {
  margin: 5px 0;
}

</style>
