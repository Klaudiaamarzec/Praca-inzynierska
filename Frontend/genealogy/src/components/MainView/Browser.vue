<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
const router = useRouter();

const documentTypes = ref([]);
const name = ref('');
const surname = ref('');
const title = ref('');
const description = ref('');
const selectedTypes = ref([]);
const showAdvancedSearch = ref(false);
const fromDate = ref('');
const toDate = ref('');
const country = ref('');
const voivodeship = ref('');
const community = ref('');
const city = ref('');
const address = ref('');
const postalCode = ref('');
const startPostalCode = ref('');
const endPostalCode = ref('');
const parish = ref('');
const longitude = ref('');
const latitude = ref('');
const minLongitude = ref('');
const maxLongitude = ref('');
const minLatitude = ref('');
const maxLatitude = ref('');

onMounted(async () => {
  try {
    const response = await fetch('http://127.0.0.1:8080/API/DocumentTypes/All');
    documentTypes.value = await response.json();
  } catch (error) {
    console.error('Błąd podczas pobierania rodzajów dokumentów:', error);
  }
});

const searchDocuments = async () => {

  try {
    const params = new URLSearchParams({
      name: name.value,
      surname: surname.value,
      title: title.value,
      description: description.value,
      typeIds: selectedTypes.value.join(','),
      fromDate: fromDate.value,
      toDate: toDate.value,
      country: country.value,
      voivodeship: voivodeship.value,
      community: community.value,
      city: city.value,
      address: address.value,
      postalCode: postalCode.value,
      startPostalCode: startPostalCode.value,
      endPostalCode: endPostalCode.value,
      parish: parish.value,
      longitude: longitude.value,
      latitude: latitude.value,
      minLongitude: minLongitude.value,
      maxLongitude: maxLongitude.value,
      minLatitude: minLatitude.value,
      maxLatitude: maxLatitude.value
    });

    for (let [key, value] of params) {
      if (value === '' || value === null || value === undefined) {
        params.delete(key);
      }
    }

    const response = await fetch(`http://127.0.0.1:8080/API/Documents/Search?${params}`, {
      method: 'GET',
      headers: {
        'Content-Type': 'application/json',
      }
    });

    if (!response.ok) {
      throw new Error('Odpowiedź serwera nie była poprawna');
    }

    const results = await response.json();

    await router.push({name: 'SearchResults', query: { results: JSON.stringify(results) },});

  } catch (error) {
    console.error('Błąd podczas wyszukiwania dokumentów:', error);
  }
};

</script>

<template>

  <section class="browser">

    <div class="browser-header">
      <p>Wyszukiwarka</p>
    </div>

    <div class="separator"></div>

    <section class="content">

      <div class="left-site">

        <div class="login-section">
          <label for="name">Imię</label>
          <input class="main-input" id="name"  v-model="name"/>
        </div>

        <div class="login-section">
          <label for="surname">Nazwisko</label>
          <input class="main-input" id="surname"  v-model="surname"/>
        </div>

      </div>

      <div class="right-site">
        <ul>
          <div class="li-section">
            <li v-for="(type, index) in documentTypes" :key="index">
              <label >
                <input class="main-input" type="checkbox" :value="type.id" v-model="selectedTypes">
                {{ type.typeName }}
              </label>
            </li>
          </div>
        </ul>
      </div>

    </section>

    <section :class="['advanced-fields', { 'is-visible': showAdvancedSearch }]">

      <div class="separator"></div>

      <div class="advanced-section">

        <div class="main-title-section">
          <label >Tytuł</label>
          <input class="main-input" type="text" id="parish" v-model="title" />
        </div>

        <div class="main-title-section">
          <label >Opis</label>
          <input class="main-input" type="text" id="parish" v-model="description" />
        </div>

        <div class="data-section">
          <label for="fromDate">Przedział dat</label>
          <input class="main-input" type="date" placeholder="od" id="fromDate" v-model="fromDate" />
          <input class="main-input" type="date" placeholder="do" id="toDate" v-model="toDate" />
        </div>

        <p>Adres</p>
        <div class="address-section">
          <input class="main-input" type="text" placeholder="Kraj" id="country" v-model="country" />
          <input class="main-input" type="text" placeholder="Województwo" id="voivodeship" v-model="voivodeship" />
          <input class="main-input" type="text" placeholder="Gmina/Powiat" id="community" v-model="community" />
          <input class="main-input" type="text" placeholder="Miasto" id="city" v-model="city" />
        </div>

        <div class="address-section">
          <input class="address-input" type="text" placeholder="Adres" id="address" v-model="address" />
          <input class="main-input" type="text" placeholder="Kod pocztowy" id="postalCode" v-model="postalCode" />
        </div>

        <div class="postal-codes-section">
          <label >Zakres kodów pocztowych</label>
          <input class="main-input" type="text" placeholder="Od" id="startPostalCode" v-model="startPostalCode" />
          <input class="main-input" type="text" placeholder="Do" id="endPostalCode" v-model="endPostalCode" />
        </div>

        <div class="parish-section">
          <label >Parafia</label>
          <input class="main-input" type="text" placeholder="parafia" id="parish" v-model="parish" />
        </div>

        <div class="coordinates-section">
          <label >Współrzędne geograficzne</label>
          <input class="main-input" type="text" placeholder="Szerokość" id="latitude" v-model="latitude" />
          <input class="main-input" type="text" placeholder="Długość" id="parish" v-model="longitude" />
        </div>

        <div class="coordinates-section">
          <label >Szerokość geograficzna</label>
          <input class="main-input" type="text" placeholder="Minimum" id="minLatitude" v-model="minLatitude" />
          <input class="main-input" type="text" placeholder="Maximum" id="maxLatitude" v-model="maxLatitude" />
        </div>

        <div class="coordinates-section">
          <label >Długość geograficzna</label>
          <input class="main-input" type="text" placeholder="Minimum" id="minLongitude" v-model="minLongitude" />
          <input class="main-input" type="text" placeholder="Maximum" id="maxLongitude" v-model="maxLongitude" />
        </div>

      </div>

    </section>

    <div class="button-section">
      <button class="advanced-search" @click="showAdvancedSearch = !showAdvancedSearch">{{ showAdvancedSearch ? 'Ukryj zaawansowane wyszukiwanie' : 'Zaawansowane wyszukiwanie' }}</button>
      <button class="main-button" @click="searchDocuments">Szukaj</button>
    </div>

  </section>
</template>

<style scoped>

.browser {
  width: auto;
  margin: 0 auto 30px;
  background: var(--light-white);
}

.main-button {
  margin-left: 150px;
}

p {
  margin: 5px 0;
}

</style>
