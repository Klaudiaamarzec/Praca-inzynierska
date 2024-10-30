<template>

  <div>

    <Header/>

    <section class="login-form-container">
      <div class="login-container">
        <h2>Rejestracja</h2>
        <form @submit.prevent="handleRegister">
          <div class="login-section">
            <label for="userName">Login:</label>
            <input class="main-input" type="text" id="userName" v-model="userName" required />
          </div>
          <div class="login-section">
            <label for="email">Email:</label>
            <input class="main-input" type="email" id="email" v-model="mail" @input="validateEmail" required/>
            <p v-if="emailErrorMessage" class="error-message">{{ emailErrorMessage }}</p>
          </div>
          <div class="login-section">
            <label for="password">Hasło:</label>
            <input class="main-input" type="password" id="password" v-model="password" required />
            <p v-if="passwordMessage" class="password-message">{{ passwordMessage }}</p>
          </div>
          <div class="login-section">
            <label for="password2">Powtórz hasło:</label>
            <input class="main-input" type="password" id="password2" v-model="password2" required />
            <p v-if="passwordMismatch" class="error-message">Hasła muszą być takie same!</p>
          </div>
          <div class="login-section">
            <label>Rola</label>
            <select class="main-select" id="idRole" v-model="idRole" required>
              <option :value="2">Zwykły użytkownik</option>
              <option :value="1">Genealog</option>
            </select>
          </div>
          <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
          <button class="send-button" type="submit">Zarejestruj się</button>
        </form>
      </div>
    </section>

    <RegisterSuccessModal v-if="showModal" :showModal="showModal" @close="showModal = false" />

  </div>

</template>

<script setup>
import { ref } from 'vue';
import Header from "@/components/MainView/Header.vue";
import RegisterSuccessModal from "../../components/MainView/RegisterSuccessModal.vue";

const userName = ref('');
const mail = ref('');
const password = ref('');
const password2 = ref('');
const idRole = ref(2);  // Domyślna rola to "Zwykły użytkownik"
const errorMessage = ref('');
const emailErrorMessage = ref('');
const passwordMessage = ref('');
const passwordMismatch = ref(false);
const showModal = ref(false);

const validatePassword = () => {
  const minLength = 8;
  const hasNumber = /\d/;
  const hasUppercase = /[A-Z]/;

  if (password.value.length < minLength) {
    passwordMessage.value = `Hasło musi mieć co najmniej ${minLength} znaków.`;
  } else if (!hasNumber.test(password.value)) {
    passwordMessage.value = "Hasło musi zawierać przynajmniej jedną cyfrę.";
  } else if (!hasUppercase.test(password.value)) {
    passwordMessage.value = "Hasło musi zawierać przynajmniej jedną wielką literę.";
  } else {
    passwordMessage.value = ''; // Hasło spełnia wszystkie wymagania
  }

  // Sprawdzenie zgodności haseł
  passwordMismatch.value = password.value !== password2.value;
};

const validateEmail = () => {
  const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  if (!emailPattern.test(mail.value)) {
    emailErrorMessage.value = "Niepoprawny adres e-mail!";
  } else {
    emailErrorMessage.value = '';  // Brak błędów, e-mail jest poprawny
  }
};

// Funkcja obsługująca rejestrację
const handleRegister = async () => {
  errorMessage.value = '';

  // Ponowna walidacja haseł przed wysłaniem
  validatePassword();
  validateEmail();

  if (emailErrorMessage.value || passwordMessage.value || passwordMismatch.value) {
    errorMessage.value = "Popraw błędy przed rejestracją.";
    return;
  }

  try {
    // Wyślij żądanie POST do backendu (dostosuj URL)
    const response = await fetch('http://127.0.0.1:8080/Auth/Register', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        userName: userName.value,
        mail: mail.value,
        password: password.value,
        idRole: {id: idRole.value},
      }),
    });

    if (response.ok) {
      // Rejestracja zakończona sukcesem
      //alert('Rejestracja zakończona sukcesem!');

      showModal.value = true;

    } else {
      // Obsługa błędów rejestracji
      const errorMsg = await response.text();
      errorMessage.value = `Błąd rejestracji: ${errorMsg}`;
      // errorMessage.value = `Błąd rejestracji`;
      // console.log("Błąd rejestracjij: ", errorMsg);
    }
  } catch (error) {
    console.error('Wystąpił błąd podczas rejestracji:', error);
    errorMessage.value = 'Wystąpił błąd podczas rejestracji';
  }
};
</script>

<style scoped>

select {
  margin-left: 0;
}

</style>
