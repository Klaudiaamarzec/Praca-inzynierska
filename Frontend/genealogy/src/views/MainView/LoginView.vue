<template>
  <div>

    <Header/>

    <section class="login-form-container">
      <div class="login-container">
        <h2>Logowanie</h2>
        <form @submit.prevent="handleLogin">
          <div v-if="!showResetField" class="login-section">
            <label for="emailOrUsername">Email lub login:</label>
            <input class="main-input" type="text" id="emailOrUsername" v-model="email" required />
          </div>
          <div v-if="!showResetField" class="login-section">
            <label for="password">Hasło:</label>
            <input class="main-input" type="password" id="password" v-model="password" required />
          </div>
          <p v-if="errorMessage && !showResetField" class="error-message">{{ errorMessage }}</p>

          <button v-if="!showResetField" class="forgot-password" @click="showResetField = !showResetField">
            Nie pamiętam hasła
          </button>

          <div v-if="showResetField" class="login-section">
            <label for="resetEmail">Email:</label>
            <input class="main-input" type="email" id="resetEmail" v-model="resetEmail" required />

            <button class="send-button" @click="handleResetPassword">Resetuj hasło</button>
            <div v-if="isLoading" class="loading-spinner"></div>
            <p v-if="resetMessage" class="reset-message">{{ resetMessage }}</p>

            <button class="go-back" @click="showResetField = false">Wróć</button>
          </div>

          <button v-if="!showResetField" class="send-button" type="submit">Zaloguj się</button>
        </form>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import Header from '../../components/MainView/Header.vue';
const errorMessage = ref('');
const resetEmail = ref('');
const resetMessage = ref('');
const showResetField = ref(false);
const isLoading = ref(false);


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

const email = ref('');
const password = ref('');

const handleLogin = async () => {

  try {
    const response = await fetch('http://127.0.0.1:8080/Auth/Login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
      body: new URLSearchParams({
        usernameOrEmail: email.value,
        password: password.value,
      }),
    });

    if (response.ok) {

      const token = await response.text();
      localStorage.setItem('jwtToken', token);

      const decodedToken = decodeJWT(token);
      const userRole = decodedToken.role;

      console.log("User role: ", userRole)

      if (userRole === 'genealogist') {
        window.location.href = '/homeGenealogist';
      } else if (userRole === 'user') {
        window.location.href = '/homeUser';
      } else {
        errorMessage.value = `Nieznana rola użytkownika!`;
      }
    } else {
      // Obsługa błędów logowania
      const errorMsg = await response.text();
      errorMessage.value = `${errorMsg}`;
    }
  } catch (error) {
    console.error('Wystąpił błąd podczas logowania:', error);
  }
};

const handleResetPassword = async () => {

  isLoading.value = true;

  try {
    const response = await fetch('http://127.0.0.1:8080/Auth/ResetPassword/Request', {
      method: 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body: new URLSearchParams({ email: resetEmail.value }),  // Zmieniono na URLSearchParams
    });

    const message = await response.text();

    if (response.ok) {
      resetMessage.value = message; // Prawidłowy komunikat od backendu
    } else {
      // Wyświetlamy komunikat z backendu lub domyślny
      resetMessage.value = message || 'Nie udało się zresetować hasła. Sprawdź poprawność adresu email.';
    }
  } catch (error) {
    console.error('Błąd podczas resetowania hasła:', error);
    resetMessage.value = 'Wystąpił błąd. Sprawdź dane i spróbuj ponownie.';
  } finally {
    isLoading.value = false;
  }
};

</script>

<style scoped>

.forgot-password {
  background: none;
  border: none;
  color: grey;
  cursor: pointer;
  text-decoration: none;
  font-size: 14px;
  margin: auto;
}

.forgot-password:hover {
  text-decoration: underline;
}

.go-back {
  background: none;
  border: none;
  color: grey;
  cursor: pointer;
  text-decoration: none;
  font-size: 14px;
  align-self: flex-start;
  margin-left: 0;
}

.go-back:hover {
  text-decoration: underline;
}

.reset-message {
  color: green;
  font-size: 14px;
  margin-top: 5px;
}

.loading-spinner {
  border: 4px solid #f3f3f3; /* Szary kolor tła */
  border-top: 4px solid var(--brown);
  border-radius: 50%;
  width: 30px;
  height: 30px;
  animation: spin 2s linear infinite;
  margin: 10px auto;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}


</style>
