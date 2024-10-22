<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
const router = useRouter();

const documentTypes = ref([]);
const name = ref('');
const surname = ref('');
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
    //emit('searchResults', results); // Emituje wyniki do HomeView

    await router.push({name: 'SearchResults', query: { results: JSON.stringify(results) },});rontend

    //await router.push({name: 'SearchResults', state: {results}});

    // const searchResults = await response.json();
    // console.log(searchResults);
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
          <input id="name"  v-model="name"/>
        </div>

        <div class="login-section">
          <label for="surname">Nazwisko</label>
          <input id="surname"  v-model="surname"/>
        </div>

      </div>

      <div class="right-site">
        <ul>
          <div class="li-section">
            <li v-for="(type, index) in documentTypes" :key="index">
              <label >
                <input type="checkbox" :value="type.id" v-model="selectedTypes">
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

        <div class="data-section">
          <label for="fromDate">Przedział dat</label>
          <input type="date" placeholder="od" id="fromDate" v-model="fromDate" />
          <input type="date" placeholder="do" id="toDate" v-model="toDate" />
        </div>

        <p>Adres</p>
        <div class="address-section">
          <input type="text" placeholder="Kraj" id="country" v-model="country" />
          <input type="text" placeholder="Województwo" id="voivodeship" v-model="voivodeship" />
          <input type="text" placeholder="Gmina/Powiat" id="community" v-model="community" />
          <input type="text" placeholder="Miasto" id="city" v-model="city" />
        </div>

        <div class="address-section">
          <input class="address-input" type="text" placeholder="Adres" id="address" v-model="address" />
          <input type="text" placeholder="Kod pocztowy" id="postalCode" v-model="postalCode" />
        </div>

        <div class="postal-codes-section">
          <label >Zakres kodów pocztowych</label>
          <input type="text" placeholder="Od" id="startPostalCode" v-model="startPostalCode" />
          <input type="text" placeholder="Do" id="endPostalCode" v-model="endPostalCode" />
        </div>

        <div class="parish-section">
          <label >Parafia</label>
          <input type="text" placeholder="parafia" id="parish" v-model="parish" />
        </div>

        <div class="coordinates-section">
          <label >Współrzędne geograficzne</label>
          <input type="text" placeholder="Szerokość" id="latitude" v-model="latitude" />
          <input type="text" placeholder="Długość" id="parish" v-model="longitude" />
        </div>

        <div class="coordinates-section">
          <label >Szerokość geograficzna</label>
          <input type="text" placeholder="Minimum" id="minLatitude" v-model="minLatitude" />
          <input type="text" placeholder="Maximum" id="maxLatitude" v-model="maxLatitude" />
        </div>

        <div class="coordinates-section">
          <label >Długość geograficzna</label>
          <input type="text" placeholder="Minimum" id="minLongitude" v-model="minLongitude" />
          <input type="text" placeholder="Maximum" id="maxLongitude" v-model="maxLongitude" />
        </div>

      </div>

    </section>

    <div class="button-section">
      <button class="advanced-search" @click="showAdvancedSearch = !showAdvancedSearch">{{ showAdvancedSearch ? 'Ukryj zaawansowane wyszukiwanie' : 'Zaawansowane wyszukiwanie' }}</button>
      <button @click="searchDocuments">Szukaj</button>
    </div>

  </section>
</template>

<style scoped>

.browser {
  border: 2px solid var(--brown);
  width: auto;
  display: flex;
  flex-direction: column;
  margin: 10px auto auto;
  border-radius: 15px;
  padding-left: 0;
  padding-right: 0;
  background: var(--light-white);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  transition: box-shadow 0.3s ease;
  height: auto;
}

.browser:hover {
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.2);
}

.button-section {
  width: 100%;
  margin-bottom: 10px;
  margin-top: 10px;
}

button {
  padding: 10px 15px;
  background-color: var(--brown);
  color: #000000;
  border: none;
  font-weight: bold;
  border-radius: 30px;
  cursor: pointer;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.15);
  transition: background-color 0.3s ease, box-shadow 0.3s ease;
  margin-left: 150px;
}

button:hover {
  background-color: var(--dark-brown);
  color: #eeebe9;
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.2);
}

.advanced-search {
  background-color: var(--light-white);
  border: 2px solid var(--brown);
  margin-left: 120px;
}

.advanced-search:hover {
  background-color: var(--brown);
  color: #eeebe9;
  box-shadow: 0 6px 12px rgba(0, 0, 0, 0.2);
}

input {
  border: 2px solid #c9ad6e;
  border-radius: 10px;
  height: 35px;
  padding-left: 5px;
  box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: border-color 0.3s ease, box-shadow 0.3s ease;
}

input:focus {
  border-color: var(--dark-brown);
  box-shadow: inset 0 3px 6px rgba(0, 0, 0, 0.15);
  outline: none;
}

.advanced-fields {
  width: 100%;
  padding: 0;
  display: block;
  max-height: 0;
  overflow: hidden;
  transition: max-height 0.5s ease;
}

.advanced-fields.is-visible {
  max-height: 500px;
}

.advanced-section {
  padding: 20px;
}

.data-section input{
  margin-left: 20px;
}

.parish-section {
  padding: 8px 0;
}

.parish-section input{
  margin-left: 20px;
  width: 355px;
}

.postal-codes-section label{
  margin-right: 35px;
}

.postal-codes-section input {
  margin-right: 20px;
}

p {
  margin: 5px 0;
}

.address-section {
  padding: 5px 0;
}

.address-section input {
  margin-right: 20px;
}

.address-input {
  width: 425px;
}

.coordinates-section {
  padding: 8px 0;
}

.coordinates-section input {
  margin-left: 20px;
}

</style>
