package com.devsuperior.dsmeta.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.devsuperior.dsmeta.dto.SaleReportDTO;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.repositories.SaleRepository;

@Service
public class SaleService {

	@Autowired
	private SaleRepository repository;

	public SaleMinDTO findById(Long id) {
		Optional<Sale> result = repository.findById(id);
		Sale entity = result.get();
		return new SaleMinDTO(entity);
	}

	public Page<SaleReportDTO> getReport(
			String minDate,
			String maxDate,
			String name,
			Pageable pageable
	) {
		LocalDate today = LocalDate.now();

		LocalDate max = maxDate == null || maxDate.isEmpty()
				? today
				: LocalDate.parse(maxDate);

		LocalDate min = minDate == null || minDate.isEmpty()
				? today.minusYears(1)
				: LocalDate.parse(minDate);

		String sellerName = name == null ? "" : name;

		Page<Sale> result = repository.searchSales(min, max, sellerName, pageable);

		return result.map(x -> new SaleReportDTO(
				x.getId(),
				x.getDate(),
				x.getAmount(),
				x.getSeller().getName()
		));
	}


}
