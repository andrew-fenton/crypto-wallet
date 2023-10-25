# Cryptocurrency Wallet Application

## Introduction

Nearing the start of 2021, I started to research a lot about blockchain technology and its applications, one of them being cryptocurrency. Since then, I've been quite intrigued by the extensive applications and capabilities of cryptocurrency. Therefore, I decided to base this project on my interest and passion for cryptocurrency by programming a cryptocurrency wallet application in Java.

This program models a digital cryptocurrency wallet that allows cryptocurrency owners to store their cryptocurrency in a wallet. The wallet also allows users to buy and sell cryptocurrency.

## Future Improvements
After taking a look at the UML diagram of the codebase, it appears that the relationships between classes have relatively low coupling. However, I would consider refactoring the Currency class to make it a static class as its fields are never changed. This would remove the need to instantiate currencies and reduce coupling further, particularly with the AccountUI and JsonReader classes. Other than that, the class with the most associations seems to be the AccountUI class which makes sense as it essentially assembles the program. Similarly, JsonReader seems to have multiple dependencies on other classes however, I believe these are necessary for the functionality of the reader. Therefore, I would likely not refactor the code any further.

In terms of cohesion, all functionality and classes seem to be relatively well focused, therefore, I do not see a reason to refactor the code from this perspective.
