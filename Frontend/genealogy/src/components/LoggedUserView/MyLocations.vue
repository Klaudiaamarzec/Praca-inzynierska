<script setup>
import {onMounted, ref} from "vue";
import ErrorModal from "@/components/LoggedUserView/ErrorModal.vue";
import LogoutModal from "@/components/MainView/LogoutModal.vue";

const results = ref([]);

const isEditMode = ref(false);
const idEditMode = ref(null);
const editedResult = ref({});

const showModal = ref(false);
let errorText = ref('');
const showLogoutModal = ref(false);
let logoutText = ref('');

onMounted(async () => {
  await getLocalizations();
});

const getLocalizations = async () => {
  try {
    const token = localStorage.getItem('jwtToken');

    const response = await fetch(`http://127.0.0.1:8080/API/PhysicalLocations/GetMyPhysicalLocations`, {
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
      }
    });

    if (response.ok) {
      results.value = await response.json();
    } else {
      console.error("Nie udało się pobrać lokalizacji");
    }
  } catch (error) {
    console.error("Błąd:", error);
  }
}

const startEditing = (id, result) => {
  isEditMode.value = true;
  idEditMode.value = id;
  editedResult.value = { ...result };
};

const cancelEdit = () => {
  editedResult.value = { ...results.value.find(result => result.id === idEditMode.value) };
  idEditMode.value = null;
  isEditMode.value = false;
}

const editLocalization = async () => {
  try {
    const token = localStorage.getItem('jwtToken');

    console.log("EDIT DATA")
    console.log(editedResult.value)

    // Usuń pole user z editedResult, jeśli istnieje
    const { user, ...cleanedResult } = editedResult.value;

    const response = await fetch(`http://127.0.0.1:8080/API/PhysicalLocations/Update/${idEditMode.value}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
      },
      body: JSON.stringify(cleanedResult),
    });

    if (response.status === 401) {
      showLogoutModal.value = true;
      logoutText.value = await response.text();
    }

    if (!response.ok) {
      showModal.value = true;
      const errorDetails = await response.text();
      console.error('Odpowiedź serwera nie była poprawna:', errorDetails);
      errorText.value = errorDetails || "Wystąpił błąd podczas dodawania dokumentu";
      return;
    }

    // Odczytujemy zaktualizowaną lokalizację
    const updatedResult = await response.text();

    // Zaktualizuj edytowaną lokalizację w tablicy 'results'
    const index = results.value.findIndex(result => result.id === updatedResult.id);
    if (index !== -1) {
      results.value[index] = updatedResult;
    }

    // Wyłącz tryb edycji i zresetuj edytowany wynik
    idEditMode.value = null;
    isEditMode.value = false;

    await getLocalizations();

  } catch (error) {
    console.error('Błąd podczas edytowania lokalizacji:', error);
    showModal.value = true;
    errorText.value = "Wystąpił problem z połączeniem z serwerem";
  }
}

</script>

<template>

  <section class="result-list">

    <div v-for="(result, index) in results" :key="index" class="list-item">

      <section v-if="idEditMode === result.id" class="header-section">
        <div class="left-section">
          <button class="button-modal" @click="cancelEdit">Anuluj</button>
        </div>
        <div class="right-section">
          <button class="button-modal" @click="editLocalization">Zapisz</button>
        </div>
      </section>

      <section v-else class="header-section">
        <div class="left-section">
        </div>
        <div class="right-section">
          <button class="button-modal" @click="startEditing(result.id, result)">Edytuj</button>
        </div>
      </section>

      <section class="advanced-section-adding" style="padding-top: 8px">

        <div class="grid-section">
          <label for="description"><strong>Opis:</strong></label>
          <input
            v-if="idEditMode === result.id"
            class="main-input"
            type="text"
            id="description"
            v-model="editedResult.description"
            placeholder="Podaj opis"
          />
          <div v-else>{{ result.description || 'Brak opisu' }}</div>
        </div>

        <div class="grid-section">
          <label for="fromDate"><strong>Data:</strong></label>
          <div v-if="idEditMode !== result.id">{{ result.date || 'Brak daty' }}</div>
          <input
            v-if="idEditMode === result.id"
            class="main-input"
            type="date"
            v-model="editedResult.date"
          />
        </div>

        <div class="grid-section">
          <label for="fromDate"><strong>Kondycja:</strong></label>
          <div v-if="idEditMode !== result.id">{{ result.condition || 'Brak' }}</div>
          <input
            v-if="idEditMode === result.id"
            class="main-input"
            type="text"
            v-model="editedResult.condition"
          />
        </div>

        <div class="grid-section">
          <label for="fromDate"><strong>Typ:</strong></label>
          <div v-if="idEditMode !== result.id">{{ result.type || 'Brak' }}</div>
          <input
            v-if="idEditMode === result.id"
            class="main-input"
            type="text"
            v-model="editedResult.type"
          />
        </div>

      </section>

      <div v-if="result.localaddress" class="separator"></div>

      <section v-if="result.localaddress" class="advanced-section-adding" style="padding-bottom: 10px">

        <div class="grid-section">
          <label for="country"><strong>Kraj:</strong></label>
          <div v-if="idEditMode !== result.id">{{ result.localaddress?.country || 'Brak kraju' }}</div>
          <input
            v-if="idEditMode === result.id"
            class="main-input"
            type="text"
            v-model="editedResult.localaddress.country"
            placeholder="Wpisz kraj"
          />
        </div>

        <div class="grid-section">
          <label for="voivodeship"><strong>Województwo:</strong></label>
          <div v-if="idEditMode !== result.id">{{ result.localaddress?.voivodeship || 'Brak województwa' }}</div>
          <input
            v-if="idEditMode === result.id"
            class="main-input"
            type="text"
            v-model="editedResult.localaddress.voivodeship"
            placeholder="Wpisz województwo"
          />
        </div>

        <div class="grid-section">
          <label for="city"><strong>Miasto:</strong></label>
          <div v-if="idEditMode !== result.id">{{ result.localaddress?.city || 'Brak miasta' }}</div>
          <input
            v-if="idEditMode === result.id"
            class="main-input"
            type="text"
            v-model="editedResult.localaddress.city"
            placeholder="Wpisz miasto"
          />
        </div>

        <div class="grid-section">
          <label for="address"><strong>Adres:</strong></label>
          <div v-if="idEditMode !== result.id">{{ result.localaddress?.address || 'Brak adresu' }}</div>
          <input
            v-if="idEditMode === result.id"
            class="main-input"
            type="text"
            v-model="editedResult.localaddress.address"
            placeholder="Wpisz adres"
          />
        </div>

        <div class="grid-section">
          <label for="postalCode"><strong>Kod pocztowy:</strong></label>
          <div v-if="idEditMode !== result.id">{{ result.localaddress?.postalCode || 'Brak kodu pocztowego' }}</div>
          <input
            v-if="idEditMode === result.id"
            class="main-input"
            type="text"
            v-model="editedResult.localaddress.postalCode"
            placeholder="Wpisz kod pocztowy"
          />
        </div>

      </section>

    </div>

  </section>

  <ErrorModal v-if="showModal" :showModal="showModal" :errorDetails="errorText" @close="showModal = false" />
  <LogoutModal v-if="showLogoutModal" :showModal="showLogoutModal" :errorDetails="logoutText" @close="showLogoutModal = false" />

</template>

<style scoped>

.header-section {
  padding-top: 10px;
}

.button-modal {
  width: 100%;
}

.grid-section {
  gap: 50px;
  padding: 2px 0;
  grid-template-columns: 2fr 10fr;
}
</style>
