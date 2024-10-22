package pl.szymanski.springfrontend.api.userservice.mapper;

import java.util.List;

public interface AbstractAPIResponseMapper<S, T> {

	T map(S source);

	List<T> map(List<S> source);
}
