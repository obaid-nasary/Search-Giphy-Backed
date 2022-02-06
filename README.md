# Search-Giphy-Backed

This is a web application that can process text data that be a word, phrase, sentence or an article using [IBM's Natural Language Understanding Keyword Extraction API](https://cloud.ibm.com/docs/natural-language-understanding?topic=natural-language-understanding-getting-started) and extracts keywords from the given text. 
The web application is also using another popular Gifs API from [GIPHY API](https://developers.giphy.com/docs/api/endpoint) to search for Gifs using the search endpoint based on the keywords extracted.
The application also has front-end which built in [Angular](https://angular.io/) the link to the reposity is [obaidnasary/Search-Giphy-Frontend/Public](https://github.com/obaidnasary/Search-Giphy-Frontend/tree/master).  

## Features

- Displays trending Gifs of the day or time
- Extracts keywords from a long text and summarizes it
- Displays a Gif based on the given text for searching


## Requirements and Used Libraries

For building and running you will need:

- [JDK 17.0.2](https://www.oracle.com/java/technologies/downloads/#java17)
- [Maven 4](http://maven.apache.org/POM)
- API Key and API URL of [IBM's Natural Language Understanding API](https://cloud.ibm.com/docs/natural-language-understanding?topic=natural-language-understanding-getting-started)
- API Key of search and trending [GIPHY API Endpoints](https://developers.giphy.com/docs/api/endpoint)

## Running locally

To run a Spring Boot application on your local machine you can either run by executing `main` method in your IDE [IntelliJ IDEA Recommended](https://www.jetbrains.com/idea/) from `src.main.java.com.example.IBAN.IbanApplication` or alternatively using the [Spring Boot Maven plugin](https://docs.spring.io/spring-boot/docs/current/reference/html/build-tool-plugins-maven-plugin.html) as the command below:

```shell
mvn sprint-boot run
```

