### Как да създадем нов плъгин
Създаването на нов плъгин не е трудно и изизква научаването на няколко нови концепции. Gradle позвоялва създаването на два вида разширения (plugins) - скриптови и обектни. 

- Скриптови - не представляват нищо повече от скриптов файл(build.gradle), който е импортнат в друг скриптов файл
- Обектните - за тях е необходимо да се имплементира *org.gradle.api.Plugin* интерфейса. Сорс кода на обектните плъгини обикновенно се съдържа в buildSrc директорията или в отделен проект и след това може да се дистрибутира като *Jar* файл.

В тази част ще разгледаме как се създава само обектен плъгин, което ще бъде от полза при разширяването на функционалностите на системата.

#### Основни характеристики при разработването на обектен плъгин
- Gradle дава пълната свобода да изберем как ще го имплементираме - дали ще създадем отделен проект за нашето разширение, дали ще го разположим в билд скриптов файл или в *buildSrc* директорията на прокета( в нашия слуай)
-  Всеки плъгин трябва да осигури имплементационен клас, който представлява входна точка към него. Това се извършва чрез имплементирането на интерфейса Plugin както споменах по-горе. Плъгинът може да бъде написан на всеки един *JVM* език, който може да бъде компилиран до байткод - например Java, Groovy, Scala.
-  Всеки плъгин може да бъде разширяван посредством extension обект и след това да бъде конфигуриран и така да му се промени default-ното му поведение.
-  Дескриптор за плъгина - файл, който съдържа метаинформация за разширението. Обикновенно се използва за свързване межеду краткото име на плъгина и имплементационния му клас.

#### Стъпки при създаване
В следващите абзация ще разгледаме как да създадем HelloWorld плъгин от самото начало. Избраният език за неговото създаване е Java.

1. Първо както казах е необходимо да създаде имплементационен клас. Плъгинът е Java файл разположен в 
	\Project\buildSrc\plugins\src\main\groovy\bg\uni\fmi\plugins\sample\HelloPlugin.java

		import org.gradle.api.Plugin;
		import org.gradle.api.Project;

		public class HelloPlugin implements Plugin<Project>{
		
		    @Override
		    public void apply(Project project) {
		
		    }
		}
Към неговата имплементация ще се върнем след малко.

2. Всеки плъгин е изграден от задачи, в които може да се имплементира определена логика, която лесно да се разширява. Gradle осигурява default-на имплементация, която може да се разшири (extends) - *org.gradle.api.DefaultTask*. Освен него *Gradle* предлага и други вградени имплементации като *SourceTask*, но всички те разширяват *DefaultTask*.
Нека създадем къстъмизиран task (custom task) в:

	\Project\buildSrc\plugins\src\main\groovy\bg\uni\fmi\tasks\sample\PrintContent.java
Поведението на task-a се капсулира в task action и за да се покаже, кое дейстиве да се изпълни един от методите се маркира с анотацията *@TaskAction* името на това действие може да е произволно. Поведението на действието се конфигурира посредством свойства.

3. Цялостен пример

		public class PrintContent extends DefaultTask {
	    @Input
	    private String personName;
	
	    @Input
	    @Optional
	    private String title;
	
	    @Input
	    private String salutation = "Dear";
	
	    @TaskAction
	    public void compose() {
	        System.out.println(generateMessage());
	    }
	
	    String generateMessage() {
	        return String.format("%s %s %s I just want to say hello to you.", capitalizeFirstLetter(getSalutation()), getTitle(), getPersonName());
	    }
	
	    String capitalizeFirstLetter(String word) {
	        return StringUtils.capitalize(word);
	    }
	
	    public String getTitle() {
	        return (title == null) ? "" : title;
	    }
	
	    public String getPersonName() {
	        return personName;
	    }
	    public String getSalutation() {
	        return salutation;
	    }
	
	    public void setPersonName(String personName) {
	        this.personName = personName;
	    }
	
	    public void setSalutation(String salutation) {
	        this.salutation = salutation;
	    }
	
	    public void setTitle(String title) {
	        this.title = title;
	    }
	}


