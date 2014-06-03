### Документация

Документацията на системата служи за подпомагане и улесняване на потребителите използващи системата. За да бъде генерирана 
не е необходимо да се посочват никакви други допълнителни конфигурационни файлове или системни свойства __system property__.
Генериране на документацията по време изпълнението се осъществявачрез изпълняването на командата

	./gradlew baseHtml

След успешно генериране ще се изведе следния изход:

	:copyStyles
	:concatPartsMd
	:rawHtml
	:baseHtml
	Documentation is generated in:
	/Project/build/html/baseHtml.html

След това тя ще може да бъде достъпена при отвяряне на baseHtml.html файла.
#### Начин на генериране на документацията
Документацията се състои от инфромация, която описва различни системни функционалности и как те да бъдат конфигурирани и настройвани.
Всичката тази информация трябва да бъде въведена в текстов вид. Това става чрез описанието й в [markdown](http://daringfireball.net/projects/markdown/) формат и съхраняването и в 
	
	project/src/main/parts/number-description.md
	
където __number__ е последователен номер, показващ къде дадения markdown файл ще се разположи, __description__ е значещо име описващо съдържанието 
на fайла. 
Всички файлове с разширение __md__ се обединяват в един общ файл __parts.md__, който се разполага в __<buildDir>/parts.md__, където
__<buildDir>__ е името на специфицираната билд директория за проекта.

