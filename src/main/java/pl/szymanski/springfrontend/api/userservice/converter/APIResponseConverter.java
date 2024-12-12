package pl.szymanski.springfrontend.api.userservice.converter;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pl.szymanski.springfrontend.api.userservice.dto.AbstractAPIDTO;
import pl.szymanski.springfrontend.api.userservice.dto.PageDTO;
import pl.szymanski.springfrontend.api.userservice.mapper.AbstractAPIResponseMapper;

public class APIResponseConverter<S extends AbstractAPIDTO,T> {

	public APIResponseConverter(AbstractAPIResponseMapper mapper) {
		this.mapper = mapper;
	}

	private AbstractAPIResponseMapper<S,T> mapper;

	public Page<T> convertToDTO(S source){
		PageDTO pageDTO = source.getPage();
		Pageable apiPageable = PageRequest.of(pageDTO.getNumber(), pageDTO.getSize());
		return new PageImpl<>(mapper.map(source.getContent()), apiPageable, pageDTO.getTotalElements());
	}
}
