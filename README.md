# Cryptocurrency Wallet Application

## Introduction

Nearing the start of 2021, I started to research a lot about blockchain technology and its applications, one of them being cryptocurrency. Since then, I've been quite intrigued by the extensive applications and capabilities of cryptocurrency. Therefore, I decided to base this project on my interest and passion for cryptocurrency by programming a cryptocurrency wallet application.

This program models a digital cryptocurrency wallet that allows cryptocurrency owners to store their cryptocurrency in a wallet. The wallet will also allow users to buy and sell cryptocurrency.

## User Stories
1. As a user, I want to be able to create and add a wallet with a name, ID, and balance to my account.
2. As a user, I want to be able to deposit money into my wallet.
3. As a user, I want to be able to view the balances of my wallet in my account.
4. As a user, I want to be able to buy and sell cryptocurrency.
5. As a user, I want to be able to save my Account to file and automatically save it on quit.
6. As a user, I want to be give the option to load my Account from file on startup.


## Phase 4: Task 2
Thu Mar 31 18:57:21 PDT 2022: Added a new wallet to account: Default <br>
Thu Mar 31 18:57:29 PDT 2022: Deposited $99384.0 into account. <br>
Thu Mar 31 18:57:34 PDT 2022: Wallet, Default, purchased 12.0 of Ethereum <br>
Thu Mar 31 18:57:41 PDT 2022: Wallet, Default, sold 2.0 of Ethereum <br>
Thu Mar 31 18:57:50 PDT 2022: Added a new wallet to account: Second Wallet <br>
Thu Mar 31 18:58:02 PDT 2022: Deposited $2339444.0 into account. <br>
Thu Mar 31 18:58:05 PDT 2022: Wallet, Second Wallet, purchased 23.0 of Bitcoin

## Phase 4: Task 3
After taking a look at the UML diagram of the codebase, it appears that the relationships between classes have relatively low coupling. However, I would consider refactoring the Currency class to make it a static class as its fields are never changed. This would remove the need to instantiate currencies and reduce coupling further, particularly with the AccountUI and JsonReader classes. Other than that, the class with the most associations seems to be the AccountUI class which makes sense as it essentially assembles the program. Similarly, JsonReader seems to have multiple dependencies on other classes however, I believe these are necessary for the functionality of the reader. Therefore, I would likely not refactor the code any further.

In terms of cohesion, all functionality and classes seem to be relatively well focused, therefore, I do not see a reason to refactor the code from this perspective.