Както виждате **PrintContent** наследява **DefaultTask** като дефинира действие **@TaskAction**, което носи името **compose**. Тук е важно да се наблегне, че **task**-a има три входни свойства **personName**, **title**, **salutation**, като **personName** е задължително, **title** е входно свойство, но е опционално и **salutation** е входно свойство със стойност по подразбиране, т.е. не е необходимо да се анотира с **@Optional**, защото то се подразбира и ако не се специфицира никаква стойност се взима **default**-ната - **"Dear"**

Методът анотиран като действие **compose()** извиква **generateMessage()**, който от своя страна извикава **capitalizeFirstLetter(String word)**, който използва външната библиотека **commons-lang**, поради тази причина, за да е достъпна по време на компилация тя трябва да се добави като **compile** зависимост в билд скрипт файла на дадения плъгин.

	  dependencies {
	  	compile 'commons-lang:commons-lang:2.6'
	  }

както и хранилище от където тази библиотека да се достъпи

	repositories {
	    mavenCentral()
	} 
3. Създаване на плъгин използващ нашата задача

Gradle позволява композиране и наседяване на плъгин, т.е. ако искаме нашият плъгин може да надгражда вградените в Gradle плъгини, например java, groovy плъгин, но в случая ще изградим изцяло нова приставка.

		public class HelloPlugin implements Plugin<Project> {
		    public static final String HELLO_TASK_NAME = "helloТаск";
		
		    @Override
		    public void apply(Project project) {
		        createTasks(project);
		    }
		
		    void createTasks(Project project) {
		        PrintContent helloTask = project.getTasks().create(HELLO_TASK_NAME, PrintContent.class);
		        helloTask.setDescription("Just say hello.");
		        helloTask.setGroup("Hello");
		    }
		}

Както споменах по-рано ние създаваме обектен плъгин и трябва да представим имплементация на интерфейса Project

	public class HelloPlugin implements Plugin<Project> {

метода, който трябва да имплементираме от този интерфейс е apply, който приема като единствен аргумент **Project project** иснтанция

Към нашият плъгин трябва да добавим предварително създадени custom task -ове, по своята същност те извън обхвата на плъгина не може да се изпълнят самостоятелно, за това ги добавяме към plugin обекта, където вече те се наричат Enhanced задачи и могат да бъдат конфигурирани.

- Всеки проект (**project**) съдържа контейнер( разбирайте множество от задачи **tasks**), към които можем да добавяме нови. Достъпът до този [TaskContainer](http://www.gradle.org/docs/current/javadoc/org/gradle/api/tasks/TaskContainer.html) се осъществява чрез **project.getTasks()** ,а добавянето на нова задача с метода **create**.

		<T extends Task> T	create(String name, Class<T> type)
		Creates a Task with the given name and type, and adds it to this container.

След това на новодобавения task можем да му добавим допълнителни свойства, които да подпомагат потребителя при търсенето му - description и group. 

**Забележка**: Когато на дадена задача няма дабавена group и никоя друга задача не зависи на него(depends On) то при извъплянване на командата gradle tasks той няма да се изведе( ще се изведе само при **tasks --all**)

4. Именуване

Сега вече имаме плъгин с един task в него, но как ще се използва от потребителите. Трябва да променим дескриотора за плъгина, както споменах по-горе. В противен случай потребителите трябва да го добавят като
	apply plugin: 'bg.uni.fmi.plugins.sample.HelloPlugin'

За това създаваме нов файл в :

	\Project\buildSrc\plugins\src\main\resources\META-INF\gradle-plugins\fmi-hello.properties

Като името, което ще се използва от потребителя е името на файла(без разширението) ,т.е. **fmi-hello** , а самото съдържание на файла е:

	implementation-class = bg.uni.fmi.plugins.sample.HelloPlugin

5. Използване
Тъй като сме създали приставката в buildSrc директорията при стартиране на основния проект, първо се билдва и тества съдържанието от buildSrc и се добавя към classpath на проекта, така плъгините, които сме създали са достъпни априори.

За да го използваме, то трябва да го apply -не в даден build.gradle файл

	apply plugin: 'fmi-hello'
	
	helloTask{
	    personName = 'Anderson'
	    title = 'Mr'
	}

Полето **personName** е задължително и за това изрично трябва да споменем стойност за него. Докато **title** е опционално, а **salitation** , тъй като има стойност по подразбиране може също така изобщо да не го споменаваме.
