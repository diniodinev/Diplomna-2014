<img src="http://newhorizons.bg/hackthehackers/wp-content/uploads/2012/01/SU-logo.jpg" />



## Цел

Целта на дипломната работа е създаване на Continious Integration система за проверка на софтуерни артефакти.

## Билдване

За билдването на системата се изпозлва билд тула Gradle. Gradle осигурява иновативен начин [wrapper](http://gradle.org/docs/current/userguide/gradle_wrapper.html) , който позволява да се работи с Gradle билдове без да е необходима ръчна инсталация. Wrapper е batch скрипт за Windows и shell скрипт за другите операционни системи.

Желателно е да се изпозлва wrapper за билдване на Gradle базирани проекти. Като цяло използването му гарантира, че за билда ще се използва тази версия на Gradle, която е необходима.

За да се билдне приложението използвайте

    ./gradlew clean createSourceSets check --continue

Това ще изчисти build директорията и ще стартира провереки на проектите.
