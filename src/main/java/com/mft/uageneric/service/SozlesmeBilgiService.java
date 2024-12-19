package com.mft.uageneric.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mft.uageneric.dto.SozlesmeBilgiKolonRS;
import com.mft.uageneric.entity.SozlesmeBilgi;
import com.mft.uageneric.entity.SozlesmeBilgiKolon;
import com.mft.uageneric.repository.SozlesmeBilgiKolonRepository;
import com.mft.uageneric.repository.SozlesmeBilgiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SozlesmeBilgiService {
    private final SozlesmeBilgiRepository sozlesmeBilgiRepository;
    private final SozlesmeBilgiKolonRepository sozlesmeBilgiKolonRepository;


    public List<SozlesmeBilgiKolonRS> getSbdGeneric(Long id){
        SozlesmeBilgi sozlesmeBilgi= sozlesmeBilgiRepository.findById(id).orElse(null);
        String faaliyetNo= sozlesmeBilgi.getFaaliyetNo();
        List<SozlesmeBilgiKolonRS> kolonlar= getKolon(faaliyetNo);

        for (SozlesmeBilgiKolonRS kolon: kolonlar) {
            try {
                Field field = sozlesmeBilgi.getClass().getDeclaredField(kolon.getCode());
                field.setAccessible(true);
                Object value = field.get(sozlesmeBilgi);
                kolon.setValue(value);
                kolonlar.add(kolon);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return kolonlar;
    }

    private SozlesmeBilgi getSbf(Long id){
        SozlesmeBilgi sozlesmeBilgi= sozlesmeBilgiRepository.findById(id).orElse(null);
        return sozlesmeBilgi;
    }

    private List<SozlesmeBilgiKolonRS> getKolon(String faaliyetNo) {
        SozlesmeBilgiKolon sozlesmeBilgiKolon = sozlesmeBilgiKolonRepository.findByFaaliyetNo(faaliyetNo);
        String json = sozlesmeBilgiKolon.getKolonlar();
        List<SozlesmeBilgiKolonRS> kolonlar = new ArrayList<>();
        try {
            SozlesmeBilgiKolonRS sozlesmeBilgiKolonRS= new SozlesmeBilgiKolonRS();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(json);
            for (JsonNode node : rootNode) {
                String label = node.get("label").asText();
                String code = node.get("code").asText();
                String type = node.get("type").asText();
                String hidden = node.get("hidden").asText();
                String category = node.get("category").asText();
                String entity = node.get("entity").asText();
                String options = node.get("options").asText();

                sozlesmeBilgiKolonRS.setLabel(label);
                sozlesmeBilgiKolonRS.setCode(code);
                sozlesmeBilgiKolonRS.setType(type);
                sozlesmeBilgiKolonRS.setHidden(hidden);
                sozlesmeBilgiKolonRS.setCategory(category);
                sozlesmeBilgiKolonRS.setEntity(entity);
                sozlesmeBilgiKolonRS.setOptions(options);
                kolonlar.add(sozlesmeBilgiKolonRS);
            }
            // İlgili alanları al


        } catch (Exception e) {
            System.out.println("hata aldik json okurken");
        }
return kolonlar;

    }
}
