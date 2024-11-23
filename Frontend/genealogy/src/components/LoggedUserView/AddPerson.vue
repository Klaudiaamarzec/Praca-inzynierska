<script setup>
import {onMounted, ref} from "vue";
import {useRouter} from 'vue-router';

import ErrorModal from "@/components/LoggedUserView/ErrorModal.vue";
import SuccessInfo from "@/components/GenealogistView/SuccessInfo.vue";
import LogoutModal from "@/components/MainView/LogoutModal.vue";
const showSuccess = ref(false);
let successText = ref('');
const showModal = ref(false);
let errorText = ref('');
const showLogoutModal = ref(false);
let logoutText = ref('');

const name = ref('');
const surname = ref('');
const birthDate = ref(null);
const rin = ref(null);
const router = useRouter();
const isEditMode = ref(false);
const isParents = ref(false);

const mother = ref('');
const father = ref('');
const child = ref('');
const newPersonId = ref(null);

const isMotherChecked = ref(false);
const isFatherChecked = ref(false);
const childMother = ref('');
const childFather = ref('');
const addedChildren = ref([]);

const personList = ref([]);
const showFamilySection = ref(false);

const goBack = () => {
  router.back();
};

onMounted(async () => {
  try {
    const response = await fetch('http://127.0.0.1:8080/API/People/All');
    personList.value = await response.json();
  } catch (error) {
    console.error('Błąd podczas pobierania listy osób:', error);
  }
});

const decodeJWT = (token) => {
  const base64Url = token.split('.')[1];
  const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
  const jsonPayload = decodeURIComponent(
    atob(base64)
      .split('')
      .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
      .join('')
  );
  return JSON.parse(jsonPayload);
};

const personDetails = (personID) => {
  const token = localStorage.getItem('jwtToken');
  const decodedToken = decodeJWT(token);
  const userRole = decodedToken.role;

  if (userRole === 'genealogist') {
    router.push({ name: 'GenealogistPersonDetails', params: { personID } });
  } else if (userRole === 'user') {
    router.push({ name: 'UserPersonDetails', params: { personID } });
  } else {
    console.log("Nieznana rola użytkownika!");
  }
}

const handleParentSelection = (selectedParent) => {
  if (selectedParent === 'mother') {
    if (isMotherChecked.value) {
      isFatherChecked.value = false;
    }
  } else if (selectedParent === 'father') {
    if (isFatherChecked.value) {
      isMotherChecked.value = false;
    }
  }
}

const addPerson = async () => {

  try {

    const personData = {

      name: name.value,
      surname: surname.value,
      rin: rin.value || null,
      birthDate: birthDate.value || null

    }

    if(name.value === '' || surname.value === '') {
      showModal.value = true;
      errorText.value = "Uzupełnij wszystkie wymagane pola";
      return;
    }

    const token = localStorage.getItem('jwtToken');

    const response = await fetch(`http://127.0.0.1:8080/API/People/Add`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
      },
      body: JSON.stringify(personData)
    });

    if (response.status === 401) {
      showLogoutModal.value = true;
      logoutText.value = await response.text();
    }

    if (!response.ok) {
      showModal.value = true;
      const errorDetails = await response.text();
      console.error('Odpowiedź serwera nie była poprawna:', errorDetails);
      errorText.value = errorDetails || "Wystąpił błąd podczas dodawania nowej osoby";
      return;
    }

    const data = await response.json();
    newPersonId.value = data.personId;
    showFamilySection.value = true;
    isEditMode.value = true;

  } catch (error) {
    console.error('Błąd podczas dodawania nowej osoby:', error);
    showModal.value = true;
    errorText.value = "Wystąpił problem z połączeniem z serwerem";
  }
}

