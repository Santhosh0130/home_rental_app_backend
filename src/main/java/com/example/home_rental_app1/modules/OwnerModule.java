package com.example.home_rental_app1.modules;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.example.home_rental_app1.dto.Owner;

import lombok.*;

@Setter
@Getter
@Document(collection = "ownerDetails")
public class OwnerModule {

    @Id
    private String userId;

    List<Owner> details;
}
