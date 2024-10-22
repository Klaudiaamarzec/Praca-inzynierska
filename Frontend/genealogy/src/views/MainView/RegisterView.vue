<template>

  <div>

    <Header/>

    <section class="login-form-container">
      <div class="login-container">
        <h2>Rejestracja</h2>
        <form @submit.prevent="handleRegister">
          <div class="login-section">
            <label for="userName">Login:</label>
            <input type="text" id="userName" v-model="userName" required />
          </div>
          <div class="login-section">
            <label for="email">Email:</label>
            <input type="email" id="email" v-model="mail" @input="validateEmail" required/>
            <p v-if="emailErrorMessage" class="error-message">{{ emailErrorMessage }}</p>
          </div>
          <div class="login-section">
            <label for="password">Hasło:</label>
            <input type="password" id="password" v-model="password" required />
            <p v-if="passwordMessage" class="password-message">{{ passwordMessage }}</p>
          </div>
          <div class="login-section">
            <label for="password2">Powtórz hasło:</label>
            <input type="password" id="password2" v-model="password2" required />
            <p v-if="passwordMismatch" class="error-message">Hasła muszą być takie same!</p>
          </div>
          <div class="login-section">
            <label>Rola</label>
            <select id="idRole" v-model="idRole" required>
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

      // if(idRole.value === 1)
      //   window.location.href = '/homeGenealogist'
      // else if(idRole.value === 2)
      //   window.location.href = '/homeUser'
      // else
      //   window.location.href = '/'
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

input, select {
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

select {
  appearance: menulist-button;
}

.login-container {
  max-height: 750px;
}

option {
  padding-left: 5px;
}

</style>
