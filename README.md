# FlashScore

### Spis treści
* [Informacje](#informacje)
* [Zrzuty ekranu](#zrzuty-ekranu)
* [Struktura](#struktura)
* [Funkcjonalności](#funkcjonalności)
* [Biblioteki](#biblioteki)




### Informacje
Aplikacja mobilna na system Android,  napisana w języku Kotlin z wykorzystaniem wzorca MVVM. Aplikacja do śledzenia wyników meczów piłki nożnej.

### Zrzuty ekranu

![Countries](https://github.com/jakubrzeznicki/FlashScore/blob/master/screenshots/1.png "Countries")
![Leagues](https://github.com/jakubrzeznicki/FlashScore/blob/master/screenshots/2 (2).png "Leagues")
![EventsList](https://github.com/jakubrzeznicki/FlashScore/blob/master/screenshots/3.png "Events List")
![Event](https://github.com/jakubrzeznicki/FlashScore/blob/master/screenshots/4.png "Event")
![Teams](https://github.com/jakubrzeznicki/FlashScore/blob/master/screenshots/5.png "Teams")
![Standings](https://github.com/jakubrzeznicki/FlashScore/blob/master/screenshots/6.png "Standings")
![EventDetails](https://github.com/jakubrzeznicki/FlashScore/blob/master/screenshots/7.png "Event Details")
![Players](https://github.com/jakubrzeznicki/FlashScore/blob/master/screenshots/8.png "Players")
![PlayerInfo](https://github.com/jakubrzeznicki/FlashScore/blob/master/screenshots/9.png "Player Information")


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
1. **adapters**: Zawiera adaptery zapewniające wyświetlanie i działanie widoków, które są w Recyclerview.
2. **data/local**: Zawiera modele danych (encje) , DAO - Obiekty dostępu do danych, oraz klasę bazy danych.
3. **data/domain**: zawiera modele domenowe.
4. **data/network**: zawiera modele danych (Dto), responses oraz interfejs api football service.
5. **di**: Zawiera obiekty dependency injection dla całej aplikacji oraz servisu.
6. **other**: Zawiera pozostałe klasy/obiekty m.in stałe, konwertory.
7. **repositories**: Zawiera klasy oraz interfejsy repozytorium.
8. **service**: Zawiera klase TrackingService, obsługującą śledzenie użytkownika, uaktualnianie czasu oraz dystansu.
9. **ui**: Zawiera aktywnośc, fragmenty oraz view modele.
10. **ui/util**: Zawiera klase fragment factory, oraz całą obsługę sprawdzania połączenia internetowego.



### Biblioteki

- Jetpack Compose
    - [androidx.room](https://developer.android.com/jetpack/androidx/releases/room) - version 2.2.6
    - [androidx.hilt](https://developer.android.com/jetpack/androidx/releases/hilt)- version 1.0.0-alpha02
    - [androidz.lifecycle](https://developer.android.com/jetpack/androidx/releases/lifecycle)- version 2.2.0
    - [androidx.navigation](https://developer.android.com/jetpack/androidx/releases/navigation) - version 2.3.2
    - [View Binding](https://developer.android.com/topic/libraries/view-binding)

    
- [Dagger Hilt](https://developer.android.com/training/dependency-injection/hilt-android)- version 2.28-alpha
- [kotlinx.coroutines](https://github.com/Kotlin/kotlinx.coroutines)- version 1.4.2
- [Retrofit]()
- [Gson]()
- [Glide](https://github.com/bumptech/glide)- version 4.11.0


- Testowanie
    - [Espresso]()
    - [JUnit]()
    - [Mockito]()
    - [mockk]()
    - [okhttp]
