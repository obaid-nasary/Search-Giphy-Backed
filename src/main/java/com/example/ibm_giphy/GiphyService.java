package com.example.ibm_giphy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GiphyService extends Throwable{

    private final GiphyRepository giphyRepository;


    @Autowired
    public GiphyService(GiphyRepository giphyRepository) {
        this.giphyRepository = giphyRepository;
    }

    public Giphy addNewLog(Giphy giphy){
        return giphyRepository.save(giphy);
    }

    public List<Giphy> getGiphyLog(){
        return giphyRepository.findAll();
    }

    public Optional<Giphy> getByUsername(String username, String password){

        return giphyRepository.findBy(username, password);
    }
}
