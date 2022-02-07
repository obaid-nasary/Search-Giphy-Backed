package com.example.ibm_giphy;


import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.natural_language_understanding.v1.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Component
@RestController
@RequestMapping(path =  "giphy")
@CrossOrigin(origins = "http://localhost:4200")
public class GiphyController {

    private final String api_key;
    private final String api_url;


    @Autowired
    public GiphyController(@Value("${apiKey}") String api_key, @Value("${apiUrl}") String api_url) {
        this.api_key = api_key;
        this.api_url = api_url;
    }


    /**
     *
     * @param searchedText
     * @return the extracted keywords from the searched text provided
     * using IBM Natural Language Understanding Api
     */
    @GetMapping("/keyword/{searchedText}")
    public String ibmKeyword(@PathVariable String searchedText) {

        IamAuthenticator authenticator = new IamAuthenticator(api_key);
        NaturalLanguageUnderstanding naturalLanguageUnderstanding = new NaturalLanguageUnderstanding("2021-08-01", authenticator);
        naturalLanguageUnderstanding.setServiceUrl(api_url);

//        String searchedText = "A daily live broadcast provides current domestic and international news, weather reports " +
//                "and interviews with newsmakers from the worlds of politics, business, media, entertainment and sports.";

//        String text = "The definite article is used before singular and plural nouns when the noun is specific or particular. The signals that the noun " +
//                "is definite, that it refers to a particular member of a group. For example: The dog that bit me ran away. Here, we're talking about a " +
//                "specific dog, the dog that bit me. I was happy to see the policeman who saved my cat! Here, we're talking about a particular policeman. " +
//                "Even if we don't know the policeman's name, it's still a particular policeman because it is the one who saved the cat. I saw the elephant " +
//                "at the zoo. Here, we're talking about a specific noun. Probably there is only one elephant at the zoo.";

//        The definite article is used before singular and plural nouns when the noun is specific or particular. The signals that the noun is definite, that it refers to a particular member of a group. For example: The dog that bit me ran away. Here, we're talking about a specific dog, the dog that bit me. I was happy to see the policeman who saved my cat! Here, we're talking about a particular policeman. Even if we don't know the policeman's name, it's still a particular policeman because it is the one who saved the cat. I saw the elephant at the zoo. Here, we're talking about a specific noun. Probably there is only one elephant at the zoo.

        KeywordsOptions keywords = new KeywordsOptions.Builder()
                .sentiment(true)
                .emotion(true)
                .limit(20)
                .build();

        SummarizationOptions summarizationOptions = new SummarizationOptions.Builder()
                .build();

        Features features = new Features.Builder().summarization(summarizationOptions)
                .keywords(keywords)
                .build();

        AnalyzeOptions parameters = new AnalyzeOptions.Builder()
                .text(searchedText)
                .features(features)
                .build();

        AnalysisResults response = naturalLanguageUnderstanding
                .analyze(parameters)
                .execute()
                .getResult();
//        System.out.println(response); //for debugging

        /**
         * Put the generated keywords into a map where the relevance of the keyword
         * is higher than or equal to 0.6 to make the response shorter for GIPHY Api to be
         * queried successfully
         *
         * The maximum amount of characters for GIPHY Api to search are 50 characters
         */
        Map<Double, String> keyword = new HashMap<>();
        for (int i = 0; i < response.getKeywords().size() - 1; i++) {
            if(response.getKeywords().get(i).getRelevance() >= 0.6) {
                keyword.put(response.getKeywords().get(i).getRelevance(), response.getKeywords().get(i).getText());
            }
        }
//        System.out.println(keyword); // for debugging
        Map<Double, String> sortedMap = new TreeMap<>(Collections.reverseOrder());
        sortedMap.putAll(keyword);
//        System.out.println(sortedMap); // for debugging

        /**
         * Iterate through the @sortedMap take the keywords from
         * the @sortedMap and concat it into @values
         */
        int count = 0;
        String values = "";
        Iterator<Double> itr = sortedMap.keySet().iterator();
        while(itr.hasNext() && count <= sortedMap.size()-1){
            values = values.concat(sortedMap.get(itr.next())) + " ";
            count++;
        }

        /**
         * Split the sorted string object by whitespaces for each word and concat it into
         * a string for the final value
         * If result less than 5 words then all the string else just 5 words with higher relevance
         */
        String[] split = values.split(" ");
        int i = 0;
        String finalValue = "";
        if(split.length > 5){
            while (i < 5){
                finalValue = finalValue.concat(split[i]) + " ";
                i++;
            }
        }else{
            while(i < split.length){
                finalValue = finalValue.concat(split[i]) + " ";
                i++;
            }
        }

        System.out.println("did you do it");

        return finalValue;
    }

    @GetMapping("/takeme")
    public String takeMe(){
        return "Obaid is your name";
    }


}
