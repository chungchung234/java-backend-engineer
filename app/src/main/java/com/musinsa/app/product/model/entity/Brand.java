package com.musinsa.app.product.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "brand")
public class Brand {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Builder
	public Brand(String name) {
		this.name = name;
	}

//	@Override
//	public boolean equals(Object o) {
//		if (this == o)
//			return true;
//		if (o == null || getClass() != o.getClass())
//			return false;
//		Brand brand = (Brand)o;
//		return Objects.equals(id, brand.id);
//	}
//
//	@Override
//	public int hashCode() {
//		return Objects.hashCode(id);
//	}
}