const addParents = async () => {
  try {

    if (!father.value && !mother.value) {
      showModal.value = true;
      errorText.value = "Uzupełnij pola, aby dodać rodziców!";
      return;
    }

    const familyData = {

      child: { id: newPersonId.value },
      father: { id: Number(father.value.id) },
      mother: { id: Number(mother.value.id) }
    }

    const token = localStorage.getItem('jwtToken');

    const response = await fetch(`http://127.0.0.1:8080/API/Family/AddFamily`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
      },
      body: JSON.stringify(familyData)
    });

    if (response.status === 401) {
      showLogoutModal.value = true;
      logoutText.value = await response.text();
    }

    if (!response.ok) {
      showModal.value = true;
      const errorDetails = await response.text();
      console.error('Odpowiedź serwera nie była poprawna:', errorDetails);
      errorText.value = errorDetails || "Wystąpił błąd podczas dodawania rodziców";
      return;
    }

    isParents.value = true;

  } catch (error) {
    console.error('Błąd podczas dodawania rodziców:', error);
    showModal.value = true;
    errorText.value = "Wystąpił problem z połączeniem z serwerem";
  }
}

const addChild = async () => {
  try {

    if (!child.value) {
      showModal.value = true;
      errorText.value = "Nie wybrano dziecka z listy!";
      return;
    }

    let familyData = {
      child: { id: Number(child.value.id) }
    };

    // Jeżeli matka jest zaznaczona
    if (isMotherChecked.value) {
      familyData.mother = { id: Number(childMother.value.id) };
      familyData.father = { id: newPersonId.value };
    }

    // Jeżeli ojciec jest zaznaczony
    if (isFatherChecked.value) {
      familyData.mother = { id: newPersonId.value };
      familyData.father = { id: Number(childFather.value.id) };
    }

    const token = localStorage.getItem('jwtToken');

    const response = await fetch(`http://127.0.0.1:8080/API/Family/AddFamily`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`,
      },
      body: JSON.stringify(familyData)
    });

    if (response.status === 401) {
      showLogoutModal.value = true;
      logoutText.value = await response.text();
    }

    if (!response.ok) {
      showModal.value = true;
      const errorDetails = await response.text();
      console.error('Odpowiedź serwera nie była poprawna:', errorDetails);
      errorText.value = errorDetails || "Wystąpił błąd podczas dodawania dziecka";
      return;
    }

    const data = await response.text();
    addedChildren.value.push(child.value);
    console.log(data);

  } catch (error) {
    console.error('Błąd podczas dodawania dziecka:', error);
    showModal.value = true;
    errorText.value = "Wystąpił problem z połączeniem z serwerem";
  }
}

</script>

<template>

  <section class="browser">
    <section class="advanced-section-adding">

      <section class="header-section">
        <div class="left-section">
          <button class="button-modal" @click="goBack">Anuluj</button>
        </div>
        <div v-if="isEditMode" class="right-section">
          <button class="button-modal" @click="personDetails(newPersonId)">Zapisz</button>
        </div>
      </section>

      <div class="grid-section">
        <label for="fromDate">Imię <span class="required-asterisk">*</span></label>
        <input v-if="!isEditMode" class="main-input" type="text" id="name" v-model="name" />
        <div v-else style="width: 100%">
          {{ name }}
        </div>
      </div>

      <div class="grid-section">
        <label for="fromDate">Nazwisko <span class="required-asterisk">*</span></label>
        <input v-if="!isEditMode" class="main-input" type="text" id="name" v-model="surname" />
        <div v-else>
          {{ surname }}
        </div>
      </div>

      <div v-if="!isEditMode" class="data-section">
        <label for="startDate">Data urodzenia</label>
        <input class="main-input" type="date" id="birthDate" v-model="birthDate" />
      </div>

      <div v-if="isParents" class="grid-section">
        <label for="fromDate">Matka</label>
        <div>{{ mother.name }} {{ mother.surname }}</div>
      </div>

      <div v-if="isParents" class="grid-section">
        <label for="fromDate">Ojciec</label>
        <div>{{ father.name }} {{ father.surname }}</div>
      </div>

      <div v-if="addedChildren.length > 0" class="grid-section">
        <label for="fromDate">Dzieci</label>
        <ul>
          <li>
            <span v-for="(kid, index) in addedChildren" :key="kid.id">
              {{ kid.name }} {{ kid.surname }}
              <span v-if="index < addedChildren.length - 1">, </span>
            </span>
          </li>
        </ul>
      </div>

      <div v-if="!isEditMode" class="end-section">
        <button class="add-button" type="submit" @click="addPerson">Dodaj</button>
      </div>

    </section>

    <div v-if="showFamilySection && !isParents" class="separator"></div>

    <section v-if="showFamilySection && !isParents" :class="['family-section-adding', { 'is-visible': showFamilySection }]">

      <div class="grid-section">
        <label for="startDate">Matka</label>
        <select class="main-select" v-model="mother" required>
          <option disabled value="">Wybierz osobę z listy</option>
          <option v-for="person in personList" :key="person.id" :value="person">
            {{ person.name }} {{ person.surname }}
          </option>
        </select>
      </div>

      <div class="grid-section">
        <label for="startDate">Ojciec</label>
        <select class="main-select" v-model="father" required>
          <option disabled value="">Wybierz osobę z listy</option>
          <option v-for="person in personList" :key="person.id" :value="person">
            {{ person.name }} {{ person.surname }}
          </option>
        </select>
      </div>

      <div class="end-section">
        <button class="add-button" type="submit" @click="addParents">Dodaj</button>
      </div>

    </section>

    <div v-if="showFamilySection" class="separator"></div>

    <section v-if="showFamilySection" :class="['family-section-adding', { 'is-visible': showFamilySection }]">

      <div class="grid-section">
        <label for="startDate">Dziecko</label>
        <select class="main-select" v-model="child" required>
          <option disabled value="">Wybierz osobę z listy</option>
          <option v-for="person in personList" :key="person.id" :value="person">
            {{ person.name }} {{ person.surname }}
          </option>
        </select>
      </div>

      <div class="coordinates-section">
        <label>Dodaj drugiego rodzica</label>
        <label>
          <input class="main-input"
                 type="checkbox"
                 v-model="isMotherChecked"
                 @change="handleParentSelection('mother')"
          />
          Matka
        </label>

        <label>
          <input class="main-input"
                 type="checkbox"
                 v-model="isFatherChecked"
                 @change="handleParentSelection('father')"
          />
          Ojciec
        </label>
      </div>

      <div v-if="isMotherChecked" class="grid-section">
        <label for="startDate">Matka</label>
        <select class="main-select" v-model="childMother" required>
          <option disabled value="">Wybierz osobę z listy</option>
          <option v-for="person in personList" :key="person.id" :value="person">
            {{ person.name }} {{ person.surname }}
          </option>
        </select>
      </div>

      <div v-if="isFatherChecked" class="grid-section">
        <label for="startDate">Ojciec</label>
        <select class="main-select" v-model="childFather" required>
          <option disabled value="">Wybierz osobę z listy</option>
          <option v-for="person in personList" :key="person.id" :value="person">
            {{ person.name }} {{ person.surname }}
          </option>
        </select>
      </div>

      <div class="end-section">
        <button class="add-button" type="submit" @click="addChild">Dodaj</button>
      </div>

    </section>

  </section>

  <ErrorModal v-if="showModal" :showModal="showModal" :errorDetails="errorText" @close="showModal = false" />
  <SuccessInfo v-if="showSuccess" :showModal="showSuccess" :successDetails="successText" redirectTo="stay" @close="showSuccess = false" />
  <LogoutModal v-if="showLogoutModal" :showModal="showLogoutModal" :errorDetails="logoutText" @close="showLogoutModal = false" />

</template>

<style scoped>

.browser {
  display: flex;
  background: var(--light-white);
  width: 60%;
  margin: auto;
  height: auto;
  background: var(--light-white);
  padding-top: 15px;
  padding-bottom: 15px;
}

p {
  margin: 5px 0;
}

.button-modal {
  width: 100%;
  margin-bottom: 10px;
}

.add-button {
  width: 7%;
  padding: 7px;
  z-index: 1;
}

.header-section {
  margin-top: 5px;
  padding: 0;
}

.main-select {
  margin-left: 0;
}

</style>
