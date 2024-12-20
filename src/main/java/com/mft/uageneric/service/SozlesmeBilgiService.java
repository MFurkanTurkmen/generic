package com.mft.uageneric.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mft.uageneric.dto.SozlesmeBilgiKolonRS;
import com.mft.uageneric.entity.SozlesmeBilgi;
import com.mft.uageneric.entity.SozlesmeBilgiKolon;
import com.mft.uageneric.repository.SozlesmeBilgiKolonRepository;
import com.mft.uageneric.repository.SozlesmeBilgiRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class SozlesmeBilgiService {
    private final SozlesmeBilgiRepository sozlesmeBilgiRepository;
    private final SozlesmeBilgiKolonRepository sozlesmeBilgiKolonRepository;
    private final EntityManager entityManager; // Add this

    public List<SozlesmeBilgiKolonRS> getSbdGeneric(Long id) {
        SozlesmeBilgi sozlesmeBilgi = sozlesmeBilgiRepository.findById(id).orElse(null);
        if (sozlesmeBilgi == null) {
            return new ArrayList<>();
        }

        String faaliyetNo = sozlesmeBilgi.getFaaliyetNo();
        List<SozlesmeBilgiKolonRS> kolonlar = getKolon(faaliyetNo);

        for (SozlesmeBilgiKolonRS kolon : kolonlar) {
            try {
                if ("combo".equals(kolon.getType())) {
                    // Combo alanları için özel işlem
                    handleComboField(sozlesmeBilgi, kolon);
                    // Combo seçeneklerini yükle
                    loadComboOptions(kolon);
                } else {
                    // Normal alanlar için değer ataması
                    Field field = sozlesmeBilgi.getClass().getDeclaredField(kolon.getCode());
                    field.setAccessible(true);
                    Object value = field.get(sozlesmeBilgi);
                    kolon.setValue(value);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return kolonlar;
    }


    private void handleComboField(SozlesmeBilgi sozlesmeBilgi, SozlesmeBilgiKolonRS kolon) {
        try {
            Field field = sozlesmeBilgi.getClass().getDeclaredField(kolon.getCode());
            field.setAccessible(true);
            Object value = field.get(sozlesmeBilgi);

            if (value != null) {
                // Hibernate proxy'yi başlat
                Hibernate.initialize(value);

                // ID'yi al
                Field idField = value.getClass().getSuperclass().getDeclaredField("id");
                idField.setAccessible(true);
                Long id = (Long) idField.get(value);
                kolon.setValue(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void loadComboOptions(SozlesmeBilgiKolonRS kolon) {
        try {
            if (kolon.getEntity() == null || kolon.getEntity().isEmpty()) {
                return;
            }

            String entityName = kolon.getEntity();
            // Entity sınıfının tam yolunu belirle
            String fullEntityName = "com.mft.uageneric.entity." + entityName;
            Class<?> entityClass = Class.forName(fullEntityName);

            // JPQL sorgusu oluştur
            String query = "SELECT e FROM " + entityName + " e";
            List<?> results = entityManager.createQuery(query).getResultList();

            List<Map<String, Object>> options = new ArrayList<>();
            for (Object result : results) {
                Map<String, Object> option = new HashMap<>();

                // ID ve ad alanlarını al
                Field idField = entityClass.getDeclaredField("id");
                Field adField = entityClass.getDeclaredField("ad");
                idField.setAccessible(true);
                adField.setAccessible(true);

                option.put("value", idField.get(result));
                option.put("label", adField.get(result));
                options.add(option);
            }
            kolon.setOptions(options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private List<SozlesmeBilgiKolonRS> getKolon(String faaliyetNo) {
        SozlesmeBilgiKolon sozlesmeBilgiKolon = sozlesmeBilgiKolonRepository.findByFaaliyetNo(faaliyetNo);
        List<SozlesmeBilgiKolonRS> kolonlar = new ArrayList<>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(sozlesmeBilgiKolon.getKolonlar());

            for (JsonNode node : rootNode) {
                SozlesmeBilgiKolonRS kolonRS = new SozlesmeBilgiKolonRS();
                kolonRS.setLabel(node.get("label").asText());
                kolonRS.setCode(node.get("code").asText());
                kolonRS.setType(node.get("type").asText());
                kolonRS.setHidden(node.get("hidden").asText());
                kolonRS.setCategory(node.get("category").asText());
                kolonRS.setEntity(node.get("entity").asText());
                kolonlar.add(kolonRS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return kolonlar;
    }
}
