# Inteligentny System Monitorowania Środowiska Naturalnego Roślin

Repozytorium łączące w sobie aplikacje, tworzące wspólnie System Monitorowania i Analizy danych pochodzących z czujników usytuowanych w otoczeniu badanej rośliny. Ma za zadanie usprawnić proces hodowli organizmów roślinnych, poprzez dostarczanie informacji o warunkach ich rozwoju.

### Arduino Plants Monitoring

Program napisany w języku obsługiwanym przez technologie Arduino. Zarządza wszystkimi czujnikami obsługiwanymi przez System Monitorujący.

Funkcje:

* Pomiar temperatury i wilgotności powietrza,
* Pomiar temperatury i poziomu nawilżenia gleby,
* Pomiar natężenia światła.

### Serial Manager

Skrypt nadzorujący działanie pracy mikrokontrolera. Do jego zadań należy pobieranie danych pomiarowych i wysyłanie żądań o dokonanie pomiarów. Jest łącznikiem pomiędzy Systemem Monitorującym, a Aplikacją Internetową.

### Plants WebApp

Aplikacja internetowa będąca interfejsem całego Systemu. Analizuje pobrane dane i przedstawia je w przejrzysty sposób. Jest dostępna z poziomu Internetu.
