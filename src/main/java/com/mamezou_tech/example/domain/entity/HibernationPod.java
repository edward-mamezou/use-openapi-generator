package com.mamezou_tech.example.domain.entity;

import com.mamezou_tech.example.domain.valueobject.HibernationPodId;
import com.mamezou_tech.example.domain.valueobject.Passenger;

public record HibernationPod(HibernationPodId podId, Passenger passenger) {
}
