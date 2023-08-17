package com.example.repository;

import com.example.dto.FilterEmailDTO;
import com.example.dto.FilterResultDTO;
import com.example.entity.EmailHistoryEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CustomEmailHistoryRepository {
    @Autowired
    private EntityManager entityManager;

    public FilterResultDTO<EmailHistoryEntity> filterPagination(Integer page, Integer size, FilterEmailDTO dto) {
        StringBuilder selectBuilder= new StringBuilder(" from EmailHistoryEntity");
        StringBuilder countBuilder=new StringBuilder("select count(id) from EmailHistoryEntity");
        StringBuilder builder=new StringBuilder(" where visible=true");
        Map<String ,Object>params=new LinkedHashMap<>();
        if(dto.getEmail()!=null){
            builder.append(" and toEmail=:toEmail");
            params.put("toEmail",dto.getEmail());
        }
        if(dto.getFromDate()!=null){
            builder.append(" and createdDate>=:fromDate");
            params.put("fromDate", LocalDateTime.of(dto.getFromDate(), LocalTime.MIN));
        }
        if (dto.getToDate()!=null){
            builder.append(" and createdDate<=:toDate");
            params.put("toDate",LocalDateTime.of(dto.getToDate(),LocalTime.MAX));
        }
        countBuilder.append(builder);
        builder.append(" order by createdDate desc");
        selectBuilder.append(builder);
        Query selectQuery=entityManager.createQuery(selectBuilder.toString());
        Query countQuery=entityManager.createQuery(countBuilder.toString());
        selectQuery.setFirstResult(page*size);
        selectQuery.setMaxResults(size);
        for (Map.Entry<String,Object> p: params.entrySet()){
            selectQuery.setParameter(p.getKey(),p.getValue());
            countQuery.setParameter(p.getKey(),p.getValue());
        }
        List<EmailHistoryEntity>entityList=selectQuery.getResultList();
        Long totalElement=(Long) countQuery.getSingleResult();
        return new FilterResultDTO<>(entityList,totalElement);
    }
}
