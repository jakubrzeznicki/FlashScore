# FlashScore

### Spis treści
* [Informacje](#informacje)
* [Zrzuty ekranu](#zrzuty-ekranu)
* [Struktura](#struktura)
* [Funkcjonalności](#funkcjonalności)
* [Biblioteki](#biblioteki)




### Informacje
Aplikacja mobilna na system Android,  napisana w języku Kotlin z wykorzystaniem wzorca MVVM. Aplikacja do śledzenia wyników meczów piłki nożnej pobierająca dane z zewnętrznego api.

Projekt cały czas w rozbudowie.

### Zrzuty ekranu

![CountriesLeaguesEventsList](https://github.com/jakubrzeznicki/FlashScore/blob/master/screenshots/1.png "Countries Leagues Events List")
![CommingEventDetailsTeamsStandings](https://github.com/jakubrzeznicki/FlashScore/blob/master/screenshots/2.png "Comming Event Details Teams Standings")
![EventDetailsPlayersPlayerInfo](https://github.com/jakubrzeznicki/FlashScore/blob/master/screenshots/3.png "Event Details Players Player Information")


### Funkcjonalności
Lista funkcjonalności (stan na 20.04.2021)
* Przegląd lig z danego kraju.
* Przegląd klubów z danej ligi.
* Przegląd meczów odbytych/aktualnych/przyszłych.
* Podgląd detali odnośnie meczu.
* Przegląd tabeli ogólnej/mecze domowe/mecze wyjazdowe.
* Przegląd zawodników z danej drużyny.
* Podgląd statystyk zawodnika takich jak: rozegrane mecze, kartki, gole.

### Struktura
Projekt zawiera testy jednostkowe oraz testy instrumentalne
Ogólna struktura projektu:
1. **adapters**: Adaptery potrzebne do recycleview.
2. **data/local**: Modele danych (encje) , DAO - Obiekty dostępu do danych, oraz klasę bazy danych.
3. **data/domain**: Modele domenowe.
4. **data/network**: Modele danych (Dto), responses oraz interfejs api football service.
5. **di**: obiekt dependency injection dla całej aplikacji oraz servisu.
6. **other**: Pozostałe klasy/obiekty m.in stałe, konwertory.
7. **repositories**: klasy oraz interfejsy repozytorium.
8 **ui**: activity, fragmenty oraz view models.
9 **ui/util**: klasa fragment factory, oraz cała obsługa sprawdzania połączenia internetowego.



### Biblioteki

- Android Jetpack
    - [androidx.room](https://developer.android.com/jetpack/androidx/releases/room) - version 2.2.6
    - [androidx.hilt](https://developer.android.com/jetpack/androidx/releases/hilt)- version 1.0.0-alpha02
    - [androidz.lifecycle](https://developer.android.com/jetpack/androidx/releases/lifecycle)- version 2.2.0
    - [androidx.navigation](https://developer.android.com/jetpack/androidx/releases/navigation) - version 2.3.2
    - [View Binding](https://developer.android.com/topic/libraries/view-binding)

    
- [Dagger Hilt](https://developer.android.com/training/dependency-injection/hilt-android)- version 2.28-alpha
- [kotlinx.coroutines](https://github.com/Kotlin/kotlinx.coroutines)- version 1.4.2
- [Retrofit]()
- [Gson](https://github.com/google/gson)- version 2.8.5
- [Glide](https://github.com/bumptech/glide)- version 4.11.0


- Test
    - [Espresso](https://developer.android.com/training/testing/espresso)
    - [JUnit](https://junit.org/junit4/)- version 4.13
    - [Mockito](https://site.mockito.org/)
    - [Robolectric](http://robolectric.org/)
    - [Truth](https://github.com/google/truth)- version 1.0.1
    - [Mockito-kotlin](https://github.com/mockito/mockito-kotlin)- version 2.2.0
    - [okhttp](https://github.com/square/okhttp) - version 4.9.1
