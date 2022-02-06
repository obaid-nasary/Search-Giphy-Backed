package com.example.ibm_giphy;


import com.ibm.cloud.sdk.core.security.IamAuthenticator;
import com.ibm.watson.natural_language_understanding.v1.NaturalLanguageUnderstanding;
import com.ibm.watson.natural_language_understanding.v1.model.*;
import com.monkeylearn.MonkeyLearn;
import com.monkeylearn.MonkeyLearnException;
import com.monkeylearn.MonkeyLearnResponse;
import org.json.simple.JSONArray;
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


    private final GiphyService giphyService;

    @Autowired
    public GiphyController(@Value("${apiKey}") String api_key, @Value("${apiUrl}") String api_url, GiphyService giphyService) {
        this.api_key = api_key;
        this.api_url = api_url;
        this.giphyService = giphyService;
    }


    @PostMapping("/keyword")
    public ResponseEntity<Giphy> addNewGiphyLog(@RequestBody Giphy newGiphy){
        Giphy giphy = giphyService.addNewLog(newGiphy);
        return new ResponseEntity<>(giphy, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public List<Giphy> getGiphyLog(){
        return giphyService.getGiphyLog();
    }

    @GetMapping("/login/{username}/{password}")
    public boolean getByUsername(@PathVariable String username, @PathVariable String password){
        Optional<Giphy> giphyList = giphyService.getByUsername(username, password);
        if(giphyList.isPresent()){
            System.out.println("I was hit by oyou");
            return true;

        }
//        return giphyService.getByUsername(username, password);

        return false;
    }



    @GetMapping("/keyword")
    public String ibmKeyword() {

        IamAuthenticator authenticator = new IamAuthenticator(api_key);
        NaturalLanguageUnderstanding naturalLanguageUnderstanding = new NaturalLanguageUnderstanding("2021-08-01", authenticator);
        naturalLanguageUnderstanding.setServiceUrl(api_url);

        String text = "A daily live broadcast provides current domestic and international news, weather reports " +
                "and interviews with newsmakers from the worlds of politics, business, media, entertainment and sports.";

//        String text = "The definite article is used before singular and plural nouns when the noun is specific or particular. The signals that the noun " +
//                "is definite, that it refers to a particular member of a group. For example: The dog that bit me ran away. Here, we're talking about a " +
//                "specific dog, the dog that bit me. I was happy to see the policeman who saved my cat! Here, we're talking about a particular policeman. " +
//                "Even if we don't know the policeman's name, it's still a particular policeman because it is the one who saved the cat. I saw the elephant " +
//                "at the zoo. Here, we're talking about a specific noun. Probably there is only one elephant at the zoo.";

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
                .text(text)
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
        for (int i = 0; i < response.getKeywords().size(); i++) {
            if(response.getKeywords().get(i).getRelevance() >= 0.6) {
                keyword.put(response.getKeywords().get(i).getRelevance(), response.getKeywords().get(i).getText());
            }
        }
//        System.out.println(keyword); // for debugging
        Map<Double, String> sortedMap = new TreeMap<>(Collections.reverseOrder());
        sortedMap.putAll(keyword);
//        System.out.println(sortedMap); // for debugging

        /**
         * Iterate through the @sortedMap take the first five keywords from
         * the @sortedMap and concat it into @values
         */
        int count = 0;
        String values = "";
        Iterator<Double> itr = sortedMap.keySet().iterator();
        while(itr.hasNext() && count < 5){
            values = values.concat(sortedMap.get(itr.next())) + " ";
            count++;
        }

        /**
         * Split the sorted string object by whitespaces for each word and concat it into
         * a string for the final value
         */
        String[] split = values.split(" ");
        int i = 0;
        String finalValue = "";
        while(i < 5){
            finalValue = finalValue.concat(split[i]) + " ";
            i++;
        }

        return finalValue;
    }












    @GetMapping("/classify")
    public JSONArray classifyText() throws MonkeyLearnException {

        MonkeyLearn ml = new MonkeyLearn("32885825f069bc35286049084e6007e470a94945");
        String[] textList = {"I am excited to see her again in the afterlife when she comes back to me and hugs me so bad that I don't feel anything at all."};
        String keywordModelId = "ex_YCya9nrn";
        String classifierModelId = "cl_o46qggZq";
//        MonkeyLearnResponse res = ml.classifiers.classify("cl_o46qggZq", textList, false);
//        ExtraParam[] extraParams = {new ExtraParam("max_keywords", "30")};
//        MonkeyLearnResponse res = ml.extractors.extract("cl_o46qggZq", textList, extraParams);
        MonkeyLearnResponse res = ml.extractors.extract(keywordModelId, textList);

//        JsonObject data = new Gson().fromJson(jsonString, JsonObject.class);
//        JsonArray names = data .get("items").getAsJsonArray();
//        for(JsonElement element : names){
//            JsonObject object = element.getAsJsonObject();
//            System.out.println(object.get("metadata").getAsJsonObject().get("name").getAsString());
//        }

//        System.out.println(res.arrayResult.get(0).subList(0, 0).get(0).getClass().getName());
//        System.out.println(res.arrayResult.stream().findFirst().stream().toArray());
//        System.out.println(res.arrayResult.get(1));
        return res.arrayResult;
    }

    @GetMapping("/concept")
    public AnalysisResults ibmConcept(){

        IamAuthenticator authenticator = new IamAuthenticator(api_key);

        NaturalLanguageUnderstanding naturalLanguageUnderstanding = new NaturalLanguageUnderstanding("2021-08-01", authenticator);
        naturalLanguageUnderstanding.setServiceUrl(api_url);

        String url = "A daily live broadcast provides current domestic and international news, weather reports \" +\n" +
                "                \"and interviews with newsmakers from the worlds of politics, business, media, entertainment and sports.";

        ConceptsOptions concepts = new ConceptsOptions.Builder()
                .limit(20)
                .build();

        Features features = new Features.Builder()
                .concepts(concepts)
                .build();

        AnalyzeOptions parameters = new AnalyzeOptions.Builder()
                .text(url)
                .features(features)
                .build();

        AnalysisResults response = naturalLanguageUnderstanding
                .analyze(parameters)
                .execute()
                .getResult();

        System.out.println(response);
        return response;
    }

    @GetMapping ("/hello")
    public String myName(){
        System.out.printf("I am called from there");
        return "Obaidullah is my name and that is how peoole know me";
    }

    @GetMapping("/entities")
    public AnalysisResults ibmEntities() {

        IamAuthenticator authenticator = new IamAuthenticator(api_key);
        NaturalLanguageUnderstanding naturalLanguageUnderstanding = new NaturalLanguageUnderstanding("2021-08-01", authenticator);
        naturalLanguageUnderstanding.setServiceUrl(api_url);

        String url = "IBM is an American multinational technology " +
                "company headquartered in Armonk, New York, " +
                "United States, with operations in over 170 countries.";

        EntitiesOptions entities = new EntitiesOptions.Builder()
                .sentiment(true)
                .emotion(true)
                .limit(2)
                .build();

        KeywordsOptions keywordsOptions = new KeywordsOptions.Builder()
                .emotion(true)
                .sentiment(true)
                .limit(2)
                .build();

        Features features = new Features.Builder()
                .entities(entities)
                .keywords(keywordsOptions)
                .build();

        AnalyzeOptions parameters = new AnalyzeOptions.Builder()
                .text(url)
                .features(features)
                .build();

        AnalysisResults response = naturalLanguageUnderstanding
                .analyze(parameters)
                .execute()
                .getResult();
        System.out.println(response);

        return response;
    }






}
